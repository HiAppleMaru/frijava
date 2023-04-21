package com.exam.member;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MemberDaoBatis implements MemberDao {
	SqlSessionFactory sqlSessionFactory;
	{//CTRL+Shift+O : 자동 import
		
		try { //예외처리함
			//마이바티스 전체 설정파일 위치 (클래스패스 기준)
			String resource = "batis/mybatis-config.xml"; //마이바티스 전체 설정파일 위치(이것만 바뀔 수 있음)
			InputStream inputStream = Resources.getResourceAsStream(resource); 
			//설정파일의 내용대로 SqlSessionFactory(마이바티스본체)를 생성		
			 
			//sqlSessionFactory 객체 생성
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		}  
		catch (IOException e) {
		e.printStackTrace();
		}
	}
	
	
	@Override
	public List<MemberVo> selectMemberList() {
		List<MemberVo> list = null; //new ArrayList<MemberVo>(); 이렇게 초기값을 줘도 됨. 
		//지금은 단순히 명령어를 내리는것이지 데이터값도 같이 주는게 아니기 때문..
		try (SqlSession session = sqlSessionFactory.openSession()) {
			//실행할 SQL문과 동일한 이름의 메서드를 사용하여 SQL문 실행
			//SELECT결과가 1행인 경우 selectOne, 2행이상인 경우 selectList 메서드 사용 
			//첫번째 인자로 실행할 SQL문의 고유한 이름을 전달
			//두번째 인자로 SQL문 실행시 필요한 데이터를 담은 객체를 전달
			list = session.selectList("com.exam.member.MemberDao.selectMemberList");
			}
		return list;
	}

	@Override
	public int insertMember(MemberVo vo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteMember(String memId) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
