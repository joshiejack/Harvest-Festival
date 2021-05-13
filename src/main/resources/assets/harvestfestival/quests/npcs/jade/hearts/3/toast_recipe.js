include('harvestfestival:quests/templates/relationships')

required_npc = 'jade' // The NPC that this quest applies to
required_hearts = 3 // Hearts required to complete the quest
prereq = '2_heart_event_1' // Previous heart level quest required to unlock the quest

function getReward () {
  return createStack('toast_recipe')
}
