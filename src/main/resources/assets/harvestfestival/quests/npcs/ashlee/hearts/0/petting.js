include('harvestfestival:quests/templates/base_npc')
include('harvestfestival:scripts/includes/town')

required_npc = 'ashlee'
prereq = '0_chicken'

/** Called when the player talks to the 'required_npc'

  If the player has thrown one animal then we can considered
  this task as having been completed. Whether it was the correct
  chicken or not it doesn't matter as they will have established
  that they know how to love animals by 'throwing' them anyways.

  SPEECH: If completed, Ashlee will tell the player good job and then
  will start teaching them about how you can feed animals by hand.
  She then gives the player 1 chicken feed to perform the task.
  If the player wants more there will be a button to claim it. //TODO: BUTTON (if player not completed petting)
 * @param {PlayerJS}      player chatting with the npc
 * @param {NPCJS}         npc being interacted with
 * @param {NPCTaskListJS} task list for the npc **/
function onNPCChat (player, npc, tasks) {
  if (player.status().get('animals_carried' > 0)) {  // TODO: Track How many animals are carried by the player
    tasks.add('say', 'finish')
    .add('unlock_note', 'harvestfestival:feeding_by_hand')
    .add('give_item', createStack('bird_feed'))
  } else tasks.add('say', 'reminder')
}
