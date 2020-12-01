package com.jsp.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.jsp.request.XSSRequestWrapper;


public class CrossSiteScriptFilter implements Filter {

	private List<String> crossParamNames = new ArrayList<String>();
	private List<String> includeURLs = new ArrayList<String>();
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpReq = (HttpServletRequest) request;
		String url = httpReq.getRequestURI();
		for(String includeURL : includeURLs) {
			if(url.contains(includeURL)) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				XSSRequestWrapper requestWrapper = new XSSRequestWrapper((HttpServletRequest) request);
				requestWrapper.inputXSSFilter(crossParamNames);
				chain.doFilter(requestWrapper, response);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
		// 적용할 URL setting
		String includeURLsParams = config.getInitParameter("includeURLs");
		StringTokenizer token = new StringTokenizer(includeURLsParams,",");
		
		while(token.hasMoreElements()) {
			includeURLs.add(token.nextToken());
		}
		
		
		// HTML filter 적용할 parameter
		String paramNames = config.getInitParameter("XSSParameter");
		token = new StringTokenizer(paramNames,",");
		
		while(token.hasMoreElements()) {
			crossParamNames.add(token.nextToken());
		}
	}
}
