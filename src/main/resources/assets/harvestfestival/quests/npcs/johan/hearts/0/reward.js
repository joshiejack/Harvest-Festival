include('harvestfestival:quests/templates/base_npc')

required_npc = 'johan'
prereq = 'market'

// PlayerJS NPCJS NPCTaskList -> Void
// Called when we are chatting to the required npc
function onNPCChat (player, npc, tasks) {
  if (hasValidCropsInInventory(player, 5)) {
    var reward = getGoldFromTeamSize(player.team().size())
      .add('say', 'well_done')
      .add('unlock_note', 'market_stall')
      .add('complete_quest')
      .add('complete_quest', 'farming')
  } else {
    // We say hello, then unlock the note about the market market_stall
    // After this we ask the player if they know how to farm already
    tasks.add('say', 'hello')
         .add('unlock_note', 'market_stall')
         .add('ask', 'grow', 'no->reply', 'yes->reply')
  }
}

// Integer -> Integer
// Calculates the amount of gold each player in this
// team should receivew hen they have completed the quest
function getGoldFromTeamSize (size) {
  return 5000 / (0.15 + (size * 0.85));
}

/** If the player has any of these crops in their inventory **/
function hasValidCropsInInventory (player, amount) {
  return player.has('ore#cropTurnip', amount)
      || player.has('ore#cropOnion', amount)
      || player.has('ore#cropSpinach', amount)
      || player.has('lavender', amount)
}
