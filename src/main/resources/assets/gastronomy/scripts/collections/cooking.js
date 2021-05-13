function isInCollection(stack) {
    return cooking.hasRecipe(stack);
}

function isObtained(player, stack) {
    return data.obtained(player, "cooked", stack);
}

function onItemCooked(player, appliance, item) {
    if (isInCollection(item)) {
        data.obtain(player, "cooked", item);
    }
}