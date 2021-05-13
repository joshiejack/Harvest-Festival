include('harvestfestival:quests/templates/base_npc')
include('harvestfestival:scripts/includes/town')

required_npc = 'harvest_goddess'
prereq = '0_birthday'

function display (info) {
    // Talk to the goddess to get a free axe/book etc
    info.setIcon(settlements.getNPC('harvestfestival:harvest_goddess'))
    .setTitle('title')
    .setDescription('description')
}

function onNPCChat (player, npc, tasks) {
  if (isBuiltOrUnderConstruction(player, 'harvestfestival:carpenter')) {
    tasks.add('say', 'built') // I see you already have a carpenter, so skip this
    .add('unlock_note', 'harvestfestival:blueprints') // Give the blueprints note
    .add('complete_quest', 'forestry') // Complete a relative quest
    .add('complete_quest', 'blueprints') // Complete a relative quest
    .add('set_team_status', 'request_carpenter', 1)
  } else {
    tasks.add('say', 'explain_axe')
  }

  tasks.add('give_item', createStack('harvestfestival:guide'))
  .add('set_team_status', 'request_book', 1)
  .add('unlock_blueprint', 'harvestfestival:basic_axe')
  .add('unlock_note', 'harvestfestival:lumber_axe')
  .add('unlock_note', 'harvestfestival:energy')
  .add('unlock_note', 'harvestfestival:economy')
  .add('unlock_note', 'harvestfestival:seasons')
  .add('unlock_note', 'harvestfestival:crafting')
  var town = towns.get(player)
  if (town == null || !town.hasBuilding('harvestfestival:park_small')) {
    tasks.add('give_item', scripting.createUnbreakableItemStack('harvestfestival:basic_axe'))
  }

  tasks.add('complete_quest')
}
