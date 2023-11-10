package dao;

import java.util.List;
import java.util.Map;

import util.ConvertUtils;
import util.JDBCUtil;
import vo.ProductVo;

public class ProductDao {
	// 싱글톤 패턴을 만든다.
		 static ProductDao instance = null;
		 ProductDao() {}
		public static ProductDao getInstance() {
			if(instance == null) 
				instance = new ProductDao();
			return instance;
		}
		
		// JDBC를 부른다.
		JDBCUtil jdbc = JDBCUtil.getInstance();
		
		public List<ProductVo> prodListParam(List<Object> param) {
			String sql = "select * \r\n" + 
					"from (select rownum rnum, a.*\r\n" + 
					"      from (select *\r\n" + 
					"            from prod)a)\r\n" + 
					"where rnum >= ? and rnum <=?";
			List<Map<String, Object>> l = jdbc.selectList(sql, param);
			return ConvertUtils.convertToList(l, ProductVo.class);
		}
		
		public List<ProductVo> admin() {
			String sql = "select no, name, price, prodbase, prodi, prodo, \r\n" + 
						"(prodbase+prodi-prodo) prodj, SUBSTR(prodexp,0,10) prodexp\r\n" + 
						"from product";
			List<Map<String, Object>> l = jdbc.selectList(sql);
			return ConvertUtils.convertToList(l, ProductVo.class);
		}
		public List<ProductVo> stock() {
			String sql = "select no, name, price, prodbase, prodi, prodo, "
					+ 	 "(prodbase+prodi-prodo) prodj, prodexp\r\n" 
					+ 	 "from product";
			List<Map<String, Object>> l = jdbc.selectList(sql);
			return ConvertUtils.convertToList(l, ProductVo.class);
		}
		public void stockUpdate(List<Object> param) {
			String sql = "update product\r\n" + 
					"set name = ?,\r\n" + 
					"    price = ?,\r\n" + 
					"    prodi = ?,\r\n" + 
					"    prodo = ?,\r\n" + 
					"    prodexp = ?\r\n" + 
					"where no = ?";
			jdbc.update(sql,param);
		}
		public List<ProductVo> prodSearchList(List<Object> param) {
			String sql = "select no, name, price, prodbase, prodi, prodo, \r\n" + 
					"        (prodbase+prodi-prodo) prodj, SUBSTR(prodexp,0,10) prodexp\r\n" + 
					"from product\r\n" + 
					"where name like'%'||?||'%'";
			List<Map<String, Object>> l = jdbc.selectList(sql,param);
			return ConvertUtils.convertToList(l, ProductVo.class);
		}
		
		
		public int prodCreate(List<Object> param) {
			String sql = "insert into prod\r\n" + 
						"values((select count(*) prod_no from prod)+1,\r\n" + 
						"        ?, ?, ?, ?)";
			return jdbc.update(sql, param);
		}
	
		
		
		
		
		
		
}
