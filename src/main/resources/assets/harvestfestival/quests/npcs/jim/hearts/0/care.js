include('harvestfestival:quests/templates/base_npc')

required_npc = 'jim'
prereq = '0_have_barn'

/** Called when the player talks to the 'required_npc'
 * @param {PlayerJS}      player  - The player chatting with the npc
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {NPCTaskListJS} tasks   - A queue of tasks for the npc to perform  **/
function onNPCChat (player, npc, tasks) {
  tasks.add('ask', 'care', 'no->onNo', 'yes->onYes')
}

/** Called when the player has answered the question

 * @param {PlayerJS}      player  - The player answereing the question
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {Number}        option  - The option selected by the player **/
function onNo (player, npc, optionID) {
  // You don't? Well then let me teach you!
  // How to pet, go pet a cow
  tasks = npc.tasks(this_id, player)
  .add('say', 'pet_cow')
  .add('unlock_note', 'harvestfestival:petting')
  .add('complete_quest')
}

/** Called when the player has answered the question

 * @param {PlayerJS}      player  - The player answereing the question
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {Number}        option  - The option selected by the player **/
function onYes (player, npc, optionID) {
  // Oh I see. Well then let me just give you a few
  // things to help you get started anyways!
  npc.tasks(this_id, player)
  .add('say', 'already')
  .add('give_item', createStack('husbandry:tool 0'))
  .add('give_item', createStack('husbandry:tool 1'))
  .add('give_item', createStack('fodder').setCount(10))
  .add('unlock_blueprint', 'harvestfestival:milker')
  .add('unlock_blueprint', 'harvestfestival:brush')
  .add('unlock_note', 'harvestfestival:petting')
  .add('unlock_note', 'harvestfestival:feeding_by_hand')
  .add('unlock_note', 'harvestfestival:trough')
  .add('unlock_note', 'harvestfestival:cleanliness')
  .add('unlock_note', 'harvestfestival:milker')
  .add('complete_quest', 'petting')
  .add('complete_quest', 'feeding')
  .add('complete_quest', 'cleaning')
  .add('complete_quest', 'milking')
  .add('complete_quest')
}
