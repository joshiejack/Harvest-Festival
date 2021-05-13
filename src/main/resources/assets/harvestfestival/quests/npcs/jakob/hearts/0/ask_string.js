include('harvestfestival:quests/templates/default_quest')
include('harvestfestival:quests/templates/base_npc')

required_npc = 'jakob'

/** Called when the player talks to the 'required_npc'
 *
 * If the player's team has already provided Jakob with
 * string then we will skip this quest and just move on
 * to asking them if they know how to fish or not!
 *
 * @param {PlayerJS}      player chatting with the npc
 * @param {NPCJS}         npc being interacted with
 * @param {NPCTaskListJS} task list for the npc **/
function onNPCChat (player, npc, tasks) {
  if (player.team().status().get('jakob_string') == 1) {
    tasks.add('say', 'already')
    // Looks like someone already helped to fix my fishing rod
    // As a good will gesture I do have a spare fishing rod for you
    .add('give_item', createStack('piscary:fishing_rod {Unbreakable:1}'))
    .add('unlock_note', 'harvestfestival:fishing')
    .add('unlock_blueprint', 'harvestfestival:basic_fishing_rod')
    .add('complete_quest', 'wait_string')
    .add('complete_quest')
  } else {
    // Have a rod for you but I need some string to help
    // repair it. I've heard that johan should have some
    // in his store! //TODO: Test 1000g_string in johan market
    tasks.add('say', 'find_string')
    .add('set_team_status', '1000g_string', 1)
    .add('complete_quest')
  }
}
