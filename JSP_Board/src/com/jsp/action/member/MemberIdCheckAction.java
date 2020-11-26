package com.jsp.action.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.service.MemberService;

public class MemberIdCheckAction implements Action{

	private MemberService memberService;
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String url = "";
		
		String id = request.getParameter("id").trim();
		
		System.out.println(id);
		PrintWriter out = response.getWriter();
		
		try {
			out.print(memberService.getMember(id) !=null ? "" : id);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} finally {
			if(out!=null) out.close();
		}
		
		return url;
	}

}
