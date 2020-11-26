package com.jsp.action.common;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.exception.InvalidPasswordException;
import com.jsp.exception.NotFoundIDException;
import com.jsp.service.MemberService;

public class LoginAction implements Action{

	private MemberService memberService;
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "redirect:/index.do";
		
		//로그인 처리
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		try {
			memberService.login(id, pwd, request.getSession());
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (NotFoundIDException | InvalidPasswordException e) {
			url = "redirect:/";
			request.getSession().setAttribute("msg", e.getMessage());
		} 
		
		return url;
	}

}
