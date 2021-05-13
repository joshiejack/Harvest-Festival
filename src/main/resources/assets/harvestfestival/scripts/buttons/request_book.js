function setupButton(npc, player, button) {
    button.setIcon(createStack("harvestfestival:guide"));
    button.setName("name");
}

function canDisplay(npc, player) {
    var town = towns.get(player); //Grab the town
    return npc.is("harvestfestival:harvest_goddess") && player.team().status().get("request_book") == 1
                                                     && (town == null || !town.hasBuilding("harvestfestival:park_small")
                                                     && !player.has("harvestfestival:guide", 1));
}

function onButtonPressed(npc, player) {
    npc.tasks(this_id, player)
    .add("give_item", createStack("harvestfestival:guide"))
    .add("say", "have_a_book")
}