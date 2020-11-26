package com.jsp.action.notice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.dto.NoticeVO;
import com.jsp.service.NoticeService;
import com.jsp.utils.ExceptionLoggerHepler;

public class NoticeRegistAction implements Action{

	private NoticeService noticeService;
	
	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "";
		
		String title = request.getParameter("title");
		String writer = request.getParameter("writer");
		String content = request.getParameter("content");
		
		NoticeVO board = new NoticeVO();
		board.setContent(content);
		board.setTitle(title);
		board.setWriter(writer);
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		try {
			noticeService.write(board);
			
			out.println("<script>");
			out.println("alert('공지등록이 정상적으로 되었습니다.')");
			out.println("window.opener.location.href='"+request.getContextPath()+"/notice/list.do';");
			out.println("window.close()");
			out.println("</script>");
		} catch (SQLException e) {
			out.println("<script>");
			out.println("alert('공지등록에 실패했습니다')");
			out.println("history.go(-1)");
			out.println("</script>");
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, noticeService);
		} finally {
			if(out!=null) out.close();
		}
		
		return url;
	}

}
