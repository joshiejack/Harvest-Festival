function onHarvestDrop(event) {
    if (!event.world().isClient()) {
        if (event.state().block().is('minecraft:carrots')) {
            event.remove('minecraft:carrot')
            event.add('minecraft:carrot')
        }

        if (event.state().block().is('minecraft:potatoes')) {
            event.remove('minecraft:potato')
            event.add('minecraft:potato')
        }
    }
 }