include('harvestfestival:quests/templates/relationships')

required_npc = 'ashlee' // The NPC that this quest applies to
required_hearts = 1 // Hearts required to complete the quest
prereq = '0_feeding' // Previous heart level quest required to unlock the quest

function getReward () {
  return createStack('baked_corn_recipe')
}
