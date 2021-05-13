function isBuiltOrUnderConstruction (player, building) {
  return isBuilt(player, building) || isBeingBuilt(player, building)
}

function isBeingBuilt (player, building) {
  var npc = settlements.getNearbyNPC(player, 'harvestfestival:yulif')
  return npc != null && npc.hasAction('build', building)
}

function isBuilt (player, building) {
  var town = towns.get(player)
  return town != null && town.hasBuilding(building)
}
