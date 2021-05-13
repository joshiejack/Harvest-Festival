function onNPCSpawned(npc) {
    npc.world().displayParticle(enchantment_table, 16, npc.x() + 0.2 * Math.random(), npc.y() + 0.5 + 0.2 * Math.random(), npc.z() + 0.2 * Math.random(), 1.1);
    npc.world().playSound(npc.pos(), "harvestfestival:goddess", neutral, 0.5, 1.1);
}

function onNPCUpdate(npc) {
    npc.world().displayParticle(explosion_normal, 16, npc.x() + 0.1 * Math.random(), npc.y() - 0.5 + 0.2 * Math.random(), npc.z() + 0.1 * Math.random(), -0.05);
}