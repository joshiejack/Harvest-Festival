function getValue(player, stack) {
    return player.team().status().get("gold_earnt");
}

function formatValue(player, stack, value) {
    return Math.min(value, 1000000).toString().replace(/(.)(?=(\d{3})+$)/g,'$1,') + "/1,000,000";
}

function isObtained(player, stack) {
    return getValue(player, stack) >= 1000000;
}

function onRightClick(player, hand) {
    //TODO: Complete this quest to the minimum standards
    return success;
}