package com.jsp.action.notice;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.dto.NoticeVO;
import com.jsp.service.NoticeService;
import com.jsp.utils.ExceptionLoggerHepler;

public class NoticeModifyAction implements Action{

	private NoticeService noticeService;
	
	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "redirect:/notice/detail.do?from=modify&nno=";
		
		int nno = Integer.parseInt(request.getParameter("nno"));
		String title = request.getParameter("title");
		String writer = request.getParameter("writer");
		String content = request.getParameter("content");
		
		NoticeVO board = new NoticeVO();
		board.setNno(nno);
		board.setContent(content);
		board.setTitle(title);
		board.setWriter(writer);
		
		
		try {
			noticeService.modify(board);
			url = url + nno;
			
		} catch (SQLException e) {
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, noticeService);
			url = null;
		} 
		
		return url;
	}

}
