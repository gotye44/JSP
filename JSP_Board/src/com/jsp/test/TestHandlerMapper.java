package com.jsp.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jsp.dispatcher.HandlerMapper;

public class TestHandlerMapper {

	private HandlerMapper handlerMapper;
	
	@Before
	public void init() throws Exception {
		setHandlerMapper(new HandlerMapper());
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@After
	public void complete() {
		
	}

	public HandlerMapper getHandlerMapper() {
		return handlerMapper;
	}

	public void setHandlerMapper(HandlerMapper handlerMapper) {
		this.handlerMapper = handlerMapper;
	}

}
