function onRightClickedNPC(player, npc) {
    if (shops.isOpen(player, npc)) {
        var shop_id = shops.get(player, npc).shop().id();
        npc.tasks(this_id, player).add("ask", shop_id + random(1, 3), "shop->open", "chat->converse"); //Ask a question, -> execute function"
    }
}

function open(player, npc)      {   shops.open(player, npc);     }
function converse(player, npc)  {   npc.chat(this_id, player);    }