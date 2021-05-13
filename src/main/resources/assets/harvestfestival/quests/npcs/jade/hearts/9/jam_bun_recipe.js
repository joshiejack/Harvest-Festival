include('harvestfestival:quests/templates/relationships')

required_npc = 'jade' // The NPC that this quest applies to
required_hearts = 9 // Hearts required to complete the quest
prereq = '8_heart_event_4' // Previous heart level quest required to unlock the quest

function getReward () {
  return createStack('jam_bun_recipe')
}
