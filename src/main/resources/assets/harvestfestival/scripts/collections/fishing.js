function isInCollection(stack) {
    return data.isInList("fishing", stack);
}

function isObtained(player, stack) {
    return data.obtained(player, "fished", stack);
}

function onItemFished(player, item) {
    if (isInCollection(item) || item.is("harvestfestival:power_berry")) {
        data.obtain(player, "fished", item);
    }
}