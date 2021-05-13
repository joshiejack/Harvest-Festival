// TODO: Second heart event, must catch 1000 fish, ship 1000 crops and have walked 1000m
include('harvestfestival:quests/templates/relationships')

required_npc = 'harvest_goddess' // The NPC that this quest applies to
required_hearts = 4 // Hearts required to complete the quest
prereq = '3_recipe' // Previous heart level quest required to unlock the quest

function onRightClickedNPC (player, npcEntity, hand) {
  if (npcEntity.is(this.getNPC()) && npcEntity.status().get(player, 'relationship') >= hearts * 5000) {
    if (player.status().get('fish_caught') >= 1000 && player.status('crops_shipped') >= 1000) {

    }
  }
}
