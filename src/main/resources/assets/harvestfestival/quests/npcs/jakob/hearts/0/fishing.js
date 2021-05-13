include('harvestfestival:quests/templates/base_npc')

required_npc = 'jakob'
prereq = '0_wait_string'

/** Called when the player talks to the 'required_npc'

 * @param {PlayerJS}      player  - The player chatting with the npc
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {NPCTaskListJS} tasks   - A queue of tasks for the npc to perform  **/
function onNPCChat (player, npc, tasks) {
  tasks.add('ask', 'fish', 'no->onQuestionAnswered', 'yes->onQuestionAnswered')
}

/** Called when the player has answered the question'

 * @param {PlayerJS}      player  - The player answereing the question
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {Number}        option  - The option selected by the player **/
function onQuestionAnswered (player, npc, option) {
  // If the player answers yes then Jakob will teach them how to fish
  // Otherwise he says very little. Either ways afterwards he asks
  // the player to go and fish up one fish from the ocean to prove
  // to him what they've learnt. He'll open his shop afterwards
  var tasks = npc.tasks(this_id, player)
  tasks.add('say', option == 0 ? 'explain' : 'skip')
  .add('unlock_note', 'harvestfestival:fishing')
  if (player.team().status().get('caught_ocean_fish') == 1) {
    tasks.add('complete_quest', 'fish')
    .add('say', 'already') // I see that someone has helped me with a fish
    // Therefore there is no need for you to prove yourself to me as well
  } else tasks.add('say', 'fetch_fish')
  tasks.add('complete_quest')
}
