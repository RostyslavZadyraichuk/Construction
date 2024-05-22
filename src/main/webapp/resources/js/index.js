$(document).ready(function () {

    let parallaxGallery = new simpleParallax($("#main-page-wallpaper")[0], {
        scale: 1.4
    });

    let commentsCount = $(".review").length;
    if (commentsCount > 3) {
        let slideBy = commentsCount % 3 === 0 ? "page" : 1

        let testimonialsSlider = tns({
            container: '.testimonials-container',
            items: 3,
            slideBy: slideBy,
            autoplay: true,
            gutter: 30,
            controls: false,
            mouseDrag: true,
            speed: 500,
            autoplayTimeout: 10000,
            freezable: false,
            navPosition: "bottom"
        });
    } else {
        let justifyContentValue;
        if (commentsCount <= 1) {
            justifyContentValue = "center";
        } else if (commentsCount === 2) {
            justifyContentValue = "space-around";
        } else {
            justifyContentValue = "space-between";
        }

        $(".testimonials-container")
            .css("width", "68vw")
            .css("margin", "auto")
            .css("display", "flex")
            .css("justify-content", justifyContentValue);
        $(".review").css("width", "32%");
    }

    $("#building-picture").css("display", "block");

    $(".vertical-list li").on("click", function () {
        let selectedIndex = $(".vertical-list .selected").index();
        let thisIndex = $(this).index();

        if (thisIndex !== selectedIndex) {
            $(".vertical-list .selected").removeClass("selected");
            $(".opacity-slider .selected").removeClass("selected");

            $(this).addClass("selected");
            $(".opacity-slider .container").eq(thisIndex).addClass("selected");
        }
    })

    $(window).on("scroll", function () {
        let verticalScroll = $(window).scrollTop();

        if (verticalScroll >= 900) {
            $("#navigate-top-arrow").addClass("visible");
        } else {
            $("#navigate-top-arrow").removeClass("visible");
        }
    })

    $("#navigate-top-arrow").on("click", function () {
        if ($(this).hasClass("visible")) {
            $("html").animate({scrollTop: 0}, "slow");
        }
    })
})