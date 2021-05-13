include('harvestfestival:quests/templates/base_npc')

required_npc = 'jim'
prereq = '0_feeding'

var milked = false

/** Called when the player talks to the 'required_npc'
 * @param {PlayerJS}      player  - The player chatting with the npc
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {NPCTaskListJS} tasks   - A queue of tasks for the npc to perform  **/
function onNPCChat (player, npc, tasks) {
  if (milked) {
    // Tell the player well done
    tasks.add('say', 'well_done')
    if (player.team().status().get('cow_tutorial_completed') == 0) {
      // Inform the player you will open your shop now!
      tasks.add('say', 'shop')
      .add('set_team_status', 'cow_tutorial_completed', 1)
    }

    tasks.add('complete_quest')
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
  if (!milked && player.isHolding('husbandry:tool 1', hand)
              && entity.is('minecraft:cow')
              && !animals.stats(entity).canProduce()
              && player.has('small_milk')) {
    milked = true
  }
}

/** Called to load data
* @param DataJS data the tag to load data from **/
function loadData (data) {
  milked = data.load('milked', milked)
}

/** Called to save data
* @param DataJS data the tag to save data to **/
function saveData (data) {
  data.save('milked', milked)
}
