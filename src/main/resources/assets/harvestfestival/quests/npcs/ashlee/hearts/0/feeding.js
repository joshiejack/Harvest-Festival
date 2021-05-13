include('harvestfestival:quests/templates/base_npc')

required_npc = 'ashlee'
prereq = '0_petting'

var fed = false

/** Called when the player talks to the 'required_npc'

  Ashlee is waiting for the player to feed the chicken
  with one piece of bird feed. On completion she explains
  that you can also place feed in the feeding tray and
  that the chickens will go and collect food when they
  are hungry by themselves. Doing it this way has no
  negative effect. She gives the player the feeding
  tray note and the nest note. //TODO: Shop should be accessible NOW
 * @param {PlayerJS}      player chatting with the npc
 * @param {NPCJS}         npc being interacted with
 * @param {NPCTaskListJS} task list for the npc **/
function onNPCChat (player, npc, tasks) {
  if (!fed) {
    tasks.add('say', 'reminder')
  } else {
    tasks.add('say', 'well_done')
    .add('unlock_note', 'harvestfestival:feeding_tray')
    .add('unlock_note', 'harvestfestival:nest')
    .add('set_player_status', 'showing_egg', 1)
    .add('complete_quest')
  }
}

/** Called when the player interacts with an entity
 * @param {PlayerJS}  player that is doing the interacting
 * @param {EntityJS}  entity that is being interacted with
 * @param {Hand}      hand one either main_hand or other_hand **/
function onEntityInteract (player, entity, hand) {
  if (!fed && player.isHolding('chicken_feed', hand)
                  && entity.is('minecraft:chicken')) {
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
