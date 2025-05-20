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
        
        .mod-link {
			text-decoration: none;
			cursor: pointer;
			color: blue;
        }
        .cancel-link{
        	text-decoration: none;
			cursor: pointer;
			color: red;
        }
        
        .upcoming{      	
        	color: #28a745;
        }
        
        .in-use{
        	color: #F29661;
        }
        
        .completed-bg {
		    background-color: #f2f2f2;
		}
		
		#pagination{
			display: flex;
			margin: 5px 0 10px 0;
			justify-content: center;
			align-items: center;
			width: 1300px;
		}
		#pagination a {
			margin: 0 5px;
			text-decoration: none;
			color: #333;
		}

		#pagination a.active-page {
			font-weight: bold;
			color: #F29661; /* 강조 색 */
		}
    
	</style>
</head>
<body>
	<div id="wrap" align="center">
		<div id="selectWrap">
			<div id="selectCheck">
				<p><input type="checkbox" value="all" checked>전체</p>
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
				<input type="text" id="search" placeholder="검색어를 입력해주세요.">
				<button type="submit" id="searchBtn">검색</button>
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
        <div id="pagination" style="margin-top: 20px;">
        	<!-- 페이징 처리 구역 -->
        </div>
	</div>
</body>
<script>
	//페이징 처리에 필요한 변수 선언
	let pageSize = 15; // 한 페이지당 15개
	let currentPage = 1;
	let allData = []; // 전체 데이터 저장
	let filteredData = []; //체크박스 필터 데이터 저장

	//예약 내역 불러오는 함수
	$(document).ready(function() {
		$.ajax({
			url: "<%=request.getContextPath()%>/reserve/allReservedList", 
			method: "GET",
			success: function(data) {
				allData = data; //전체 예약 내역 저장
				filteredData = filterData(); //체크박스 필터링 함수 호출
				renderTable();  //예약 리스트 출력 함수 호출
				renderPagination(); //페이징 출력 함수 호출
			    //체크박스 '전체'에 트리거
			   	$("input[type='checkbox']:checked").trigger("change");		
			},
			error: function(err) {
				console.error('예약 데이터 로딩 실패', err);
			}
		});
	});
  
	//예약 리스트 출력 함수
	function renderTable() {
		$("#reserveList").empty(); 
		
		if (filteredData.length === 0) {
			$("#reserveList").append(
				"<tr id='noResultMsg'><td colspan='9' style='text-align:center;'>검색된 결과가 없습니다.</td></tr>"
			);
		    return;
		} else {
			$("#noResultMsg").remove();
		}	
		
		let start = (currentPage - 1) * pageSize; 
		let end = start + pageSize;
		let pageData = filteredData.slice(start, end); 
							
		//현재 시간을 비교하여 이용현황 보여줄 Date객체 생성
		let now = new Date();
		
		pageData.forEach(function(vo) {
			let reserveDate = new Date(vo.reserveDate);
			let startTime = new Date(reserveDate);
			startTime.setHours(vo.reserveStart, 0, 0, 0);        	
			
			let endTime = new Date(reserveDate);
			endTime.setHours(vo.reserveEnd, 0, 0, 0);
			
			let status = ""; //이용현황을 저장할 변수 선언
			//이용현황에 따라 CSS 다르게 설정할 변수 선언
			let spanClass = ""; 
			let trClass = "";
			
			//현재 시간과 예약시간을 비교하여 이용현황 저장
			if(now < startTime){
			  status = "이용전";
			  spanClass = "upcoming";
			}else if(now >= startTime && now <= endTime){
			  status = "이용중";
			  spanClass = "in-use";
			}else{
			  status = "이용완료";
			  trClass = "completed-bg";
			  spanClass = "completed";
			}
			
			//예약일시 날짜와 시간 분리
			var dateTime = vo.reserveTime.split(" ");
			var date = dateTime[0];
			var time = dateTime[1];
			var seat = vo.reserveSeat != 0 ? "-" + vo.reserveSeat + "번" : "";

			
			//이용현황에 따른 수정 가능 여부 판단
			let actionTd = "";
			if(status === "이용전"){
				if(vo.reserveRoom.includes("study")){
					actionTd = "<a href='<%=request.getContextPath()%>/reserve/reserveStudy' class='mod-link'>수정</a>";
				}else if(vo.reserveRoom.includes("meeting")){
					actionTd = "<a href='<%=request.getContextPath()%>/reserve/reserveMeeting' class='mod-link'>수정</a>";
				}
				actionTd += "<br><a href='#' class='cancel-link'>삭제</a>";
			}else{
				actionTd = "수정불가";
			}
			
			//동적으로 테이블 생성
			$("#reserveList").append(
				"<tr class='" + trClass + "'>"
				+ "<td>" + vo.reserveNum + "</td>"
				+ "<td>" + vo.reserveDate + "</td>"
				+ "<td>" + vo.reserveStart + ":00 ~ " + vo.reserveEnd + ":00</td>"
				+ "<td>" + vo.reserveRoom + seat + "</td>"
				+ "<td>" + vo.reserveName + "</td>"
				+ "<td>" + vo.tel + "</td>"
				+ "<td>" + date + "<br>" + time + "</td>"
				+ "<td><span class='" + spanClass + "'>" + status + "</span></td>"
				+ "<td>" + actionTd + "</td>"
				+ "</tr>"
				+ "<input type='hidden' class='start-time' value='" + vo.reserveStart + "'/>"
				+ "<input type='hidden' class='end-time' value='" + vo.reserveEnd + "'/>"
				+ "<input type='hidden' class='room' value='" + vo.reserveRoom + "'/>"
				+ "<input type='hidden' class='seat' value='" + seat + "'/>"
			);
		});
	}
  
	//체크박스 필터링 함수
	function filterData() {
		const checked = $("input[type='checkbox']:checked");
		let selectedStatuses = []; //체크한 체크박스의 value값 담을 배열 선언
		
		checked.each(function () {
			const value = $(this).val();
			//'전체' 외의 체크박스 체크시 배열에 value값 저장
			if (value !== "all") selectedStatuses.push(value);
		});
		
		let baseData = [...allData];
		
		//배열의 길이가 0이 아니거나, 체크박스 중 '전체'를 클릭하지 않았을 경우
		if (selectedStatuses.length !== 0 && !checked.filter('[value="all"]').is(':checked')) {		
			//전체 리스트 중 선택한 체크박스의 값에 따라 데이터 리턴
			baseData = baseData.filter(vo => {
				
				let now = new Date();
				let reserveDate = new Date(vo.reserveDate);
				let startTime = new Date(reserveDate);
				startTime.setHours(vo.reserveStart, 0, 0, 0);
				let endTime = new Date(reserveDate);
				endTime.setHours(vo.reserveEnd, 0, 0, 0);
				
				let status = '';
				if (now < startTime) status = 'upcoming';
				else if (now >= startTime && now <= endTime) status = 'in-use';
				else status = 'completed';
				
				return selectedStatuses.includes(status);	
			});				
		} //if문 끝
		
		//날짜 검색 필터링
		const startDate = $("#startDate").val();
		const endDate = $("#endDate").val();
		
		//검색 시작 날짜와 종료 날짜 중 하나가 있을 경우
		if (startDate || endDate) {
		    baseData = baseData.filter(vo => {
		        const resDate = new Date(vo.reserveDate);
		        if (startDate && resDate < new Date(startDate)) return false;
		        if (endDate && resDate > new Date(endDate)) return false;
		        return true;
		    });
		}
		
		//검색어 필터링 
		const keyword = $("#keyword").val(); //검색 조건 값
		const searchVal = $("#search").val().toLowerCase(); //사용자가 입력한 검색어의 값
		
		//사용자가 검색어를 입력했다면?
		if(searchVal){
			baseData = baseData.filter(vo => {				
				//모든 값을 소문자 문자열로 변환해서 검사
				const lowerSearchVal = searchVal.toLowerCase();
				
				if(keyword === "name"){ //검색 조건을 '예약자명'으로 했을 경우
					//DB의 예약자명에 사용자가 입력한 검색어가 포함되어 있는지 검사하고 결과값 반환
					// vo.reserveName?. 의 값이 null이거나 undefined일 경우 에러 날 수 있으므로 이를 방지함 (undefined 반환)
					return (vo.reserveName ?? "").toLowerCase().includes(lowerSearchVal);
				}else if(keyword === "room"){ //검색 조건을 '예약시설명'으로 했을 경우
					return (vo.reserveRoom ?? "").toLowerCase().includes(lowerSearchVal);
				}else if(keyword === "all"){ //검색 조건을 '전체'로 했을 경우

					return Object.values(vo).some(val =>
						 //Object.values(vo) vo 객체의 값들만 배열로 뽑아옴
						 //some() 배열 중 하나라도 조건을 만족하면 true
	                	String(val ?? "").toLowerCase().includes(lowerSearchVal)
	                );	
				}				
			});	
		}		
		return baseData;	
	} //filterData() 끝 
	
	
	//'검색'버튼 click 이벤트
	$("#searchBtn").on("click", function () {
		currentPage = 1;
		filteredData = filterData(); //체크박스 필터링 함수 호출
		renderTable(); //예약 리스트 출력 함수 호출
		renderPagination(); //페이징 출력 함수 호출
	});
	
	
	//검색어를 입력하고 엔터를 눌렀을 경우 이벤트
	$("#search").on("keypress", function (e) {
		if (e.which === 13) {
			$("#searchBtn").click();
		}
	});
	
  
	//페이징 출력 함수
	function renderPagination() {
		$("#pagination").empty();
		let totalPages = Math.ceil(filteredData.length / pageSize);

		for(let i = 1; i <= totalPages; i++) {
		  let page = $("<a href='#'>").text(i).on("click", function(e) {
			e.preventDefault(); // a 태그 기본 동작 방지
		    currentPage = i;
		    renderTable();
		    renderPagination();
		  });
		
			if(i === currentPage){
				page.addClass("active-page");
			}
			
			$("#pagination").append(page);
		}
	}
  
  
	//체크박스 중 '전체' 누르면 나머지 체크 해제
	$("input[value='all']").on("change", function() {
		if (this.checked) {
			$("input[type='checkbox']").not(this).prop("checked", false);
			$("#reserveList tr").show();
		}
	});
  
	
	//나머지 체크박스 누르면 '전체' 체크 해제
	$("input[type='checkbox']").not("[value='all']").on("change", function() {
		$("input[value='all']").prop("checked", false).prop("indeterminate", false);
	});
  
  
	//선택한 체크박스에 따라 조회 내역 필터링하여 보여주기
	$("input[type='checkbox']").on("change", function(){
		if($(this).val() === 'all') {
			$("input[type='checkbox']").not(this).prop("checked", false);
		} else {
			$("input[value='all']").prop("checked", false).prop("indeterminate", false);
		}
	
		currentPage = 1;
		filteredData = filterData();
		renderTable();
		renderPagination();
	});
	
	
	//수정 버튼을 누를 경우
	$("#reserveList").on("click", ".mod-link", function(e){
		e.preventDefault(); //기본동작방지
	  
		//수정을 누른 행의 정보 바인딩
		const reserveNum = $(this).closest("tr").find("td").eq(0).text(); 
		const reserveDate = $(this).closest("tr").find("td").eq(1).text(); 
//		const startTime = 
//		const endTime = 
//		const roomName = 
//		const roomSeat =	

	  
	  
	  
	
	  // 여기에 수정 로직 추가하거나, 모달창 띄우기, 페이지 이동 등등
	});
	

</script>
</html>