function getValue(player, stack) {
    return player.status().get("fish_caught");
}

function formatValue(player, stack, value) {
    return Math.min(value, 100) + "/100";
}

function isObtained(player, stack) {
    return getValue(player, stack) >= 100;
}

function onRightClick(player, hand) {
    //TODO: Complete this quest to the minimum standards
    return success;
}