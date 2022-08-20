# Libro - bookstore

## 1.설명
* Libro 는 이탈리아어로 book이라는 뜻을 가집니다.
* 이 프로젝트는 온라인 서점 플랫폼입니다.

## 2.요구사항
* 스프링 시큐리티를 통한 로그인, 회원가입, 권한부여(접근 권한 설정)
* 검색/페이징 가능해야함
* 각 상품마다 리뷰 게시판이 존재해야함. 리뷰는 댓글 형식으로
* 상품 카테고리별로 상품을 보여주는 카테고리 게시판이 존재해야함
* 카테고리 게시판에서는 좋아요(인기순)으로 정렬해야함. 좋아요가 없다면 id desc 기준으로 정렬함.
* 고객은 등급이 존재하고 마이페이지에 보여줘야함(주문건수 기준)
* 주문한 상품 리스트가 존재해야함. 주문7일 안으로 환불가능하게함(배송료 6천원)
* 환불시에는 계좌에 돈이없다면 마이너스로 표기함.
* 현금충전과 자산 정보, 주소가 마이페이지에 존재해야함
* 상품 주문시 품절됬다면 구매버튼없애고 품절문구 띄어야함
* 상품 주문시 자산이 상품가격보다 모자라다면 구매 불가페이지 띄어주어야함
* 판매자 등록을 한 유저만 상품 판매가 가능함. 판매자인경우 마이페이지에 xx판매자님 이라고 넣기
* 상품은 삭제 불가능하다. 수정만 가능
* 상품 등록시 사진이 없다면 wrongPage로 리다이렉트

## 3.파일 저장 전략
* 파일은 random uuid + "_" + originalFileName = saveFileName 이라는 형식으로 저장한다.
* 서비스 로직에서 파일 저장 전략에따라 saveFileName을 만들어 저장한다.
* db에는 saveFileName이 저장되며 파일을 가져올때도 saveFileName으로 로컬에 저장된 사진 파일을 불러온다.

## 엔티티설계
### user
* id, email, password, auth, balance(자산), count(주문건수), address
### item
* id, title, author(사용자 입력받아야함), writer(상품등록자), price, remaining(수량), category, year, good
### orders
* id, itemTitle, member, status(Enum - ORDER, CANCEL), date(LocalDate 로컬데이트타임 아님)
### comment
* id, user, content, itemNum, createdDate

## url 설계
<pre>
/user/itemHome
/user/itemHome/search 
/user/item/post
/user/item/{title}
/user/item/category/{category}
/user/item/good/{title}
/user/item/edit/{title}
/user/comment/{title}
/user/comment/post/{title}
/user/comment/delete/{title}
/user/myPage/{email}
/user/myPage/address/{email}
/user/myPage/deposit/{email}
/user/myPage/ordersList/{email}

구매 버튼 -> 
구매버튼 클릭시 구자산 정보가져와서 가격이랑 비교해야함


/user/item/orders/{title}
/user/item/denial/{title} - 잔액 부족시 구매 거부

/user/item/cancel/{title} - orderList안에서 진행함. 타이틀은 itemTitle임.

주문다 만들고 주문 리스트에서 주문취소 잘 뜨는지 안뜨는지 확인하고 취소 로직까지 다만들기
</pre>

## sql signed/unsigned??
<pre>
user테이블의 balance 객체는 int형이다.
그리고 상품을 환불하는 과정에서 자산에 돈이없다면 음수로까지 갈 수 있게 기획했다.
하지만 음수로 못가게 막혀있다면??하는 생각이 들엇다.
signed는 음수까지 포함하는 int형이고, unsigned는 정수만 포함하는 int형이다.
따라서 balance객체는 unsinged이면 안된다!!!!
걱정하지 않아도 되는것은 mysql의 default는 signed이다!!
</pre>

## 여러 조건으로 페이징 정렬
<pre>
@PageableDefault 는 sort조건이 한개밖에 되지 않는다.
그런데 해당 프로젝트에서는 기본적으로 좋아요(인기)순으로 정렬하고
좋아요가 없을경우 id를 기준으로 desc 정렬하려고 한다.
이때는 @SortDefault를 사용하면된다.
<hr>
@PageableDefault(page = 0, size = 10)
@SortDefault.SortDefaults({
    @SortDefault(sort = "good", direction = Sort.Direction.DESC),
    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
}) Pageable pageable
<hr>
@PageableDefault 로는 페이지와 사이즈만 정하고 정렬조건은 @SortDefault로 해주면된다.
이번 프로젝트에서는 가격순으로 정렬을 사용하지 않았지만 위의 방법을 응용하면 간단하게 정렬할 수 있다.
</pre>

## 상품 수정
* 상품 수정시 파일의 이름은 db에 반드시 존재한다.
* 따라서 파일을 변경하며(파일의 이름 + 파일) 수정하는지, 기존 파일을 유지하며 수정하는지가 중요하다.
* 기존 파일을 유지하며 수정한다면 display:none 으로 들어있는 saveFileName을 dto로 그대로 받아와 수정하면된다.
* 파일을 변경하며 수정한다면 새로운 saveFileName을 만들어 저장하며 수정하면된다.

## 수정시 기존 셀렉트 박스 보여주기
> selected를 걸어서 기본으로 보여지게하고<br>
th:value로 값을 넣고 동일한 값을 th:text로 넣어주어야 값도 입력되고 시각적으로 보여지기까지 한다.<br>
th:text를 넣지않고 th:value만 넣으면 값이 보이지 않는다.
