<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../css/reserveStudy.css">
<meta charset="UTF-8">
<title>스터디룸 예약</title>
</head>
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<!-- jQuery UI JS -->
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
<!-- jQuery UI CSS -->
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<body>
	<div align="center" style="margin-top: 50px;">
		<c:choose>
			<c:when test="${not empty param.reserveNum && sessionScope.id != 'admin'}">
				<p class="title">스터디룸 예약 수정</p>
			</c:when>
			<c:when test="${sessionScope.id == 'admin' && not empty param.reserveNum}">
				<p class="title">스터디룸 예약 수정 - 관리자용</p>
			</c:when>
			<c:otherwise>
				<p class="title">스터디룸 예약</p>
			</c:otherwise>
		</c:choose>		
		<form method="post" align="left" style="margin-left: 30%;">			
			<c:if test="${not empty param.reserveNum}">				
				<p><span class="dott">▪</span> 예약내역</p>
				<div id="reservedList">
					<p>- 예약날짜 : ${param.reserveDate}</p>
					<p>- 예약시간 : ${param.startTime}:00 ~ ${param.endTime}:00</p>
					<p>- 예약시설 : ${param.roomName} - ${param.roomSeat}번 좌석</p>
				</div>
			</c:if>
			<p><span class="dott">▪</span> 이용자정보</p>
			<c:choose>
				<c:when test="${sessionScope.id == 'admin' && empty param.reserveNum}"> <!-- 관리자 아이디로 스터디룸 예약 -->
					<input type="text" name="userID" id="userID" value="<%=session.getAttribute("id")%>" readonly>
				</c:when>
				<c:when test="${sessionScope.id == 'admin' && empty param.reserveId}"><!-- 관리자가 마이페이지에서 관리자 예약 수정시 -->
					<input type="text" name="userID" id="userID" value="<%=session.getAttribute("id")%>" readonly>
				</c:when>
				<c:when test="${sessionScope.id == 'admin'&& param.reserveId != 'admin'}"><!-- 관리자 아이디로 회원 예약 수정시 -->
					<input type="text" name="userID" id="userID" value="${param.reserveId}" readonly>
				</c:when>
				<c:when test="${not empty param.reserveNum || sessionScope.id != 'admin'}"><!-- 회원이 예약 수정할 경우 -->
					<input type="text" name="userID" id="userID" value="<%=session.getAttribute("id")%>" readonly>
				</c:when>			
			</c:choose>
			<p><br><span class="dott">▪</span> 이용날짜</p>
			<input type="text" name="reserveDate" id="reserveDate" placeholder="날짜를 선택해주세요.">
			<p>예약은 현재 날짜로부터 1개월까지만 가능합니다.</p><br>					
				<p><span class="dott">▪</span> 이용시간</p> 
				<p id="noReservationMsg" style="display: none; color: red;">
				  예약 가능한 시간이 없습니다. 다른 날짜에 이용해주세요.
				</p>
			<div id="reserveTime">	
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
			<p><span class="dott">▪</span> 스터디룸 선택</p>
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
            <c:if test="${sessionScope.id == 'admin' && not empty param.reserveNum}">
            	<p><span class="dott">▪</span> 관리자 메모</p>
				<textarea id="adminMemo" rows="6" cols="70" placeholder="관리자 메모 작성란"></textarea><br>
			</c:if>	
            <c:choose>		
            <c:when test="${not empty param.reserveNum}">
				<button type="button" id="updateBtn" class="reserve-btn">예약 수정하기</button>	
			</c:when>
			<c:otherwise>
				<button type="button" id="reserveBtn" class="reserve-btn">스터디룸 예약하기</button>	
			</c:otherwise>
			</c:choose>
	    </form>
	</div>	
</body>

<script>
	//날짜선택 위젯 초기화
	$(function() {
		const today = $.datepicker.formatDate('yy/mm/dd', new Date());
	
		//법정공휴일이면 선택 불가능하게 설정 (추가로 휴관할 경우 여기에 날짜 추가하면 됨!)
		const holidays = [
			  "2025/01/01", // 신정
			  "2025/03/01", // 삼일절
			  "2025/05/05", // 어린이날
			  "2025/06/06", // 현충일
			  "2025/08/15", // 광복절
			  "2025/10/03", // 개천절
			  "2025/10/09", // 한글날
			  "2025/12/25"  // 성탄절
		];
		
		$( "#reserveDate" ).datepicker({  
			minDate: 0, // 현재 날짜
			maxDate: "+1M", // 최대 1개월
			dateFormat: "yy/mm/dd",
			defaultDate: today,
			beforeShowDay: function(date){
				const day = date.getDay();
				const formatted = $.datepicker.formatDate('yy/mm/dd', date);
				
				//월요일(정기휴관일) + 법정공휴일은 선택 못하도록 막음
				if(day === 1 || holidays.includes(formatted)){
					return [false, "", "선택불가"];
				}
				return [true, ""];			
			}
		}).val(today);
		
		resetSelect();
		
		//날짜 초기화 후 트리거 적용
		setTimeout(() => {
			$("#reserveDate").trigger('change');
		}, 200);
	
	});

	
	//사용자가 당일 날짜를 선택할 경우 현재 시간보다 이후의 시간만 보여주는 함수
	function resetSelect() {
	  $('#reserveDate').off('change').on('change', function () {
	    
		//<select>가 숨겨져 있을 경우 다시 활성화
		document.getElementById("reserveTime").style.display = "block";
		document.getElementById("noReservationMsg").style.display = "none";
		    
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
			//당일 날짜인데 현재 시간이 19시 이후일 경우	    	  
	    	if(currentHour >= 19){
		    	  //셀렉트 숨기고 알림 문구 보여주기 ('예약 가능한 시간이 없습니다.')
		    	  document.getElementById("reserveTime").style.display = "none";
		    	  document.getElementById("noReservationMsg").style.display = "block";	  	  		    	  
		    //당일 날짜인데 현재 시간이 19시 이전일 경우 현재 시간보다 이후 시간의 옵션만 보여줌	  
	        }else if (hour > currentHour) {
		          startTimeSelect.append("<option value='" + time + "'>" + time + "</option>");
		    }   	    	
	      }else { //미래 날짜 선택시 옵션 다 보여줌        
	        startTimeSelect.append("<option value='" + time + "'>" + time + "</option>");
	      }
	    }	        
	    resetSelectEndTime();
	    
	    //변경된 시작시간에 맞춰 종료시간도 자동 변경
	    startTimeSelect.trigger('change');
	  });
	};

	
	//시작시간 선택 시 종료시간을 동적으로 변경하여 <option>태그에 보여주는 함수
	function resetSelectEndTime() {
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
	 };
	
	
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
		$('.seat-btn').removeClass('selected-seat-btn');
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
            	//예약 가능한 시간이 없을 경우 (당일 19시 이후 예약일 경우)
            	if(data === "NO_AVAILABLE_TIME"){
            		return;
            	}          	
                $('.seat-btn').removeClass('reserved').prop('disabled', false);              
            	data.forEach(seat => {
            		//예약된 좌석이 있을 경우 해당 좌석 class 추가하는 함수 호출
            		reservedSeat(seat.reserveSeat)     		
            	});            	
            }, //success
            error: function(xhr, status, error) {
            	alert('서버 오류' + error);
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
    
	//예약하기 버튼을 눌렀을때 실행되는 이벤트리스너 
	if(document.getElementById("reserveBtn")){
		document.getElementById("reserveBtn").addEventListener("click", (event) => {
			
			//사용자가 선택한 스터디룸 정보 가져오기
	        const reserveDate = document.getElementById("reserveDate").value;
	        const formattedDate = reserveDate.replaceAll("/", "-"); 
	        const startTime = document.getElementById("StartTime").value;
	        const endTime = document.getElementById("EndTime").value;
	        const roomName = document.querySelector(".selected-btn").innerText;
	        const roomCode = document.querySelector(".selected-btn").value;
	        const selectedSeat = document.querySelector(".selected-seat-btn"); 
	        const userId = document.getElementById("userID").value;
	        if (!selectedSeat) { //선택된 좌석이 없을 경우
	            alert("이용할 좌석을 선택해주세요.");
	            return;
	        }       
  
	        const seat = selectedSeat.dataset.seat;
	              
	       	//동일 날짜와 시간대에 예약된 건이 있는지 체크 (중복예약방지)
	       	$.ajax({
	       		url: "<%=request.getContextPath()%>/reserve/checkReserve",
	       		type: "POST",
	       		dataType: "json",
	       		data: {
	                userID: userId,
	                reserveDate: formattedDate,
	                StartTime: startTime,
	                EndTime: endTime
	       		},
	       		success: function(response){
	       			if(response.isReserved){ //조회된 결과가 있을 경우
	       				alert("동일한 시간대에 이미 예약된 건이 있습니다.")
	       				return;
	       			}
	       			
	    	        //사용자가 모든 정보를 선택하고 예약하기 버튼을 클릭했을 경우
	    	        //확인용 컨펌창 띄우기
	    	        const confirmResult = confirm("아래 내용대로 예약을 진행하시겠습니까?\n\n" +       
	    	 	            "- 이용일자 : " + reserveDate + "\n" +
	    	 	            "- 이용시간 : " + startTime + " ~ " + endTime + "\n" +
	    	 	            "- 이용시설 : " + roomName + "\n" +
	    	 	            "- 이용좌석 : " + seat);      
	    	        
	    	        //컨펌창에서 취소를 누를 경우 메소드 빠져나가기
	    	        if(!confirmResult){return;}
	    	        
	    	        $.ajax({
	    	        	url: "<%=request.getContextPath()%>/reserve/studyRoomReserve",
	    	            type: "POST",
	    	            data: {
	    	                userID: document.getElementById("userID").value,
	    	                reserveDate: reserveDate,
	    	                StartTime: startTime,
	    	                EndTime: endTime,
	    	                roomCode : roomCode,
	    	                seat : seat
	    	            },
	    	            success: function(response) {
	    	            	if(response.trim() === "OK"){
	    	                	alert("예약이 완료되었습니다.");
	    		                //예약이 완료되면 예약 확인 페이지로 이동
	    		                window.location.href = "<%=request.getContextPath()%>/reserve/reserveCheck";
	    	            	}else{
	    	            		alert("예약에 실패하였습니다. 다시 시도해주세요.");
	    	            	}
	    	            },
	    	            error: function(xhr, status, error) {
	    	                alert("서버 오류 발생!" + error);
	    	            }     	
	    	        });	
	       		},
	       		error: function(xhr, status, error){
	       			alert("서버 오류 발생!" + error);
	       		}  
	       	});	    
		})
	}//예약하기버튼 클릭이벤트리스너
	
	
	//예약 수정하기 버튼을 눌렀을때 실행되는 이벤트리스너 
	if(document.getElementById("updateBtn")){
		document.getElementById("updateBtn").addEventListener("click", (event) => {
			
			const reserveNum = "${param.reserveNum}";
			
			//사용자가 선택한 스터디룸 정보 가져오기
	        const reserveDate = document.getElementById("reserveDate").value;
	        const formattedDate = reserveDate.replaceAll("/", "-"); 
	        const startTime = document.getElementById("StartTime").value;
	        const endTime = document.getElementById("EndTime").value;
	        const roomName = document.querySelector(".selected-btn").innerText;
	        const roomCode = document.querySelector(".selected-btn").value;
	        const selectedSeat = document.querySelector(".selected-seat-btn");      
      			
	        const adminId = "${sessionScope.id}";
	        const userId = "${param.reserveId}";
	        
	        const adminMemoElement = document.getElementById("adminMemo");
	        let adminMemo = "";
	        if(adminId === "admin"){
	        	adminMemo = adminMemoElement.value;
	        }
    
	        if (!selectedSeat) { //선택된 좌석이 없을 경우
	            alert("이용할 좌석을 선택해주세요.");
	            return;
	        }        
	        
	        const seat = selectedSeat.dataset.seat;
	            
	        //관리자가 메모를 적지 않았을 경우
	        if(adminId === "admin"){
	        	if(typeof adminMemo !== "string" || !adminMemo.trim()){
	        		alert("관리자 메모란을 기입해주세요.");
	        		return;
	        	}
	        }
	                 
	        let confirmResult = "";
	        
	       	//동일 날짜와 시간대에 예약된 건이 있는지 체크 (중복예약방지)
	       	$.ajax({
	       		url: "<%=request.getContextPath()%>/reserve/checkReserve",
	       		type: "POST",
	       		dataType: "json",
	       		data: {
	                userID: document.getElementById("userID").value,
	                reserveDate: formattedDate,
	                StartTime: startTime,
	                EndTime: endTime,
	                reserveNum: reserveNum
	       		},
	       		success: function(response){
	       			console.log("response.isReserved:", response.isReserved, typeof response.isReserved);
	       			if(response.isReserved){ //조회된 결과가 있을 경우
	       				alert("동일한 시간대에 이미 예약된 건이 있습니다.")
	       				return;
	       			}	       			
	    	        //관리자가 예약 내역을 수정할 경우
	    	        if(adminId == 'admin'){        	
	    		        confirmResult = confirm("아래 내용대로 예약을 수정하시겠습니까?\n\n" +  
	    		        		"- 이용자ID : " + userId + "\n" + 
	    		 	            "- 이용일자 : " + reserveDate + "\n" +
	    		 	            "- 이용시간 : " + startTime + " ~ " + endTime + "\n" +
	    		 	            "- 이용시설 : " + roomName + "\n" +
	    		 	            "- 이용좌석 : " + seat + "\n" +
	    		 	            "- 관리자메모 : " + adminMemo);           	
	    	        }else{
	    		        //사용자가 모든 정보를 선택하고 예약 수정하기 버튼을 클릭했을 경우
	    		        //확인용 컨펌창 띄우기
	    		        confirmResult = confirm("아래 내용대로 예약을 수정하시겠습니까?\n\n" +       
	    		 	            "- 이용일자 : " + reserveDate + "\n" +
	    		 	            "- 이용시간 : " + startTime + " ~ " + endTime + "\n" +
	    		 	            "- 이용시설 : " + roomName + "\n" +
	    		 	            "- 이용좌석 : " + seat);           	
	    	        }
	    	        
	    	        //컨펌창에서 취소를 누를 경우 메소드 빠져나가기
	    	        if(!confirmResult){return;}
	    	        
	    	        $.ajax({
	    	        	url: "<%=request.getContextPath()%>/reserve/studyRoomUpdate",
	    	            type: "POST",
	    	            data: {
	    	                userID: document.getElementById("userID").value,
	    	                reserveDate: reserveDate,
	    	                StartTime: startTime,
	    	                EndTime: endTime,
	    	                roomCode : roomCode,
	    	                seat : seat,
	    	                reserveNum : reserveNum,
	    	                reserveNotice : adminId === 'admin' ? adminMemo : ""                
	    	            },
	    	            success: function(response) {
	    	                alert("예약이 수정되었습니다.");
	    	              	//예약이 수정되면 예약 확인 페이지로 이동
	    	                if(adminId === 'admin'){
	    	                	window.location.href = "<%=request.getContextPath()%>/reserve/reserveAdmin";
	    	                }else{
	    	                	window.location.href = "<%=request.getContextPath()%>/reserve/reserveCheck";
	    	                }   
	    	            },
	    	            error: function(xhr, status, error) {
	    	                alert("예약 수정에 실패하였습니다. " + error);
	    	            }     	
	    	        });				
	       		},
	       		error: function(xhr, status, error){
	       			alert("서버 오류 발생!" + error);
	       		}  
	       	});		      
		})
	}
	
	//관리자 메모 동적으로 값 넣기
	window.addEventListener('DOMContentLoaded', () => {
		const memo = sessionStorage.getItem("reserveNotice");
		const adminId = "${sessionScope.id}";
		//console.log(memo);
		if(adminId === "admin"){
			//console.log(memo);
			if (memo != "null") {
				document.getElementById("adminMemo").value = memo;
			}else{
			  document.getElementById("adminMemo").value = "";
			}	
		}
	});
    
    
</script>
</html>