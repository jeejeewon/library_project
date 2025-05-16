<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>    
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <title>시설 예약 관리 화면</title>
	<style>
		#reserveTable {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-size: 18px;
            text-align: left;
            width: 1300px;
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
		    <thead>
		        <tr align="center" style="font-weight: bold;">
		            <th>예약번호</th>
		            <th>예약일자</th>
		            <th>예약시간</th>
		            <th>예약시설명</th>                               
		            <th>예약자명</th>       
		            <th>연락처</th>                                                                    
		            <th>예약일시</th>                               
		            <th>이용현황</th>                               
		            <th>예약관리</th>                               
		        </tr>
		    </thead>
		    <tbody id="reserveList">
		        <!-- 동적으로 예약 리스트가 들어갈 자리 -->
		    </tbody>
        </table>
	</div>
</body>
<script>
  $(document).ready(function() {
    $.ajax({
      url: "<%=request.getContextPath()%>/reserve/allReservedList", 
      method: "GET",
      success: function(data) {
        $("#reserveList").empty();
        data.forEach(function(vo) {
          $("#reserveList").append(
            "<tr>"
              + "<td>" + vo.reserveNum + "</td>"
              + "<td>" + vo.reserveDate + "</td>"
              + "<td>" + vo.reserveStart + ":00 ~ " + vo.reserveEnd + ":00</td>"
              + "<td>" + vo.reserveRoom + "</td>"
              + "<td>" + vo.reserveId + "</td>"
              + "<td>연락처</td>"
              + "<td>" + vo.reserveTime + "</td>"
              + "<td>이용현황</td>"
              + "<td>수정삭제</td>"
              + "</tr>"
          );
        });
      },
      error: function(err) {
        console.error('예약 데이터 로딩 실패', err);
      }
    });
  });

</script>
</html>