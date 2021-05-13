function onPlayerLogin(player) {
    knowledge.unlockBlueprint(player, 'harvestfestival:kiln')
    quest.complete(player)
}