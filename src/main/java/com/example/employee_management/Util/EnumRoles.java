package com.example.employee_management.Util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumRoles {
	ADMIN("1","ADMIN"),
	MANAGER("2","MANAGER"),
	USER("3","USER"),
	MANAGER_DEV("4","MANAGER_DEV"),
	MANAGER_HR("5","MANAGER_HR"),
	MANAGER_FINANCE("6","MANAGER_FINANCE")
	;

	private final String code;
	private final String text;

}
