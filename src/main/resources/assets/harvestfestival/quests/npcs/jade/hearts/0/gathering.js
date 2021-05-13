include('harvestfestival:quests/templates/base_npc')

required_npc = 'jade' // The NPC that this quest applies to
prereq = '0_gifting'

function onNPCChat (player, npc, tasks) {
  // TODO: In this quest Jade will ask you if you know how to make money?
  /*
       If the player says:
        # YES -> Jade will say that's wonderful, let me give you a couple things to help you get started
                 She then gives you the notes for gathering/shipping mechanics and the shipping bin blueprint

                 #COMPLETE_QUEST
        # NO -> Jade explains how each day valuable items will grow around the town and how this varies depending
                upon the season. She will explain how you can make money from this using a shipping bin.
                She then give the player the gathering notes and a blueprint/note for the shipping

                # COMPLETE_QUEST
  */

  tasks.add('say', 'thanks')
  .add('ask', 'shipping', 'no->explain', 'yes->skip')
}

function giveNotes (tasks) {
  tasks.add('unlock_note', 'harvestfestival:wilderness')
  tasks.add('unlock_note', 'harvestfestival:shipping')
  tasks.add('unlock_blueprint', 'harvestfestival:shipping_bin')
  tasks.add('complete_quest')
}

function explain (player, npc) {
  giveNotes(npc.tasks(this_id, player).add('say', 'explain'))
}

function skip (player, npc) {
  giveNotes(npc.tasks(this_id, player).add('say', 'skip'))
}
