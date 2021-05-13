function onGetBreakSpeed(player, state, pos, original) {
    if (player.isWielding("sword") && (state.block() == "harvestfestival:crate" || (state.block() == "harvestfestival:rock" && state.property("rock") == "ice_crystal"))) {
        return original * 12.5;
    }

    return original;
}