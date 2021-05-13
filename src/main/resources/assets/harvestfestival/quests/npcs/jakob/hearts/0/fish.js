include('harvestfestival:quests/templates/base_npc')

required_npc = 'jakob'
prereq = '0_fishing'

var fished = false

/** Called when the player talks to the 'required_npc'
 * Jakob is waiting for the player to return to him with a fish
 * that they have caught in an ocean biome. The player will have
 * to actually fish to complete this quest. They cannot just turn
 * up with a random fish found from somewhere else.
 * @param {PlayerJS}      player  - The player chatting with the npc
 * @param {NPCJS}         npc     - The NPC being interacted with
 * @param {NPCTaskListJS} tasks   - A queue of tasks for the npc to perform  **/
function onNPCChat (player, npc, tasks) {
  if (player.team().status().get('caught_ocean_fish') == 1) {
    // Jakob says that he sees a team mate has managed to prove themselves
    // And therefore you do not need to anymore. To apologise for the trouble
    // He decides that he'll give you some additional bait
    tasks.add('say', 'sorry')
    .add('give_item', createStack('piscary:bait 0').setCount(5))
    .add('complete_quest')
  } else {
    var held = player.getHeldItem(main_hand)
    if (held.is('ore#fish')) {
      // Jakob says. I can see that you have a fish to show me but my records
      // do not show that you have actually fished from an ocean biome yet
      // Come back to me when you have completed this task!
      if (!fished) tasks.add('say', 'not_fished')
      else {
        // Jakob tells the player they did a great job with the fishing!
        // He informs them that he will now open his shop for them to make use of
        tasks.add('say', 'good_job')
        .add('set_team_status', 'caught_ocean_fish', 1)
        .add('complete_quest')
      }
    } else if (fished) {
      // I can see you have been fishing in the ocean. I can smell it on you!
      // Do you think you can hold what you caught so that I may inspect it?
      tasks.add('say', 'been_fishing')
    } else tasks.add('say', 'go_fishing') // Reminder
  }
}

/** Called when the player fishes up an item
 * We will use this to track that the player has in fact caught a fish
 * @param {PlayerJS}      player  - The player chatting with the npc
 * @param {ItemStackJS}   item    - The item stack that was fished up
 * @param {EntityJS}      hook    - The hook entity that caught the item **/
function onItemFished (player, item, hook) {
  if (!fished && item.is('ore#fish')
              && player.world().biome(hook.pos()).isType('OCEAN')) {
    fished = true
  }
}

/** Called to load data
* @param {DataJS} data  -  the tag to load data from **/
function loadData (data) {
  fished = data.load('fished', fished)
}

/** Called to save data
* @param {DataJS} data  -  the tag to save data to **/
function saveData (data) {
  data.save('fished', fished)
}
