<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div align="center" style="margin-top: 100px; color: red;">
		reserveMeeting.jsp <br>
		[ 도서관안내 ]-[ 시설예약 ]-[ 미팅룸 예약 ] 뷰
	</div>	
	<div>
		<form action="meetingRoomList" method="post">
			<p>▪ 이용날짜</p>
			<input type="date" name="reserveDate" id="reserveDate">
			<div>
				<p>▪ 이용시간</p>
				<p>▪ 시작시간
					<select id="StartTime">
						<option value="10:00">10:00</option>
						<option value="11:00">11:00</option>
						<option value="12:00">12:00</option>
						<option value="13:00">13:00</option>
						<option value="14:00">14:00</option>
						<option value="15:00">15:00</option>
						<option value="16:00">16:00</option>
						<option value="17:00">17:00</option>
						<option value="18:00">18:00</option>
						<option value="19:00">19:00</option>
					</select>
				</p>
				<p>▪ 종료시간
					<select id="EndTime">
						<option value="11:00">11:00</option>
						<option value="12:00">12:00</option>
						<option value="13:00">13:00</option>
						<option value="14:00">14:00</option>
						<option value="15:00">15:00</option>
						<option value="16:00">16:00</option>
						<option value="17:00">17:00</option>
						<option value="18:00">18:00</option>
						<option value="19:00">19:00</option>
						<option value="20:00">20:00</option>
					</select>
				</p>
			</div>
			<p>▪ 미팅룸 선택</p>
			<button type="button" id="roomListBtn" onclick="MeetingRoomList();">예약가능한 미팅룸 보기</button>	
	        <div id="roomList">
                 예약 가능한 미팅룸이 동적으로 추가될 자리
             </div>   		
	    </form>
		<button type="button" id="reserveBtn" >미팅룸 예약하기</button>	
	</div>
	
		
</body>

<script>

	//예약 가능한 회의실 목록을 가져오는 ajax 함수
    function MeetingRoomList() {
		
		alert("예약 가능한 미팅룸 보기 버튼 클릭됨");		
		
/* 		console.log($("#StartTime option:selected").val());
    	console.log($("#EndTime option:selected").val()); */
    	
	    //예약 날짜, 시작시간, 종료시간을 가져와서 ajax로 서버에 요청	
        $.ajax({
            type: "GET",
            //요청할 서버페이지 주소 경로
            url: "<%=request.getContextPath()%>/reserve/meetingRoomList",
            data:{
            	Date: $("#reserveDate").val(),
            	StartTime: $("#StartTime option:selected").val(),
            	EndTime: $("#EndTime option:selected").val()           	
            },
            success: function(data) {
            	
            	//서버페이지에서 반환받은 데이터를 동적으로 처리하는 곳
            	
    
            },
            error: function(xhr, status, error) {
                console.error("Error fetching meeting room list:", error);
            }
        });
    }

    document.getElementById("reserveBtn").addEventListener("click", function() {
        alert("미팅룸 예약하기 버튼 클릭됨");
    });

</script>

</html>