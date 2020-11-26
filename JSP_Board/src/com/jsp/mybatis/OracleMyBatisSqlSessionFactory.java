package com.jsp.mybatis;

import java.io.Reader;
import java.sql.Connection;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;

public class OracleMyBatisSqlSessionFactory implements SqlSessionFactory{

	private static SqlSessionFactory sqlSessionFactory;
	static {
		String config = "com/jsp/mybatis/sqlConfig/sqlConfig.xml";
		try {
			Reader reader = Resources.getResourceAsReader(config);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			
			System.out.println("sqlSessionFactory 성공");
		} catch (Exception e) {
			System.out.println("sqlSessionFactory 실패");
			e.printStackTrace();
		}
	}
	@Override
	public Configuration getConfiguration() {
		// TODO Auto-generated method stub
		return sqlSessionFactory.getConfiguration();
	}
	@Override
	public SqlSession openSession() {
		// TODO Auto-generated method stub
		return sqlSessionFactory.openSession();
	}
	@Override
	public SqlSession openSession(boolean autoCommit) {
		// TODO Auto-generated method stub
		return sqlSessionFactory.openSession(autoCommit);
	}
	@Override
	public SqlSession openSession(Connection conn) {
		// TODO Auto-generated method stub
		return sqlSessionFactory.openSession(conn);
	}
	@Override
	public SqlSession openSession(TransactionIsolationLevel transLevel) {
		// TODO Auto-generated method stub
		return sqlSessionFactory.openSession(transLevel);
	}
	@Override
	public SqlSession openSession(ExecutorType exeType) {
		// TODO Auto-generated method stub
		return sqlSessionFactory.openSession(exeType);
	}
	@Override
	public SqlSession openSession(ExecutorType arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return sqlSessionFactory.openSession(arg0,arg1);
	}
	@Override
	public SqlSession openSession(ExecutorType arg0, TransactionIsolationLevel arg1) {
		// TODO Auto-generated method stub
		return sqlSessionFactory.openSession(arg0,arg1);
	}
	@Override
	public SqlSession openSession(ExecutorType arg0, Connection arg1) {
		// TODO Auto-generated method stub
		return sqlSessionFactory.openSession(arg0,arg1);
	}

}
