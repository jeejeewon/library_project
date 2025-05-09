function check() {

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

	$("form").submit();
}