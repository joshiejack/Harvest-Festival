function setup(settings) {
    settings.setType("player");
    settings.setDefault();
}

function onRightClickedNPC(player, npc) { //TODO: Move to jeimmi quests
    if (npc.is("jeimmi") && npc.town().population() >= 24) {
        npc.say(player, "You have done a wonderful job of building up this town blah blah");
        npc.give(player, createStack("harvestfestival:power_berry"));
        quest.complete(player);
    }
}