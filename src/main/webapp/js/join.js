//약관동의 체크박스를 클릭했을 때
$('#checkAll').click(function() {
	// "모든 약관 동의" 체크 박스의 상태를 따라 개별 약관 체크 박스들을 체크/해제
	$('.agree-checkbox').prop('checked', this.checked);
	// 모든 체크 박스의 상태를 확인해서 버튼 활성화 여부 결정
	checkButtonStatus();
});

// 개별 약관 체크 박스들을 클릭했을 때
$('.agree-checkbox').click(function() {
	// 개별 약관 중 하나라도 체크가 안 되어 있으면 "모든 약관 동의" 해제
	if ($('.agree-checkbox:not(.optional)').length === $('.agree-checkbox:not(.optional):checked').length) {
		// 필수 약관이 모두 체크되었을 때만
		$('#checkAll').prop('checked', true);
	} else {
		$('#checkAll').prop('checked', false);
	}
	// 모든 체크 박스의 상태를 확인해서 버튼 활성화 여부 결정
	checkButtonStatus();
});

// 버튼 활성화 상태를 확인하는 함수 (필수 약관만 체크)
function checkButtonStatus() {
	// 필수 약관 체크 박스의 개수와 체크된 필수 약관 체크 박스의 개수를 비교
	if ($('.agree-checkbox:not(.optional)').length === $('.agree-checkbox:not(.optional):checked').length) {
		$('#nextButton').prop('disabled', false); // 필수가 모두 체크되면 버튼 활성화
		$("#agreeInput").text("");
	} else {
		$('#nextButton').prop('disabled', true); // 아니면 비활성화
		$("#agreeInput").text("약관에 동의해 주세요!").css("color", "red");
	}
}

$("#id").focusout(function() {

	//제대로 입력 했을 경우 
	if ($("#id").val().length >= 3 && $("#id").val().length < 20) {

		//회원가입을 위해 입력한 아이디가 DB의 member테이블에 저장되어 있는지 확인요청
		//Ajax기술을 이용하여 비동기 통신으로 MemberController에 요청함
		$.ajax(
			{
				url: "http://localhost:8090/LibraryProject/member/joinIdCheck.me",
				type: "post", //요청 방식 POST로 설정
				async: true, //true는 비동기 요청, false는 동기 요청  중 하나 
				data: { id: $("#id").val() }, //MemberController서버페이지에 요청할 값 설정	
				dataType: "text", //MemberController서버페이지로 부터  예상 응답받을 데이터 종류 설정
				//요청 통신에 성공했을때
				//success속성에 설정된 콜백함수가 자동으로 호출되어
				//data매개변수로 MemberContoller서버페이지가 응답한 데이터가 넘어 옴
				success: function(data, textSatus) {
					//"not_usable" 또는 "useable" 둘중  하나를 
					//data매개변수로 전달 받는다.
					//MemberController서버페이지에서 전송된 아이디 중복?인지 아닌지 판단하여
					//현재 join.jsp중앙화면에 보여 주는 구문 처리 
					if (data == 'usable') { //아이디가  DB에 없으면?

						$("#idInput").text("사용할수 있는 ID입니다.").css("color", "blue");

					} else {//아이디가 DB에 있으면?(입력한 아이디 중복)
						$("#idInput").text("이미 사용중인 ID입니다.").css("color", "red");

					}
				}
			}
		);

	} else {//제대로 입력 하지 않았을 경우 

		$("#idInput").text("한글,특수문자 없이 3~20글자사이로 작성해 주세요!").css("color", "red");
	}
});



$("#pass").focusout(function() {
	if ($("#pass").val().length < 4) {
		$("#passInput").text("한글,특수문자 없이 4글자 이상으로 작성해 주세요!").css("color", "red");
	} else {
		$("#passInput").text("올바르게 입력되었습니다.").css("color", "blue");
	}
});


$("#name").focusout(function() {
	if ($("#name").val().length < 2 || $("#name").val().length > 6) {
		$("#nameInput").text("이름을 제대로 작성하여주세요.").css("color", "red");
	} else {
		$("#nameInput").text("이름입력완료!").css("color", "blue");
	}
});


$("#age").focusout(function() {
	if ($("#age").val() == "") {
		$("#ageInput").text("나이를 입력해주세요.").css("color", "red");

	} else {
		$("#ageInput").text("나이입력완료!").css("color", "blue");
	}
});


$(".gender").click(function() {
	$("#genderInput").text("성별체크완료!").css("color", "blue");
});


$("#email").focusout(function() {
	var mail = $("#email");
	var mailValue = mail.val();
	var mailReg = /^\w{5,12}@[a-z]{2,10}[\.][a-z]{2,3}[\.]?[a-z]{0,2}$/;
	var rsEmail = mailReg.test(mailValue);

	if (!rsEmail) {
		$("#emailInput").text("이메일 형식이 올바르지 않습니다.").css("color", "red");

	} else {
		$("#emailInput").text("올바르게 입력되었습니다.").css("color", "blue");
	}
});


$("#tel").focusout(function() {
	var t = $("#tel");
	var telVal = t.val();
	var tReg = RegExp(/^0[0-9]{8,10}$/);
	var rsTel = tReg.test(telVal);

	if (!rsTel) {
		$("#telInput").text("전화번호 형식이 올바르지 않습니다.").css("color", "red");
	} else {
		$("#telInput").text("올바르게 입력되었습니다.").css("color", "blue");
	}
});


//회원가입 버튼 클릭시 호출되는 함수 
function check() {

	var checkboxes = $(".agree-checkbox");

	var checkboxes = $(".agree-checkbox");
	   console.log("총 약관 체크박스 개수:", checkboxes.length); 
	   console.log("체크된 약관 체크박스 개수:", checkboxes.filter(":checked").length); // 몇 개가 체크됐는지

	   // 모든 약관 동의 체크했는지 최종 검사
	   if (checkboxes.length !== checkboxes.filter(":checked").length) {
	       $("#agreeInput").text("모든 약관에 동의해 주세요!").css("color", "red");
	       console.log("약관 동의 검사 실패! 함수 종료."); 
	       return; 
	   }

	   $("#agreeInput").text("약관 동의 완료!").css("color", "blue");

	//====================================================================================================

	var id = $("#id");
	var idValue = id.val();
	var idReg = RegExp(/^[A-Za-z0-9_\-]{3,20}$/);
	var resultId = idReg.test(idValue);

	if (!resultId) {
		$("#idInput").text("한글,특수문자 없이 3~20글자사이로 작성해 주세요!").css("color", "red");
		id.focus();

		return false;
	}

	//====================================================================================================

	var pass = $("#pass");
	var passValue = pass.val();
	var passReg = RegExp(/^[A-Za-z0-9_\-]{4,20}$/);
	var resultPass = passReg.test(passValue);

	if (!resultPass) {
		$("#passInput").text("한글,특수문자 없이 4글자 이상으로 작성해 주세요!").css("color", "red");
		pass.focus();

		return false;
	} else {
		$("#passInput").text("올바르게 입력되었습니다.").css("color", "blue");
	}

	//====================================================================================================

	var name = $("#name");
	var nameValue = name.val();
	var nameReg = RegExp(/^[가-힣]{2,6}$/);
	var resultName = nameReg.test(nameValue);

	if (!resultName) {
		$("#nameInput").text("이름을 한글로 작성하여주세요.").css("color", "red");
		name.focus();

		return false;
	} else {
		$("#nameInput").text("올바르게 입력되었습니다.").css("color", "blue");
	}

	//====================================================================================================

	var age = $("#age");
	var ageValue = age.val();

	if (ageValue == "") {
		$("#ageInput").text("나이를 입력해주세요.").css("color", "red");
		age.focus();

		return false;
	} else {
		$("#ageInput").text("올바르게 입력되었습니다.").css("color", "blue");
	}

	//====================================================================================================

	var gender = $(".gender:checked");
	var genderValue = gender.val();
	genderValue = $.trim(genderValue);

	if (genderValue == "") {

		$("#genderInput").text("성별을 체크 해주세요.").css("color", "red");

		return false;

	}
	//====================================================================================================

	var email = $("#email");
	var emailValue = email.val();
	var emailReg = /^\w{5,12}@[a-z]{2,10}[\.][a-z]{2,3}[\.]?[a-z]{0,2}$/;
	var resultEmail = emailReg.test(emailValue);

	if (!resultEmail) {
		$("#emailInput").text("이메일 형식이 올바르지 않습니다.").css("color", "red");
		email.focus();
		return false;
	} else {
		$("#emailInput").text("올바르게 입력되었습니다.").css("color", "blue");
	}

	//====================================================================================================

	var tel = $("#tel");
	var telValue = tel.val();
	var telReg = RegExp(/^0[0-9]{8,10}$/);
	var resultTel = telReg.test(telValue);
	if (!resultTel) {
		$("#telInput").text("전화번호 형식이 올바르지 않습니다.").css("color", "red");
		tel.focus();
		return false;
	} else {
		$("#telInput").text("올바르게 입력되었습니다.").css("color", "blue");
	}

	//====================================================================================================


	//<form aciton="/member/joinPro.me">으로 회원 가입 요청!
	$("form").submit();

}//check함수 끝 