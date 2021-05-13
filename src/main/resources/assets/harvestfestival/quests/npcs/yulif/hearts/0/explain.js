include('harvestfestival:quests/templates/default_quest')
include('harvestfestival:quests/templates/base_npc')

required_npc = 'yulif'

function canStart (player, scripts) {
  if (prereq == undefined) {
    return true
  } else {
    return scripts.completed('harvestfestival:npcs_harvest_goddess_hearts_0_blueprints')
  }
}

function onNPCChat (player, npc, tasks) {
  var unlocked_blueprints = player.team().status().get('helped_yulif') == 1
  // If any of the team members have completed this quest already
  // Then we'll skip the tutorial aspect and yulif will just thank them
  // And then he'll give the player the shovel and hammer blueprint/notes
  if (unlocked_blueprints) {
      // Yulif says Oh I see someone has already given me a helping hand, that's wonderful...
      // Well then m8 have some blueprints
    tasks.add('unlock_blueprint', 'harvestfestival:basic_hammer')
      .add('unlock_note', 'harvestfestival:hammer')
      .add('unlock_blueprint', 'harvestfestival:basic_shovel')
      .add('unlock_note', 'harvestfestival:shovel')
      .add('complete_quest', 'flint')
      .add('complete_quest')
      .add('say', 'already')
  } else {
    // Yulif explains about wanting to get a traveller to town
    // He asks the player for 3 flint to make a hammer as he is short by half
    // He gives the player a shovel to assist them, to make it easier
    // He also gives the player the note and blueprint
    tasks.add('say', 'ask_for_flint')
    .add('give_item', scripting.createUnbreakableItemStack('harvestfestival:basic_shovel'))
    .add('unlock_blueprint', 'harvestfestival:basic_shovel')
    .add('unlock_note', 'harvestfestival:shovel')
    .add('unlock_note', 'harvestfestival:shops')
    .add('unlock_note', 'harvestfestival:carpenter')
    .add('complete_quest')
  }
}
