function getValue(player, stack) {
    return player.team().status().get("crops_shipped");
}

function formatValue(player, stack, value) {
    return Math.min(value, 1000) + "/1000";
}

function isObtained(player, stack) {
    return getValue(player, stack) >= 1000;
}

function onRightClick(player, hand) {
    //TODO: Complete this quest to the minimum standards
    return success;
}