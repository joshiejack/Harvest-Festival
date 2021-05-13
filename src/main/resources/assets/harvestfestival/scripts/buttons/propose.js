function setupButton(npc, player, button) {
    button.setIcon(createStack("harvestfestival:blue_feather"));
    button.setName("Will you marry me?");
}

function canDisplay(npc, player) { //Only if the npc is a marriage candidate and the player isn't married
    return npc.status().get(player, "marriable") == 1 && player.getHeldItem(main_hand).is("harvestfestival:blue_feather");
}

function onButtonPressed(npc, player) {
    var status = npc.status();
    //If the npc has this status with any player
    if (player.status().get("married") == 1) {
        status.adjustWithRange(player, "relationship", -10000, 0, 50000); //You are married!
        npc.say(player, npc.substring("proposal.married.player"));
    } else if (player.status().get("engaged") == 1) {
        status.adjustWithRange(player, "relationship", -5000, 0, 50000); //You are engaged!
        npc.say(player, npc.substring("proposal.engaged.player"));
    } else if (status.is(npc.world(), "engaged", 1)) {
        npc.say(player, npc.substring("proposal.engaged.npc"));
    } else if (status.is(npc.world(), "married"), 1) {
        status.adjustWithRange(player, "relationship", -500, 0, 50000); //Don't ask the married
        npc.say(player, npc.substring("proposal.married.npc"));
    } else {
        var hearts = (status.get(player, "relationship") / 5000);
        if (hearts == 10) {
            npc.say(player, npc.substring("proposal.accept"));
            player.status().set("engaged", 1);
            status.set(player, "engaged", 1); //Mark the player and npc as engaged
            npc.take(player, main_hand, 1); //Take the blue feather away
        } else if (hearts >= 8) {
            status.adjustWithRange(player, "relationship", -1000, 0, 50000); //Asked too early!
            npc.say(player, npc.substring("proposal.unready"));
        } else if (hearts >= 6) {
            status.adjustWithRange(player, "relationship", -2500, 0, 50000);
            npc.say(player, npc.substring("proposal.early"));
        } else if (hearts >= 4) {
             status.adjustWithRange(player, "relationship", -5000, 0, 50000);
             npc.say(player, npc.substring("proposal.unknown"));
         } else {
            status.adjustWithRange(player, "relationship", -10000, 0, 50000);
            npc.say(player, npc.substring("proposal.unbelievable"));
         }
    }
}