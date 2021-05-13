include('harvestfestival:quests/templates/base_npc')

required_npc = 'jakob'
prereq = '0_ask_string'

/** Called when the player talks to the 'required_npc'

 * @param {PlayerJS}      player chatting with the npc
 * @param {NPCJS}         npc being interacted with
 * @param {NPCTaskListJS} task list for the npc **/
function onNPCChat (player, npc, tasks) {
  if (player.has('ore#string')) {
    // I See that you found some string for me! That is wonderful
    // I should be able to fix this rod right up for you
    // I'll even throw in some free bait to make things easier
    tasks.add('say', 'well_done')
    .add('give_item', createStack('piscary:fishing_rod {Unbreakable: 1}'))
    .add('give_item', createStack('piscary:bait 0').setCount(15))
    .add('unlock_blueprint', 'harvestfestival:basic_fishing_rod')
    .add('complete_quest')
  } else {
    tasks.add('reminder') // Remind the player
  }
}
