function onFoodEaten(player, item) {
    energy.increaseMaxEnergy(player);
    energy.increaseMaxHealth(player);
    player.feed(20, 1); //Feed me again
    player.heal(20);
}