$(document).ready(function () {

    const floorsCount = 5;
    //stage -1 - hidden
    //stage 0 - default
    //stage-1 - elem #s1 -> плита
    //stage-2 - bricks
    //stage-3 - windows
    //stage-4 - isolation
    //stage-5 - paint
    const floorsStages = [5, 4, 3, 2, 1, 0, -1]

    //from 0 to floors + 1
    const activeFloorForHook = floorsStages.findIndex(e => e === 0);
    const activeFloorForLadder = floorsStages.findIndex(e => e === 4);

    //stage 1 - territory landing and cleaning
    //stage 2 - measurements and dirt out
    //stage 3 - digging
    //stage 4 - underground building
    //stage 5
    //stage 6
    //stage 7
    //stage 8
    //stage 9
    //stage 10
    //stage 11
    //dynamic stage 1 - for basement
    //dynamic stage 2 - for floors and roof
    loadFront(5, 2);
    loadBack(5, 2);
    buildCran(floorsCount, activeFloorForHook);
    buildBuilding(floorsCount, floorsStages);

    //stage 0 -default
    //stage 1 - isolation
    //stage 2 - painting
    // buildLadder(floorsCount, activeFloorForLadder);

})

//TODO create enum for stages
function setBuildingStage(element, stage) {
    const minStage = 1;
    const maxStage = 5;

    if (stage === -1) {
        element.css("opacity", "0");
    }

    for (let i = minStage; i <= maxStage; i++) {
        let stageElements = $(element).find(".stage-" + i);

        if (i <= stage) {
            //it's over isolation stage
            if (i === 4 && stage > 4) {
                stageElements.css("opacity", "0");
                continue;
            }

            stageElements.css("opacity", "1");
        } else {
            stageElements.css("opacity", "0");
        }
    }
}

function setLadderStage(element, stage) {
    const minStage = 1;
    const maxStage = 2;

    if (stage === -1) {
        element.css("opacity", "0");
    }

    for (let i = minStage; i <= maxStage; i++) {
        let stageElements = $(element).find(".stage-" + i);

        if (i === stage) {
            stageElements.css("opacity", "1");
        } else {
            stageElements.css("opacity", "0");
        }
    }
}

function loadFront(stage, dynamicStage) {
    let fileName = "s" + stage;
    if (dynamicStage != null) {
        fileName += "_" + dynamicStage;
    }
    let frontType = stage === 11 ? "front-finish" : "front-element";

    $.get("../img/icogram/static/front/" + fileName + ".svg", function (svg) {
        let front = $(svg)
            .addClass("icogram-element")
            .addClass(frontType)
            .addClass(fileName);
        $("#front").append(front);
    }, "html");

}

function loadBack(stage, dynamicStage) {
    let fileName = "s" + stage;
    if (dynamicStage != null) {
        fileName += "_" + dynamicStage;
    }

    $.get("../img/icogram/static/back/" + fileName + ".svg", function (svg) {
        let back = $(svg)
            .addClass("icogram-element")
            .addClass("back-element")
            .addClass(fileName);
        $("#back").append(back);
    }, "html");

}

function buildLadder(floorsCount, isolationFloor) {
    $.get("../img/icogram/dynamic/ladder_base.svg", function (svg) {
        let ladder = $(svg)
            .addClass("icogram-element")
            .addClass("ladder-base");
        $("#ladder").append(ladder);

        if (isolationFloor === -1) {
            setLadderStage(ladder, -1);
        } else if (isolationFloor > 2) {
            setLadderStage(ladder, 0);
        } else {
            setLadderStage(ladder, isolationFloor + 1);
        }
    }, "html");

    $.get("../img/icogram/dynamic/ladder_floor.svg", function (svg) {
        let ladderTemplate = $(svg)
            .addClass("icogram-element")
            .addClass("ladder-floor");

        for (let i = 1; i <= floorsCount; i++) {
            let ladder = ladderTemplate.clone()
                .addClass("f" + i);
            $("#ladder").append(ladder);

            if (i > isolationFloor) {
                setLadderStage(ladder, -1);
            } else if (i < isolationFloor - 1) {
                setLadderStage(ladder, 0);
            } else {
                setLadderStage(ladder, isolationFloor - i + 1);
            }
        }
    }, "html");
}

//floors stages = [base-stage,...floors-stages...,roof-stage]
function buildBuilding(floorsCount, floorsStages) {
    let baseStage = floorsStages[0];
    let roofStage = floorsStages[floorsStages.length - 1];
    let floorStages = floorsStages.slice(1, floorsStages.length - 1)

    $.get("../img/icogram/dynamic/base_full.svg", function (svg) {
        let base = $(svg)
            .addClass("icogram-element")
            .addClass("base");

        $("#building-base").append(base);
        setBuildingStage(base, baseStage);
    }, "html");

    $.get("../img/icogram/dynamic/roof_full.svg", function (svg) {
        let roof = $(svg)
            .addClass("icogram-element")
            .addClass("roof")
            .addClass("f" + floorsCount);

        $("#building-roof").append(roof);
        setBuildingStage(roof, roofStage);
    }, "html");

    $.get("../img/icogram/dynamic/floor_full.svg", function (svg) {
        let floorTemplate = $(svg)
            .addClass("icogram-element")
            .addClass("floor");

        for (let i = 1; i <= floorsCount; i++) {
            let floor = floorTemplate.clone()
                .addClass("f" + i);
            $("#building-floors").append(floor);
            setBuildingStage(floor, floorStages[i - 1]);
        }
    }, "html");
}

function buildCran(floorsCount, floorInActive) {
    let cranPartsCount = floorsCount < 6 ? floorsCount + 1 : floorsCount;
    let cranFront = $("#cran-front");
    let cranBack = $("#cran-back");

    $.get("../img/icogram/dynamic/cran_top.svg", function (svg) {
        let cran = $(svg)
            .addClass("icogram-element")
            .addClass("cran-top")
            // .addClass("f" + floorsCount);
            .addClass("f" + cranPartsCount);

        cranFront.append(cran);
    }, "html");

    $.get("../img/icogram/dynamic/hook.svg", function (svg) {
        let hook = $(svg)
            .addClass("icogram-element")
            .addClass("hook");
        let maxHeight = $("#cran-back").height();

        cranFront.append(hook);
        if (floorInActive === -1) {
            hook.find("#hook-full").css("opacity", "0");
            hook.addClass("f" + (floorsCount + 1))
                .addClass("last");
        } else {
            hook.addClass("f" + floorInActive);

            if (floorInActive === (floorsCount + 1)) {
                hook.addClass("last");
            } else {
                let leftTrosOffset = (floorsCount - floorInActive) * (-13);
                let rightTrosOffset = leftTrosOffset - 1;

                let leftTrosPoints = $("#left_tros").attr("points").split(" ");
                leftTrosPoints[3] = leftTrosOffset;
                leftTrosPoints[5] = leftTrosOffset;
                $("#left_tros").attr("points", leftTrosPoints.join(" "));

                let rightTrosPoints = $("#right_tros").attr("points").split(" ");
                rightTrosPoints[3] = rightTrosOffset;
                rightTrosPoints[5] = rightTrosOffset;
                $("#right_tros").attr("points", rightTrosPoints.join(" "));
            }
        }

        let viewBoxValues = hook.attr("viewBox").split(" ");
        viewBoxValues[1] = (parseFloat(viewBoxValues[1]) - maxHeight).toString();
        viewBoxValues[3] = (parseFloat(viewBoxValues[3]) + maxHeight).toString();
        hook.attr("viewBox", viewBoxValues.join(" "));
    }, "html");

    $.get("../img/icogram/dynamic/cran_bottom.svg", function (svg) {
        let base = $(svg)
            .addClass("icogram-element")
            .addClass("cran-base");

        cranBack.append(base);
    }, "html");

    $.get("../img/icogram/dynamic/cran_part.svg", function (svg) {
        let partTemplate = $(svg)
            .addClass("icogram-element")
            .addClass("cran-part");

        for (let i = 0; i < cranPartsCount; i++) {
            let part = partTemplate.clone()
                .addClass("f" + i);
            cranBack.append(part);
        }
    }, "html");
}

//TODO recreate icogram with overlapping logic of elements
//TODO reformat icogram colors from 100+ colors to palette
//TODO unite all icogram parts' colors to one css file