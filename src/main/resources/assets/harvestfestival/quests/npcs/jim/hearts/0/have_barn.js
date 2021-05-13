include('harvestfestival:quests/templates/default_quest')
include('harvestfestival:quests/templates/base_npc')
include('harvestfestival:scripts/includes/town')

required_npc = 'jim'


/** Called when the player talks to the 'required_npc'
 * @param {PlayerJS}      player  - The player chatting with the npc
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {NPCTaskListJS} tasks   - A queue of tasks for the npc to perform  **/
function onNPCChat (player, npc, tasks) {
  if (hasBuilding('harvestfestival:barn_1') && animals.count('minecraft:cow', player.world(), town) > 0) {
    // I see that you already have a barn with a cow! That is wonderful
    tasks.add('say', 'already')
    .add('complete_quest')
  } else {
    tasks.add('ask', 'have_barn', 'no->onAnsweredNo', 'yes->onAnsweredYes')
  }
}

/** Called when the player has answered the question'
 * @param {PlayerJS}      player  - The player answereing the question
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {Number}        option  - The option selected by the player **/
function onAnsweredNo(player, npc, option) {
  var tasks = npc.tasks(this_id, player)
  if (hasBuilding('harvestfestival:barn_1')) {
    // I see that you have a barn! Well then let me give you a cow
    // to put inside it! I also have a lead for you to make things easier
    tasks.add('say', 'cow')
    .add('spawn_entity', 'minecraft:cow', player.world(), player.pos())
    .add('say', 'lead')
    .add('give_item', createStack('minecraft:lead'))
    .add('say', 'put_away')
    .add('complete_quest')
  } else if (isBeingBuilt(player, 'harvestfestival:barn_1')) {
    // I see that a barn is being built but I'll need you to talk to me
    // When it is actually finished
    tasks.add('say', 'being_built')
  }  else {
    // Hmm you say you have a barn but looking at the plans for your
    // farm I can see that you do not actually have one yet.
    // Please do come back when you do and please don't lie!
    adjustRelationship(required_npc, -100)
    .add('say', 'no_you_dont')
  }
}

/** Called when the player has answered the question as a no'
 * @param {PlayerJS}      player  - The player answereing the question
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {Number}        option  - The option selected by the player **/
function onAnsweredNo(player, npc, option) {
  // Oh I see come back and see me when you have one then!
  npc.tasks(this_id, player).add('say', 'come_back')
}