include('harvestfestival:quests/templates/relationships')

required_npc = 'ashlee' // The NPC that this quest applies to
required_hearts = 3 // Hearts required to complete the quest
prereq = '1_baked_corn_recipe' // Previous heart level quest required to unlock the quest

function getReward () {
  return createStack('popcorn_recipe')
}
