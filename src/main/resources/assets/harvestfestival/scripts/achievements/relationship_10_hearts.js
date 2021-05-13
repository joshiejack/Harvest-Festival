function isObtained(player, stack) {
    var npc_statuses = settlements.statuses(player.world());
    for (var i = 0; i < npc_statuses.length; i++) {
        var hearts = (npc_statuses[i].get(player, "relationship") / 5000);
        if (hearts >= 10) {
            return true;
        }
    }

    return false;
}

function onRightClick(player, hand) {
    return success;
}