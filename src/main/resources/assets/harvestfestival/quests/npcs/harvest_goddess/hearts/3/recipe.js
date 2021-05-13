include('harvestfestival:quests/templates/relationships')

required_npc = 'harvest_goddess' // The NPC that this quest applies to
required_hearts = 3 // Hearts required to complete the quest
prereq = '2_heart_event_1' // Previous heart level quest required to unlock the quest

 /* //TODO: New Reward for 3 heart level with the harvest goddess
 function getReward() {
     return createStack("gastronomy:recipe").setStack("strawberry_jam");
 } */
