package com.qualitestgroup.util.db;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum ConnectionType {
	ORACLE("Oracle"), MS_SQL("MSSQL");
	
	private static Set<String> values;
	
	static {
		values = new HashSet<>();
		for (ConnectionType type : ConnectionType.values()) {
			values.add(type.text);
		}
		values = Collections.unmodifiableSet(values);
	}
	
	private String text;
	
	ConnectionType(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}
	
	/**
	 * Convert a string to ConnectionType. If type does not match predefined values, return ORACLE by default.
	 * @param type
	 * @return ConnectionType
	 */
	public static ConnectionType fromString(String type) {
		if (type != null) {
			for (ConnectionType ctype : ConnectionType.values()) {
				if (type.equalsIgnoreCase(ctype.text)) {
					return ctype;
				}
			}
		}
		
		return ORACLE;
	}
	
	public static Set<String> validValues() {
		return values;
	}
}
