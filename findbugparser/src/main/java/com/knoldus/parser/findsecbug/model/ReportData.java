package com.knoldus.parser.findsecbug.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportData {
	Map<String, Map<String, List<String>>> parsedMap = new HashMap<>();
	int size;
	String message;
	public Map<String, Map<String, List<String>>> getParsedMap() {
		return parsedMap;
	}
	public void setParsedMap(Map<String, Map<String, List<String>>> parsedMap) {
		this.parsedMap = parsedMap;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "ReportData [parsedMap=" + parsedMap + ", size=" + size + ", message=" + message + "]";
	}
	
}
