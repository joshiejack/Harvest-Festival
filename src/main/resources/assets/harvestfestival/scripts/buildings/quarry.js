function newDay(world, town, building) {
    var hf_mine = mine.get(world, "hf_mine");
    var max_floor = hf_mine.getMaxFloorForID(town.id());
    var positions = building.waypointsByPrefix("node");
    var count = 0;
    for (var it = positions.iterator(); it.hasNext();) {
        var target = iterator.next();
        if (random(0, 4) == 0 && world.isAir(target)) {
            var state = loot_registries.get("quarry").get(world);
            if (state != null && isValid(state, max_floor)) {
                if (world.getState(target.down()).block() == "minecraft:stone") {
                    world.setState(target, state);

                    count++;

                    if (count >= 8) {
                        break; //Exit the loop
                    }
                }
            }
        }
    }
}

function isValid(state, max_floor) {
    if (state.block() == "harvestfestival:node") {
        switch (state.property("node")) {
          case "amethyst":
          case "copper":
              return max_floor >= 10;
          case "gem":
          case "silver":
          case "topaz":
              return max_floor >= 51;
          case "diamond":
          case "emerald":
              return max_floor >= 131;
          case "gold":
          case "jade":
          case "ruby":
              return max_floor >= 91;
          case "mystril":
              return max_floor >= 222;
        }
    } else return true;
}