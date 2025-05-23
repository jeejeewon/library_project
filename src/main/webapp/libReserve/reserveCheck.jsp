<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../css/reserveCheck.css">
<meta charset="UTF-8">
<title>시설 예약 내역</title>
<style type="text/css">
	.title{		
		font-size: 25px;
		font-weight: bold;
		margin-top: 50px;
		width: 50%;
		color: #2d3081;
		text-align: left;
	}
</style>
</head>
<body>
	<div align="center">
		<p class="title">시설 예약 내역</p>
		<table id="reserveTable">
		    <tr>
		    	<td id="text-r"colspan="5" align="right">* 예약내역은 최근 10건까지만 보여집니다.</td>
		    <tr>
            <tr align="center" style="font-weight: bold;">
                <th>예약날짜</th>
                <th>예약시설</th>
                <th>예약시간</th>
                <th>예약수정</th>
                <th>예약일시</th>                               
            </tr>     
            <c:choose>
			    <c:when test="${empty reserveList}">
			        <tr>
			            <td colspan="5" align="center">예약된 내역이 없습니다.</td>
			        </tr>
			    </c:when>
		    <c:otherwise>  
	            <c:forEach var="vo" items="${reserveList}">   	
	            	<c:if test="${vo.status == '이용전' || vo.status == '이용중'}">
		                <tr align="center">
					        <td>${vo.reserveDate}</td>
		                    <td class="roomType">${vo.roomName}<c:if test="${vo.reserveSeat != 0}"> - ${vo.reserveSeat}번 좌석</c:if></td>
		                    <td>${vo.reserveStart}:00 ~ ${vo.reserveEnd}:00</td>    
		                    <c:choose>
		                    	<c:when test="${vo.status == '이용전'}">
			                    	<td>
				                    	<a href="#" style="text-decoration: none; color: blue;" class="updateBtn" 
				                    	data-reserve-num="${vo.reserveNum}" data-reserve-roomname="${vo.roomName}"
				                    	data-reserve-date="${vo.reserveDate}" data-reserve-start="${vo.reserveStart}"
				                    	data-reserve-end="${vo.reserveEnd}" data-reserve-seat="${vo.reserveSeat}">수정</a> &nbsp;
				                    	
				                    	<a href="#" style="text-decoration: none; color: red;" class="deleteBtn" 
				                    	data-reserve-id="${vo.reserveId}" data-reserve-num="${vo.reserveNum}"
				                    	data-reserve-date="${vo.reserveDate}" data-reserve-start="${vo.reserveStart}"
				                    	data-reserve-end="${vo.reserveEnd}" data-reserve-roomname="${vo.roomName}"
				                    	data-reserve-seat="${vo.reserveSeat}">삭제</a>
			                    	</td>		                    	
		                    	</c:when>
		                    	<c:otherwise>
		                    		<td style="color: #28a745;">이용중</td>
		                    	</c:otherwise>
		                    </c:choose>
		                	<td>
		                		<fmt:formatDate value="${vo.reserveTime}" pattern="yyyy-MM-dd"/><br>
		                		<fmt:formatDate value="${vo.reserveTime}" pattern="HH:mm:ss"/>	
		                	</td> 
		                </tr>
	                </c:if>
	                <c:if test="${vo.status == '이용완료'}">
	            		<tr align="center" id="pastReserve">   
	            		    <td id="reserveDate">${vo.reserveDate}</td>       	   
		                    <td id="roomName">${vo.roomName}<c:if test="${vo.reserveSeat != 0}"> - ${vo.reserveSeat}번 좌석</c:if></td>
		                    <td>${vo.reserveStart}:00 ~ ${vo.reserveEnd}:00</td>    
		                    <td>이용완료</td>		               
		                	<td>
		                		<fmt:formatDate value="${vo.reserveTime}" pattern="yyyy-MM-dd"/><br>
		                		<fmt:formatDate value="${vo.reserveTime}" pattern="HH:mm:ss"/>	
		                	</td>             	
	            		</tr>	            		
	            	</c:if>
	             </c:forEach>
				</c:otherwise>
			</c:choose>
        </table>
	</div>
</body>
<script>

	//예약 삭제 버튼을 눌렀을 경우 실행되는 함수
	document.querySelectorAll(".deleteBtn").forEach(button => {
	    button.addEventListener("click", function(event) {
	        event.preventDefault(); // 링크 기본 동작 방지	
	       	
	        // 클릭한 버튼의 데이터 속성 가져오기
	        const reserveId = this.getAttribute("data-reserve-id");
	        const reserveNum = this.getAttribute("data-reserve-num");
	    	const reserveDate = this.getAttribute("data-reserve-date");
	    	const startTime = this.getAttribute("data-reserve-start");
	    	const endTime = this.getAttribute("data-reserve-end");
	    	const roomName = this.getAttribute("data-reserve-roomname");
	    	const roomSeat = this.getAttribute("data-reserve-seat");
	
	        //console.log('reserve_id:', reserveId);
	        //console.log('reserve_num:', reserveNum);
	        //console.log('reserve-roomname:', roomName);	        

	        //사용자가 삭제 버튼을 클릭했을 경우
	        //확인용 컨펌창 띄우기
	        const confirmResult = confirm("아래 예약을 삭제하시겠습니까?\n\n" +       
	    	            "- 이용일자 : " + reserveDate + "\n" +
	    	            "- 이용시간 : " + startTime + ":00 ~ " + endTime + ":00 \n" +
	    	            "- 이용시설 : " + roomName + 
	    	            (roomSeat != 0 ? "-" + roomSeat + "번 좌석" : ""));      
	        
	        //컨펌창에서 취소를 누를 경우 메소드 빠져나가기
	        if(!confirmResult){return;}
	        	
	        // AJAX 요청
	        $.ajax({
	            url: "<%=request.getContextPath()%>/reserve/deleteReserve",   
	            type: "POST",
	            data: {
	                reserve_id: reserveId,
	                reserve_num: reserveNum
	            },
	            success: function(response) {
	                alert("예약이 삭제되었습니다.");
	                window.location.href = "<%=request.getContextPath()%>/reserve/reserveCheck";
	            },
	            error: function(error) {
	                alert("예약 삭제에 실패하였습니다.");
	            }
	        });
	    });
	});
	
	
	//예약 수정 버튼을 눌렀을 경우 실행되는 함수
	document.querySelectorAll(".updateBtn").forEach(button => {
	    button.addEventListener("click", function(event) {
	        event.preventDefault(); // 링크 기본 동작 방지	
	       	
	        // 클릭한 버튼의 데이터 속성 가져오기 (예약번호, 아이디, 시설명)
	        const reserveNum = this.getAttribute("data-reserve-num");
	    	const roomName = this.getAttribute("data-reserve-roomname");
	    	const reserveDate = this.getAttribute("data-reserve-date");
	    	const startTime = this.getAttribute("data-reserve-start");
	    	const endTime = this.getAttribute("data-reserve-end");
	    	const roomSeat = this.getAttribute("data-reserve-seat");

	        //console.log('reserveNum:', reserveNum);
	        //console.log('room:', roomName + " - " + roomSeat);
	        //console.log('reserveDate:', reserveDate);
	        //console.log('time:', startTime + ":00 ~ " + endTime + ":00");
	        
	        //시설명에 따라 보여줄 뷰 화면 설정 (studyRoom/meetingRoom)
	        let url = "";
	        if(roomName.startsWith("스터디룸")){
	        	url = "<%=request.getContextPath()%>/reserve/reserveStudy";
	        }else{
	        	url = "<%=request.getContextPath()%>/reserve/reserveMeeting";
	        }
	        
	        url += "?reserveNum=" + reserveNum;
	        url += "&roomName=" + roomName;
	        url += "&reserveDate=" + reserveDate;
	        url += "&startTime=" + startTime;
	        url += "&endTime=" + endTime;
	        url += "&roomSeat=" + roomSeat;
	        
	        //console.log('url: ', url);
	        
	        window.location.href = url;           	       
	    });
	});
	
</script>
</html>