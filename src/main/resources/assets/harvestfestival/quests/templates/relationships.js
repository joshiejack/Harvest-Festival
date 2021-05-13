var required_npc = 'unknown'
var required_hearts = 0
var prereq = 0
var reward

function setup (settings) {
  if (required_hearts == 0) { settings.setDefault() }
}

function canStart (player, scripts) {
  if (required_hearts == 0) {
    return true
  } else {
    return scripts.completed(getNPC().replace(':', ':npcs_') + '_hearts_' + prereq)
  }
}

function getNPC () {
  return 'harvestfestival:' + required_npc
}

function getReward () {
  return undefined
}

function onRightClickedNPC (player, npc, hand) {
  if (npc.is(this.getNPC()) && (required_hearts == 0 || npc.status().get(player, 'relationship') >= required_hearts * 5000)) {
    var tasks = npc.tasks(this_id, player)
    tasks.add('say', 'text')
    reward = getReward()
    if (reward != undefined) {
      tasks.add('gift', reward)
    }
  }
}
