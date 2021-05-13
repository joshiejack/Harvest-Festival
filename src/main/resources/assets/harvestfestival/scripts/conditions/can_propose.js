function isValid(world, pos, entity, player, stack, input) {
    var npc_statuses = settlements.statuses(world);
    for (var i = 0; i < npc_statuses.length; i++) {
        var hearts = (npc_statuses[i].get(player, "relationship") / 5000);
        if (hearts > 8) {
            return true;
        }
    }

    return false;
}