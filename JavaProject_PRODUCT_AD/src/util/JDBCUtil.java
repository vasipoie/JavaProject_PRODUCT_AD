package util;

import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {
	/*
	 * JDBC를 좀 더 쉽고 편하게 사용하기 위한 Utility 클래스
	 * 
	 * Map<String, Object> selectOne(String sql)
	 * Map<String, Object> selectOne(String sql, List<Object> param)
	 * List<Map<String, Object>> selectList(String sql)
	 * List<Map<String, Object>> selectList(String sql, List<Object> param)
	 * int update(String sql)
	 * int update(String sql, List<Object> param)
	 * */
	
	// 싱글톤 패턴 : 인스턴스의 생성을 제한하여 하나의 인스턴스만 사용하는 디자인 패턴
	
	// 인스턴스를 보관할 변수
	 static JDBCUtil instance = null;
	// JDBCUtil 객체를 만들 수 없게(인스턴스화 할 수 없게) 으로 제한함
	 JDBCUtil() {} 
	public static JDBCUtil getInstance() {
		if(instance == null) 
			instance = new JDBCUtil();
		return instance;
	}
	
	public void init() {
		
	}

	
	public static void main(String[] args) {
		JDBCUtil jdbc = new JDBCUtil();
		String sql = "SELECT * FROM PROD";
		//db컬럼 가져오는거
		List<Map<String,Object>> list = jdbc.selectList(sql);
		for(Map<String, Object> map : list) {
//			String PROD_NAME = (String) map.get("PROD_NAME");
//			if(PROD_NAME.contains("컴퓨터"))
//			System.out.println(map);
//		}
			String PROD_BUYER = (String) map.get("PROD_BUYER");
			if(PROD_BUYER.equals("P10102")) {
				System.out.println(map);
			}
		}
	}
	
	 String url = "jdbc:oracle:thin:@192.168.143.34:1521:xe";
	 String user = "pc02_java";
	 String password = "java";
	
	 Connection conn = null;
	 ResultSet rs = null;
	 PreparedStatement ps = null;
	
	public List<Map<String, Object>> selectList(String sql, List<Object> param){
		// sql => "SELECT * FROM MEMBER WHERE MEM_ADD1 LIKE '%'||?||'%'"
		// sql => "SELECT * FROM JAVA_BOARD WHERE WRITER=?"
		// sql => "SELECT * FROM JAVA_BOARD WHERE BOARD_NUM > ?"
		List<Map<String, Object>> result = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				if(result == null) result = new ArrayList<>();
				Map<String, Object> row = new HashMap<>();
				for(int i = 1; i <= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				result.add(row);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(conn != null) try { conn.close(); } catch(Exception e) {}
		}
		return result;
	}
	
	public <T> List<T> selectList1(String sql, List<Object> param){
		// sql => "SELECT * FROM MEMBER WHERE MEM_ADD1 LIKE '%'||?||'%'"
		// sql => "SELECT * FROM JAVA_BOARD WHERE WRITER=?"
		// sql => "SELECT * FROM JAVA_BOARD WHERE BOARD_NUM > ?"
		List<Map<String, Object>> convertMaps = selectList(sql, param);
        List<T> convertValueObjects = (List<T>) ConvertUtils.convertToList(convertMaps, Object.class);
		return convertValueObjects;
	}
	
	public List<Map<String, Object>> selectList(String sql){
		// sql => "SELECT * FROM MEMBER"
		// sql => "SELECT * FROM JAVA_BOARD"
		// sql => "SELECT * FROM JAVA_BOARD WHERE BOARD_NUM > 10"
		List<Map<String, Object>> result = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				if(result == null) result = new ArrayList<>();
				Map<String, Object> row = new HashMap<>();
				for(int i = 1; i <= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				result.add(row);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(ps != null) try { ps.close(); } catch(Exception e) {}
			if(conn != null) try { conn.close(); } catch(Exception e) {}
		}
		return result;
	}
	/*
	 * int : 변경된 수
	 */
	public int update(String sql, List<Object> param) {
		// sql => "DELETE FROM JAVA_BOARD WHERE BOARD_NUMBER=?"
		// sql => "UPDATE JAVA_BOARD SET TITLE='하하' WHERE BOARD_NUMBER=?"
		// sql => "INSERT MY_MEMBER (MEM_ID, MEM_PASS, MEM_NAME) VALUES (?, ?, ?)"
		int result = 0;
		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}
			result = ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) try {  rs.close();  } catch (Exception e) { }
			if(ps != null) try {  ps.close();  } catch (Exception e) { }
			if(conn != null) try { conn.close(); } catch (Exception e) { }
		}
		return result;
	}
	public int update(String sql) {
		// sql => "DELETE FROM JAVA_BOARD"
		// sql => "UPDATE JAVA_BOARD SET TITLE='하하'"
		// sql => "UPDATE JAVA_BOARD SET TITLE='하하' WHERE BOARD_NUMBER=1"
		// sql => "INSERT MY_MEMBER (MEM_ID, MEM_PASS, MEM_NAME) VALUES ('admin', '1234', '홍길동')"
		int result = 0;
		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			result = ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) try {  rs.close();  } catch (Exception e) { }
			if(ps != null) try {  ps.close();  } catch (Exception e) { }
			if(conn != null) try { conn.close(); } catch (Exception e) { }
		}
		return result;
	}
	
	
	
	public Map<String, Object> selectOne(String sql, List<Object> param){
		// sql => "SELECT * FROM JAVA_BOARD WHERE BOARD_NUMBER=?"
		// param => [1]
		//
		// sql => "SELECT * FROM JAVA_BOARD WHERE WRITER=? AND TITLE=?"
		// param => ["홍길동", "안녕"]
		Map<String, Object> row = null;
		
		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++) {
				ps.setObject(i + 1, param.get(i));
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				row = new HashMap<>();
				for(int i = 1; i <= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					Object value = rs.getObject(i);
					row.put(key,value);
				}
				// {DATETIME=2022-08-05 16:35:08.0, WRITER=홍길동, TITLE=안녕하세요, CONTENT=안녕안녕, BOARD_NUMBER=1}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) try {  rs.close();  } catch (Exception e) { }
			if(ps != null) try {  ps.close();  } catch (Exception e) { }
			if(conn != null) try { conn.close(); } catch (Exception e) { }
		}
		return row;
	}
	public Map<String, Object> selectOne(String sql){
		// sql => "SELECT * FROM JAVA_BOARD 
		//			WHERE BOARD_NUMBER=(SELECT MAX(BOARD_NUMBER) FROM JAVA_BOARD)"
		// sql => "SELECT * FROM MEMBER MEM_ID='a001' AND MEM_PASS='123'"
		Map<String, Object> row = null;
		
		try {
			conn = DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				row = new HashMap<>();
				for(int i = 1; i <= columnCount; i++) {
					String key = rsmd.getColumnLabel(i);
					// getColumnName vs getColumnLabel
					// getColumnName : 원본 컬럼명을 가져옴
					// getColumnLabel : as로 선언된 별명을 가져옴, 없으면 원본 컬럼명
					Object value = rs.getObject(i);
					row.put(key,value);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) try {  rs.close();  } catch (Exception e) { }
			if(ps != null) try {  ps.close();  } catch (Exception e) { }
			if(conn != null) try { conn.close(); } catch (Exception e) { }
		}
		
		return row;
	}
}






