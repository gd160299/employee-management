package com.example.employee_management.Common;

import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CallStoredProcedureCommon {
    @PersistenceContext
    private EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    public CallStoredProcedureCommon(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void callStoredProcedure(String packageName, Map<String, Object> parameters) {
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(packageName);

        // Đăng ký các tham số của stored procedure
        for (String paramName : parameters.keySet()) {
            Object paramValue = parameters.get(paramName);
            Class<?> paramType = paramValue.getClass();
            storedProcedure.registerStoredProcedureParameter(paramName, paramType, ParameterMode.IN);
        }

        // Đặt giá trị cho các tham số
        for (String paramName : parameters.keySet()) {
            Object paramValue = parameters.get(paramName);
            storedProcedure.setParameter(paramName, paramValue);
        }

        storedProcedure.execute();
    }

    private void setFieldValue(Object object, String columnName, Object value) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            DbColumnMapper annotation = field.getAnnotation(DbColumnMapper.class);
            if (annotation != null && annotation.value().equalsIgnoreCase(columnName)) {
                field.setAccessible(true);
                if (value instanceof BigDecimal && field.getType().equals(Long.class)) {
                    // Chuyển đổi BigDecimal sang Long
                    field.set(object, ((BigDecimal) value).longValue());
                } else {
                    field.set(object, value);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Field not found for column: " + columnName);
    }


    private String generateStoredProcedureSql(String packageName, Set<String> paramNames) {
        StringBuilder sqlBuilder = new StringBuilder("BEGIN ");
        sqlBuilder.append(packageName).append("(");

        for (String paramName : paramNames) {
            sqlBuilder.append("?");
            sqlBuilder.append(",");
        }

        sqlBuilder.append("?");
        sqlBuilder.append("); END;");

        return sqlBuilder.toString();
    }

    public <T> List<T> callStoredProcedureWithRefCursor(String packageName, Map<String, Object> parameters, Class<T> dtoClass) {
        String sql = generateStoredProcedureSql(packageName, parameters.keySet());
        List<Object> paramValues = new ArrayList<>(parameters.values());

        List<T> result = jdbcTemplate.execute(sql, (CallableStatementCallback<List<T>>) cs -> {
            cs.registerOutParameter(paramValues.size() + 1, OracleTypes.CURSOR);

            for (int i = 0; i < paramValues.size(); i++) {
                cs.setObject(i + 1, paramValues.get(i));
            }

            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(paramValues.size() + 1);
            List<T> resultList = new ArrayList<>();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                T obj = null;
                try {
                    obj = dtoClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    try {
                        setFieldValue(obj, columnName, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                resultList.add(obj);
            }

            return resultList;
        });

        return result;
    }

    public <T> T callStoredProcedureForSingleResult(String packageName, Map<String, Object> parameters, Class<T> dtoClass) {
        String sql = generateStoredProcedureSql(packageName, parameters.keySet());
        List<Object> paramValues = new ArrayList<>(parameters.values());

        T result = jdbcTemplate.execute(sql, (CallableStatementCallback<T>) cs -> {
            cs.registerOutParameter(paramValues.size() + 1, OracleTypes.CURSOR);

            for (int i = 0; i < paramValues.size(); i++) {
                cs.setObject(i + 1, paramValues.get(i));
            }

            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(paramValues.size() + 1);
            T obj = null;

            if (rs.next()) {
                try {
                    obj = dtoClass.getDeclaredConstructor().newInstance();

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        setFieldValue(obj, columnName, value);
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            return obj;
        });

        return result;
    }


}
