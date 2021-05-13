function onItemFished(player, item) {
    if (data.isInList("fishing", item)) {
        player.status().adjust("fish_caught", 1);
    }
}