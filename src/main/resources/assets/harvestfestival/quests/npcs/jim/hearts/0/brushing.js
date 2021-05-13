include('harvestfestival:quests/templates/base_npc')

required_npc = 'jim'
prereq = '0_feeding'

var brushed = false

/** Called when the player talks to the 'required_npc'
 * @param {PlayerJS}      player  - The player chatting with the npc
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {NPCTaskListJS} tasks   - A queue of tasks for the npc to perform  **/
function onNPCChat (player, npc, tasks) {
  if (brushed) {
    // Tell the player well done, then teach them how to use the milker
    // And how they can get millk from their cow. She also explains that
    // You can still use a bucket to get milk but the downside to that
    // is that when you go to sell it, You will lose the bucket
    tasks.add('say', 'well_done')
    .add('give_item', createStack('husbandry:tool 1'))
    .add('unlock_note', 'harvestfestival:milker')
    .add('unlock_blueprint', 'harvestfestival:milker')
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
  if (!brushed && player.isHolding('husbandry:tool 0', hand)
              && entity.is('minecraft:cow') && animals.stats(entity).isClean()) {
    brushed = true
  }
}

/** Called to load data
* @param DataJS data the tag to load data from **/
function loadData (data) {
  brushed = data.load('brushed', brushed)
}

/** Called to save data
* @param DataJS data the tag to save data to **/
function save (data) {
  data.save('brushed', brushed)
}
