package com.jsp.action.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.dto.MemberVO;
import com.jsp.service.MemberService;
import com.jsp.utils.ExceptionLoggerHepler;

public class MemberRegistAction implements Action{

	private MemberService memberService;
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "";
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String email = request.getParameter("email");
		String picture = request.getParameter("picture");
		String authority = request.getParameter("authority");
		String name = request.getParameter("name");
		
		String[] phones = request.getParameterValues("phone");
		String phone = "";
		phone += phones[0]+"-";
		phone += phones[1]+"-";
		phone += phones[2];
				
		MemberVO member = new MemberVO();
		member.setId(id);
		member.setPwd(pwd);
		member.setEmail(email);
		member.setPicture(picture);
		member.setAuthority(authority);
		member.setName(name);
		member.setPhone(phone);
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		try {
			memberService.regist(member);
			
			out.println("<script>");
			out.println("alert('회원등록이 정상적으로 되었습니다.')");
			out.println("window.opener.location.href='"+request.getContextPath()+"/member/list.do';");
			out.println("window.close()");
			out.println("</script>");
		} catch (SQLException e) {
			out.println("<script>");
			out.println("alert('회원등록에 실패했습니다')");
			out.println("history.go(-1)");
			out.println("</script>");
			
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, memberService);
		} finally {
			if(out!=null) out.close();
		}
		
		return url;
	}

}
