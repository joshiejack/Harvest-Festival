function onFoodEaten(player, stack) {
    player.removeEffect("energy:tired");
    player.removeEffect("energy:fatigue");
    player.removeEffect("energy:exhaustion");
    player.addEffect("energy:buzzed", 1500, 0);
}