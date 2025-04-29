<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
	button {
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
		
	button:hover {
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
		<div id="infoBtn" align="center">
			<button value="1F" onclick="mapImgClick()" >1F</button>
			<button value="2F" onclick="mapImgClick()">2F</button>
			<button value="3F" onclick="mapImgClick()">3F</button>	 			
			<button value="4F" onclick="mapImgClick()">4F</button>	 			
		</div>	
		<div id="mapImg">
			시설 배치도 나올 자리
		</div>
		<div id="mapRoom">
			시설 현황 나올 자리	
		</div>
	</div>
</body>

<script>

	//층버튼 클릭 시 해당 층의 시설 배치도 보여주는 함수
	function mapImgClick() {
        var mapImg = document.getElementById("mapImg");
        var btnValue = event.target.value;
        
        if (btnValue == "1F") {
            mapImg.innerHTML = "<p style='font-size:24px;'>1F</p><br><br><img src='../infomation/img/mapImg1F.PNG' alt='1F Map'>";
        } else if (btnValue == "2F") {
            mapImg.innerHTML = "<p style='font-size:24px;'>2F</p><br><br><img src='../infomation/img/mapImg2F.PNG' alt='2F Map'>";
        } else if (btnValue == "3F") {
            mapImg.innerHTML = "<p style='font-size:24px;'>3F</p><br><br><img src='../infomation/img/mapImg3F.PNG' alt='3F Map'>";
        } else if (btnValue == "4F") {
            mapImg.innerHTML = "<p style='font-size:24px;'>4F</p><br><br><img src='../infomation/img/mapImg4F.PNG' alt='4F Map'>";
    	}
	}//mapImgClick
	
	//층버튼을 누르지않아도 기본적으로 1F 시설 배치도 보여주는 함수
	function showDefaultMap() {
		document.getElementById("mapImg").innerHTML = "<p style='font-size:24px;'>1F</p><br><br><img src='../infomation/img/mapImg1F.PNG' alt='1F Map'>";
	}

</script>

</html>

