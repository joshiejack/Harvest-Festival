function setupButton(npc, player, button) {
    button.setIcon(player.getHeldItem(main_hand));
    button.setName("Gift held item!");
}

function canDisplay(npc, player) {
    if (npc.is("harvestfestival:harvest_goddess")) return false; //No gifting the goddess this way, through the water only
    else return canGiveGift(npc, player) && !player.getHeldItem(main_hand).isEmpty(); //Only display the button if we haven't gifted instead of always! Smart function!
}

function canGiveGift(npc, player) {
    return npc.status().get(player, "has_gifted") == 0;
}

function onButtonPressed(npc, player) {
    onGiftGiven(npc, player, player.getHeldItem(main_hand));
}

function onGiftGiven(npc, player, stack) {
    var tasks = npc.tasks(this_id, player);
    var category = gifts.category(stack);
    if (category.name() == "none") {
        tasks.add("say", npc.substring("gift.impossible"));
        return false;
    } else {
        var status = npc.status();
        var quality = gifts.quality(npc, stack);
        var value = quality.value();
        var birthday_day = npc.data("birth_day");
        var birthday_season = npc.data("birth_season");
        if (calendar.day(player.world()) == birthday_day && season.is(player.world(), birthday_season) && status.get(player, "birthday_gift") == 0) { //Only one birthday gift bonus
            value = value * 10; //Multiplier for a birthday gift
            status.set("birthday_gift", 1);
        } else if (season.get(player.world()) == winter && calendar.day(player.world()) == 25 && status.get(player, "christmas_gift") == 0) {
            value = value * 5;
            status.set("christmas_gift", 1); //Bonus on christmas day
        }

        status.adjustWithRange(player, "relationship", value, 0, 50000); //Max of 50,000 for relationships
        status.set(player, "has_gifted", 1); //Mark them as having been gifted
        status.adjust(player, "gift_count", 1); //Increase the amount of gifts given
        //elapsed
        var last = status.get(player, "last_gift"); //Grab the day we last gifted
        var today = calendar.elapsed(player.world()); //Grab today
        if (today - last == 1) { //If it has been ONE day
            status.adjust(player, "gift_streak", 1); //Then our gift streak can go up
        } else status.set(player, "gift_streak", 0); //Reset the gift streak

        status.set(player, "last_gift", today); //Set the last gift date
        tasks.add("say", npc.substring("gift." + quality.name())).add("take_held", main_hand, 1)
        return true;
    }
}