function onEntityKilled(player, entity) {
    player.status().adjust("kill_count_" + entity.registry(), 1);
}