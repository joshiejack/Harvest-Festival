include('harvestfestival:quests/templates/default_quest')
include('harvestfestival:quests/templates/base_npc')

required_npc = 'jade' // The NPC that this quest applies to

function onNPCChat (player, npc, tasks) {
  tasks.add('say', 'hello')
  .add('set_npc_status', 'marriable', '1') // Mark Jade as a marriage candidate
        // Jade asks the player if they know how to make friends?
  .add('ask', 'friends', 'no->askToGatherFlower', 'yes->giveNotes')
}

function askToGatherFlower (player, npc) {
  var tasks = npc.tasks(this_id, player)
    // Jade explains that if you talk to a person each day, then you will build friendship with them
    // She also explains that if you ignore people for too long then your friendship will degrade over time
    // So if you want to remain friends with someone make sure to talk to them
  .add('say', 'chatting')
  .add('unlock_note', 'harvestfestival:chatting')
    // Jade then explains that if you really want someone to like you
    // then you can bring them gifts that they really like each day
    // She also explains that if you give people a gift on their birthday
    // they will like you even more, as well as on other special holidays
    // Jade then asks the player to go and get a flower to give to her
  .add('say', 'gather_flower')
  .add('complete_quest')
}

function giveNotes (player, npc) {
  var tasks = npc.tasks(this_id, player)
    // Jade says oh okay, then says she'll give the player some notes about just in case they need to brush up on their knowledge
  tasks.add('unlock_note', 'harvestfestival:relationships')
  .add('unlock_note', 'harvestfestival:chatting')
  .add('unlock_note', 'harvestfestival:gifting')
  .add('say', 'okay')
  .add('complete_quest')
  .add('complete_quest', 'gifting')
}
