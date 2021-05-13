function onAnimalLoved (player, animal) {
 // TODO: Add this event call from JAVA
  if (!animal.hasTrait('carriable')) { // TODO: Add this function to animal scripting
    player.status().adjust('animals_petted', 1)
  }
}
