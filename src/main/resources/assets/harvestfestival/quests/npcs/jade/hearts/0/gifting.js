include('harvestfestival:quests/templates/base_npc')

required_npc = 'jade' // The NPC that this quest applies to
prereq = '0_welcome'

function onNPCChat (player, npc, tasks) {
  // If the player hasn't gifted jade yet today, then she'll tell them how to
  if (npc.status().get(player, 'has_gifted') == 0) {
      // If the player is holding a flower, jade will explain how to gift it to her
    var held = player.getHeldItem(main_hand)
    var category = gifts.category(held)
    if (category.name() === 'flower') {
          // Jade explains how to gift the flower to her by clicking on the button
          // She tells the player to talk to her again after they have clicked the button
      tasks.add('say', 'gift_button')
    } else {
          // Jade reminds the player that she would like a flower so she can teach them how to gift
          // reminding them to hold it to show her so that she can see it
          // But she also says that if they know how, any other gift will do
      tasks.add('say', 'reminder')
    }a
  } else {
      // Jade notices the player has gifted her so she thanks them
    tasks.add('unlock_note', 'harvestfestival:gifting')
    .add('say', 'thanks')
          // Jade explains that you can see your relationship levels to people in the book
          // as well as whether you have gifted them or talked to them that day, if you need a reminder
    .add('unlock_note', 'harvestfestival:relationships')
    .add('complete_quest')
    .add('next')
  }
}
