var wood = false;

/** @Called to setup the data for this script**/
function setup(settings) {
    settings.setType("team"); //Marks this as a team script, by default scripts are player specific, valid values are "player", "team" and "global"
    settings.setRepeat("always"); //Marks this script as always being repeatable
    /** Set a comma separated list of events, when these triggers are fired your canStart method will be called by default
        e.g. return "onEntityInteract";

        If for some reason for trigger purposes you wish to have a different function called simply put
        return "onEntityInteract:doSomething" instead. The function will be called on trigger firing but it will not be able to start the script
        **/
    settings.setTriggers("onEntityInteract:canStart");
}


/** @return true if the script can be started, if this script requires another one to be completed,
    you can use scripts.hasCompleted(scriptName) to check. If you need them to have completed it multiple times
    instead make use of scripts.hasCompleted(scriptName, 5) to check if they have completed that script 5 times **/
function canStart(player, scripts) {
    return true;
}

/** An optional method, if you want the script to remain hidden then simply do not add this function to your scripts**/
function display(information) {
    /**
    The name of this script to be displayed in the logger book,
        The name is queried before the book is opened so you can update it if you please
        It doesn't have to be a static value **/
    information.setName("Harvest Goddess");

    /** set an itemstack, use createStack to do this. It will be used in the logger book
        If you do not have this function then the script will simply have no icon**/
    information.setIcon(createStack("minecraft:book"));

    /** set a description of the current task for this script
        It should be dynamic if the script changes.
        If you do not set this then the logger book will display an error **/
    if (!question) {
        information.setDescription("Talk to the Harvest Goddess");
    } else information.setDescription("");
}

/** @return the task you want the npc to do this as this moment
    npc.say(player, "hello");
    npc.ask(player, "OK", "Are you ok?", "Yes", "No"); //Answer a question, QuestionID, Question, Options
    npc.sayTranslated(player, "harvestfestival.quest.goddess.chat1");
    npc.askTranslated(player, "OK", "harvestfestival.quest.goddess.ask1", "harvestfestival.quest.goddess.answer1", "harvestfestival.quest.goddess.answer2");
    npc.walk(40, 64, 32); //Walk to a block coordinate
    npc.wait(40); //Stands still
    npc.setHeldItem("main_hand", createStack("bow"));
    **/
function onRightClickedNPC(player, npc) {
    npc.walk(player.position().offset(5, 0, 5));
    //npc.say(player, script, "I've got a gift for you... So hey which weapon would you like?");
    //npc.ask(player, script, "weapon", "", "Bow!", "Sword!", "Axe", "Hammer");
}

function onQuestionAnswered(player, npc, questionid, selected) {
    if (questionid == "weapon") {
        if (selected == 0) player.giveItem(createStack("bow"));
        if (selected == 1) player.giveItem(createStack("diamond_sword"));
        if (selected == 2) player.giveItem(createStack("diamond_axe"));
        if (selected == 3) player.giveItem(createStack("harvestfestival:hammer_mystril"));
    }
}

function onEntityInteract(player, target, hand) {
    wood = true;
}

function saveData(data) {
    data.save("wood", wood);
}

function loadData(data) {
    wood = data.load("wood", wood);
}