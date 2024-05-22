$(document).ready(function () {
    const endTopScrollArea = window.innerHeight;
    const startBottomScrollArea = endTopScrollArea - endTopScrollArea * 0.05;

    let lastModifiedTask = null;

    let picturesGallery = tns({
        container: '#gallery-slider',
        items: 1,
        slideBy: 1,
        autoplay: false,
        controls: false,
        mouseDrag: true,
        speed: 500,
        freezable: false,
        navPosition: "bottom"
    });

    let schemesGallery = tns({
        container: '#schemes-slider',
        items: 1,
        slideBy: 1,
        autoplay: false,
        controls: false,
        mouseDrag: true,
        speed: 500,
        freezable: false,
        navPosition: "bottom"
    });

    $("#project-gallery .icon-arrow-left-simple").on("click", function () {
        picturesGallery.goTo("prev");
    });

    $("#project-gallery .icon-arrow-right-simple").on("click", function () {
        picturesGallery.goTo("next");
    });

    $("#schemes-container .icon-arrow-left-simple").on("click", function () {
        schemesGallery.goTo("prev");
    });

    $("#schemes-container .icon-arrow-right-simple").on("click", function () {
        schemesGallery.goTo("next");
    });

    $(".icon-cross-circle").on("click", function () {
        $("#project-gallery").animate({"opacity":"0"}, 100, "linear", function () {
            $(this).css("display", "none");
        })
    })

    $("#main-image").on("click", function () {
        $("#project-gallery").css("display", "flex")
            .animate({"opacity":"1"}, 100, "linear");
    })

    $(".project-details .selector div").on("click", function () {
        if (!$(this).hasClass("active")) {
            let index = $(this).index();
            let leftPos = (index * -100) + "%";

            $(".project-details .selector .active").removeClass("active");
            $(this).addClass("active");
            $(".container-2 > div").animate({"left": leftPos}, 300, "swing");
        }
    })

    $("#scale-buttons div").on("click", function () {
        if (!$(this).hasClass("active")) {
            let index = $(this).index();

            $("#scale-buttons .active").removeClass("active");
            $(this).addClass("active");
        }
    })

    $("#edit-active-task").on("click", function () {
        let tools = $(".tools");

        if (tools.hasClass("open")) {
            tools.removeClass("open");
        } else {
            tools.addClass("open");
        }
    })

    $("#visual-container .task .activator").on("dblclick", function () {
        let task = $(this).parent();

        if (lastModifiedTask !== task) {
            deactivateActiveInput();

            lastModifiedTask = task;
            $(this).css("display", "none");
            task.find("input").removeClass("inactive").prop("disabled", false);
        }
    })

    $(document).on("click", function () {
        if (lastModifiedTask != null && lastModifiedTask.find("input:hover").length === 0) {
            deactivateActiveInput();
        }
    })

    $(document).on("keyup", function (e) {
        if (e.key === "Escape" || e.key === "Enter") {
            deactivateActiveInput();
        }
    })

    $("#finish-active-task").on("click", function () {

    })

    function deactivateActiveInput() {
        if (lastModifiedTask != null) {
            lastModifiedTask.find(".activator").css("display", "block");
            lastModifiedTask.find("input").addClass("inactive").prop("disabled", true);
            lastModifiedTask = null;
        }
    }

})

function saveChanges() {

}

function changeTask() {

}