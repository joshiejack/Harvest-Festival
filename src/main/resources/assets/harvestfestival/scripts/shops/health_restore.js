function getDisplayStack() { //TODO: Restore energy stack
    return createStack("apple");
}

function getDisplayName() { //TODO: Restore energy stack
    return "harvestfestival.shop.clinic.restore_health";
}

function purchase(player) {
    player.heal(20);
}