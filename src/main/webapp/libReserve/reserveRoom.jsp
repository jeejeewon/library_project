<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	h2{		
		font-size: 25px;
		font-weight: bold;
		margin: 50px;
		border-bottom: 1px solid #dedede;
		width: 50%;
		margin: 50px auto;
		padding-bottom: 10px;		
		color: #2d3081;
		text-align: left;
	}
</style>
</head>
<body>
    <div align="center" style="margin-top: 300px;">
	    <h2>시설 예약 안내</h2>
		<p>예약 관련 안내사항</p>
		<br>
		<p>안내사항 밑에 동의하기 어쩌고 버튼 클릭시 예약할 수 있게!</p>
	</div>	
	<div align="center" style="margin-top: 50px;">
		<a href="<%=request.getContextPath()%>/reserve/reserveStudy">스터디룸 예약하러가기</a><br>
		<a href="<%=request.getContextPath()%>/reserve/reserveMeeting">미팅룸 예약하러가기</a><br>
	</div>
</body>
</html>