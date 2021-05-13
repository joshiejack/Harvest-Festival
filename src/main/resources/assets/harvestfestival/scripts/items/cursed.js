/** Will apply the cursed potion effect if it is not active
    @param  {PlayerJS}  player  -   the player with the item selected/worn
**/
function onItemSelected(player) {
    if (!player.hasEffect('harvestfestival:cursed')) {
        if (player.hasEffect('energy:exhaustion'))
            player.addEffect('harvestfestival:cursed', 300, 2)
        else if (player.hasEffect('energy:tired') || player.hasEffect('energy:fatigue'))
            player.addEffect('harvestfestival:cursed', 300, 1)
        else
            player.addEffect('harvestfestival:cursed', 300, 0)
    }
}

/** Increases the energy used when using a cursed tool
    @param  {AddExhaustionEventJS}  event  -   the event being called
**/
function onExhaustionAdded(event) {
    var player = event.player()
    if (player.hasEffect('harvestfestival:cursed'))
        event.setNewValue(event.getNewValue() * 10)
}