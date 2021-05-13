function newDay(world, town, building) {
    var is_holiday = holiday.is(world);
    var is_sunday = calendar.weekday(world) == sunday;
    if (is_holiday || !is_sunday) {
        town.destroy(world, building, "harvestfestival:sunday_market"); //Destroy the market if it shouldn't be here
    } else if (!is_holiday && is_sunday) { //Build the sunday markets
        town.build(world, building, "harvestfestival:sunday_market");
    }
}