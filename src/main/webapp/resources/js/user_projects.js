$(document).ready(function () {

    $(".views-select div").on("click", function () {
        if (!$(this).hasClass("active")) {
            $(".views-select .active").removeClass("active");
            $(this).addClass("active");
        }
    })

    $(".sort-select .active").on("click", function () {
        let container = $(".sort-select");

        if (container.hasClass("open")) {
            container.removeClass("open")
                .find(".icon")
                .removeClass("icon-arrow-up-triangle")
                .addClass("icon-arrow-down-triangle");
        } else {
            container.addClass("open")
                .find(".icon")
                .removeClass("icon-arrow-down-triangle")
                .addClass("icon-arrow-up-triangle");
        }
    })

    $(".sort-select .variant").on("click", function () {
        let newText = $(this).text();

        $(".sort-select .active").click().find(".active-text").text(newText);
    })

})