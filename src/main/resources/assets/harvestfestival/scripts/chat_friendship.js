function onRightClickedNPC(player, npc) {
    var status = npc.status();
    if (status.get(player, "has_met") == 0) {
        status.set(player, "has_met", 1);
    }

    //If we haven't talked to this npc, then mark them as having talked
    if (status.get(player, "has_talked") == 0) {
        var last = status.get(player, "last_talked");
        status.set(player, "has_talked", 1);
        var adjust = 100;
        var today = calendar.elapsed(npc.world());
        var difference = today - last; //Today = 2, Last = 1
        //If we haven't talked for more than one day, lose 20 points per day, up to maximum of 100 loss
        if (difference > 1) {
            //Days * 20, up to 100
            var loss = 20 * (difference - 1);
            if (loss >= 100) {
                loss = 100;
            }

            adjust = adjust - loss;
        }

        status.adjustWithRange(player, "relationship", adjust, 0, 50000);
        status.set(player, "last_talked", today);
    }
}