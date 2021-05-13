function getValue(player, stack) {
    return player.status().get("kill_count_minecraft:slime");
}

function formatValue(player, stack, value) {
    return Math.min(value, 10) + "/10";
}

function isObtained(player, stack) {
    return getValue(player, stack) >= 10;
}

function onRightClick(player, hand) {
    //TODO: Complete this quest to the minimum standards
    return success;
}