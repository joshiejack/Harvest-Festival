include('harvestfestival:scripts/includes/globals')

var required_npc
var prereq

function canStart (player, scripts) {
  if (prereq == undefined) {
    return true
  } else {
    return scripts.completed(getNPC().replace(':', ':npcs_') + '_hearts_' + prereq)
  }
}

function onRightClickedNPC (player, npc) {
  if (npc.is(getNPC())) {
    onNPCChat(player, npc, npc.tasks(this_id, player))
  }
}

function getNPC () {
  return 'harvestfestival:' + required_npc
}

/** Adjust the NPCs relationship status
* @param {NPCTaskListJS} tasks    - A queue of tasks for the npc to perform
* @param {integer}       amount   - The amount to adjust the relationship by  **/
function adjustRelationship (tasks, amount) {
  return tasks.add('adjust_npc_status', 'harvestfestival:' + required_npc, 'relationship', amount, 0, MAX_RELATIONSHIP)
}
