function getSeasons(world, pos) {
    var town = towns.find(world, pos);
    if (town.id() != 0) {
        var building = town.getClosestBuilding(world, pos);
        if (building.is("harvestfestival:greenhouse")) {
            var positions = building.waypointsByPrefix("farmland");
            for (var it = positions.iterator(); it.hasNext();) {
                var target = iterator.next();
                if (target.is(pos)) {
                    return new Array(spring, summer, autumn, winter);
                }
            }
        }
    }

    return null;
}