package com.jsp.action.member;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.dto.MemberVO;
import com.jsp.service.MemberService;
import com.jsp.utils.GetUploadPath;

public class MemberRemoveAction implements Action{

	private MemberService memberService;
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "/member/removeSucess";
		String id = request.getParameter("id");
		
		MemberVO member;
		try {
			// 이미지 파일 삭제
			member = memberService.getMember(id);
			
			String savePath = GetUploadPath.getUploadPath("member.picture.upload");
			File imageFile = new File(savePath, member.getPicture());
			if(imageFile.exists()) {
				imageFile.delete();
			}
			
			// 삭제되는 회원이 로그인 회원인 경우 로그아웃
			MemberVO loginUser = (MemberVO) request.getSession().getAttribute("loginUser");
			if(loginUser.getId().equals(member.getId())) {
				request.getSession().invalidate();
			}
			
			request.setAttribute("member", member);
			
			memberService.remove(id);
		} catch (SQLException e) {
		} finally {
		}
		
		return url;
	}

}
