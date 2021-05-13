include('harvestfestival:quests/templates/base_npc')
include('harvestfestival:scripts/includes/relationship_status')

required_npc = 'harvest_goddess'
prereq = '0_blueprints'

function canStart (player, scripts) {
  var status = settlements.status('harvestfestival:harvest_goddess')
  return status.get(player, 'gift_streak') >= 14 && hasPrereq(script)
}

/* Interaction with the npc when interacted with */
function onNPCChat (player, npc, tasks) {
  var town = towns.get(player)
	// Hi there, i'm the harvest goddess the blablah of this land.
	// I have a story I would like to tell you but before i start
	// i'd to like just ask when you were born?
  tasks.add('say', 'text')
  .add('ask', 'gender', 'boy->setGender', 'girl->setGender')
}

var boys = ['liam', 'luke', 'nathan', 'trent'] // No benji or brandon as they are "special"
var girls = ['alicia', 'candice', 'jade', 'nicole'] // no cloe or goddess as they are "special"
var h = 'harvestfestival.npc.'
var n = '.name'

var gender, crush

function confirm (player, npc) {
  adjustNPCRelationship(tasks, crush, 5000)
  .add('complete_quest')
  .add('say', 'birthday_set')
  .add('next')
}

function restart (player, npc) {
  npc.tasks(this_id, player).add('ask', 'gender', 'boy->setGender', 'girl->setGender')
}

function formatName (name) {
  return h + name + n
}

function setGender (player, npc, option) {
  this.gender = option
  var tasks = npc.tasks(this_id, player)
  var array = option == 0 ? boys : girls
  if (option == 0) { tasks.add('say', 'boy.comment') } else if (option == 1) { tasks.add('say', 'girl.comment') }
  tasks.add('ask', 'who', 'back->restart', formatName(array[0]) + '->setNPC', formatName(array[1]) + '->setNPC',
                                           formatName(array[2]) + '->setNPC', formatName(array[3]) + '->setNPC')
}

function setNPC (player, npc, option) {
  var array = gender == 0 ? boys : girls
  this.crush = array[option]
  npc.tasks(this_id, player).add('ask', 'sure', 'yes->confirm', 'no->restart', '#', formatName(array[crush])) // # used to split off the methods from the formatting
}
