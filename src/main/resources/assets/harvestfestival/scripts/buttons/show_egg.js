/** Called to setup initial button data
 * @param {NPCJS}     npc being interacted with
 * @param {PlayerJS}  player clicking the button
 * @param {Button}    button to initialize the data for **/
function setupButton (npc, player, button) {
  button.setName('name') // TODO: Make default?
  button.setIcon('small_egg')
}

/** If the button should currently be displayed
 * @param   {NPCJS}     npc being interacted with
 * @param   {PlayerJS}  player clicking the button
 * @return  {boolean}   true if button should display **/
function canDisplay (npc, player) {
  return player.status().get('showing_egg') == 1 && player.has('ore#egg')
}

/** Called when the button is clicked to perform an action
 * Ashlee will thank the player for the egg, she informs them
 * that they can keep it but as a well done she will give them
 * some chicken treats to use. She explains that they when
 * you feed animals varying combinations of normal treats and
 * species specific treats that they will produce better as well
 * as treating making them happier.
 * @param   {NPCJS}     npc being interacted with
 * @param   {PlayerJS}  player clicking the button **/
function onButtonPressed (npc, player) {
  npc.tasks(this_id, player)
  .add('set_player_status', 'showing_egg', 0)
  .add('say', 'thanks')
  .add('give_item', createStack('chicken_treat').setCount(5))
  .add('unlock_note', 'harvestfestival:animal_treats')
}
