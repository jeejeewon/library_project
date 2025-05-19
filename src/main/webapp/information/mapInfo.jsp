<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
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
	
	#infoBtn{
		text-align: left;
		width: 50%;
	}
	
	.floor-btn {
		background-color: #87CEEB;
		border: none;
		color: white;
		padding: 10px 15px;
		text-align: center;
		text-decoration: none;
		display: inline-block;
		font-size: 20px;
		margin: 6px 0;
		cursor: pointer;
		border-radius: 12px;
		width: 70px;

	}
		
	.floor-btn:hover {
      background-color: #50B8E2;    
    }
    
    .floor-btn.active{
    	background-color: #50B8E2; 
    }
    
	html, body {
		height: 100%;
		margin: 0;
	}
	
	#infoContainer {
		min-height: 500px;
	}
	
	#inpotitle {
		margin-bottom: 30px;
	}
	
	#libraryInpo{
		margin-bottom: 100px;
	}
	
	#libraryInpo h3 {
		text-align: left;
		font-size: 24px;
		font-weight: bold;
		width: 50%;		
	}
	
	#inpoContext p{
		font-size: 17px;
		line-height: 26px;
		text-align: left;
		width: 50%;	
	
	}
	
	#roomInpo {
		margin: 2rem auto;
		max-width: 900px;
		font-family: 'Noto Sans KR', sans-serif;
		color: #333;
	}
	
	#roomInpo table {
		width: 100%;
		border-collapse: collapse;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
	}
	
	#roomInpo th,
	#roomInpo td {
		border: 1px solid #ccc;
		padding: 0.8rem;
		text-align: center;
		vertical-align: middle;
	}
	
	#roomInpo thead th {
		background-color: #f3f6f9;
		font-weight: 600;
		color: #2a2a2a;
	}
	
	#roomInpo td {
		font-size: 0.95rem;
	}
	
	#roomInpo2 {
		margin: 2rem auto;
		max-width: 900px;
		font-family: 'Noto Sans KR', sans-serif;
		color: #333;
	}
	
	#roomInpo2 table {
		width: 100%;
		border-collapse: collapse;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
	}
	
	#roomInpo2 th,
	#roomInpo2 td {
		border: 1px solid #ccc;
		padding: 0.8rem;
		text-align: center;
		vertical-align: middle;
	}
	
	#roomInpo2 thead th {
		background-color: #f3f6f9;
		font-weight: 600;
		color: #2a2a2a;
	}
	
	#roomInpo2 td {
		font-size: 0.95rem;
	}
	


	
</style>
</head>
<body onload="showDefaultMap()">	
	<div id="infoContainer" align="center"> 
		<h2>도서관 소개</h2>
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
		<h2>도서관 안내</h2>
		<div style="display: flex; width: 50%;">
			<div id="infoBtn" style="display: flex; flex-direction: column; align-items: flex-start; margin-right: 20px;">
				<button class="floor-btn" value="1F" onclick="mapImgClick(this)" >1층</button>
				<button class="floor-btn" value="2F" onclick="mapImgClick(this)">2층</button>
				<button class="floor-btn" value="3F" onclick="mapImgClick(this)">3층</button>	 			
				<button class="floor-btn" value="4F" onclick="mapImgClick(this)">4층</button>	 			
			</div>	
			<div id="mapImg"><!-- 시설 배치도 보여줄 자리 --></div>
		</div>	
		<h2>시설현황</h2>
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
						<td>철근콘크리트 슬라브· 지하2층,지상4층</td>
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
		        <td>146</td>
		        <td>24</td>
		      </tr>
		      <tr>
		        <td rowspan="3">자료실</td>
		        <td>어린이실</td>
		        <td>1층</td>
		        <td>216</td>
		        <td>24</td>
		      </tr>
		      <tr>
		        <td>종합자료실</td>
		        <td>3층</td>
		        <td>424</td>
		        <td>50</td>
		      </tr>
		      <tr>
		        <td>디지털자료실</td>
		        <td>4층</td>
		        <td>80</td>
		        <td>20</td>
		      </tr>
		      <tr>
		        <td rowspan="8">기타 시설</td>
		        <td>미팅룸 A</td>
		        <td>1층</td>
		        <td>121</td>
		        <td>30</td>
		      </tr>
		      <tr>
		        <td>미팅룸 B</td>
		        <td>2층</td>
		        <td>57</td>
		        <td>15</td>
		      </tr>
		      <tr>
		        <td>미팅룸 C</td>
		        <td>3층</td>
		        <td>120</td>
		        <td>17</td>
		      </tr>
		      <tr>
		        <td>스터디룸 A</td>
		        <td>3층</td>
		        <td>234</td>
		        <td>30</td>
		      </tr>
		      <tr>
		        <td>스터디룸 B</td>
		        <td>3층</td>
		        <td>240</td>
		        <td>34</td>
		      </tr>
		      <tr>
		        <td>스터디룸 C</td>
		        <td>4층</td>
		        <td>95</td>
		        <td>10</td>
		      </tr>
		      <tr>
		        <td>제 1휴게실</td>
		        <td>1층</td>
		        <td>102</td>
		        <td>32</td>
		      </tr>
		      <tr>
		        <td>제 2휴게실</td>
		        <td>3층</td>
		        <td>121</td>
		        <td>38</td>
		      </tr>
		      <tr>
		        <td colspan="2">서고</td>
		        <td>지하 1층</td>
		        <td>311</td>
		        <td>-</td>
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
            mapImg.innerHTML = "<img src='../information/img/mapImg1F.PNG' alt='1F Map'>";
        } else if (btnValue == "2F") {
            mapImg.innerHTML = "<img src='../information/img/mapImg2F.PNG' alt='2F Map'>";
        } else if (btnValue == "3F") {
            mapImg.innerHTML = "<img src='../information/img/mapImg3F.PNG' alt='3F Map'>";
        } else if (btnValue == "4F") {
            mapImg.innerHTML = "<img src='../information/img/mapImg4F.PNG' alt='4F Map'>";
    	}
	}//mapImgClick
	
	//층버튼을 누르지 않아도 기본적으로 1F 시설 배치도 보여주는 함수
	function showDefaultMap() {
		document.getElementById("mapImg").innerHTML = "<img src='../information/img/mapImg1F.PNG' alt='1F Map'>";
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

