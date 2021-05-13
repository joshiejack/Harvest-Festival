function onRightClick(player, hand) {
    var item = player.getHeldItem(hand);
    if (mine.isIn(player)) {
        mine.exit(player);
        item.shrink(1);
        return success;
    } else return pass;
}