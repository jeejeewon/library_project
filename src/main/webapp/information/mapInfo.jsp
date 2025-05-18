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
	
	.floor-btn {
		background-color: #87CEEB;
	    border: none;
	    color: white;
	    padding: 10px 28px;
	    text-align: center;
	    text-decoration: none;
	    display: inline-block; 
	    font-size: 20px;
	    margin: 4px 2px;
	    cursor: pointer;
	    border-radius: 12px;	  
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
	
</style>
</head>
<body onload="showDefaultMap()">	
	<div id="infoContainer" align="center"> 
		<h2>도서관 소개</h2>
		<div id="LibraryInpo">
			<p>도서관 소개 문구</p>
		</div>
		<h2>도서관 안내</h2>
		<div id="infoBtn" align="center">
			<button class="floor-btn" value="1F" onclick="mapImgClick(this)" >1층</button>
			<button class="floor-btn" value="2F" onclick="mapImgClick(this)">2층</button>
			<button class="floor-btn" value="3F" onclick="mapImgClick(this)">3층</button>	 			
			<button class="floor-btn" value="4F" onclick="mapImgClick(this)">4층</button>	 			
		</div>	
		<div id="mapImg"><!-- 시설 배치도 보여줄 자리 --></div>
		<h2>시설현황</h2>
		<div id="mapRoom">
			시설 현황 나올 자리
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
            mapImg.innerHTML = "<p style='font-size:24px;'>1F</p><br><br><img src='../information/img/mapImg1F.PNG' alt='1F Map'>";
        } else if (btnValue == "2F") {
            mapImg.innerHTML = "<p style='font-size:24px;'>2F</p><br><br><img src='../information/img/mapImg2F.PNG' alt='2F Map'>";
        } else if (btnValue == "3F") {
            mapImg.innerHTML = "<p style='font-size:24px;'>3F</p><br><br><img src='../information/img/mapImg3F.PNG' alt='3F Map'>";
        } else if (btnValue == "4F") {
            mapImg.innerHTML = "<p style='font-size:24px;'>4F</p><br><br><img src='../information/img/mapImg4F.PNG' alt='4F Map'>";
    	}
	}//mapImgClick
	
	//층버튼을 누르지 않아도 기본적으로 1F 시설 배치도 보여주는 함수
	function showDefaultMap() {
		document.getElementById("mapImg").innerHTML = "<p style='font-size:24px;'>1F</p><br><br><img src='../information/img/mapImg1F.PNG' alt='1F Map'>";
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

