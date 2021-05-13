function onItemShipped(world, team, shipped, gold) {
    team.status().adjust(world, "gold_earnt", gold);
}