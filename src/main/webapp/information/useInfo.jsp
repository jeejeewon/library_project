<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../css/information.css">
<meta charset="UTF-8">
<title>이용안내</title>
</head>
<body>
	<div align="center" id="useInfo">
		<p class="title">도서관 이용안내</p>
		<div id="libTime">
			<p class="tit">이용시간</p>
			<table class="use-info-table">
				<thead>
					<tr>
						<th rowspan="2">구분</th>
						<th colspan="2">이용시간</th>
					</tr>
					<tr>
						<th>화-금</th>
						<th>토/일</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>열람실</td>
						<td rowspan="5">10:00 - 20:00</td>
						<td rowspan="3">10:00 - 18:00</td>
					</tr>
					<tr>
						<td>자료실</td>
					</tr>
					<tr>
						<td>문화강좌실</td>
					</tr>
					<tr>
						<td>미팅룸</td>
						<td rowspan="2"">10:00 - 20:00</td>
					</tr>
					<tr>
						<td>스터디룸</td>
					</tr>
					<tr>
						<td>북카페</td>
						<td>10:00 - 18:00</td>
						<td>10:00 - 18:00</td>
					</tr>
				</tbody>
			</table>
			<p class="tit">휴관일</p>
			<p class="holiday"><span>•</span> 정기휴관 : 매주 월요일<br>
			<span>•</span> 법정공휴일(토/일요일이 법정공휴일과 겹칠 경우 휴관)및 국가가 정한 임시 휴일<br>
			<span>•</span> 임시휴관일(도서관장이 필요하다고 인정하는 날)
			</p>
		</div>
		<p class="title">도서 대출 안내</p>
		<div id="bookLoan">	
			<p class="tit">대출안내</p>
			<table class="use-info-table">
				<thead>
					<tr>
						<th>구분</th>
						<th>설명</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>대출방법</td>
						<td class="con">본인 회원증(모바일 또는 실물회원증)제시</td>
					</tr>
					<tr>
						<td>대출기한</td>
						<td class="con">14일</td>
					</tr>
					<tr>
						<td>대출권수</td>
						<td class="con">1인 5권</td>
					</tr>	
					<tr>
						<td>무인예약대출</td>
						<td class="con">도서관 1층 무인자동화코너에서 수령가능</td>
					</tr>
					<tr>
						<td>재대출</td>
						<td class="con">동일자료 연속대출 불가 (익일 후 대출가능)</td>
					</tr>		
					<tr>
						<td>연체시</td>
						<td class="con">연체일수만큼 대출중지<span style="color: red;">(여러 권일 경우 최장 연체일수 적용)</span></td>
					</tr>
					<tr>
						<td>자료분실/파손</td>
						<td class="con">대출도서를 분실, 파손하였을 경우 동일 도서로 변상</td>
					</tr>																				
				</tbody>
			</table>
			<div id="conInpo">
				<p class="tit">반납안내</p>
				<p class="con2"><span>•</span> 개관시간내에는 언제든지 반납가능</p>
				<p class="con2"><span>•</span> 자료실 이용시간 외 반납은 무인반납기에서 가능</p>
			</div>
			<div id="conInpo">
				<p class="tit">유의사항 안내</p>
				<p class="con2"><span>•</span> 타인의 회원증으로 대출불가</p>
				<p class="con2"><span>•</span> 회원증을 분실하면 즉시 분실신고를 하고 2주 후 재발급 받아야함</p>
			</div>
		</div>	
	</div>
</body>
</html>