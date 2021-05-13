function canSpawnHere(slime) {
    return slime.world().id() == mine.id("hf_mine");
}

function spawnCustomParticles(slime) {
    if (!slime.world().isClient()) {
        slime.world().displayParticle(snow_shovel, 32, slime.x() + 0.2 * Math.random(), slime.y() + 0.5 + 0.2 * Math.random(), slime.z() + 0.2 * Math.random(), 0);
    }

    return true;
}