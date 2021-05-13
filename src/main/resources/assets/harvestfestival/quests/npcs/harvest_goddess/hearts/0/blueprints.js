include('harvestfestival:quests/templates/base_npc')
include('harvestfestival:scripts/includes/town')

required_npc = 'harvest_goddess'
prereq = '0_forestry'

function display (info, player) {
    // If isBuilt -> Talk to the Goddess to complete the quest
	// Else -> Build the Carpenter?
  info.setTitle('title')
  .setIcon(settlements.building('harvestfestival:carpenter'))
  if (isBuilt(player, 'harvestfestival:carpenter')) {
    info.setDescription('talk')
  } else info.setDescription('build')
}

function onRightClickedNPC (player, npcEntity) {
  if (npcEntity.is(npc)) {
    onNPCChat(player, npcEntity.tasks(this_id, player))
  } else if (isBuilt(player, 'harvestfestival:carpenter')) {
    if (npc.is('harvesfestival:jade') || npc.is('harvestfestival:yulif')) {
      if (random(1, 5) == 0) { // Random reminders
        npcEntity.tasks(this_id, player).add('say', 'talk_to_goddess')
      }
    }
  }
}

function onNPCChat (player, npc, tasks) {
  if (isBuilt(player, 'harvestfestival:carpenter')) {
    tasks.add('say', 'thanks_for_carpenter')
    .add('set_team_status', 'harvestfestival:carpenter', 0)
    .add('give_item', gifts.random('flower'))
    .add('say', 'bye', player.name())
    .add('complete_quest')
  } else {
    var current_time = player.world().time()
    var last_time = player.status().get('last_carpenter_reminder')
    if (last_time == 0 || current_time - last_time >= 1500) {
			// Also enable the ask for another carpenter button here
      if (random(1, 5) == 1) {
        player.status().set('last_carpenter_reminder', current_time)
        tasks.add('say', 'build_carpenter') // Remind the player to be building the carpenter
      } else if (random(1, 3) == 1) {
        player.status().set('last_carpenter_reminder', current_time)
        tasks.add('say', 'request_carpenter') // Tell the player if they lost the blueprint that they can request a new carpenter if they visit me
      }
    }
  }
}
