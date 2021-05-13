include('harvestfestival:quests/templates/base_npc')

required_npc = 'jim'
prereq = '0_have_barn'

var petted = false

/** Called when the player talks to the 'required_npc'
 * @param {PlayerJS}      player  - The player chatting with the npc
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {NPCTaskListJS} tasks   - A queue of tasks for the npc to perform  **/
function onNPCChat (player, npc, tasks) {
  if (petted) {
    // Tell the player well done, then teach them how to feed by hand
    // Mention that there's two ways to feed but for now to just feed by Hand
    tasks.add('say', 'well_done')
    .add('give_item', createStack('fodder').setCount(10))
    .add('unlock_note', 'harvestfestival:feeding_by_hand')
    .add('unlock_note', 'harvestfestival:trough')
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
  if (!petted && player.getHeldItem(main_hand).isEmpty()
              && entity.is('minecraft:cow')) {
    petted = true
  }
}

/** Called to load data
* @param DataJS data the tag to load data from **/
function loadData (nbt) {
  petted = nbt.load('petted', petted)
}

/** Called to save data
* @param DataJS data the tag to save data to **/
function saveData (nbt) {
  nbt.save('petted', petted)
}
