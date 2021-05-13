function isInCollection(stack) {
    return data.isInList("mining", stack);
}

function isObtained(player, stack) {
    return data.obtained(player, "mined", stack);
}

function onHarvestDrop(event) {
    var player = event.player();
    if (mine.isIn(player)) {
        for (var i; i < event.length(); i++) {
            var item = event.item(i);
            if (isInCollection(item)) {
                data.obtain(player, "mined", item);
            }
        }
    }
}