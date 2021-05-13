var potential_mobs = ['minecraft:zombie', 'minecraft:skeleton', 'minecraft:spider', 'minecraft:slime', 'minecraft:magma_cube', 'harvestfestival:frost_slime']
var target_count = 10
var target_entity = 'minecraft:zombie'
var killed = 0

function setup (settings) {
  settings.setType('team')
  settings.setDaily()
}

/** Called to initiate the targets for this script.
    This is called on the main quest, not the individual ones. **/

function getTaskTitle() {
    return "Monster Slayer"
}

function getTaskDescription () {
  return 'Kill ' + target_count + ' ' + target_entity
}

function onTaskCreation () {
  target_count = random(15, 25)
  target_entity = potential_mobs[random(0, 5)]
  killed = 0 // Reset the counter
}

function onEntityKilled (player, entity) {
  if (killed < target_count) {
    if (entity.is(target_entity)) {
      killed = kislled + 1
      if (killed >= target_count) {
                //
      }
    }
  }
}

/** Called on the main script when started via quest board
    in order to copy the data to the localised script version. **/
function saveData (data) {
  data.save('target_count', target_count)
  data.save('target_entity', target_entity)
  data.save('killed', killed)
}

/** Called when the script is started via quest board as well as normally **/
function loadData (data) {
  target_count = data.load('target_count', target_count)
  target_entity = data.load('target_entity', target_entity)
  killed = data.load('killed', killed)
}
