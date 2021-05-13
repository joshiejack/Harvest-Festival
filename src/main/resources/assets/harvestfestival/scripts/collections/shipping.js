function isInCollection(stack) {
    return !collections.isIn("fishing", stack) &&
           !collections.isIn("mining", stack) &&
           !collections.isIn("cooking", stack);
}

function isObtained(player, stack) {
    return data.obtained(player, "shipped", stack);
}

function onItemShipped(world, team, item, value) {
    if (isInCollection(item)) {
        data.obtain(world, team, "shipped", item);
    }
}