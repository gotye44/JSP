package com.jsp.action.member;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jsp.action.Action;
import com.jsp.dto.MemberVO;
import com.jsp.service.MemberService;

public class MemberDisableAction implements Action{
	
	private MemberService memberService;
	
	public void setMemberservice(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "/member/stopSuccess";
		
		String id = request.getParameter("id");
		
		HttpSession session = request.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		if(id.equals(loginUser.getId())) {
			url = "/member/stopFail";
		}else {
			try {
				memberService.disabled(id);
			} catch (SQLException e) {
				e.printStackTrace();
				url = null;
			}
		}
		request.setAttribute("id", id);
		return url;
	}

}
