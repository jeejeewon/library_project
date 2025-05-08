<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <div align="center" style="margin-top: 300px; color: red;">
		reserveRoom.jsp <br>
	    [ 도서관안내 ]-[ 시설예약 ] 뷰
	</div>
	
	<div align="center" style="margin-top: 50px;">
		<p>예약 관련 안내사항</p>
		<!-- 안내사항 밑에 동의하기 어쩌고 버튼 클릭시 예약할 수 있게! -->
		<br>
		<a href="<%=request.getContextPath()%>/reserve/reserveStudy">스터디룸 예약하러가기</a><br>
		<a href="<%=request.getContextPath()%>/reserve/reserveMeeting">미팅룸 예약하러가기</a><br>
	</div>
</body>
</html>