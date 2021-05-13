include('harvestfestival:quests/templates/base_npc')

required_npc = 'jim'
prereq = '0_petting'

var fed = false

/** Called when the player talks to the 'required_npc'
 * @param {PlayerJS}      player  - The player chatting with the npc
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {NPCTaskListJS} tasks   - A queue of tasks for the npc to perform  **/
function onNPCChat (player, npc, tasks) {
  if (fed) {
    // Tell the player well done, then teach them how to clean animals
    // tell them how it affects their happiness if they aren't cleaned
    tasks.add('say', 'well_done')
    .add('give_item', createStack('husbandry:tool 0'))
    .add('unlock_note', 'harvestfestival:cleanliness')
    .add('unlock_blueprint', 'harvestfestival:brush')
    .add('complete_quest')
  } else tasks.add('say', 'reminder') // Remind the player to pet a cow
}

/** Called when the player interacts with an entity
 * @param {PlayerJS}  player  - The player performing the interaction
 * @param {EntityJS}  entity  - The entity being interacted with
 * @param {Hand}      hand    - The hand being used
                                ONE OF:
                                main_hand
                                other_hand
**/
function onEntityInteract (player, entity, hand) {
  if (!fed && player.isHolding('fodder', hand)
              && entity.is('minecraft:cow')) {
    fed = true
  }
}

/** Called to load data
* @param DataJS data the tag to load data from **/
function loadData (data) {
  fed = data.load('fed', fed)
}

/** Called to save data
* @param DataJS data the tag to save data to **/
function saveData (data) {
  data.save('fed', fed)
}
