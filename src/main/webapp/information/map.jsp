<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>

	#mapStyle{
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100%;
	}
	
	h2{		
		font-size: 25px;
		font-weight: bold;
		margin: 50px;
		border-bottom: 1px solid #7c7c7c;
		width: 50%;
		margin: 50px auto;
		padding-bottom: 10px;		
		color: #002c66;
	}
	
	#mapContent{
		margin: 50px auto;
		width: 50%;

	}
	.tit {
		font-size: 22px;
		text-align: left;
		font-weight: bold;
		margin: 0 0 10px 0;
	}
	
	#mapContent li{
		margin-bottom: 5px;
	}
	
	#mapContent h4{
		font-size: 18px;
		margin-bottom: 10px;
		font-weight: bold;
		display: flex;
		align-items: center;
		gap: 0.5rem; /* 이미지와 텍스트 간격 */
		font-size: 1.2rem; /* 필요시 조정 */
	}
	
	#mapContent ul{
		margin-bottom: 10px;
	}
	#mapInpo {
		margin-left: 20px;
		margin-bottom: 50px;
	}
	
	#mapInpo ul{
		margin-left: 90px;
	}	
	
	.map-inpo-li{
		margin-left: 20px;
	}
		
	.map-title {
		display: flex;
		align-items: center;
		gap: 0.5rem;
		font-weight: bold;

	}
	
	.map-title span{
		background-color: #002c66;
		width: 4.5rem;
        height: 4.5rem;
        border-radius: 50%;
        display: flex;
        justify-content: center; 
		align-items: center;    
	}
	
	.map-title img {
		object-fit: contain;		
	}
	
	.dott{
		color: #007bff;
	}
	

</style>
<meta charset="UTF-8">
<title>오시는 길</title>
<script>
	const script = document.createElement("script");
	script.src = "https://dapi.kakao.com/v2/maps/sdk.js?appkey=앱키&autoload=false&libraries=clusterer,services";
	document.head.appendChild(script);

	script.onload = () => {
		kakao.maps.load(() => {
			
			const node = document.getElementById('map'); //지도를 표시할 <div>
			const center = new kakao.maps.LatLng(35.1828835404, 129.0503001507); //도서관 좌표값
			
			//지도 생성 및 설정
			const map = new kakao.maps.Map(node, {
				center,
				level: 3 //지도 확대/축소 레벨 (1~14, 숫자가 클수록 더 멀리서 보임)
			});
	
			// 마커 생성
			const marker = new kakao.maps.Marker({
				position: center, //도서관 좌표값에 마커 추가
				map: map
			});
		});
	};
</script>

</head>
<body>
	<h2>찾아오시는 길</h2>
	<div id="mapStyle">
		<div id="map" style="width: 50%; height: 500px; background-color: gray"><!-- 카카오맵이 들어올 div --></div>
	</div>
	<div id="mapContent">
		<p class="tit">대중교통 이용안내</p>
		<div id="mapInpo">
			<h4 class="map-title">
				<span><img src="../information/img/ico_subway.png" alt="지하철아이콘"></span>
				지하철 이용시
			</h4>
				<ul>
					<li><span class="dott">•</span> 1호선 동래역 하차후, 버스이용(마을버스 17번)</li>
					<li class="map-inpo-li">- 2번출구앞 좌측편 동래지하철역 버스정류장에서 마을버스 17번 이용</li>
					<li><span class="dott">•</span> 3호선 종합운동장역 하차후, 버스이용(54번, 83-1번, 마을버스 17번)</li>	
					<li class="map-inpo-li">- 9번출구앞에서 약50m직진 후, 삼정그린아파트 정류장에서 54번, 83-1번, 마을버스 17번 이용</li>	
					<li><span class="dott">•</span> 1,2호선 서면역 하차후, 버스이용(133번, 54번, 81번, 83-1번, 103번)</li>	
					<li class="map-inpo-li">- 13번출구로 나와서 부전시장입구 정류장에서 133번, 54번, 81번, 83-1번, 103번 이용</li>			
				</ul>
			<h4 class="map-title">
				<span><img src="../information/img/ico_bus.png" alt="버스 아이콘"></span>
				버스 이용시
			</h4>
				<ul>
					<li><span class="dott">•</span> 동래방면 : 44번, 마을버스 17번</li>			
					<li><span class="dott">•</span> 구포,가야방면 : 33번</li>			
					<li><span class="dott">•</span> 만덕방면 : 133번</li>			
					<li><span class="dott">•</span> 해운대방면 : 63번</li>			
					<li><span class="dott">•</span> 서면방면 : 54번, 81번, 103번</li>			
				</ul>
		</div>
		<p class="tit">도로교통 이용안내</p>
		<div id="mapInpoCar">
			<ul>
				<li><span class="dott">•</span> 미남, 동래, 연산교차로 -> 거성사거리에서 초읍방향으로 900m 직진</li>
				<li><span class="dott">•</span> 서면교차로 -> 부암교차로(초읍방향) -> 어린이대공원 앞에서 우회전</li>
				<li><span class="dott">•</span> 하마정사거리 -> 부산시민공원 정문(국립국악원)에서 좌회전 -> 초읍삼거리 우회전</li>
			</ul>
		</div>
	</div>
</body>


</html>