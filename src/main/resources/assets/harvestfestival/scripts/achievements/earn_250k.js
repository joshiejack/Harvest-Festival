function getValue(player, stack) {
    return player.team().status().get("gold_earnt");
}

function formatValue(player, stack, value) {
    return Math.min(value, 250000).toString().replace(/(.)(?=(\d{3})+$)/g,'$1,') + "/250,000";
}

function isObtained(player, stack) {
    return getValue(player, stack) >= 250000;
}

function onRightClick(player, hand) {
    //TODO: Complete this quest to the minimum standards
    return success;
}