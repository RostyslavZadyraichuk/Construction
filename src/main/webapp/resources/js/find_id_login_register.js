$(document).ready(function () {

    const emailRegex = new RegExp("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
    const phoneNumberRegex = new RegExp("380(50|67|93|66|63|97)[0-9]{7}");

    const emailInputs = $(".email-input");
    const phoneInput = $("#register-phone");
    const passwordInput = $("#register-password");
    const passwordConfirmInput = $("#register-password-confirm");
    const usernameInput = $("#register-username");

    emailInputs.keyup(function () {
        if ($(this).val() === "") {
            $(this).removeClass("invalid").removeClass("valid");
        } else if (emailRegex.test($(this).val())) {
            $(this).removeClass("invalid").addClass("valid");
        } else {
            $(this).removeClass("valid").addClass("invalid");
        }

        checkFormIsDone();
    })

    phoneInput.inputmask("+38 (099) 999-99-99").keyup(function () {
        let value = phoneMaskToString($(this).val());
        if (value === "") {
            $(this).removeClass("invalid").removeClass("valid");
        } else if (phoneNumberRegex.test(value)) {
            $(this).removeClass("invalid").addClass("valid");
        } else {
            $(this).removeClass("valid").addClass("invalid");
        }

        checkFormIsDone();
    })

    function phoneMaskToString(phoneMask) {
        return phoneMask.replaceAll(/[\s_()+-]+/g, "")
    }

    passwordConfirmInput.keyup(function () {
        let password = passwordInput.val();
        let confirm = passwordConfirmInput.val();

        if (confirm === "") {
            $(this).removeClass("invalid").removeClass("valid");
        } else if (confirm === password) {
            $(this).removeClass("invalid").addClass("valid");
        } else {
            $(this).addClass("invalid").removeClass("valid");
        }

        checkFormIsDone();
    })

    usernameInput.keyup(function () {
        //todo check username is already in DB
        let username = usernameInput.val();

        if (username === "") {
            $(this).removeClass("valid").removeClass("invalid")
        } else {
            $(this).addClass("valid").removeClass("invalid")
        }

        checkFormIsDone();
    })

    function checkFormIsDone() {
        let usernameIsDone = usernameInput.hasClass("valid");
        let userEmailIsDone = $("#register-email").hasClass("valid");
        let companyEmailIsDone = $("#register-company-email").hasClass("valid");
        let phoneIsDone = phoneInput.hasClass("valid");
        let passwordIsDone = passwordConfirmInput.hasClass("valid");

        if (userEmailIsDone && usernameIsDone && passwordIsDone) {
            $("#register-user").removeClass("inactive").prop("disabled", false);
            $("#register-user-bankid").removeClass("inactive").prop("disabled", false);

            if (companyEmailIsDone && phoneIsDone) {
                $("#register-company").removeClass("inactive").prop("disabled", false);
            } else {
                $("#register-company").addClass("inactive").prop("disabled", true);
            }
        } else {
            $("#register-user").addClass("inactive").prop("disabled", true);
            $("#register-user-bankid").addClass("inactive").prop("disabled", true);
            $("#register-company").addClass("inactive").prop("disabled", true);
        }
    }


    let parallaxGallery = new simpleParallax($("#background-image")[0], {
        scale: 1
    });

    $("#switcher-down").on("click", function () {
        $(this).animate({"opacity": "0"}, 250, "linear", function () {
            $(this).css("display", "none");
            $("#switcher-up").css("display", "flex")
                .animate({"opacity": "1"}, 250, "linear");
        })
        $("#forms-container").animate({"top": "-100%"}, 500);
    })

    $("#switcher-up").on("click", function () {
        $(this).animate({"opacity": "0"}, 250, "linear", function () {
            $(this).css("display", "none");
            $("#switcher-down").css("display", "flex")
                .animate({"opacity": "1"}, 250, "linear");
        })
        $("#forms-container").animate({"top": "0"}, 500);
    })

    $("#switcher-login").on("click", function () {
        if (!$(this).hasClass("active")) {
            $(".forms").animate({"left": "0"}, 200, "linear");

            $(".empty-left").addClass("active");
            $(".login").addClass("active")
                .find(".icon").removeClass("icon-arrow-left-triangle");
            $(".register").removeClass("active")
                .find(".icon").addClass("icon-arrow-right-triangle");
            $(".empty-right").removeClass("active");
        }
    })

    $("#switcher-register").on("click", function () {
        if (!$(this).hasClass("active")) {
            $(".forms").animate({"left": "-100%"}, 200, "linear");

            $(".empty-left").removeClass("active");
            $(".login").removeClass("active")
                .find(".icon").addClass("icon-arrow-left-triangle");
            $(".register").addClass("active")
                .find(".icon").removeClass("icon-arrow-right-triangle");
            $(".empty-right").addClass("active");
        }
    })

    $("#contractor-radio").on("click", function () {
        $(".general-constructor-radio").addClass("active");
        $(".company-owner-radio").removeClass("active");

        $("#user-fields").animate({"width": "100%"}, 300, "linear")
            .children().animate({"width": "70%"}, 300, "linear");
        $("#company-fields").animate({"width": "0"}, 300, "linear");
        $("#register-buttons").animate({"left": "0"}, 300, "linear");
    })

    $("#company-radio").on("click", function () {
        $(".general-constructor-radio").removeClass("active");
        $(".company-owner-radio").addClass("active");

        $("#user-fields").animate({"width": "37%"}, 300, "linear")
            .children().animate({"width": "100%"}, 300, "linear");
        $("#company-fields").animate({"width": "60%"}, 300, "linear");
        $("#register-buttons").animate({"left": "-100%"}, 300, "linear");
    })

})

function findProject() {

}

function login() {

}

function registerContractor(useBankId) {

}

function registerCompany(validated) {

}

function loginTrial() {

}