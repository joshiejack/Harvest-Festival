function onHarvestDrop(event) {
    if (!event.world().isClient()) {
        event.remove("minecraft:wheat_seeds")
        event.remove("minecraft:beetroot_seeds")
        event.remove("minecraft:melon_seeds")
        event.remove("minecraft:pumpkin_seeds")
    }
 }