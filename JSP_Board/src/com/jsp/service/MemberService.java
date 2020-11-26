package com.jsp.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.jsp.dto.MemberVO;
import com.jsp.exception.InvalidPasswordException;
import com.jsp.exception.NotFoundIDException;
import com.jsp.request.SearchCriteria;


public interface MemberService {
	
	//로그인
	void login(String id, String pwd,HttpSession session) throws SQLException, NotFoundIDException, InvalidPasswordException;
	
	//회원 리스트 조회
	List<MemberVO> getMemberList(SearchCriteria cri) throws SQLException;
	
	Map<String , Object> getSearchMemberList(SearchCriteria cri) throws SQLException;
	
	//회원정보 조회
	MemberVO getMember(String id) throws SQLException;
	
	//회원 등록
	void regist(MemberVO member) throws SQLException;
	
	//회원 수정
	void modify(MemberVO member) throws SQLException;
	
	//회원 삭제
	void remove(String id) throws SQLException;
	
	//회원 정지
	void disabled(String id) throws SQLException;
	
	//회원 활성
	void enabled(String id) throws SQLException;
	
}
