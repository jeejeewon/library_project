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
        	color: #F29661;
        }
        
        .in-use{
        	color: #28a745;
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
			
			//동적으로 테이블 생성
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

		//배열에 값이 없거나, '전체'버튼 체크할 경우 
		if (selectedStatuses.length === 0 || checked.filter('[value="all"]').is(':checked')) {
			return [...allData]; //전체 리턴
		}
		
		//전체 리스트 중 선택한 체크박스의 값에 따라 데이터 리턴
		return allData.filter(vo => {
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
	}  
  
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
  
  
  

</script>
</html>