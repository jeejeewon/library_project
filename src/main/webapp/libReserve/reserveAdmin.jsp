<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>    
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <title>관리자 시설 예약 관리</title>
	<style>
		#reserveTable {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-size: 18px;
            text-align: center;
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
        
        #selectWrap{
			width: 1300px;
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin: 20px 0 20px 0;
        
        }
        
        #selectDiv{
			display: flex;
			align-items: center;
			gap: 10px;
			margin-left: auto; 
        }
        
        #selectCheck{
			display: flex;
			gap: 10px;
			white-space: nowrap;		 
        }
        
        .action-link {
			text-decoration: none;
			cursor: pointer;
        }
        
        .upcoming{
        	color: #FFBB00;
        }
        
        .in-use{
        	color: #28a745;
        }
        
        .completed {
		    background-color: #f2f2f2;
		}
        
	</style>
</head>
<body>
	<div id="wrap" align="center">
		<div id="selectWrap">
			<div id="selectCheck">
				<p><input type="checkbox" value="all">전체</p>
				<p><input type="checkbox" value="upcoming">이용전</p>
				<p><input type="checkbox" value="in-use">이용중</p>
				<p><input type="checkbox" value="completed">이용완료</p>
			</div>
			<div id="selectDiv">
				<input id="startDate" type="date">
				<input id="endDate" type="date">
				<select id="keyword">
					<option value="all">전체</option>
					<option value="name">예약자명</option>
					<option value="room">예약시설명</option>
				</select>
				<input type="text" placeholder="검색어를 입력해주세요.">
			</div>
		</div>
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
        
        var now = new Date();
        
        data.forEach(function(vo) {
        	
        	//현재 시간과 예약 시간을 비교하기 위한 Date 객체 생성
        	var reserveDate = new Date(vo.reserveDate);
        	var startTime = new Date(reserveDate);
        	startTime.setHours(vo.reserveStart, 0, 0, 0);        	
        	var endTime = new Date(reserveDate);
        	endTime.setHours(vo.reserveEnd, 0, 0, 0);
        	
        	//현재 시간과 비교해서 이용현황 표시 (이용전, 이용중, 이용완료)
        	var status = "";
        	var spanClass = "";
        	var trClass = "";
        	if(now < startTime){
        		status = "이용전";
        		spanClass = "upcoming";
        	}else if(now >= startTime && now <= endTime){
        		status = "이용중";
        		spanClass = "in-use";
        	}else{
        		status = "이용완료";
        		trClass = "completed";
        	}        	
        	
        	//예약일시 날짜와 시간으로 분리
	        var dateTime = vo.reserveTime.split(" ");  // ["2025-05-18", "19:52:44"]
	        var date = dateTime[0];
	        var time = dateTime[1];
	        
         	$("#reserveList").append(
            "<tr class='" + trClass + "'>"
              + "<td>" + vo.reserveNum + "</td>"
              + "<td>" + vo.reserveDate + "</td>"
              + "<td>" + vo.reserveStart + ":00 ~ " + vo.reserveEnd + ":00</td>"
              + "<td>" + vo.reserveRoom + "</td>"
              + "<td>" + vo.reserveName + "</td>"
              + "<td>" + vo.tel + "</td>"
              + "<td>" + date + "<br>" + time + "</td>"
              + "<td><span class='" + spanClass + "'>" + status + "</span></td>"
              + "<td><a href='#' class='action-link'>수정</a><br>"
              + "<a href='#' class='action-link' style='color: red;'>삭제</a></td>"
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