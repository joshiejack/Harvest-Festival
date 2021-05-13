function newDay(world, town, building) {
    //IF day is not a festival && day is not sunday, spawn a market stall building
    //IF day is a sunday or is not a festival, destroy market stall building
    var is_holiday = holiday.is(world);
    var is_sunday = calendar.weekday(world) == sunday;
    if (is_holiday || (is_sunday && !town.hasBuilding("harvestfestival:park_medium"))) {
        town.destroy(world, building, "harvestfestival:market_stall");
    } else if (!is_holiday && !is_sunday) {
        town.build(world, building, "harvestfestival:market_stall");
    }
}