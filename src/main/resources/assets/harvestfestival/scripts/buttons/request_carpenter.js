function setupButton(npc, player, button) {
    button.setIcon(createStack("settlements:blueprint"));
    button.setName("harvestfestival.script.request_carpenter.name");
}

function canDisplay(npc, player) {
    return npc.is("harvestfestival:harvest_goddess") && player.team().status().get("request_carpenter") == 1 && !player.has("settlements:blueprint", 1);
}

function onButtonPressed(npc, player) {
    var tasks = npc.tasks(this_id, player);
    if (player.has("logWood", 24)) {
        tasks.add("give_item", settlements.blueprint("harvestfestival:carpenter"))
        .add("take_item", "logWood", 24)
        .add("say", "have_a_blueprint")
    } else tasks.add("say", "need_logs");
}