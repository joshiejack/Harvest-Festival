include('harvestfestival:quests/templates/relationships')

required_npc = 'harvest_goddess' // The NPC that this quest applies to
required_hearts = 5 // Hearts required to complete the quest
prereq = '4_heart_event_2' // Previous heart level quest required to unlock the quest

function getReward () {
  return createStack('strawberry_milk_recipe')
}
