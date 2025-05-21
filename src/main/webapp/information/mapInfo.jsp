<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../css/information.css">
<meta charset="UTF-8">
</head>
<body onload="showDefaultMap()">	
	<div id="infoContainer" align="center"> 
		<p class="title">도서관 소개</p>
		<div id="libraryInpo">
			<div id="inpotitle">
				<h3>시민의 마음을 채우는 지식의 공간</h3>
				<h3>부산광역시립시민도서관입니다.</h3>
			</div>
			<div id="inpoContext">		
				<p>"찾는 도서관 얻는 새 지식"</p>
				<br>
				<p>우리 중앙도서관은 21세기 정보문화센터로서 앞서가는 도서관이 되고자<br>
				1990년 개관 이래 다양한 정보서비스 제공과 평생학습 생활화를 통한 지역주민들의 삶의 질 향상을 위해 노력하고 있습니다. <br>
				특성화주제인 부산의 역사와 문화 전반을 담은 다양한 독서문화프로그램 개발과 부산교육역사관 개관◦운영으로 <br>
				학생들이 조선후기부터 현재까지의 부산교육 역사를 한 곳에서 체험할 수 있도록 하고 있음은 
				물론, 학부모교육과 연수 등 선진 지식정보 문화센터로서의 역할 수행을 다하고 있습니다. 
				<br><br>
				21세기 화두는 정보화 문화 입니다.<br>
				정보화 문화를 통한 책과 이용자의 중심에 있는 도서관 지역민과 함께 성장하고 발전하는 중앙도서관이 되고자 노력하겠습니다.
				</p>
			</div>
		</div>
		<p class="title">도서관 안내</p>
		<div style="display: flex; width: 50%;">
			<div id="infoBtn" style="display: flex; flex-direction: column; align-items: flex-start; margin-right: 20px;">
					<button class="floor-btn" value="1F" onclick="mapImgClick(this)" >1F</button>
					<button class="floor-btn" value="2F" onclick="mapImgClick(this)">2F</button>
					<button class="floor-btn" value="3F" onclick="mapImgClick(this)">3F</button>						 			
			</div>	
			<div id="mapImg"><!-- 시설 배치도 보여줄 자리 --></div>
		</div>	
		<p class="title">시설현황</p>
		<div id="roomInpo">
			<table>
				<colgroup>
					<col style="width: 10%;">
					<col style="width: 25%;">
					<col style="width: 20%;" span="2">
					<col style="width: 25%;">
				</colgroup>
				<thead>
					<tr>
						<th rowspan="2" scope="col">구분</th>
						<th rowspan="2" scope="col">대지</th>
						<th colspan="2" scope="col">건 물</th>
						<th rowspan="2" scope="col">비고</th>
					</tr>
					<tr>
						<th scope="col">구조</th>
						<th scope="col">면적</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>본관</td>
						<td>22,466㎡</td>
						<td>철근콘크리트 슬라브·지상3층</td>
						<td>6,260㎡</td>
						<td>개관 '1990.4.28</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div id="roomInpo2">
		  <table>
		    <thead>
		      <tr>
		        <th colspan="2">구분</th>
		        <th>층 별</th>
		        <th>면적(㎡)</th>
		        <th>좌석 수</th>
		      </tr>
		    </thead>
		    <tbody>
		      <tr>
		        <td rowspan="3">열람실</td>
		        <td>중앙 열람실</td>
		        <td>1층</td>
		        <td>522</td>
		        <td>269</td>
		      </tr>
		      <tr>
		        <td>제 1 열람실</td>
		        <td>1층</td>
		        <td>116</td>
		        <td>58</td>
		      </tr>
		      <tr>
		        <td>제 2 열람실</td>
		        <td>2층</td>
		        <td>210</td>
		        <td>73</td>
		      </tr>
		      <tr>
		        <td rowspan="3">자료실</td>
		        <td>어린이실</td>
		        <td>1층</td>
		        <td>216</td>
		        <td>73</td>
		      </tr>
		      <tr>
		        <td>디지털자료실</td>
		        <td>3층</td>
		        <td>183</td>
		        <td>60</td>
		      </tr>
		      <tr>
		        <td>보존서고</td>
		        <td>2층</td>
		        <td>80</td>
		        <td>-</td>
		      </tr>
		      <tr>
		        <td rowspan="10">기타 시설</td>
		        <td>미팅룸 A</td>
		        <td>1층</td>
		        <td>82</td>
		        <td>10</td>
		      </tr>
		      <tr>
		        <td>미팅룸 B</td>
		        <td>3층</td>
		        <td>132</td>
		        <td>15</td>
		      </tr>
		      <tr>
		        <td>미팅룸 C</td>
		        <td>3층</td>
		        <td>124</td>
		        <td>15</td>
		      </tr>
		      <tr>
		        <td>스터디룸 A</td>
		        <td>2층</td>
		        <td>173</td>
		        <td>30</td>
		      </tr>
		      <tr>
		        <td>스터디룸 B</td>
		        <td>2층</td>
		        <td>182</td>
		        <td>30</td>
		      </tr>
		      <tr>
		        <td>스터디룸 C</td>
		        <td>2층</td>
		        <td>194</td>
		        <td>30</td>
		      </tr>
		      <tr>
		        <td>제 1휴게실</td>
		        <td>1층</td>
		        <td>96</td>
		        <td>32</td>
		      </tr>
		      <tr>
		        <td>제 2휴게실</td>
		        <td>3층</td>
		        <td>121</td>
		        <td>38</td>
		      </tr>
		      <tr>
		        <td>문화강좌실</td>
		        <td>3층</td>
		        <td>98</td>
		        <td>20</td>
		      </tr>
		      <tr>
		        <td>북카페</td>
		        <td>2층</td>
		        <td>153</td>
		        <td>38</td>
		      </tr>
		    </tbody>
		  </table>
		</div>
	</div>
</body>
<script>
	//층버튼 클릭 시 해당 층의 시설 배치도 보여주는 함수
	function mapImgClick(btn) {
        var mapImg = document.getElementById("mapImg");
        var btnValue = event.target.value;
        
        // 모든 버튼의 active 클래스 제거
        document.querySelectorAll('.floor-btn').forEach(btn => {
          btn.classList.remove('active');
        });

        // 클릭된 버튼에 active 클래스 추가
        btn.classList.add('active');
        
        if (btnValue == "1F") {
            mapImg.innerHTML = "<img src='../information/img/mapImg_1F.png' alt='1F Map'>";
        } else if (btnValue == "2F") {
            mapImg.innerHTML = "<img src='../information/img/mapImg_2F.png' alt='2F Map'>";
        } else if (btnValue == "3F") {
            mapImg.innerHTML = "<img src='../information/img/mapImg_3F.png' alt='3F Map'>";
        } 
	}//mapImgClick
	
	//층버튼을 누르지 않아도 기본적으로 1F 시설 배치도 보여주는 함수
	function showDefaultMap() {
		document.getElementById("mapImg").innerHTML = "<img src='../information/img/mapImg_1F.png' alt='1F Map'>";	
	
	    // 1F 버튼에 active 클래스 추가
	    const firstFloorBtn = document.querySelector('.floor-btn[value="1F"]');
	    if (firstFloorBtn) {
	        firstFloorBtn.classList.add('active');
	    }	
	}
	
	//층버튼 클릭시 버튼 효과
	document.getElementById("infoBtn").addEventListener("click", event	=> {
		if (event.target && event.target.classList.contains("seat-btn")) {
    	   document.querySelectorAll(".seat-btn").forEach(btn => {
    		   //버튼 효과 초기화
               btn.classList.remove("selected-seat-btn");
           });
    	   //클릭한 버튼에 효과 주기
           event.target.classList.add("selected-seat-btn");
       }
    });

</script>
</html>

