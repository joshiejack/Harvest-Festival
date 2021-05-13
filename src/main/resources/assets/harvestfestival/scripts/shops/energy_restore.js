function getDisplayStack() { //TODO: Restore energy stack
    return createStack("wheat");
}

function getDisplayName() { //TODO: Restore energy stack
    return "harvestfestival.shop.clinic.restore_energy";
}

function purchase(player) {
    player.feed(20, 20);
}