/* The Harvest Goddess will ask for the player what their birthday is
   When she has this information fully confirmed then this quest is
   considered completed */

include('harvestfestival:quests/templates/default_quest')
include('harvestfestival:quests/templates/base_npc')

required_npc = 'harvest_goddess'

/* Display the info about this quest */
function display (info) {
	// Talk to the harvest goddess to set your birthday
  info.setIcon(settlements.getNPCIcon('harvestfestival:harvest_goddess'))
  .setTitle('hello')
  .setDescription('locate') // TODO: auto-localization
}

/* Interaction with the npc when interacted with */
function onNPCChat (player, npc, tasks) {
  var town = towns.get(player)
	// Hi there, i'm the harvest goddess the blablah of this land.
	// I have a story I would like to tell you but before i start
	// i'd to like just ask when you were born?
  tasks.add('say', 'when_born')
  .add('ask', 'season', spring.getUnlocalizedName() + '->setSeason', summer.getUnlocalizedName() + '->setSeason',
					           autumn.getUnlocalizedName() + '->setSeason', winter.getUnlocalizedName() + '->setSeason')
}

/** Set birthday functions **/
var season_id, day

// Set the birthday values for this player
function confirm (player, npc) {
  birthday_is_set = true
  player.status().set('birth_season', season_id)
  player.status().set('birth_day', day)
    // A) Oh that's wonderful. Thanks for setting your birthday
  var tasks = npc.tasks(this_id, player)
  tasks.add('complete_quest')
  .add('say', 'birthday_set')
  .add('next')
    // onRightClickedNPC(player, npc); //Continue with the quest //TODO: Call the interaction to java not this script?
}

function restart (player, npc) {
  npc.tasks(this_id, player).add('ask', 'season', spring.getUnlocalizedName() + '->setSeason', summer.getUnlocalizedName() + '->setSeason', autumn.getUnlocalizedName() + '->setSeason', winter.getUnlocalizedName() + '->setSeason')
}

function queueSetDay (player, npc) { npc.tasks(this_id, player).add('ask', 'birthday', 'back->restart', '1-7->setDay', '8-14->setDay', '15-21->setDay', '22-28->setDay') }

function setSeason (player, npc, option) {
  this.season_id = option
  var tasks = npc.tasks(this_id, player)
  if (option == 0) { tasks.add('say', 'spring.comment') } else if (option == 1) { tasks.add('say', 'summer.comment') } else if (option == 2) { tasks.add('say', 'autumn.comment') } else if (option == 3) { tasks.add('say', 'winter.comment') }
  tasks.add('ask', 'birthday', 'back->restart', '1-7->setDay', '8-14->setDay', '15-21->setDay', '22-28->setDay')
}

function setDay (player, npc, option) {
  var tasks = npc.tasks(this_id, player)
  if (option == 1) {
    tasks.add('ask', '', 'back->queueSetDay', '1->setDay1To7', '2->setDay1To7', '3->setDay1To7', '4->setDay1To7', '5->setDay1To7', '6->setDay1To7', '7->setDay1To7')
  } else if (option == 2) {
    tasks.add('ask', '', 'back->queueSetDay', '8->setDay8To14', '9->setDay8To14', '10->setDay8To14', '11->setDay8To14', '12->setDay8To14', '13->setDay8To14', '14->setDay8To14')
  } else if (option == 3) {
    tasks.add('ask', '', 'back->queueSetDay', '15->setDay15To21', '16->setDay15To21', '17->setDay15To21', '18->setDay15To21', '19->setDay15To21', '20->setDay15To21', '21->setDay15To21')
  } else if (option == 4) {
    tasks.add('ask', '', 'back->queueSetDay', '22->setDay22To28', '23->setDay22To28', '24->setDay22To28', '25->setDay22To28', '26->setDay22To28', '27->setDay22To28', '28->setDay22To28')
  }
}

function setBirthday (player, npc, day) {
  this.day = day // Day is set
  npc.tasks(this_id, player).add('ask', 'sure', 'yes->confirm', 'no->restart', '#', season.fromID(this.season_id).getUnlocalizedName(), day | 0) // # used to split off the methods from the formatting
}

// setBirthdayFunctions
function setDay1To7 (player, npc, option) { setBirthday(player, npc, option) }
function setDay8To14 (player, npc, option) { setBirthday(player, npc, 7 + option) }
function setDay15To21 (player, npc, option) { setBirthday(player, npc, 14 + option) }
function setDay22To28 (player, npc, option) { setBirthday(player, npc, 21 + option) }
