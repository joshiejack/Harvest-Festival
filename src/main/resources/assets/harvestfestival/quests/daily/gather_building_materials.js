/** Called to setup specific information about the the quest

 * @param {Settings}      settings  - The settings object to manipulate  **/
function setup (settings) {
  settings.setType('team')
  settings.setDaily()
}

var materials = ['logWood', 'stone', 'blockGlass']
var count
var material

/** Called when this task is selected as a daily quest.
    We will use this time to setup the random targets for the quest **/
function onTaskCreation () {
  count = random(1, 6) * 10
  material = materials[random(0, 2)]
}

/** Called when looking at the quest board to display information about
 *  the task at hand. Make use of saved variables to make this dynamic
 * @return {String} title of the task, it should be a title of a job or something simple
 *                it is always followed by the word "Required" so it needs to make sense
 *                                     - Monster Slayer
 *                                     - Angler
 *                                     - Mailman
 *                                     - Lumberjack
 *                                     - Help        **/
function getTaskTitle () {
  return material === 'logWood' ? 'Lumberjack' : material === 'tone' ? 'Stonecutter' : 'Glassmaker'
}

/** Called when looking at the quest board to display information about
 *  the task at hand. Make use of saved variables to make this dynamic
 * @return {String} description of the task at hand, make sure to include all details       **/
function getTaskDescription () {
  return 'I am looking for someone to help collect ' + count + ' ' + material + '. Come and see me when you are done!\n\n-Yulif'
}

/** Called to load data regarding this quest
* @param DataJS data the tag to load data from **/
function loadData (data) {
  count = data.load('count', 10)
  material = data.load('material', 'logWood')
}

/** Called to save data regarding this quest
* @param DataJS data the tag to save data to **/
function saveData (data) {
  data.save('count', count)
  data.save('material', material)
}
