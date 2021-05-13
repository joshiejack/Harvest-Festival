function setup(settings) {
    settings.setType("player");
    settings.setDefault();
}

//If we have fished up a power berry then this script should be marked as completed
function onItemFished(player, item) {
    if (item.is("harvestfestival:power_berry")) {
        quest.complete(player);
    }
}