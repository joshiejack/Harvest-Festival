include('harvestfestival:quests/templates/base_npc')
include('harvestfestival:scripts/includes/town')

required_npc = 'ashlee'
prereq = '0_coop'

/** Called when the player interacts with 'required_npc'
 * This will skip many of the quests in this quest line
 * @param {PlayerJS}      player chatting with the npc
 * @param {NPCJS}         npc being interacted with
 * @param {NPCTaskListJS} task list for the npc **/
function onNPCChat (player, npc, tasks) {
  tasks.add('ask', 'care', 'no->start', 'yes->complete')
}

/** Called when the player already knows how to care for chickens
 * This will skip many of the quests in this quest line
 * @param {PlayerJS}   player answering the question
 * @param {NPCJS}      npc being interacted with
 * @param {Integer}    option selected by the player **/
function complete (player, npc, optionID) {
  var tasks = npc.tasks(this_id, player)
  .add('give_item', createStack('bird_feed').setCount('10'))
  .add('unlock_note', 'harvestfestival:carrying_animals')
  .add('unlock_note', 'harvestfestival:feeding_by_hand')
  .add('unlock_note', 'harvestfestival:feeding_tray')
  .add('unlock_note', 'harvestfestival:nest')
  .add('complete_quest', 'carry')
  .add('complete_quest', 'feed')
  .add('complete_quest')
}

/** Called when the player doesn't know how to care for chickens
 * Ashlee gives the player a note teaching how to carry small
 * animals around. And how picking them increases their affection.
 * She asks the player to perform this task and then return to her.
 * @param {PlayerJS}   player answering the question
 * @param {NPCJS}      npc being interacted with
 * @param {Integer}    option selected by the player **/
function start (player, npc, option) {
  var tasks = npc.tasks(this_id, player)
  .add('say', 'care')
  .add('give_note', 'harvestfestival:carrying_animals')
  .add('complete_quest')
}
