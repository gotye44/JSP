package com.jsp.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.josephoconnell.html.HTMLInputFilter;

public class XSSRequestWrapper extends HttpServletRequestWrapper{

	private Map<String, String[]> parameterMap = null;
	List<String> crossParamNames = null;
	
	public XSSRequestWrapper(HttpServletRequest request) {
		super(request);
		parameterMap = new HashMap<String, String[]>(request.getParameterMap());
	}
	
	public void inputXSSFilter(List<String> crossParamNames) {
		this.crossParamNames = crossParamNames;
		
		for(String paramName : crossParamNames) {
			String[] paramValues = parameterMap.get(paramName);
			for(int i=0; paramValues != null && i<paramValues.length; i++) {
				paramValues[i] = HTMLInputFilter.htmlSpecialChars(paramValues[i]);
			}
			parameterMap.put(paramName, paramValues);
		}
	}

	
	@Override
	public String getParameter(String name) {
		syncMap();
		return parameterMap.get(name)!=null ? parameterMap.get(name)[0] : null;
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		syncMap();
		return parameterMap;
	}
	
	@Override
	public String[] getParameterValues(String name) {
		syncMap();
		return parameterMap.get(name);
	}

	private void syncMap() {
		Map<String, String[]> requestMap = super.getParameterMap();
		
		Set<String> paramNames = requestMap.keySet();
		
		Iterator<String> it = paramNames.iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			if(!this.crossParamNames.contains(key)) {
				parameterMap.put(key, requestMap.get(key));
			}
		}
	}
}
