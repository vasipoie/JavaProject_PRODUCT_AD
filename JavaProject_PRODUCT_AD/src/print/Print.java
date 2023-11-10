package print;

import java.util.List;

import vo.ProductVo;

public class Print {
	
		public void printVar() {
			System.out.println("-----------------------------");
		}

		public void printHome() {
			printVar();
			System.out.println("1. 관리자");
			System.out.println("2. 일반 회원");
			printVar();
		}
		
		public void printAdmin() {
			printVar();
			System.out.println("0. 관리자 로그인");
			System.out.println("1. 신규 상품 등록");
			System.out.println("2. 상품 정보 수정");
			System.out.println("3. 상품 정보 삭제");
			System.out.println("4. 뒤로가기");
			printVar();
		}
		
		public void printMember() {
			printVar();
			System.out.println("0. 회원 로그인");
			System.out.println("1. 상품 구매");
			System.out.println("2. 구매 상품 조회");
			System.out.println("3. 장바구니");
			System.out.println("4. 회원가입");
			printVar();
		}
		
		public void printList(List<ProductVo> l) {
			//전체 상품 조회
			printVar();
			System.out.println("No\t상품명\t가격\t재고\t상세설명");
			for(ProductVo product : l) {
				System.out.printf("%d\t%s\t%d\t%d\t%s\n"
						, product.getProd_no(), product.getProd_name()
						, product.getProd_price(), product.getProd_count()
						, product.getProd_exp());
			}
			printVar();
			System.out.println("1. 다음페이지");
			System.out.println("2. 이전페이지");
			System.out.println("3. 홈");
			printVar();
		}
		
			// 선택 메뉴 출력
//			System.out.println("1. 신규 상품 등록");
//			System.out.println("2. 상품 정보 수정");
//			System.out.println("3. 상품 정보 삭제");
//			System.out.println("4. 뒤로가기");
		
		
		
		
		public void printStock() {
			printVar();
			System.out.println("1. 재고 수정");
			System.out.println("2. 품절 추가");
			printVar();
		}
}
