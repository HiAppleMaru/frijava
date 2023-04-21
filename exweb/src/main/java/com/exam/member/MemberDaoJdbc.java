package com.exam.member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDaoJdbc implements MemberDao { //MemberDao -> Ctrl+Alt+R 로 MemberDaoJdbc로 바꿔줌.
	//Data Access Object -> DAO
	//분리한 메소드들을 다 여기로 옮길차례...
	
	String url = "jdbc:oracle:thin:@localhost:1521:xe"; //데이터베이스 서버 주소
	String user = "web"; //데이터베이스 접속 아이디
	String password = "web01"; //데이터베이스 접속 비밀번호 
	//처음에 서블릿 객체가 요청이 올때 한번만 읽을 수 있도록 .....
	
	@Override
	public List<MemberVo> selectMemberList() {
		//이제 db관련 코드 분리할차례...
		List<MemberVo> list = new ArrayList<MemberVo>(); //칸이 여러개 있는 공간 만들기, 한칸 한칸에 한명씩 한명씩
		
		String sql = "SELECT mem_id, mem_Pass, mem_Name, mem_Point FROM MEMBER";
		//try () 내부에 선언된 변수의 값은
		//try-catch 블럭의 실행이 완료된 후 자동으로 close() 매서드 실행
		try(
			//지정한 데이터베이스에 접속(로그인)
			Connection conn = DriverManager.getConnection(url, user, password);//지정한 데이터베이스에 접속(로그인)
			//해당 연결을 통해 실행할 SQL문을 담은 명령문 객체 생성
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery(); //반환값은 조회 결과 레코드(row)들
			//rs의 메모리도 반납해야됨...
				
			) {
		//SQL문 실행 (SELECT문 실행은 executeQuery()메서드 사용) 
			//처음 ResultSet 객체는 첫 레코드(row) 이전을 가리키고 있음
			// .next() 메서드를 실행하면 다음 레코드를 가리키게 된다.
			// .next() 메서드는 다음 레코드가 있으면 ture를 반환하고, 없으면 false를 반환
		while (rs.next()) {
			MemberVo vo = new MemberVo(); //while문 돌때마다 vo에 Id,Pass,Name,Point값을 저장하게 됨. 
										  //지금은 1개씩의 값밖에 저장... while문 돌다가 마지막 값만 최종 저장됨..
										  //차례대로 저장할 방법 써야됨...
			//컬럼값의 데이터타입에 따라서 get타입("컬럼명") 메서드를 사용하여 컬럼값 읽기
		    vo.setMemId(rs.getString("mem_id")); //현재 가리키는 레코드(row)의 "mem_id"컬럼값 읽기
		    vo.setMemPass(rs.getString("mem_pass")); //현재 가리키는 레코드(row)의 "mem_pass"컬럼값 읽기
		    vo.setMemName(rs.getString("mem_name")); //현재 가리키는 레코드(row)의 "mem_name"컬럼값 읽기
		    vo.setMemPoint(rs.getInt("mem_point")); //현재 가리키는 레코드(row)의 "mem_id"컬럼값 읽기
			list.add(vo);
		}
		//배열이나 리스트에 while문에서 읽은 값을 저장해야됨... 한명의 정보 Id,Pass,Name,Point를 묶어서 저장할 수 있는 방법을 생각...
		//기존에 없는 데이터타입을 만들고 싶을때... 클래스 작성
		//클래스 -> 1. 현실에 실존하는 사물을 묘사
		//		  2. 나만의 가상의 형식 제작
		
	}   catch (SQLException e) {
		e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public int insertMember(MemberVo vo) {
		String sql = "insert into member ( mem_id, mem_pass, mem_name, mem_point ) "
				+ "values ( ?, ?, ?, ? )";
		
		//try () 내부에 선언된 변수의 값은
		//try-catch 블럭의 실행이 완료된 후 자동으로 close() 매서드 실행
		
		int n=0; 
		try(
				//지정한 데이터베이스에 접속(로그인)
				Connection conn = DriverManager.getConnection(url, user, password);//지정한 데이터베이스에 접속(로그인)
				//해당 연결을 통해 실행할 SQL문을 담은 명령문 객체 생성
				PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			//pstmt 명령문 객체에 담겨 있는 SQL문의 ?에 값을 채워넣기
			//채워넣는 값의 타입에 따라서 set타입명() 메서드 사용
			pstmt.setString(1, vo.getMemId()); //1번째 ?에 memId 값을 넣기
			pstmt.setString(2, vo.getMemPass()); //2번째 ?에 memPass 값을 넣기
			pstmt.setString(3, vo.getMemName()); //3번째 ?에 memName 값을 넣기
			pstmt.setInt(4, vo.getMemPoint()); //4번째 ?에 memPoint 값을 넣기
			
			//SQL문 실행 (INSERT, UPDATE, DELETE 문 실행은 executeUpdate()메서드 사용) 
			n = pstmt.executeUpdate(); //반환값은 SQL문 실행으로 영향받은 레코드(row) 수
			//System.out.println( n + "명의 회원 추가 성공");
		}		 catch (SQLException e) {
					e.printStackTrace();}
		return n;
	}
	@Override
	public int deleteMember(String memId) {
		String sql = "delete from member where mem_id = ? ";
				
		
		int n=0; 
		try(
				Connection conn = DriverManager.getConnection(url, user, password);//지정한 데이터베이스에 접속(로그인)
				PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			pstmt.setString(1, memId); //1번째 ?에 memId 값을 넣기
			
			//SQL문 실행 (INSERT, UPDATE, DELETE 문 실행은 executeUpdate()메서드 사용) 
			n = pstmt.executeUpdate(); //반환값은 SQL문 실행으로 영향받은 레코드(row) 수
		}
			 catch (SQLException e) {
					e.printStackTrace();}
		return n;
	}

	
	
}
