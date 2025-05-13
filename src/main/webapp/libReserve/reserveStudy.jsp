<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<style>	
	    table {
	      border-collapse: collapse;
	      width: 40%;
	      height: 200px;
	      align-items: center;
	      margin-top: 20px;
	      margin-left: 10px;
	      border: 1px solid #333;
		  color: #5D5D5D;	      
	    }
	    td {
	      width: 50px;
	      height: 40px;
	      text-align: center;
	      vertical-align: middle;
	      font-weight: bold;
	    }
	    .seat {
	      width: 40px;   
	      border: 1px solid #333;
	    }
	    .seat-btn {
	      display: block;
	      width: 100%;
	      height: 100%;
	      font-weight: bold;
          background-color: #FAED7D;
	      border: none;
	      font-size: 18px;
	      cursor: pointer;     
		  color: #5D5D5D;
	    }	
	    /* 좌석 선택시 선택효과*/		
		.selected-seat-btn {
            background-color: #F2CB61;      
        }	
	    .empty {
	    	width: 20px;	    	
	    }
	    .study-room-btn {
	    	border: 0;
			padding: 15px 25px;
			display: inline-block;
			text-align: center;
			color: white;
            border-radius: 10px;       
            background-color: #9abf7f;
            margin: 5px;
            cursor: pointer; 
	    }
	    /* 스터디룸 선택시 선택효과*/		
		.selected-btn {
            background-color: #758f62;      
        }
        /* 예약된 좌석 효과*/
        .seat-btn.reserved {
			background-color: #8C8C8C;
			color: white;
			cursor: not-allowed;
		}		    
	</style>
	

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
		[ 도서관안내 ]-[ 시설예약 ]-[ 스터디룸 예약 ] 뷰
	</div>	
	<div align="center" style="margin-top: 50px;">
		<form method="post" align="left" style="margin-left: 30%;">
			
			<p>▪ 이용자정보</p>
			<input type="text" name="userID" id="userID" value="<%=session.getAttribute("id")%>" readonly>
			<p><br>▪ 이용날짜</p>
			<input type="text" name="reserveDate" id="reserveDate" placeholder="날짜를 선택해주세요.">
			<p>예약은 현재 날짜로부터 1개월까지만 가능합니다.</p><br>		
			<div id="reserveTime">
				<p>▪ 이용시간</p> 
				<p>&nbsp;&nbsp;&nbsp;- 시작시간
 					<select name="StartTime" id="StartTime">
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
					<select name="EndTime" id="EndTime">
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
			</div><br>
			<p>▪ 스터디룸 선택</p>
			<div id="roomList" style="margin-top:10px;">
				<button type="button" class="study-room-btn" value="studyA">스터디룸A</button>
				<button type="button" class="study-room-btn" value="studyB">스터디룸B</button>
				<button type="button" class="study-room-btn" value="studyC">스터디룸C</button>
				<div id="roomTable">
					<table id="seatTable">					
					    <!-- 0번째 줄 -->
					    <tr>
					      <td class="empty" rowspan="5"></td>
					      <td class="empty" rowspan="5"></td>
						  <td class="seat"><button type="button" class="seat-btn" data-seat="1">1</button></td>
						  <td class="seat"><button type="button" class="seat-btn" data-seat="11">11</button></td>
						  <td class="empty" rowspan="5"></td>
						  <td class="seat"><button type="button" class="seat-btn" data-seat="21">21</button></td>
					    </tr>
					    <!-- 1번째 줄 -->
					    <tr>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="2">2</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="12">12</button></td>				     
					      <td class="seat"><button type="button" class="seat-btn" data-seat="22">22</button></td>
					    </tr>
					    <!-- 2번째 줄 -->
					    <tr>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="3">3</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="13">13</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="23">23</button></td>
					    </tr>
					    <!-- 3번째 줄 -->
					    <tr>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="4">4</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="14">14</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="24">24</button></td>
					    </tr>
					    <!-- 4번째 줄 -->
					    <tr>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="5">5</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="15">15</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="25">25</button></td>
					    </tr>
					    <!-- 5번째 줄 (입출구) -->
					    <tr>
					      <td class="entrance">입출구</td>
					      <td class="empty" colspan="5"></td>
					    </tr>
					    <!-- 6번째 줄 -->
					    <tr>
					      <td class="empty" rowspan="5"></td>
					      <td class="empty" rowspan="5"></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="6">6</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="16">16</button></td>
					      <td class="empty" rowspan="5"></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="26">26</button></td>
					    </tr>
					    <!-- 7번째 줄 -->
					    <tr>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="7">7</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="17">17</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="27">27</button></td>
					    </tr>
					    <!-- 8번째 줄 -->
					    <tr>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="8">8</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="18">18</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="28">28</button></td>
					    </tr>
					    <!-- 9번째 줄 -->
					    <tr>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="9">9</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="19">19</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="29">29</button></td>
					    </tr>
					    <!-- 10번째 줄 -->
					    <tr>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="10">10</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="20">20</button></td>
					      <td class="seat"><button type="button" class="seat-btn" data-seat="30">30</button></td>
					    </tr>				    
					</table>     
				</div>     
             </div>   		
            <br>
			<button type="button" id="reserveBtn" >스터디룸 예약하기</button>	
	    </form>
	</div>	
</body>

<script>
	//날짜선택 위젯 초기화
	$(function() {
		const today = $.datepicker.formatDate('yy/mm/dd', new Date());
	
		$( "#reserveDate" ).datepicker({  
			minDate: 0, // 현재 날짜
			maxDate: "+1M", // 최대 1개월
			dateFormat: "yy/mm/dd",
			defaultDate: today
		}).val(today);

		updateSeatStatus();
	});

	
	//사용자가 당일 날짜를 선택할 경우 현재 시간보다 이후의 시간만 보여주는 함수
	$(function () {
	  $('#reserveDate').on('change', function () {
	    // 사용자가 선택한 날짜
	    const selectedDate = $('#reserveDate').val();

	    // 현재 날짜와 시간 구하기
	    const now = new Date(); 	    
	    const yyyy = now.getFullYear();
	    const mm = String(now.getMonth() + 1).padStart(2, '0');
	    const dd = String(now.getDate()).padStart(2, '0');
	    const today = yyyy + "/" + mm + "/" + dd;
	    const currentHour = now.getHours();
	    const startTimeSelect = $('#StartTime');
	    const endTimeSelect = $('#EndTime');

	    const startTime = [
	      "10:00", "11:00", "12:00", "13:00", "14:00",
	      "15:00", "16:00", "17:00", "18:00", "19:00"
	    ];

	    //시작시간 select 초기화
	    startTimeSelect.empty();
	    
	    for (let time of startTime) {
	      const hour = parseInt(time.split(":")[0]);

	      if (selectedDate === today) {
	        //당일 날짜인 경우 현재 시간보다 이후만 보여줌
	        if (hour > currentHour) {
	          startTimeSelect.append("<option value='" + time + "'>" + time + "</option>");
	        }
	      } else {
	        //미래 날짜는 전부 보여줌
	        startTimeSelect.append("<option value='" + time + "'>" + time + "</option>");
	      }
	    }	    
	    //변경된 시작시간에 맞춰 종료시간도 자동 변경
	    startTimeSelect.trigger('change');
	  });
	});

	
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
	        "16:00", "17:00", "18:00", "19:00", "20:00"];
	 	  
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
	
	
	//첫 화면에 스터디룸A가 선택된 채로 보여주는 함수
	document.addEventListener('DOMContentLoaded', function () {
		document.querySelector('.study-room-btn[value="studyA"]').classList.add('selected-btn');
		updateSeatStatus();
	});
	
	//사용자가 스터디룸을 클릭하였을때 선택되었다는 효과를 주는 함수
	document.getElementById("roomList").addEventListener("click", event	=> {
		if (event.target && event.target.classList.contains("study-room-btn")) {
    	   document.querySelectorAll(".study-room-btn").forEach(btn => {
    		   //버튼 효과 초기화
               btn.classList.remove("selected-btn");
           });
    	   //클릭한 버튼에 효과 주기
           event.target.classList.add("selected-btn");
    	   
           updateSeatStatus();
       }
    });
	
	
	//사용자가 스터디룸 좌석을 클릭하였을때 선택되었다는 효과를 주는 함수
	document.getElementById("roomTable").addEventListener("click", event	=> {
		if (event.target && event.target.classList.contains("seat-btn")) {
    	   document.querySelectorAll(".seat-btn").forEach(btn => {
    		   //버튼 효과 초기화
               btn.classList.remove("selected-seat-btn");
           });
    	   //클릭한 버튼에 효과 주기
           event.target.classList.add("selected-seat-btn");
       }
    });
	
	
	//스터디룸 클릭시 selected-btn 클래스 설정
	$(document).on('click', '.study-room-btn', function(){		
		$('.study-room-btn').removeClass('selected-btn');
		$(this).addClass('selected-btn');		
		updateSeatStatus();
	})
	
	
	//사용자가 선택한 날짜, 시간, 스터디룸에 따라 좌석 현황을 동적으로 보여주는 함수
	$('#reserveDate, #reserveTime').on('change', function() {       
		updateSeatStatus();
    }); 
	
	function updateSeatStatus(){		
		const selectedRoom = $('.study-room-btn.selected-btn').val();
		const date = $("#reserveDate").val();
		
		//디폴트 날짜값이 컨트롤러로 안 넘어가는 경우 사용될 조건문
		if(!date){
			return;
		}
		
        $.ajax({
            url: "<%=request.getContextPath()%>/reserve/studyRoomList",
            type: 'POST',
            data: {
            	Date: date,
            	StartTime: $("#StartTime option:selected").val(),
            	EndTime: $("#EndTime option:selected").val(),
            	studyRoom: selectedRoom
            },
            success: function(data) {     
            	
                $('.seat-btn').removeClass('reserved').prop('disabled', false);
                
            	data.forEach(seat => {
            		//예약된 좌석이 있을 경우 해당 좌석 class 추가하는 함수 호출
            		reservedSeat(seat.seat_num)     		
            	});
            	
            }, //success
            error: function(xhr, status, error) {
            	alert('서버 오류: ' + error);
            }
        });		
	}
	

	//예약된 좌석이 있을 경우 해당 좌석에 class 추가하는 함수
	function reservedSeat(reservedSeatNum) {		
	  $('.seat-btn').each(function() {
	    let seatNum = $(this).data("seat"); 
	    if (reservedSeatNum === seatNum) {		     
	      $(this).addClass('reserved').prop('disabled', true);  // 예: 회색처리 + 클릭 못하게
	    }
	  });
	}
    
    
    
</script>
</html>