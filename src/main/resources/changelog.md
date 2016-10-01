##0.5.5
* Fixed too much winter fog showing up underground
* Fixed how two tall crops render when breaking the bottom block
* Fixed formatting errors causing crashes
* Properly fixed not being able to skip the intro quest
* Make entities avoid water if possible
* Added Support for IE Hemp as a test

##0.5.4
* Added config option for giving the cheat buildings instead of blueprints
* Added a hardy turnip crop, that takes three days to grow and can grow in any season (Only available in the tutorial)
* Added tooltip to animal spawners
* Added days to grow tooltip to seeds
* Added command to dump list of shops for CraftTweaker purposes (/hf shops)
* Added command that makes all recipes learnt/unlearnt
* Added signs to buildings that display the shops opening times
* Added buttons to the npcs chat, that allow you to ask for opening hours, or to gift them.
* Added sounds when builder places blocks
* Added simplified chinese localisation (credit to Neko)
* Added command for setting a tools level (/hf tool [player] <level>)
* Change the colour of leaves/grass for all seasons
* Reverse the logic for crops being shippable. This may make existing crops unsellable.
* Jade will trade shears for sickles, along with the buckets/hoes up until you get the blacksmith
* Jade will no longer trade for seeds once you have the general store
* Make Goddess flower spawning work with a larger range of plants
* Halve the amount of time the goddess takes to spawn from a flower
* Change flower sound to plant
* Now registering all cropEggplant etc as acceptable ingredients
* Watering cans in creative are now filled
* Improved npc schedule ai slightly
* Improve builder ai slightly
* Reduce the build time of buildings ever so slightly
* Made buildings ungiftable
* Clicking doors, gates, trapdoors, buttons or levers won't drop chickens
* Yulif will now tell the player what he is building
* Will no longer create a yulif when spawning in npcs
* Hide [BuildingGen] items unless debug mode is on
* Removed tools with 100% from creative
* Fixed the goddess flower never despawning if dropped on land
* Fixed you not being able to skip the carpenter quest
* Fixed pumpkin/melon renders
* Fixed grass only giving one fodder
* Fixed grass being destroyed when cut
* Fixed grass being purple when it dies
* Fixed potential crashes when a block tries to get my crops state
* Fixed that a few achievements required itself to be unlocked
* Fixed nests always producing small eggs for loved chickens
* Fixed cows and some other entities having transparent eyes
* Fixed café having anvil where it shouldn't
* Fixed the missing side and broken basement for the general store
* Fixed hammers not leveling when breaking rocks normally
* Fixed watering cans not being repairable
* Fixed sky in custom biomes, now blending it in with my colour instead
* Fixed toggledownfall not working with HFs weather
* Fixed jade taking turnips, when she says she won't
* Fixed first- and third person render of rocks/branches
* Fixed missing localisations for proposals
* Fixed that indoor farmland was being set to wet on a rainy day
* Fixed /hf time being able to set the worlds time to negative

##0.5.3
* Jade will now swap hoes/buckets for hoes/watering cans from after the tutorial until you complete the blacksmith tutorial
* My tools can no longer be gifted to npcs
* If animal spawning is disabled, the carpenter will sell beds
* Fix jade not giving you seeds sometimes
* Fix side related errors when displaying chat
* Fix the builder/goddess always reminding you to build things
* Actually fix potential crashes with mods that are calling out of bound ids? We'll see...

##0.5.2
* Fix Jade's demonic eyes
* Fix the render of the mine blocks
* Fix Jade not taking flowers in the crops tutorial
* Fix crash when exploding gathering blocks/ores
* Fix potential crashes with npcs models and certain mods
* Fix potential crashes with mods that are calling out of bound ids
* Fix potential crash when a crash happens when posting a crash report
* Fix potential crash when breaking rocks in the mine
* Null check fonts, in case something goes awry
* Fix Frying Pan/Pan/Mixer taking items when they can't be used, now also displays an error message
* Reduce the lag when changing position when trying to place buildings

##0.5.1
* Fix crash with builder
* Fix crash with quests

##0.5.0
* Initial Release