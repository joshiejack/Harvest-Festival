include('harvestfestival:scripts/includes/globals')

/** Adjust the NPCs relationship status
* @param {NPCTaskListJS} tasks    - A queue of tasks for the npc to perform
* @param {string}        npc_name - The registry name of this npc
* @param {integer}       amount   - The amount to adjust the relationship by  **/
function adjustNPCRelationship (tasks, npc_name, amount) {
  return tasks.add('adjust_npc_status', 'harvestfestival:' + npc_name, 'relationship', amount, 0, MAX_RELATIONSHIP)
}
