function watch(player, television) {
    //TODO: Episodes of fishing hour
    var episode = (calendar.day(player.world()) % 9); //9 total episodes
    television.chatter(player, "harvestfestival.television.channel.fishing.episode" + episode);
    //TODO: 1. Piranha, 2. Electric Ray, 3. Blassop, 4. Fossil, 5, Lungfish, 6. Manta Ray, 7. Pufferfish, 8. Pirate Loot, 9. Anglerfish
 }