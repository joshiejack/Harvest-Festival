function onItemShipped(world, team, shipped, gold) {
    if (data.isInList("crops", shipped)) {
        team.status().adjust(world, "crops_shipped", shipped.count());
    }
}