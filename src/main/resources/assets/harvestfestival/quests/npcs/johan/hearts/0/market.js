include('harvestfestival:quests/templates/default_quest')
include('harvestfestival:quests/templates/base_npc')

required_npc = 'johan'

// PlayerJS NPCJS NPCTaskList -> Void
// Called when we are chatting to the required npc
function onNPCChat (player, npc, tasks) {
  var town = towns.get(player)
  var team_started = player.team().status().get('received_johan_seeds') == 1
  if (team_started) {
    // I see that someone in your team has already started to help me!
    // I will help you along too but I do not have any additional seeds
    // to provide you for free but I do at least have some spare tools
    giveNotesAndBlueprints(tasks)
      .add('say', 'started')
      .add('unlock_note', 'market_stall')
      .add('give_item', createStack('harvestfestival:basic_hoe {Unbreakable:1}'))
      .add('give_item', createStack('horticulture:watering_can {Unbreakable:1}'))
      .add('complete_quest')
  } else {
    // We say hello, then unlock the note about the market market_stall
    // After this we ask the player if they know how to farm already
    tasks.add('say', 'hello')
         .add('unlock_note', 'market_stall')
         .add('ask', 'grow', 'no->reply', 'yes->reply')
  }
}

// PlayerJS NPCJS Integer -> Void
// Reply is called when the question 'grow' is asked
// We used reply to determine the result of the action
// Different seeds will be given based upon the season
function reply (player, npc, option) {
  var tasks = npc.tasks(this_id, player)
  giveNotesAndBlueprints(tasks.add((option == 0) ? 'backstory' : 'skip'))
    .add('give_item', createStack(getItem(player.world())).setCount(5))
    .add('give_item', createStack('harvestfestival:basic_hoe {Unbreakable:1}'))
    .add('give_item', createStack('horticulture:watering_can {Unbreakable:1}'))
    .add('set_team_status', 'received_johan_seeds', 1)
}

// Helper Functions
/** returns seed bag based on season **/
function getItem (world) {
  switch (season.get(world)) {
    case spring:
      return 'turnip_seed_bag'
    case summer:
      return 'onion_seed_bag'
    case autumn:
      return 'spinach_seed_bag'
    default:
      return 'winter_seed_bag'
  }
}

/** unlocks all the relevant notes and blueprints for this quest **/
function giveNotesAndBlueprints (tasks) {
  return tasks.add('unlock_note', 'hoe')
              .add('unlock_note', 'watering_can')
              .add('unlock_note', 'crop_farming')
              .add('unlock_blueprint', 'basic_hoe')
              .add('unlock_blueprint', 'basic_watering_can')
}
