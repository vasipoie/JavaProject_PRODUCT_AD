package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.ScanUtil;
import dao.ProductDao;
import vo.ProductVo;

public class ProductService {
	//얕은복사
	static public Map<String, Object> sessionStorage = Controller.sessionStorage;
	
	 static ProductService instance = null;
	 ProductService() {}
	public static ProductService getInstance() {
		if(instance == null) 
			instance = new ProductService();
		return instance;
	}
	
	// Dao를 부른다
	ProductDao prodDao = ProductDao.getInstance();
	
	
	public List<ProductVo> stock() {
		return prodDao.stock();
	}
	public void stockModify(int prod_no) {
		prod_no = (int)sessionStorage.get("no");
		String name = ScanUtil.nextLine("상품명 : ");
		int price = ScanUtil.nextInt("가격 : ");
		int prodi = ScanUtil.nextInt("입고수량 : ");
		int prodo= ScanUtil.nextInt("출고수량 : ");
		String prodexp= ScanUtil.nextLine("상품 설명 : ");
		
		List<Object> param = new ArrayList<Object>();
		param.add(name);
		param.add(price);
		param.add(prodi);
		param.add(prodo);
		param.add(prodexp);
		param.add(prod_no);
		
		stockUpdate(param);
	}
	public List<ProductVo> prodSearchList(List<Object> param) {
		return prodDao.prodSearchList(param);
	}
	 void stockUpdate(List<Object> param) {
		prodDao.stockUpdate(param);
	}
	
	 
	 
	public List<ProductVo> prodList(List<Object> param) {
		return prodDao.prodListParam(param);
	} 
	 
	public boolean prodCreate(List<Object> param) {
		if(prodDao.prodCreate(param) !=0) {
			return true;
		}
		return false;
	}
	

	
	
	
}
