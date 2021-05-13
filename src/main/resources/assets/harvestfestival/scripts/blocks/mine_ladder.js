function onLadder(world, entity) {
    if (!world.isClient() && entity.is("player") && mine.isIn(entity)) {
        //Update the mine max, used for the elevator and quarry, PER TEAM
        mine.updateMaxFloor(entity);

        //Update the player_max, used for brandon relationship and mine berry, PER FLOOR
        if (world.id() == mine.id("hf_mine")) {
            var floor = mine.floor(entity);
            var label = ("max_mine_floor");
            var player_data = entity.status();
            var pMax = player_data.get(label);
            if (pMax < floor) {
                player_data.set(label, floor);
                if (floor >= 121) {
                    mail.send(entity, "harvestfestival:mine_berry");
                }
            }
        }
    }
}