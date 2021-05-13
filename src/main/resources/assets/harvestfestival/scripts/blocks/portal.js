function onEntityCollision(world, pos, state, entity) {
    if (!world.isClient() && entity.portalTimer() == 0) {
        if (mine.isIn(entity)) {
            var floor = mine.floor(entity);
            if (floor == 1) {
                mine.exit(entity);
            } else {
                var target = entity.y() < 10 ? floor + 1 : floor - 1;
                mine.enter(entity, world.id(), mine.id(entity), target);
            }
        } else mine.enter(entity, mine.id("hf_mine"), towns.find(world, pos).id(), 1);
    }
}