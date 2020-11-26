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

public class NoticeDetailAction implements Action{

	private NoticeService noticeService;
	
	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "/notice/detail";
		
		int nno = Integer.parseInt(request.getParameter("nno"));
		String from = request.getParameter("from");
		NoticeVO notice = new NoticeVO();
		try {
			if(from !=null && from.equals("modify")) {
				notice = noticeService.getNoticeModify(nno);
			}else {
				notice = noticeService.getNotice(nno);
			}
			request.setAttribute("notice", notice);
		} catch (SQLException e) {
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, noticeService);
			url = null;
		}
		
		return url;
	}

}
