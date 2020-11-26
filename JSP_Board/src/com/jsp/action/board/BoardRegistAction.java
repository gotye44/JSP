package com.jsp.action.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.dto.BoardVO;
import com.jsp.service.BoardService;
import com.jsp.utils.ExceptionLoggerHepler;

public class BoardRegistAction implements Action{

	private BoardService boardService;
	
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "";
		
		String title = request.getParameter("title");
		String writer = request.getParameter("writer");
		String content = request.getParameter("content");
		
		BoardVO board = new BoardVO();
		board.setContent(content);
		board.setTitle(title);
		board.setWriter(writer);
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		try {
			boardService.write(board);
			
			out.println("<script>");
			out.println("alert('새글등록이 정상적으로 되었습니다.')");
			out.println("window.opener.location.href='"+request.getContextPath()+"/board/list.do';");
			out.println("window.close()");
			out.println("</script>");
		} catch (SQLException e) {
			out.println("<script>");
			out.println("alert('새글등록에 실패했습니다')");
			out.println("history.go(-1)");
			out.println("</script>");
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, boardService);
		} finally {
			if(out!=null) out.close();
		}
		
		return url;
	}

}
