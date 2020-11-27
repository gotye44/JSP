package com.jsp.service;

import java.sql.SQLException;
import java.util.Map;

import com.jsp.dto.ReplyVO;
import com.jsp.request.SearchCriteria;

public interface ReplyService {
	
	Map<String, Object> getReplyList(int bno, SearchCriteria cri) throws SQLException;
	
	void registReply(ReplyVO reply) throws SQLException;
	
	void modifyReply(ReplyVO reply) throws SQLException;
	
	void removeReply(int rno) throws SQLException;
}
