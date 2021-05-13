include('harvestfestival:quests/templates/default_quest')
include('harvestfestival:quests/templates/base_npc')

required_npc = 'yulif'
prereq = '0_explain'

function onNPCChat (player, npc, tasks) {
  // Yulif has asked for flint, so now we either need to acknowledge that the
  // player has it, or remind them
  if (player.has('flint', 3)) {
    // Yulif says thank you for the flint
    tasks.add('say', 'thanks_for_flint')
      .add('take_item', 'flint', 3)
      .add('say', 'have_a_hammer') // He then gives the player a hammer
      .add('unlock_blueprint', 'harvestfestival:basic_hammer')
      .add('unlock_note', 'harvestfestival:hammer')
      .add('unlock_note', 'harvestfestival:shops')
      .add('unlock_note', 'harvestfestival:carpenter')
      .add('give_item', createStack('harvestfestival:basic_hammer'))
      .add('say', 'new_blueprints') // He then explains that he has a shop
      // explaining that the player can either get a field built for the traveller
      // or that he can build them a house, and how he'll have more as the town grows
      .add('set_team_status', 'helped_yulif', 1)
      .add('complete_quest')
  } else if (random(1, 5) == 1) {
      // Yulif is going to give two different potential reminders
    if (random(1, 2) == 1) {
      tasks.add('say', 'check_blueprints') // If the player wants a new shovel
    } else { tasks.add('say', 'need_3_flint') } // This is what hte player needs
  }
}
