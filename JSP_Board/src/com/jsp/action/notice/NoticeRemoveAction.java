package com.jsp.action.notice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.service.NoticeService;
import com.jsp.utils.ExceptionLoggerHepler;

public class NoticeRemoveAction implements Action{

	private NoticeService noticeService;
	
	public void setnoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "";
		
		int nno = Integer.parseInt(request.getParameter("nno"));
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		try {
			noticeService.remove(nno);
			
			out.println("<script>");
			out.println("alert('삭제되었습니다.')");
			out.println("window.opener.location.reload(true);");
			out.println("window.close()");
			out.println("</script>");
		} catch (SQLException e) {
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, noticeService);
			url = null;
		}
		
		
		return url;
	}

}
