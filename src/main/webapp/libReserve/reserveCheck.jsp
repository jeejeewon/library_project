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
            <c:forEach var="vo" items="${reserveList}" begin="0" end="9" step="1">
            	<c:if test="${!vo.isFuture}">
            		<tr align="center" id="pastReserve">   
            		    <td>${vo.reserve_date}</td>         		   
	                    <td>${vo.reserve_room}</td>
	                    <td>${vo.reserve_start}:00 ~ ${vo.reserve_end}:00</td>    
	                    <td>이용완료</td>		               
	                	<td>
	                		<fmt:formatDate value="${vo.reserve_time}" pattern="yyyy-MM-dd"/><br>
	                		<fmt:formatDate value="${vo.reserve_time}" pattern="HH:mm:ss"/>	
	                	</td>             	
            		</tr>
            	</c:if>
            	<c:if test="${vo.isFuture}">
	                <tr align="center">
				        <td>${vo.reserve_date}</td>
	                    <td class="roomType">${vo.reserve_room}</td>
	                    <td>${vo.reserve_start}:00 ~ ${vo.reserve_end}:00</td>    
		                <c:if test="${vo.isFuture}">
	                    	<td>
		                    	<a href="#" style="text-decoration: none; color: blue;" class="updateBtn" 
		                    	data-reserve-id="${vo.reserve_id}" data-reserve-num="${vo.reserve_num}" data-room-type="${vo.reserve_room}">수정</a> &nbsp;
		                    	
		                    	<a href="#" style="text-decoration: none; color: red;" class="deleteBtn" 
		                    	data-reserve-id="${vo.reserve_id}" data-reserve-num="${vo.reserve_num}">삭제</a>
	                    	</td>
	                	</c:if>
	                	<td>
	                		<fmt:formatDate value="${vo.reserve_time}" pattern="yyyy-MM-dd"/><br>
	                		<fmt:formatDate value="${vo.reserve_time}" pattern="HH:mm:ss"/>	
	                	</td> 
	                </tr>
                </c:if>
             </c:forEach>
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
	
	        console.log('reserve_id:', reserveId);
	        console.log('reserve_num:', reserveNum);
	
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