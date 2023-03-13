function validateAddress() {

	var address = $("#address").val();
	var city = $("#city").val();
	var zipcode = $("#zipcode").val();

	var returnValue = true;

	if (address === "") {
		$("#addressError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#addressError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (city === "") {
		$("#cityError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#cityError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (zipcode === "") {
		$("#zipcodeError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#zipcodeError").css({
			"visibility" : "hidden"
		});
	}
	;

	return returnValue;
};

function validateProduct() {
	var productName = $("#productName").val();
	var description = $("#description").val();
	var category = $("#category").val();
	var productPrice = $("#productPrice").val();

	var productPriceNum = Number(productPrice);
	var returnValue = true;

	if (productName === "") {
		$("#productNameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#productNameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (description === "" || description.length > 90) {
		$("#descriptionError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#descriptionError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (category === "") {
		$("#categoryError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#categoryError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (productPrice === "" || productPriceNum <= 0) {
		$("#productPriceError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#productPriceError").css({
			"visibility" : "hidden"
		});
	}
	;

	return returnValue;
};

function validateRegForm() {

	var firstName = $("#firstName").val();
	var lastName = $("#lastName").val();
	var email = $("#email").val();
	var password = $("#password").val();
	var address = $("#address").val();
	var city = $("#city").val();
	var zipcode = $("#zipcode").val();
	var customerPhone = $("#customerPhone").val();

	var returnValue = true;
	var regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;

	if (firstName === "") {
		$("#firstNameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#firstNameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (lastName === "") {
		$("#lastNameError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#lastNameError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (email === "" || !regEmail.test(email)) {
		$("#emailError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#emailError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (password === "") {
		$("#passwordError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#passwordError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (address === "") {
		$("#addressError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#addressError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (city === "") {
		$("#cityError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#cityError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (zipcode === "") {
		$("#zipcodeError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#zipcodeError").css({
			"visibility" : "hidden"
		});
	}
	;

	if (customerPhone === "" || customerPhone.length < 9
			|| customerPhone.length > 15) {
		$("#customerPhoneError").css({
			"visibility" : "visible"
		});
		returnValue = false;
	} else {
		$("#customerPhoneError").css({
			"visibility" : "hidden"
		});
	}

	return returnValue;
};