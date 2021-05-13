function setupButton(npc, player, button) {
    button.setIcon(createStack("minecraft:clock"));
    button.setName("When are you open?");
}

function canDisplay(npc, player) {
    return shops.has(player, npc) && !shops.isOpen(player, npc);
}

function onButtonPressed(npc, player) {
    npc.tasks(this_id, player).add('say', npc.substring("hours"));
}