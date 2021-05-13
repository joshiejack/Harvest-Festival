// 500 gifts
include('harvestfestival:quests/templates/relationships')

required_npc = 'harvest_goddess' // The NPC that this quest applies to
required_hearts = 8 // Hearts required to complete the quest
prereq = '7_power_berry' // Previous heart level quest required to unlock the quest

function onRightClickedNPC (player, npcEntity, hand) {
  if (npcEntity.is(this.getNPC()) && npcEntity.status().get(player, 'relationship') >= hearts * 5000) {
    if (npc.status().get(player, 'gift_count') >= 500) {

    }
  }
}
