var crops = ["cropTurnip", "cropPotato", "cropCucumber", "cropStrawberry", "cropCabbage",
            "cropOnion", "cropTomato", "cropPumpkin", "cropPineapple", "cropCorn",
            "cropEggplant", "cropSpinach", "cropCarrot", "cropSweetPotato", "cropGreenPepper"];

function canStart(player, scripts) {
    return scripts.completed("jenni_0k"); //TODO: Add additional requirements where applicable for UNLOCKED crops
}

function onRightClickedNPC(player, npc) {
    if (npc.is("jenni")) {
        var count = 0;
        for (var i = 0; i < crops.length; i++) {
            if (data.obtained(player, "shipped", crops[i])) {
                count = count + 1;
            }
        }

        if (count >= crops.length) {
            npc.say(player, "You have done a fantabulous job, here have a power berry!");
            npc.give(player, createStack("harvestfestival:power_berry"));
            quest.complete(player);
        }
    }
}