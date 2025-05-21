<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">


	h2{		
		font-size: 25px;
		font-weight: bold;
		margin: 50px;
		border-bottom: 1px solid #7c7c7c;
		width: 50%;
		margin: 50px auto;
		padding-bottom: 10px;		
		color: #002c66;
		text-align: left;
	}
	
	#roomInpo {
		margin: 2rem auto;
		max-width: 900px;
		font-family: 'Noto Sans KR', sans-serif;
		color: #333;
	}
	
	.tit {
		font-size: 22px;
		text-align: left;
		font-weight: bold;
		margin: 0 0 10px 0;
	}
	
	.con{
		text-align: left;
	}
	
	.con span {
		color: #007bff;	
	}
	
	#notice {
		border: 1px solid #ddd;
		padding: 35px 40px;
		border-top: 1px solid #222;
	}
	
	#notice ul{	
		display: inline-block;
		width: 100%;
		margin-block-start: 1em;
	    margin-block-end: 1em;
	    padding-inline-start: 40px;
	    unicode-bidi: isolate;
	    list-style-type: disc;
	}
	
	
	#notice li{
		display: list-item;
	    text-align: -webkit-match-parent;
	    unicode-bidi: isolate;
	}
	
	#notice ul li{
		width: 50%;
		min-height: 100px;
		float: left;
		display: table;
		background-repeat: no-repeat;
		background-position: 0 50%;	
	}
	
	#notice ul li p{
		text-align: left;
		display: table-cell;
	    vertical-align: middle;
	    padding: 0 20px 0 90px;
	    letter-spacing: -0.06em;
	    font-family: 'NotoKrR';
	    word-break: keep-all;
	}
	
	div{
		display: block;
	    unicode-bidi: isolate
	}
	
	#notice ul li.icon1{background-image: url("../information/img/ico_room1.png");}
	#notice ul li.icon2{background-image: url("../information/img/ico_room2.png");}
	#notice ul li.icon3{background-image: url("../information/img/ico_room3.png");}
	#notice ul li.icon4{background-image: url("../information/img/ico_room4.png");}
	#notice ul li.icon5{background-image: url("../information/img/ico_room5.png");}
	#notice ul li.icon6{background-image: url("../information/img/ico_room6.png");}
	
	.step-list {
		display: inline-block;
		width: 100%;
		overflow: hidden;
	}
	
	.step-list li{
		width: 50%;
		float: left;
		padding: 2% 5% 2% 0%;
	}
	.step-list li dl dt {
		padding: 10px 20px;
	    border: 1px solid #222;
	    font-family: 'Roboto', sans-serif;
	    font-weight: 600;
	    color: #222;
	    letter-spacing: 0.01em;
	    text-align: center;
	}
	
	.step-list li dl dd{
		display: table;
	    width: 100%;
	    min-height: 100px;
	    background: #f5f5f5;
	    font-family: 'NotoKrM';
	    font-size: 15px;
	    color: #222;
	    word-break: keep-all;
	    text-align: center;
	}
	
	.step-list li dl dd p{
		display: table-cell;
	    vertical-align: middle;
	    padding: 0 15px;
	    line-height: 1.5;
	}
	
	dt {
	    display: block;
    	unicode-bidi: isolate;
	}
	
	ol{
	    margin-block-start: 1em;
	    margin-block-end: 1em;
	    padding-inline-start: 40px;
	    unicode-bidi: isolate;
	}
	
	#roomUse {
		margin: 2rem auto;
		max-width: 900px;
		font-family: 'Noto Sans KR', sans-serif;
		color: #333;	
	}
	
	#roomUse table{
		width: 100%;
		border-collapse: collapse;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
	}
	
	#roomUse th, td {
		border: 1px solid #ccc;
		padding: 0.8rem;
		text-align: center;
		vertical-align: middle;
	}
	
	#roomUse thead th{
		background-color: #f3f6f9;
		font-weight: 600;
		color: #2a2a2a;	
	}
	
	#roomUse td{
		font-size: 0.95rem;	
	}

	.reserve-btn {
		color: white;
		background-color: #003c83;
		padding: 10px 20px 10px 20px;
		border-radius: 10px;
	}
	
	.reserve-btn:hover {
		background-color: #002c66;
	}
	
</style>
</head>
<body>
    <div align="center" id="content">
	    <h2>시설 예약 안내</h2>
		<div id="roomInpo">
			<p class="tit">이용시간</p>
			<div id="time" style="margin-bottom: 50px;">				
				<p class="con"><span>•</span> 화-금 : 10:00 - 20:00</p>
				<p class="con"><span>•</span> 토/일 : 10:00 - 20:00</p>
				<p class="con"><span>•</span> 정기휴관일 및 법정공휴일, 임시휴관일에는 이용이 불가능합니다.</p>
				<p class="con"><span>•</span> 독서토론 및 독서진흥 활동을 위한 목적외 정치, 종교, 영리, 개인 목적으로는 시설 이용이 불가능합니다.</p>
			</div>
			<p class="tit">참고 및 주의사항</p>
			<div id="notice" style="margin-bottom: 50px;">
				<ul>
					<li class="icon1"><p>냉 · 난방 가동은 도서관 전체 냉 · 난방이 운영될 때에 한하며, 이용시설공간 사용은 무료</p></li>
					<li class="icon2"><p>이용시설공간에 설치된 시설물을 제외하고, 행사에 필요한 비품은 사용자가 준비</p></li>
					<li class="icon3"><p>사용자는 사용시간 내 각종 사건 사고에 대비한 사전 안전 조치를 하여야 함</p></li>
					<li class="icon4"><p>사용 중 시설물 파손 및 도난 등 문제 발생 시 사용자 원상복구</p></li>
					<li class="icon5"><p>이용목적과 어긋나는 상업행위 및 정치, 종교교육 활동 등 금지</p></li>
					<li class="icon6"><p>시설 이용 시 시립도서관 회원증을 반드시 지참</p></li>
				</ul>
			</div>
			<p class="tit">이용방법</p>
			<div style="margin-bottom: 50px; align-items: center;">
				<ol class="step-list">
					<li>
						<dl>
							<dt>STEP 1</dt>
							<dd><p>회원가입 후 홈페이지에서 시설이용 신청</p></dd>
						</dl>
					</li>
					<li>
						<dl>
							<dt>STEP 2</dt>
							<dd><p>도서관 회원증을 태깅하여 예약시간 내 출입가능</p></dd>
						</dl>
					</li>					
				</ol>
			</div>	
			<div id="roomUse">
				<p class="tit">이용시설공간</p>
				<table>
					<colgroup>
						<col style="width: 20%;">
						<col style="width: 10%;">
						<col style="width: 10%;">
						<col style="width: 25%;">
					</colgroup>
					<thead>
						<tr>
							<th>구분</th>
							<th>층수</th>
							<th>좌석수</th>
							<th>예약</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>미팅룸 A</td>
							<td>1층</td>
							<td>10</td>
							<td rowspan="3"><a class="reserve-btn" href="<%=request.getContextPath()%>/reserve/reserveMeeting">미팅룸 예약하러가기</a></td>
						</tr>
						<tr>
							<td>미팅룸 B</td>
							<td rowspan="2">3층</td>
							<td rowspan="2">15</td>
						</tr>
						<tr>
							<td>미팅룸 C</td>
						</tr>
						<tr>
							<td>스터디룸 A</td>
							<td rowspan="3">2층</td>
							<td rowspan="3">30</td>
							<td rowspan="3"><a class="reserve-btn" href="<%=request.getContextPath()%>/reserve/reserveStudy">스터디룸 예약하러가기</a></td>
						</tr>
						<tr><td>스터디룸 B</td></tr>
						<tr><td>스터디룸 C</td></tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>	
</body>
</html>