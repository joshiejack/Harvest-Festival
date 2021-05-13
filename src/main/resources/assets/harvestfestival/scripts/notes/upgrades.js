var base_items = ["horticulture:watering_can", "harvestfestival:basic_hoe", "husbandry:sickle", "harvestfestival:basic_hammer",
                    "harvestfestival:basic_sword", "harvestfestival:basic_shovel", "harvestfestival:basic_axe", "piscary:fishing_rod"];
var tool_tiers = ["basic", "copper", "silver", "gold", "mystril", "blessed", "mythic"];
var materials = ["copper", "silver", "gold", "mystril", "mythic"];
var item_map = {}
var material_map = {}

function init(gui) {
    item_map["basic"] = gui.createCyclingStack(createStacks(base_items));
    for (var i = 1; i < tool_tiers.length; i++) {
        var tier = tool_tiers[i];
        item_map[tier] = gui.createCyclingStack(createStacks(["harvestfestival:" + tier + "_watering_can", "harvestfestival:" + tier + "_hoe", "harvestfestival:" + tier + "_sickle", "harvestfestival:" + tier + "_hammer",
                    "harvestfestival:" + tier + "_sword", "harvestfestival:" + tier + "_shovel", "harvestfestival:" + tier + "_axe", "harvestfestival:" + tier + "_fishing_rod"]));
    }

    material_map["copper"] = createStack("copper_ingot").setCount(5);
    material_map["silver"] = createStack("silver_ingot").setCount(5);
    material_map["gold"] = createStack("gold_ingot").setCount(5);
    material_map["mystril"] = createStack("mystril_ingot").setCount(5);
    material_map["mythic"] = createStack("mythic_stone");
}

function draw(gui, page) {
    drawTier(gui, 2500, "basic", "copper", 55);
    drawTier(gui, 5000, "copper", "silver", 75);
    drawTier(gui, 10000, "silver", "gold", 95);
    drawTier(gui, 25000, "gold", "mystril", 115);
    drawTier(gui, 100000, "blessed", "mythic", 135);
}

function drawTier(gui, cost, original_tier, upgrade_tier, y_position) {
    var ingot = material_map[upgrade_tier];
    var original = item_map[original_tier];
    var upgrade = item_map[upgrade_tier];
    gui.drawRightArrow(87, y_position + 3);
    gui.drawCyclingStack(original, 69, y_position + 3, 1);
    gui.drawCyclingStack(upgrade, 104, y_position + 3, 1);
    gui.drawGold(cost, 20, y_position + 2);
    gui.drawStack(ingot, -1, y_position - 2, 1);
}