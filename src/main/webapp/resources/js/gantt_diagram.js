let scrollElem;
let dragGridModeOn = true;
let selectedScale = "year";
let showWeekends = false;

$(document).ready(function () {
    $.getScript("../external/dhtmlxgantt.js", function () {

        //start from Sunday
        const workingDays = [false, true, true, true, true, true, false];

        configureGantt(workingDays);
        addCustomEventListeners();
        addGanttEventListeners();

        gantt.init("timeline");
        gantt.load("../json/default_diagram.json", "json", function () {
            changeProjectDateRange();
            gantt.render();
        });

        /*
            // gantt.attachEvent("onGanttRender", function () {
            //     gantt.config.start_date = new Date(2020, 1, 1);
            //     gantt.config.end_date = end;
            //     console.log(start);
            //     console.log(end);
            //     gantt.render();
            // });

            //export
            // gantt.exportToJSON({
            //     name: "gantt.json",
            //     data: gantt.serialize(),
            //     config: "",
            //     columns: "",
            //     worktime: ""
            // })

            // let data = json
            // gantt.parse(data);
            // gantt.render()
            // gantt.refreshData()
        */
    })
})

function configureGantt(workingDays) {
    gantt.plugins({
        export_api: true,
        marker: true,
        drag_timeline: true
    });

    // gantt.config.readonly = true;
    gantt.config.date_format = "%Y/%m/%d";
    gantt.config.duration_unit = "day";
    gantt.config.drag_progress = false;
    gantt.config.drag_links = false;
    gantt.config.details_on_dblclick = false;
    gantt.config.row_height = 30;
    gantt.config.multiselect_one_level = false;
    gantt.config.select_task = false;
    gantt.config.autoscroll = false;
    gantt.config.round_dnd_dates = false;
    gantt.config.correct_work_time = true;
    gantt.config.time_step = 60 * 24;
    gantt.config.work_time = true;
    gantt.i18n.setLocale("ua");

    gantt.config.drag_timeline = {
        useKey: false
    };

    gantt.templates.progress_text = function(start, end, task){
        return Math.round(task.progress * 10000) / 100 + " %";
    };

    gantt.config.editor_types.date.is_changed = function (value, id, column, node) {
        let currentValue = this.get_value(id, column, node);
        currentValue = getNextWorkDay(currentValue, workingDays);

        if (currentValue && value && currentValue.valueOf && value.valueOf) {
            return currentValue.valueOf() != value.valueOf();
        } else {
            return currentValue != value;
        }
    }

    let textEditor = {type: "text", map_to: "text"};
    let dateEditor = {type: "date", map_to: "start_date", min: function (taskId) {
            console.log(taskId)
            if (Number(taskId) === gantt.getChildren(1)[0]) {
                return gantt.config.start_date;
            }
            return gantt.getTask(taskId).$target
                .map(linkId => gantt.getLink(linkId))
                .map(link => gantt.getTask(link.source).end_date)
                .reduce(function (a, b) { return a > b ? a : b; })
        }};
    let durationEditor = {type: "number", map_to: "duration", min:1, max: 100};

    gantt.config.columns = [
        {name: "text", tree: true, width: 400, resize: true, editor: textEditor, label: "Назва"},
        // {name: "text", tree: true, width: 200, resize: true, editor: textEditor},
        {name: "start_date", align: "center", resize: true, editor: dateEditor, label: "Початок"},
        {name: "duration", align: "center", editor: durationEditor, label: "Тривалість"}
    ];

    gantt.config.layout = {
        css: "gantt_container",
        cols: [
            {
                width:400,
                minWidth: 200,
                maxWidth: 600,
                rows:[
                    {view: "grid", scrollX: "gridScroll", scrollable: true, scrollY: "scrollVer"},
                    {view: "scrollbar", id: "gridScroll", group:"horizontal"}
                ]
            },
            {resizer: true, width: 1},
            {
                rows:[
                    {view: "timeline", scrollX: "scrollHor", scrollY: "scrollVer"},
                    {view: "scrollbar", id: "scrollHor", group:"horizontal"}
                ]
            },
            {view: "scrollbar", id: "scrollVer"}
        ]
    };

    let markerId = gantt.addMarker({
        start_date: new Date(),
        css: "today",
        text: "Сьогодні"
    });

    for (let i = 0; i < workingDays.length; i++) {
        gantt.setWorkTime({day:i, hours: workingDays[i]})
    }

    gantt.templates.timeline_cell_class = function(task, date){
        return setWeekends(task, date);
    };

    setInterval(function(){
        let today = gantt.getMarker(markerId);
        today.start_date = new Date();
        today.text = "Сьогодні";
        gantt.updateMarker(markerId);
    }, 1000*60);
    setCustomEditMapping();
    setScaleConfig(selectedScale);
}

function addCustomEventListeners() {
    $(".gantt_data_area").mousedown(function (e) {
        e.preventDefault();
        $(this).addClass("mouseDrag");
    }).mouseup(function (e) {
        e.preventDefault();
        $(this).removeClass("mouseDrag");
    });

    $("#scale-buttons button").on("click",function(e){
        if (!$(this).hasClass("selected")) {
            let selectedScale = $(this).attr("value");
            setScaleConfig(selectedScale);
            gantt.render();
            gantt.showTask(getCurrentActiveTask());

            $(this).parent().find(".selected").removeClass("selected");
            $(this).addClass("selected");
        }
    });
}

function addGanttEventListeners() {
    gantt.ext.inlineEditors.attachEvent("onEditEnd", function (state) {
        let task = gantt.getTask(state.id);
        gantt.showTask(task.id);
        if (state.columnName === "duration" || state.columnName === "start_date") {
            moveNextTasks(task);
        }
    })

    gantt.ext.inlineEditors.attachEvent("onBeforeEditStart", function(state){
        let task = gantt.getTask(state.id);
        gantt.showTask(task.id);
        return !(task.$no_start === true && state.columnName === "start_date");
    });

    gantt.attachEvent("onGanttRender", function () {
        scrollElem = $(".gantt_grid_data").parents(".gantt_layout_y").find(".gantt_hor_scroll");
        loadIcons();
        setGridDragEvent();
    })

    gantt.attachEvent("onTaskDrag", function(id, mode, task){
        if (mode === "resize") {
            resizeTask(task);
        } else if (mode === "move") {
            moveTask(task, true);
        }
    });

    gantt.attachEvent("onAfterTaskDrag", function(id, mode, e){
        if ((mode === "resize" && !gantt.getState().drag_from_start) || mode === "move") {
            moveNextTasks(gantt.getTask(id));
        }
    });

    gantt.attachEvent("onAfterTaskUpdate", function (id, task) {
        changeProjectDateRange();
        gantt.render();
    });

    gantt.attachEvent("onTaskOpened", function (id) {
        $(".gantt_grid_editor_placeholder").remove();
    })

    gantt.attachEvent("onTaskClosed", function (id) {
        $(".gantt_grid_editor_placeholder").remove();
    })
}

function setScaleConfig(scale) {
    switch (scale) {
        case "week":
            let weekScaleTemplate = function (date) {
                let dateToStr = gantt.date.date_to_str("%d %M");
                let endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
                return dateToStr(date) + " - " + dateToStr(endDate);
            };
            gantt.config.scales = [
                {unit: "week", step: 1, format: weekScaleTemplate},
                {unit: "day", step: 1, format: "%D"}
            ];
            gantt.config.scale_height = 50;
            showWeekends = true;
            break;
        case "month":
            gantt.config.scales = [
                {unit: "month", step: 1, format: "%M, %Y"},
                {unit: "day", step: 7, format: "%j, %D"}
            ];
            gantt.config.scale_height = 50;
            showWeekends = false;
            break;
        case "year":
            gantt.config.scales = [
                {unit: "year", step: 1, format: "%Y"},
                {unit: "month", step: 1, format: "%M"}
            ];
            gantt.config.scale_height = 90;
            showWeekends = false;
            break;
        default:
            console.log("Wrong timeline scale")
    }
}

function setGridDragEvent() {
    let mouseXStart;
    let gridScrollPos;

    $(".gantt_grid_data").mousedown(function (e) {
        if (dragGridModeOn) {
            e.preventDefault();
            $(this).addClass("mouseDrag");
            gridScrollPos = scrollElem.scrollLeft();
            mouseXStart = e.pageX;
        }
    }).mousemove(function (e) {
        if (dragGridModeOn) {
            e.preventDefault();
            if ($(this).hasClass("mouseDrag")) {
                let mouseXEnd = e.pageX;
                scrollElem.scrollLeft(gridScrollPos + mouseXStart - mouseXEnd);
            }
        }
    }).mouseup(function (e) {
        if (dragGridModeOn) {
            e.preventDefault();
            $(this).removeClass("mouseDrag");
        }
    });
}

function changeProjectDateRange() {
    let mainTask = gantt.getTask(1);
    let start = new Date(mainTask.start_date.getTime());
    start.setDate(start.getDate() - 30);
    let end = new Date(mainTask.end_date.getTime());
    end.setDate(end.getDate() + 30);
    gantt.config.start_date = start;
    gantt.config.end_date = end;
}

function setWeekends(task, date) {
    if (showWeekends && !gantt.isWorkTime({task:task, date:date})) {
        return "weekend";
    }
    return "";
}

function dateDiff(first, second) {
    return Math.round((first.getTime() - second.getTime()) / (1000 * 60 * 60 * 24));
}

function getNextWorkDay(date, workingDays) {
    for (let i = 0; i < 7; i++) {
        if (workingDays[date.getDay()]) {
            return date;
        }
        date.setDate(date.getDate() + 1);
    }
}

function getCurrentActiveTask() {
    let minDate = gantt.getTask(1).start_date;
    let date = new Date();

    if (date <= minDate) {
        return 1;
    }

    while (date > minDate) {
        let tasks = gantt.getTaskByTime(date, date)
            .filter(task => task.$no_start === false);
        if (tasks.length !== 0) {
            return tasks[0].id;
        }
        date.setDate(date.getDate() - 1);
    }

    return 1;
}

function loadIcons() {
    let interval = setInterval(function () {
        let elements = $('.gantt_cell');
        if (elements.length !== 0) {
            clearInterval(interval);
            $(".gantt_open").addClass("icon-plus");
            $(".gantt_close").addClass("icon-minus");
        }
    }, 1);
}

function resizeTask(task) {
    if (gantt.getState().drag_from_start) {
        changeTaskStartDate(task, task.start_date, true);
    } else {
        changeTaskEndDate(task, task.end_date);
    }
    gantt.render();
}

function moveTask(task, singleMove, newStartDate, newEndDate) {
    let newStart = newStartDate;
    let newEnd = newEndDate;
    if (newStart === undefined) {
        newStart = task.start_date;
    }
    if (newEnd === undefined) {
        newEnd = task.end_date;
    }

    changeTaskStartDate(task, newStart, singleMove);
    changeTaskEndDate(task, newEnd);
    gantt.render();
}

function changeTaskStartDate(task, newStartDate, singleMove) {
    let minimalStart = newStartDate;

    if (singleMove) {
        let linksOfPrevious = gantt.getLinks().filter(link => link.target === task.id);
        if (linksOfPrevious.length !== 0) {
            minimalStart = linksOfPrevious.reduce((max, p) =>
                gantt.getTask(p.source).end_date > max ? gantt.getTask(p.source).end_date : max, gantt.getTask(linksOfPrevious[0].source).end_date);
        }
    }

    if (newStartDate >= minimalStart) {
        task.start_date = newStartDate;
    } else {
        task.start_date = minimalStart;
    }
}

function changeTaskEndDate(task, newEndDate) {
    let minimalEnd = new Date(task.start_date.getTime());
    minimalEnd.setDate(minimalEnd.getDate() + task.duration);

    if (newEndDate > minimalEnd) {
        task.end_date = newEndDate;
    } else {
        task.end_date = minimalEnd;
    }
}

function moveNextTasks(task, previousTasks, alreadyMoved) {

    if (alreadyMoved === undefined) {
        alreadyMoved = []
    }

    let offset = 0;
    if (previousTasks !== undefined) {
        let maxPrevEndDate = previousTasks.reduce((max, p) =>
            p.end_date > max ? p.end_date : max, previousTasks[0].end_date);
        offset = dateDiff(maxPrevEndDate, task.start_date);
    }
    task.start_date.setDate(task.start_date.getDate() + offset);
    task.end_date.setDate(task.end_date.getDate() + offset);
    if (!alreadyMoved.includes(task.id)) {
        moveTask(task, false, task.start_date, task.end_date);
        alreadyMoved.push(task.id);
    }

    let linksOfNext = gantt.getLinks().filter(link => link.source === task.id);
    let nextTasks = linksOfNext.map(link => gantt.getTask(link.target));
    for (let i = 0; i < nextTasks.length; i++) {
        let dependentLinks = gantt.getLinks().filter(link => link.target === nextTasks[i].id);
        let previousTasks = dependentLinks.map(link => gantt.getTask(link.source));
        moveNextTasks(nextTasks[i], previousTasks, alreadyMoved);
    }
}

function setCustomEditMapping() {
    let lastScrollPos;
    let selectedTaskId;

    let mapping = {
        init: function init(controller, grid) {
            let gantt = grid.$gantt;

            gantt.attachEvent("onTaskDblClick", function (id, e) {
                lastScrollPos = scrollElem.scrollLeft();
                selectedTaskId = id;
                dragGridModeOn = false;
                let selectedTask = gantt.getTask(selectedTaskId);
                gantt.showTask(id);

                if ($(e.target).hasClass("gantt_task_content") && selectedTask.$no_start) {
                    if (selectedTask.$open) {
                        gantt.close(selectedTaskId);
                    } else {
                        gantt.open(selectedTaskId);
                    }
                }

                let state = controller.getState();
                let cell = controller.locateCell(e.target);

                if (cell && controller.getEditorConfig(cell.columnName)) {
                    if (controller.isVisible() && state.id == cell.id && state.columnName == cell.columnName) {// do nothing if editor is already active in this cell
                    } else {
                        controller.startEdit(cell.id, cell.columnName);
                    }
                    return false;
                }
                return true;
            });
            gantt.attachEvent("onEmptyClick", function () {
                if (controller.isVisible() && controller.isChanged()) {
                    controller.save();
                } else {
                    controller.hide();
                }
            });
            gantt.attachEvent("onTaskClick", function (id, e) {
                if (gantt._is_icon_open_click(e)) {
                    if (gantt.getTask(id).$open) {
                        gantt.close(id);
                    } else {
                        gantt.open(id);
                    }
                }

                if (id !== selectedTaskId && controller.isVisible()) {
                        if (controller.isChanged()) {
                            controller.save();
                        }
                        controller.hide();
                }
            });
        },
        onShow: function onShow(controller, placeholder, grid) {
            let gantt = grid.$gantt;

            if (gantt.ext && gantt.ext.keyboardNavigation) {
                let keyNav = gantt.ext.keyboardNavigation;
                keyNav.attachEvent("onKeyDown", function (command, e) {
                    let keyboard = gantt.constants.KEY_CODES;
                    let keyCode = e.keyCode;
                    let preventKeyNav = false;

                    switch (keyCode) {
                        case keyboard.SPACE:
                            if (controller.isVisible()) {
                                preventKeyNav = true;
                            }

                            break;
                    }

                    return !preventKeyNav;
                });
            }

            placeholder.onkeydown = function (e) {
                e = e || window.event;
                let keyboard = gantt.constants.KEY_CODES;

                if (e.defaultPrevented || e.shiftKey && e.keyCode != keyboard.TAB) {
                    return;
                }

                let shouldPrevent = true;

                switch (e.keyCode) {
                    case gantt.keys.edit_save:
                        controller.save();
                        break;

                    case gantt.keys.edit_cancel:
                        controller.hide();
                        break;

                    case keyboard.UP:
                    case keyboard.DOWN:
                        if (controller.isVisible()) {
                            controller.hide();
                            shouldPrevent = false;
                        }

                        break;

                    case keyboard.TAB:
                        if (e.shiftKey) {
                            controller.editPrevCell(true);
                        } else {
                            controller.editNextCell(true);
                        }

                        break;

                    default:
                        shouldPrevent = false;
                        break;
                }

                if (shouldPrevent) {
                    e.preventDefault();
                }
            };
        },
        onHide: function onHide() {
            dragGridModeOn = true;
            scrollElem.scrollLeft(lastScrollPos + 1);
            scrollElem.scrollLeft(lastScrollPos - 1);
            let actualTaskId = getCurrentActiveTask();
            gantt.showTask(actualTaskId);
        },
        destroy: function destroy() {}
    };

    gantt.ext.inlineEditors.setMapping(mapping);
}