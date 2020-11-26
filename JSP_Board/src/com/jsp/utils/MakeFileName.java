package com.jsp.utils;

import java.util.UUID;

public class MakeFileName {
	
	public static String toUUIDFIleName(String fileName, String delimiter) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid + delimiter + fileName;
	}
}
