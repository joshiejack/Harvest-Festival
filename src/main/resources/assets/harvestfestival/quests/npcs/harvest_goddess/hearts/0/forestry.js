include('harvestfestival:quests/templates/base_npc')

required_npc = 'harvest_goddess'
prereq = '0_axe'

function display (info) {
    // Gather 24 logs to learn about blueprints
    info.setIcon(createStack('minecraft:log 0'))
    .setTitle('gather')
    .setDescription('logs')
}

function onNPCChat (player, npc, tasks) {
  if (player.has('logWood', 24)) {
    tasks.add('say', 'thanks_for_wood')
		.add('take_item', 'logWood', 24)
		.add('give_item', settlements.blueprint('harvestfestival:carpenter'))
		.add('say', 'explain_blueprints')
        .add('unlock_note', 'harvestfestival:towns')
		.add('unlock_note', 'harvestfestival:blueprints')
		.add('set_team_status', 'request_carpenter', 1)
		.add('complete_quest')
  } else if (random(1, 5) == 1) {
    tasks.add('say', 'remind_logs')
  }
}
