function setup(settings) {
    settings.setType("player");
    settings.setDefault();
}

//If the player has been in this world for more than a year, then hoe will generate a power berry
function onUseHoe(player, pos) {
    var year = season.asYear(player.world().time());
    var birthday = season.asYear(player.spawnday());
    if (year - birthday >= 1 && Math.random() >= 0.95) {
        player.world().drop(pos, createStack("harvestfestival:power_berry"));
        quest.complete(player);
    }
}