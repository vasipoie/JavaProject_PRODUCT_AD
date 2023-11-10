package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import util.ScanUtil;
import util.View;
import print.Print;
import service.ProductService;
import vo.ProductVo;

public class Controller extends Print{
	
	static public Map<String, Object> sessionStorage = new HashMap<>();

	ProductService productService = ProductService.getInstance();
	
	Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		new Controller().start();
	}

	 void start() {
		View view = View.HOME;
		while(true){
			switch(view) {
			case HOME:
				view = home();
				break;
			case ADMIN:	//관리자페이지
				view = admin();
				break;
			case PROD_ALL:	//전체상품리스트조회
				view = prodAll();
				break;
			case MEMBER: //일반회원
				view = member();
				break;
			case ADMIN_LOGIN: //관리자 로그인
				view = home();
				break;
			case PROD_CREATE: //신규 상품 등록
				view = prodCreate();
				break;
			case PROD_UPDATE: //상품 정보 수정
				view = home();
				break;
			case PROD_DELETE: //상품 정보 삭제
				view = home();
				break;
			case MEMBER_LOGIN: //회원 로그인
				view = home();
				break;
			case MEMBER_PRODBUY: //상품구매
				view = home();
				break;
			case MEMBER_PRODCHECK: //구매상품조회
				view = home();
				break;
			case MEMBER_PRODBAG: //장바구니
				view = home();
				break;
			case MEMBER_SIGNUP: //회원가입
				view = home();
				break;
				
			}
		}
	}
	

	
	

	//1.관리자-2.재고관리-1.재고수정
//	private View prodMod() {
//		List<ProductVo> list = productService.stock();
//		pirntStockList(list);
//		System.out.println("-----------------------------");
//		int prod_no  = ScanUtil.nextInt("수정을 원하는 No를 선택하세요\s");
//		System.out.println("-----------------------------");
//		String mod = ScanUtil.nextLine(prod_no+"번 상품 수정을 원하십니까?(Y/N)\s");
//		if(mod.equals("Y")||mod.equals("y")) {
//			sessionStorage.put("no", prod_no);
//			productService.stockModify(prod_no);
//		}
//		System.out.println(prod_no+"번 상품이 수정되었습니다");
//		return View.PROD_MOD;
//	}


	//1.관리자-2.재고관리
//	 View prodManage() {
//		List<Product> list = productService.stock();
//		pirntStockList(list);
//		printStock();
//		System.out.println("-----------------------------");
//		int select  = ScanUtil.nextInt("원하는 번호를 선택하세요\s");
//		System.out.println("-----------------------------");
//		switch(select){
//		case 1:
//			return View.PROD_MOD;
//		case 2:
//			return View.PROD_SOLDOUT;
//		case 3:
//			return View.ADMIN;
//		case 4:
//			return View.HOME;
//		default :
//			return View.ADMIN;
//		}
//	}
	
	//1.관리자-2.재고관리
//	private View prodManage() {
//		List<ProductVo> list = productService.stock();
//		pirntStockList(list);
//		String name = ScanUtil.nextLine("검색하고싶은 상품명을 입력하세요\s");
//		List<Object> param = new ArrayList();
//		param.add(name);
//		List<ProductVo> l = productService.prodSearchList(param);
////		System.out.println(l);
//		printSelectSearch(l);
//		return View.PROD_MOD2;
//	}

	//1.관리자-1.신규상품등록
	private View prodCreate() {
		printVar();
		System.out.println("새로운 상품을 등록합니다");
		List<Object> param = new ArrayList<Object>();
		String name = ScanUtil.nextLine("상품 이름>>> ");
		String exp = ScanUtil.nextLine("상품 설명>>> ");
		int price = ScanUtil.nextInt("상품 가격>>> ");
		int stock = ScanUtil.nextInt("상품 재고>>> ");
		param.add(name);
		param.add(exp);
		param.add(price);
		param.add(stock);
		boolean chk = productService.prodCreate(param);
		printVar();
		System.out.println("상품이 등록됐습니다");
		printVar();
		System.out.println("상품 전체 리스트에서 확인하세요");
		return View.PROD_ALL;
	}
	
	//1.관리자-전체 상품 조회
	private View prodAll() {
		List<Object> param = new ArrayList<Object>();
		int pageNo = 1;
		if(sessionStorage.containsKey("pageNo")) {
			pageNo = (int) sessionStorage.get("pageNo");
		}
		int start_no = 1+10*(pageNo-1);
		int last_no = 10*pageNo;
		param.add(start_no);
		param.add(last_no);
		List<ProductVo> list = productService.prodList(param);
		printList(list);	//페이징처리
		int select = ScanUtil.nextInt("원하는 번호를 선택하세요\s");
		switch(select) {
		case 1:
			if(10*pageNo > last_no) {
				pageNo = pageNo-1;
			}
			sessionStorage.put("pageNo", pageNo+1);
			return View.PROD_ALL;
		case 2:
			if(pageNo-1 <= 0) {
				System.out.println("첫 페이지 입니다");
				pageNo = 2;
			}
			sessionStorage.put("pageNo", pageNo-1);
			return View.PROD_ALL;
		case 3:
			sessionStorage.remove("pageNo");
			return View.ADMIN;
		default:
			return View.ADMIN;
		}
	}

	//일반회원 페이지
	private View member() {
		printMember();
		int select  = ScanUtil.nextInt("원하는 번호를 선택하세요\s");
		switch(select) {
		case 0:
			return View.MEMBER_LOGIN;
		case 1:
			return View.MEMBER_PRODBUY;
		case 2:
			return View.MEMBER_PRODCHECK;
		case 3:
			return View.MEMBER_PRODBAG;
		case 4:
			return View.MEMBER_SIGNUP;
		default :
			return View.MEMBER;
		}
	}
	
	//관리자 페이지
	private View admin() {
		printAdmin();
		int select  = ScanUtil.nextInt("메뉴를 선택하세요\s");
		switch(select){
		case 0:
			return View.ADMIN_LOGIN;
		case 1:
			return View.PROD_CREATE;
		case 2:
			return View.PROD_UPDATE;
		case 3:
			return View.PROD_DELETE;
		case 4:
			return View.HOME;
		default :
			return View.ADMIN;
		}
	}

	private View home() {
		printHome();
		int select = ScanUtil.nextInt("메뉴를 선택하세요.\s");
		switch(select) {
		case 1:
			return View.ADMIN;
		case 2:
			return View.MEMBER;
		default :
			return View.HOME;
		}
	}
	
}
