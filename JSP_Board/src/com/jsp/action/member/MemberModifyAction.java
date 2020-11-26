package com.jsp.action.member;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jsp.action.Action;
import com.jsp.dto.MemberVO;
import com.jsp.request.MemberModifyRequest;
import com.jsp.service.MemberService;
import com.jsp.utils.ExceptionLoggerHepler;
import com.jsp.utils.GetUploadPath;
import com.jsp.utils.MakeFileName;
import com.jsp.utils.ServletFileUploadBuilder;

public class MemberModifyAction implements Action{

	private MemberService memberService;
	
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "redirect:/member/detail.do?id=";
		
		try {
			MemberVO member = modifyFile(request, response); // 파일을 저장하고 수정할 MemberVO 리턴
			
			url = url+member.getId();
			
			memberService.modify(member);
			
			// 로그인 사용자 수정 여부 확인
			HttpSession session = request.getSession();
			MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
			
			if(member.getId().equals(loginUser.getId())) {
				
				member = memberService.getMember(member.getId());
				session.setAttribute("loginUser", member);
				
				url = "";
				
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("location.href='detail.do?id="+ member.getId() +"'");
				out.println("window.opener.parent.location.reload()");
				out.println("</script>");
				out.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, memberService);
			url = null;
		} catch (Exception e) {
			e.printStackTrace();
			url = null;
		}	
		
		return url;
	}

	
	// 업로드 파일 환경설정
	private static final int MEMORY_THRESHOLD = 1024*500;
	private static final int MAX_FILE_SIZE = 1024*1024*1;
	private static final int MAX_REQUEST_SIZE = 1024*1024*2;
	private MemberVO modifyFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		MemberVO member = null;
		MemberModifyRequest modifyReq = new MemberModifyRequest();
		
		ServletFileUpload upload = ServletFileUploadBuilder.build(request, MEMORY_THRESHOLD, MAX_FILE_SIZE, MAX_REQUEST_SIZE);
		
		// 저장경로
		String uploadPath = GetUploadPath.getUploadPath("member.picture.upload");
		File file = new File(uploadPath);
		if(!file.mkdirs()) {
			System.out.println(uploadPath + "가 이미 존재하거나 생성을 실패했습니다.");
		}
		
		List<FileItem> formItems = upload.parseRequest(request);
		// 파일 개수 확인
		if(formItems != null && formItems.size() >0) {
			for(FileItem item : formItems) { // form items 반복하여 꺼내는 구문
				if(!item.isFormField()) {	// 파일일 경우 해당
					// uuid+구분자+파일명
					String fileName = MakeFileName.toUUIDFIleName(".jpg", "");
					String filePath = uploadPath + File.separator + fileName;
					File storeFile = new File(filePath);
					
					// local HDD에 저장
					item.write(storeFile);
					
					modifyReq.setPicture(storeFile); // 저장된 사진 파일
				}else {	// 텍스트 파라미터
					Class<?> cls = modifyReq.getClass();
					Method[] methods = cls.getMethods();
					
					for(Method method : methods) {
						if(method.getName().contains("set")) {
							String setFieldName = ((method.getName().replace("set", "")).charAt(0)+"").toLowerCase()
													+ method.getName().substring(4);
							if(setFieldName.equals(item.getFieldName())) {
								method.invoke(modifyReq, item.getString("utf-8"));
							}
						}
					}
				}
			}
		}
		// 기존파일 삭제
		if(modifyReq.getUploadPicture()!=null && !modifyReq.getUploadPicture().isEmpty()) {
			File deleteFile = new File(uploadPath, modifyReq.getOldPicture());
			if(deleteFile.exists()) {
				deleteFile.delete();
			}
		}
		return modifyReq.toParseMember();
	}
}
