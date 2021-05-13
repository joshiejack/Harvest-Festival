include('harvestfestival:quests/templates/relationships')

required_npc = 'harvest_goddess' // The NPC that this quest applies to
required_hearts = 7 // Hearts required to complete the quest
prereq = '6_heart_event_3' // Previous heart level quest required to unlock the quest

function getReward () {
  return createStack('harvestfestival:power_berry')
}
