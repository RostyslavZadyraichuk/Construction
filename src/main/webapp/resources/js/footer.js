$(document).ready(function () {
    const firstNameRegex = new RegExp("^[a-zA-Z ]+$");
    const emailRegex = new RegExp("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
    const phoneNumberRegex = new RegExp("380(50|67|93|66|63|97)[0-9]{7}");

    const firstNameInput = $("#first-name");
    const emailInput = $("#email");
    const phoneNumberInput = $("#phone-number");

    firstNameInput.keyup(function () {
        let input = $(this).val();
        if (input === "") {
            $(this).removeClass("invalid").removeClass("valid");
        } else if (firstNameRegex.test(input)) {
            $(this).removeClass("invalid").addClass("valid");
        } else {
            $(this).removeClass("valid").addClass("invalid");
        }
        checkFormIsDone();
    })

    emailInput.keyup(function () {
        let input = $(this).val();
        if (input === "") {
            $(this).removeClass("invalid").removeClass("valid");
        } else if (emailRegex.test(input)) {
            $(this).removeClass("invalid").addClass("valid");
        } else {
            $(this).removeClass("valid").addClass("invalid");
        }
        checkFormIsDone();
    })

    phoneNumberInput.inputmask("+38 (099) 999-99-99")
        .keyup(function () {
        let input = $(this).val();
        input = phoneMaskToString(input);
        if (input === "") {
            $(this).removeClass("invalid").removeClass("valid");
        } else if (phoneNumberRegex.test(input)) {
            $(this).removeClass("invalid").addClass("valid");
        } else {
            $(this).removeClass("valid").addClass("invalid");
        }
            checkFormIsDone();
    })

    function phoneMaskToString(phoneMask) {
        return phoneMask.replaceAll(/[\s_()+-]+/g, "")
    }

    function checkFormIsDone() {
        let nameIsDone = firstNameInput.hasClass("valid");
        let emailIsDone = emailInput.hasClass("valid");
        let phoneIsDone = phoneNumberInput.hasClass("valid");

        if (nameIsDone && emailIsDone && phoneIsDone) {
            $("footer button").removeClass("inactive").prop("disabled", false);
        } else {
            $("footer button").addClass("inactive").prop("disabled", true);
        }
    }
})