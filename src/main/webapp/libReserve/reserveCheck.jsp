<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<style>
		#reserveTable {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-size: 18px;
            text-align: left;
            width: 1000px;
            font-size: 15px;
        }
        
        #reserveTable th, #reserveTable td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }
        
        #reserveTable th {
            background-color: #f2f2f2;
        }
        
        #reserveTable td{
        	padding: 8px;
            vertical-align: middle; 
        }
        
        #pastReserve{
        	background-color: #f2f2f2;
        }
        
	</style>
</head>
<body>
	<div align="center">
		<table id="reserveTable">
		    <tr>
		    	<td colspan="5" align="right">* 예약내역은 최근 10건까지만 보여집니다.</td>
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
	            <c:forEach var="vo" items="${reserveList}" begin="0" end="9" step="1">
	            	<c:if test="${!vo.isFuture}">
	            		<tr align="center" id="pastReserve">   
	            		    <td id="reserveDate">${vo.reserveDate}</td>    
	            		    <input type="hidden" id="reserveStart" value="${vo.reserveStart}"></input>	   
	            		    <input type="hidden" id="reserveEnd" value="${vo.reserveEnd}"></input>	   	   
		                    <td id="roomName">${vo.roomName}<c:if test="${vo.reserveSeat != 0}"> - ${vo.reserveSeat}번 좌석</c:if></td>
		                    <td>${vo.reserveStart}:00 ~ ${vo.reserveEnd}:00</td>    
		                    <td>이용완료</td>		               
		                	<td>
		                		<fmt:formatDate value="${vo.reserveTime}" pattern="yyyy-MM-dd"/><br>
		                		<fmt:formatDate value="${vo.reserveTime}" pattern="HH:mm:ss"/>	
		                	</td>             	
	            		</tr>
	            	</c:if>
	            	<c:if test="${vo.isFuture}">
		                <tr align="center">
					        <td>${vo.reserveDate}</td>
		                    <td class="roomType">${vo.roomName}<c:if test="${vo.reserveSeat != 0}"> - ${vo.reserveSeat}번 좌석</c:if></td>
		                    <td>${vo.reserveStart}:00 ~ ${vo.reserveEnd}:00</td>    
			                <c:if test="${vo.isFuture}">
		                    	<td>
			                    	<a href="#" style="text-decoration: none; color: blue;" class="updateBtn" 
			                    	data-reserve-id="${vo.reserveId}" data-reserve-num="${vo.reserveNum}" data-room-type="${vo.reserveRoom}">수정</a> &nbsp;
			                    	
			                    	<a href="#" style="text-decoration: none; color: red;" class="deleteBtn" 
			                    	data-reserve-id="${vo.reserveId}" data-reserve-num="${vo.reserveNum}"
			                    	data-reserve-date="${vo.reserveDate}" data-reserve-start="${vo.reserveStart}"
			                    	data-reserve-end="${vo.reserveEnd}" data-reserve-roomname="${vo.roomName}"
			                    	data-reserve-seat="${vo.reserveSeat}">삭제</a>
		                    	</td>
		                	</c:if>
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
	
	        console.log('reserve_id:', reserveId);
	        console.log('reserve_num:', reserveNum);
	        console.log('reserve-roomname:', roomName);
	        

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
	

	
	
	
</script>
</html>