include('harvestfestival:quests/templates/default_quest')
include('harvestfestival:quests/templates/base_npc')
include('harvestfestival:scripts/includes/town')

required_npc = 'ashlee'

/* PlayerJS NPCJs NPCTaskListJS -> Void
Produces the relevant chat for this quest */
function onNPCChat (player, npc, tasks) {
  if (isBeingBuilt(player, 'harvestfestival:coop_1')) {
    // I see that you have a coop under construction!
    // Come back and talk to me when it is finished
    tasks.add('say', 'building')
  } else if (isBuilt('harvestfestival:coop_1')) {
    // Coop is built so we can complete this quest
    tasks.add('say', 'built')
    // TODO: CHECK THE CODE FOR This
    /* Intention is that the player will receive
    a free chicken if the town does not already have one
    if it does then now free chicken shall be obtained! */
    var town = towns.get(player)
    if (animals.count('minecraft:chicken', player.world(), town) == 0) {
      tasks.add('spawn_entity', 'minecraft:chicken', player.world(), player.pos())
      .add('say', 'free_chicken')
    } else {
      tasks.add('say', 'existing_chicken')
    }

    tasks.add('complete_quest')
  } else if (player.team().status().get('received_free_coop') == 0) {
    // Gives a free blueprint if the team has not received one yet!
    // Otherwise the player must buy it from Yulif! No Extras
    tasks.add('give_item', createStack('#TODO: COOP_1_BLUEPRINT'))
    .add('say', 'blueprint')
    .add('set_team_status', 'received_free_coop', 1)
  } else {
    // Reminds them to build a coop, tells them she's sorry if they lost it
    // But she believes that Yulif does sell them if they need one
    tasks.add('say', 'build')
  }
}
