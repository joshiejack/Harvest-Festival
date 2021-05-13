// TODO: Must have unlocked 9 tv channels
include('harvestfestival:quests/templates/relationships')

required_npc = 'harvest_goddess' // The NPC that this quest applies to
required_hearts = 6 // Hearts required to complete the quest
prereq = '5_strawberry_milk_recipe' // Previous heart level quest required to unlock the quest

function onRightClickedNPC (player, npc, hand) {
  if (npc.is(this.getNPC()) && npc.status().get(player, 'relationship') >= hearts * 5000) {

  }
}
