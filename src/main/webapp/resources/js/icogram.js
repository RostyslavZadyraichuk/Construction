$(document).ready(function () {

    const floorsCount = 5;

    loadFront(5, 1);
    loadBack(5, 1);
    buildCran(floorsCount, 0);
    buildBuilding(floorsCount);
    buildLadder(floorsCount);

})

function loadFront(stage, dynamicStage) {
    let fileName = "s" + stage;
    if (dynamicStage != null) {
        fileName += "_" + dynamicStage;
    }
    let frontType = stage === "11" ? "front-finish" : "front-element";
    let front = '<img class="icogram-element ' + frontType + '" src="../img/icogram/static/front/' + fileName + '.svg">';

    $("#front").append(front);
}

function loadBack(stage, dynamicStage) {
    let fileName = "s" + stage;
    if (dynamicStage != null) {
        fileName += "_" + dynamicStage;
    }
    let back = '<img class="icogram-element back-element" src="../img/icogram/static/back/' + fileName + '.svg">';

    $("#back").append(back);
}

function buildLadder(floorsCount) {
    let ladder = $("#ladder");

    let ladderBase = '<img class="icogram-element ladder-base" src="../img/icogram/dynamic/ladder_base.svg">';
    ladder.append(ladderBase);

    //define when +1 additional ladder cause roof
    for (let i = 0; i < floorsCount; i++) {
        let ladderFloor = '<img class="icogram-element ladder-floor f' + i + '" src="../img/icogram/dynamic/ladder_floor.svg">';
        ladder.append(ladderFloor);
    }
}

function buildBuilding(floorsCount) {
    let base = '<img class="icogram-element base" src="../img/icogram/dynamic/base_full.svg">';
    $("#building-base").append(base);

    let roof = '<img class="icogram-element roof-paint f' + floorsCount + '" src="../img/icogram/dynamic/roof_full.svg">';
    $("#building-roof").append(roof);

    for (let i = 0; i < floorsCount; i++) {
        let floor = '<img class="icogram-element floor-paint f' + i + '" src="../img/icogram/dynamic/floor_full.svg">';
        $("#building-floors").append(floor);
    }
}

function buildCran(floorsCount, floorInActive) {
    let cranPartsCount = floorsCount < 6 ? floorsCount + 1 : floorsCount;
    let cranFront = $("#cran-front");
    let cranBack = $("#cran-back");

    let cranTop = '<img class="icogram-element cran-top f' + floorsCount + '" src="../img/icogram/dynamic/cran_top.svg"/>'
    cranFront.append(cranTop);

    $(".cran-top").addClass("f" + cranPartsCount);
    let cranHook = '<img class="icogram-element hook f' + floorInActive + '" src="../img/icogram/dynamic/hook.svg">';
    cranFront.append(cranHook);

    let cranBottom = '<img class="icogram-element cran-base" src="../img/icogram/dynamic/cran_bottom.svg">'
    cranBack.append(cranBottom);

    for (let i = 0; i < cranPartsCount; i++) {
        let cranPartTag = '<img class="icogram-element cran-part f' + i + '" src="../img/icogram/dynamic/cran_part.svg">';
        cranBack.append(cranPartTag);
    }
}

//TODO recreate icogram with overlapping logic of elements
//TODO reformat icogram colors from 100+ colors to palette
//TODO unite all icogram parts' colors to one css file