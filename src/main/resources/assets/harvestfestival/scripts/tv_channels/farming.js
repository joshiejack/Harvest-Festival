function watch(player, television) {
    var tip = (calendar.day(player.world()) % 20); //20 total episodes
    television.chatter(player, "harvestfestival.television.channel.farming.tip" + tip);
 }