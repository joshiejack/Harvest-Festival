var festivals = ["cow_festival_won", "chicken_festival_won", "sheep_festival_won", "cooking_festival_won"];

function setup(settings) {
    settings.setType("player");
    settings.setDefault();
}

function onFestivalWon(player, festival_name) {
    player.status().set(festival_name, 1);
    var count = 0;
    for (var i = 0; i < festivals.length; i++) {
        if (player.status().get(festivals[i]) == 1) {
            count = count + 1;
        }
    }

    if (count >= festivals.length) {
        player.give(createStack("harvestfestival:power_berry"));
        quest.complete(player);
    }
}