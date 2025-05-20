<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>미팅룸 예약하기</title>
	<style>
	
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
        .roombtn {
			border: 0;
			padding: 15px 25px;
			display: inline-block;
			text-align: center;
			color: white;
            border-radius: 10px;       
            background-color: #64b1cc;
            margin: 5px;
            cursor: pointer; 
        }
        
        /* 미팅룸 선택시 선택효과*/		
		.selected-btn {
            background-color: #156c8a;      
        }	
        
        #reserveBtn, #updateBtn {
			margin-top: 20px;
			margin-bottom: 50px;
			margin-left: 15%;
		}
		
		#reservedList {
	        width: 500px;	
	        margin-top: 3px;
	        margin-bottom: 30px;
		}
		
		#reservedList p{		
			margin-left: 30px;
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
	<div align="center" style="margin-top: 50px;">
		<c:choose>
			<c:when test="${not empty param.reserveNum && sessionScope.id != 'admin'}">
				<h2>미팅룸 예약 수정</h2>
			</c:when>
			<c:when test="${sessionScope.id == 'admin'}">
				<h2>미팅룸 예약 수정 - 관리자용</h2>
			</c:when>
			<c:otherwise>
				<h2>미팅룸 예약</h2>
			</c:otherwise>
		</c:choose>	
		<form method="post" align="left" style="margin-left: 30%;">
			<c:if test="${not empty param.reserveNum}">				
				<p>▪ 예약내역</p>
				<div id="reservedList">
					<p>- 예약날짜 : ${param.reserveDate}</p>
					<p>- 예약시간 : ${param.startTime}:00 ~ ${param.endTime}:00</p>
					<p>- 예약시설 : ${param.roomName}</p>
				</div>
			</c:if>
			<p>▪ 이용자정보</p>
			<c:choose>
				<c:when test="${sessionScope.id == 'admin'}">
					<input type="text" name="userID" id="userID" value="${param.reserveId}" readonly>
				</c:when>
				<c:when test="${not empty param.reserveNum || sessionScope.id != 'admin'}">
					<input type="text" name="userID" id="userID" value="<%=session.getAttribute("id")%>" readonly>
				</c:when>			

			</c:choose>			
			<p><br>▪ 이용날짜</p>
			<input type="text" name="reserveDate" id="reserveDate" placeholder="날짜를 선택해주세요.">
			<p>예약은 현재 날짜 +3일 부터 1개월까지만 가능합니다.</p><br>
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
			<p>▪ 미팅룸 선택</p>
	        <div id="roomList" style="margin-top:10px;">
                 <p style="color: blue;">이용하실 날짜와 시간을 선택하면 예약 가능한 미팅룸이 나타납니다.</p>              
             </div>   		
            <br>
            <c:if test="${sessionScope.id == 'admin'}">
            	<p>▪ 관리자 메모</p>
				<textarea id="adminMemo" rows="6" cols="70" placeholder="관리자 메모 작성란"></textarea><br>
			</c:if>	
            <c:choose>			
            <c:when test="${not empty param.reserveNum}">
				<button type="button" id="updateBtn">예약 수정하기</button>	
			</c:when>
			<c:otherwise>       
				<button type="button" id="reserveBtn" >미팅룸 예약하기</button>	
			</c:otherwise>
			</c:choose>			
	    </form>
	</div>	
</body>


<script>

	//날짜선택 위젯 초기화
	$(function() {
	  $( "#reserveDate" ).datepicker({  
	  	minDate: +3, // 현재 날짜 +3일
	  	maxDate: "+1M", // 최대 1개월
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
            	
            	//예약날짜를 선택하지 않았을 경우 알림 문구 보여주기
                if (data.error) {                
                	document.getElementById("roomList").innerHTML = "<p style='color: red;'>" + data.error + "</p>"; 
                    return;
                }else { //예약날짜를 선택했을 경우 알림 문구 제거
                	document.getElementById("roomList").innerHTML = ""; 
                }
            	
                //예약 가능한 미팅룸이 없을 경우 알림 문구 보여주기
            	if(data.length == 0){
        			document.getElementById("roomList").innerHTML = "<p style='color: red;'>예약 가능한 미팅룸이 없습니다.</p>";	
        		
        		//예약가능한 미팅룸이 있을 경우 버튼 생성
            	}else{
            		
                    data.forEach(room => {
                       		
                    		var roombtn = document.createElement("button");
                    		roombtn.innerHTML = room.roomName;                   
                    		roombtn.setAttribute("class", "roombtn"); //클릭했을 경우 효과를 주기 위해 id 설정           		
                    		roombtn.setAttribute("roomCode", room.reserveRoom);
                    		roombtn.setAttribute("roomName", room.roomName);         
                    		roombtn.setAttribute("type", "button");
                    		
                    		document.getElementById("roomList").appendChild(roombtn);	
                    	   		
                	}); 
            	
            	}//else
            		
            }, //success
            error: function(xhr, status, error) {
            	alert('서버 오류: ' + error);
            }
        });
    }); //예약날짜와 시간 선택 시 예약 가능한 미팅룸을 동적으로 보여주는 함수

</script>

<script>

	//사용자가 미팅룸을 클릭하였을때 선택되었다는 효과를 주는 함수
	document.getElementById("roomList").addEventListener("click", event	=> {
		if (event.target && event.target.classList.contains("roombtn")) {
    	   document.querySelectorAll(".roombtn").forEach(btn => {
    		   //버튼 효과 초기화
               btn.classList.remove("selected-btn");
           });
    	   //클릭한 버튼에 효과 주기
           event.target.classList.add("selected-btn");
       }
    });
	
	
	//미팅룸 선택후 예약하기 버튼 클릭 시
	if(document.getElementById("reserveBtn")){	
		document.getElementById("reserveBtn").addEventListener("click", (event) => {
	        var selectedRoom = document.querySelector(".selected-btn");
	        if (!selectedRoom) { //선택된 미팅룸이 없을 경우
	            alert("미팅룸을 선택해주세요.");
	            return;
	        } 
	        
	        //사용자가 선택한 미팅룸 정보 가져오기
	        const reserveDate = document.getElementById("reserveDate").value;
	        const startTime = document.getElementById("StartTime").value;
	        const endTime = document.getElementById("EndTime").value;
	        const roomName = selectedRoom.getAttribute("roomName");
	        const roomCode = selectedRoom.getAttribute("roomCode");
	       
	       //사용자가 모든 정보를 선택하고 예약하기 버튼을 클릭했을 경우
	       //확인용 컨펌창 띄우기
	       const confirmResult = confirm("아래 내용대로 예약을 진행하시겠습니까?\n\n" +       
		            "- 이용일자 : " + reserveDate + "\n" +
		            "- 이용시간 : " + startTime + " ~ " + endTime + "\n" +
		            "- 이용시설 : " + roomName);      
	       
	       //컨펌창에서 취소를 누를 경우 메소드 빠져나가기
	       if(!confirmResult){return;}
	           
	      $.ajax({
	            url: "<%=request.getContextPath()%>/reserve/meetingRoomReserve",
	            type: "POST",
	            data: {
	                userID: document.getElementById("userID").value,
	                reserveDate: reserveDate,
	                StartTime: startTime,
	                EndTime: endTime,
	                roomCode: roomCode,
	                roomName: roomName
	            },
	            success: function(response) {
	                alert("예약이 완료되었습니다.");   
	                //예약이 완료되면 예약 확인 페이지로 이동
	                window.location.href = "<%=request.getContextPath()%>/reserve/reserveCheck";
	            },
	            error: function(xhr, status, error) {	
	                alert("예약 실패: " + error);
	            }
	        });
	    }); //미팅룸 예약하기 버튼 클릭 시 실행되는 이벤트 함수
	}
    
	
	//예약 수정하기 버튼을 눌렀을때 실행되는 이벤트리스너 
	if(document.getElementById("updateBtn")){
		document.getElementById("updateBtn").addEventListener("click", (event) => {
	        var selectedRoom = document.querySelector(".selected-btn");
	        if (!selectedRoom) { //선택된 미팅룸이 없을 경우
	            alert("미팅룸을 선택해주세요.");
	            return;
	        } 
	        
	        const reserveNum = "${param.reserveNum}";
	        
	        //사용자가 선택한 미팅룸 정보 가져오기
	        const reserveDate = document.getElementById("reserveDate").value;
	        const startTime = document.getElementById("StartTime").value;
	        const endTime = document.getElementById("EndTime").value;
	        const roomName = selectedRoom.getAttribute("roomName");
	        const roomCode = selectedRoom.getAttribute("roomCode");
	       
	        const adminId = "${sessionScope.id}";
	        const userId = "${param.reserveId}";
	        
	        const adminMemoElement = document.getElementById("adminMemo");
	        let adminMemo = "";
	        if(adminId === "admin"){
	        	adminMemo = adminMemoElement ? adminMemoElement.value : "";
	        }

	        //관리자가 메모를 적지 않았을 경우
	        if(adminId === "admin"){
	        	if(typeof adminMemo !== "string" || !adminMemo.trim()){
	        		alert("관리자 메모란을 기입해주세요.");
	        		return;
	        	}
	        }
	        
	        
	        let confirmResult = "";
	        
	        //관리자가 예약 내역을 수정할 경우
	        if(adminId == 'admin'){        	
		        confirmResult = confirm("아래 내용대로 예약을 수정하시겠습니까?\n\n" +  
		        		"- 이용자ID : " + userId + "\n" + 
		 	            "- 이용일자 : " + reserveDate + "\n" +
		 	            "- 이용시간 : " + startTime + " ~ " + endTime + "\n" +
		 	            "- 이용시설 : " + roomName + "\n" +
		 	            "- 관리자메모 : " + adminMemo);           	
	        }else{
	 	       //사용자가 모든 정보를 선택하고 예약 수정하기 버튼을 클릭했을 경우
	 	       //확인용 컨펌창 띄우기
	 	       confirmResult = confirm("아래 내용대로 예약을 수정하시겠습니까?\n\n" +       
	 		            "- 이용일자 : " + reserveDate + "\n" +
	 		            "- 이용시간 : " + startTime + " ~ " + endTime + "\n" +
	 		            "- 이용시설 : " + roomName);             	
	        }      
	   
	       //컨펌창에서 취소를 누를 경우 메소드 빠져나가기
	       if(!confirmResult){return;}  
	           
	        $.ajax({
	        	url: "<%=request.getContextPath()%>/reserve/meetingRoomUpdate",
	            type: "POST",
	            data: {
	                userID: document.getElementById("userID").value,
	                reserveDate: reserveDate,
	                StartTime: startTime,
	                EndTime: endTime,
	                roomCode : roomCode,
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
	                alert("예약 수정 실패: " + error);
	            }     	
	        });	
		})
	} //예약 수정
    
	
	//관리자 메모 동적으로 값 넣기
	window.addEventListener('DOMContentLoaded', () => {
		const memo = sessionStorage.getItem("reserveNotice");	
		const adminId = "${sessionScope.id}";
		if(adminId === "admin"){
			console.log(memo);
			if (memo != "null") {
				document.getElementById("adminMemo").value = memo;
			}else{
			  document.getElementById("adminMemo").value = "";
			}	
		}
	});
		
    
</script>

</html>