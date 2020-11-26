package com.jsp.action.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.service.BoardService;
import com.jsp.utils.ExceptionLoggerHepler;

public class BoardRemoveAction implements Action{

	private BoardService boardService;
	
	public void setboardService(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "";
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		try {
			boardService.remove(bno);
			
			out.println("<script>");
			out.println("alert('삭제되었습니다.')");
			out.println("window.opener.location.reload(true);");
			out.println("window.close()");
			out.println("</script>");
		} catch (SQLException e) {
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, boardService);
			url = null;
		}
		
		
		return url;
	}

}
