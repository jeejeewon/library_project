<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../css/libReserve.css">
<meta charset="UTF-8">
<title>시설 예약 안내</title>
</head>
<body>
    <div align="center" id="content">
	    <p class="title">시설 예약 안내</p>
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
							<dt class="step-dt">STEP 1</dt>
							<dd><p>회원가입 후 홈페이지에서 시설이용 신청</p></dd>
						</dl>
					</li>
					<li>
						<dl>
							<dt class="step-dt">STEP 2</dt>
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