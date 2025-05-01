<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<!-- jQuery UI JS -->
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
<!-- jQuery UI CSS -->
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">


<body>
	<div align="center" style="margin-top: 100px; color: red;">
		reserveMeeting.jsp <br>
		[ 도서관안내 ]-[ 시설예약 ]-[ 미팅룸 예약 ] 뷰
	</div>	
	<div align="center" style="margin-top: 50px;">
		<form action="meetingRoomList" method="post" align="left" style="margin-left: 30%;">
			<p>▪ 이용날짜</p>
			<input type="text" name="reserveDate" id="reserveDate" placeholder="날짜를 선택해주세요.">
			<p>예약 가능 날짜는 현재 날짜 +3일 부터 1개월 +10일 까지 입니다.</p><br>
			<div id="reserveTime">
				<p>▪ 이용시간</p> 
				<p>&nbsp;&nbsp;&nbsp;- 시작시간
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
				<p>&nbsp;&nbsp;&nbsp;- 종료시간
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
	        <div id="roomList">
                 예약 가능한 미팅룸이 동적으로 추가될 자리
             </div>   		
	    </form>
		<button type="button" id="reserveBtn" >미팅룸 예약하기</button>	
	</div>	
</body>


<script>

	//날짜선택 위젯 초기화
	$( function() {
	  $( "#reserveDate" ).datepicker({  
	  	minDate: +3, // 현재 날짜 +3일
	  	maxDate: "+1M +10D", // 최대 1개월 +10일
	  	dateFormat: "yy/mm/dd"}); // 날짜 포맷 설정
	} );
	
	
	
	//시작시간 선택 시 종료시간을 동적으로 변경하여 <option>태그에 보여주는 함수
	$(function() {
	    $('#StartTime').on('change', function() {
	      const startTime = $(this).val(); // 사용자가 선택한 시작시간
	      const endTimeSelect = $('#EndTime'); 
	      
	      //종료시간 select 초기화
	      endTimeSelect.empty(); 
	      
	 	  //예약 가능한 종료시간 배열 (시작시간보다 1시간씩 추가)
	      const endTimes = [
	        "11:00", "12:00", "13:00", "14:00", "15:00", 
	        "16:00", "17:00", "18:00", "19:00", "20:00"
	      ];
	 	  
	      //선택한 시작시간 인덱스 찾기
	      const startIndex = endTimes.indexOf(startTime);
	      
	      //시작시간 이후로 종료시간을 추가
	      for (let i = startIndex + 1; i < endTimes.length; i++) {
	    	  endTimeSelect.append("<option value='" + endTimes[i] + "'>" + endTimes[i] + "</option>");
	      }
	      
	      //첫 번째 종료시간 옵션 자동 선택
	      endTimeSelect.val(endTimeSelect.find('option:first').val());	           
	    });	    
	 });
	
	
	
	//사용자가 선택한 날짜와 시간에 따라 예약 가능한 미팅룸을 동적으로 추가하는 함수
	$('#reserveDate, #reserveTime').on('change', function() {
        
        $.ajax({
            url: "<%=request.getContextPath()%>/reserve/meetingRoomList",
            type: 'POST',
            data: {
            	Date: $("#reserveDate").val(),
            	StartTime: $("#StartTime option:selected").val(),
            	EndTime: $("#EndTime option:selected").val()  
            },
            success: function(data) {
            	
            	//예약날짜를 선택하지 않았을 경우 알림창 띄우기
                if (data.error) {
                    alert(data.error); 
                    return;
                }
           
            	//서버페이지에서 반환받은 데이터를 동적으로 처리하는 곳
            },
            error: function(xhr, status, error) {
            	alert('서버 오류: ' + error);
            }
        });
    });
	



</script>

</html>