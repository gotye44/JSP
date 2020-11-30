package com.jsp.action.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsp.action.Action;
import com.jsp.dto.MemberVO;
import com.jsp.service.MemberService;

public class MemberToJsonAction implements Action{
	
	private MemberService memberService;
	
	public void setMemberService( MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "";
		
		String id = request.getParameter("id").trim();
		
		MemberVO member= null;
		
		try {
			member = memberService.getMember(id);
			ObjectMapper mapper = new ObjectMapper();
			
			response.setContentType("application/json;charset=utf-8");
			
			PrintWriter out = response.getWriter();
			
			out.println(mapper.writeValueAsString(member));
			out.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return url;
	}

}
