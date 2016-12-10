##0.5.20
* Fix animals not giving birth
* Fix ketchup being uncookable

##0.5.19
* Fix potential crash when breaking double blocks
* Fix potential crash when being killed while gifting an item

##0.5.18
* Fix bugs accidentally introduced aka...
* Fix farmland not dehydrating each day
* Fix blacksmith not upgrading tools

##0.5.17
* Happy New Year!
* Fix a potential crash with farmland when it's raining (after it turned to dirt!)

##0.5.16
* Fix missing localisation for some CT support

##0.5.15
* Add CraftTweaker support for adding shops to npcs that have none already
* Add CraftTweaker support for removing items from shops
* Add CraftTweaker support for adjusting values of items in shops
* Add /hf items [shopid] command that lists the purhasable ids
* Add /hf npcs command that lists the npc ids
* Move /hf npc to /hf findnpcs
* Changed the id system for shops, ignore the warning about missing registries
* Changed how the hoe charges up and allow it to work with other mods soil
* Support for planting my crops/no moisture on soil that extends BlockFarmland
* Fix crosshairs randomly dissappearing
* Fix applecore support for chocolate and riceballs
* Fix world time not updating when reloading a server
* Fix jade still giving you seeds after the general store is built
* Fix building render bug when mod is used in conjunction with Psi
* More reliable teleports to correct locations in the mine
* Only check the date on seeds if they require more than a year
* Potentially fix the chick apocalypse?

##0.5.14
* Will no longer tick towns and animals if the chunks they're in are unloaded
* Yulif will now build whenever his stuck timer reaches max
* Yulif will now teleport faster if he hasn't moved at all

##0.5.13
* Fixed potential crashes when loading player data in specific circumstances
* Fixed Blacksmith saying he'll take zero more days when finishing repairing tools

##0.5.12
* Fixed a potential crash with seed drops
* Doubled the amount of fodder that is stored in troughs
* Made chickens lay eggs more reliably

##0.5.11
* Fixed a crash with the shipping bin when loading worlds when a hopper is inserting items
* Fixed removing blocks that aren't snow in winter
* Fixed jim not reminding you to feed your cow by hand during the tutorial
* Fixed accidentally giving xp on the client side
* Fixed yulif always saying that he's working on the basement
* Fixed the sign on yulif's building to have the correct opening hours
* Added a bunch of random greetings to npcs
* Added config options that disable drops from grass
* Added config options that stop vanilla crops from dropping, growing or being harvestable
* Added a config option for 12 hour clock (by TehNut)

##0.5.10
* Changed rewards of certain quests seeds to give different crops based on seasons
* Fixed jade not giving sickle for shears
* Allow double flowers to count as flowers
* Removed sneak clicking to gift (use the icon)
* Can now sneak click jade to trade for up to 10 seeds a time (when she trades for them)
* Jade will only remind you about flowers when she still has the quest available
* Items in the offhand now count towards costs, and will be taken over items in the main inventory
* Quests that required you to hold all the items before, will now check for one of the items being held
  but they will take from everywhere in the inventory, prioritising what you are holding first. So for example
  you can now have five of any flowers.

##0.5.9
* Fix bug with JEI loading on npc gui
* Fix goddess asking for 64 logs instead of a dozen
* Fix jade giving you turnips instead of hardy turnips when doing the tutorial
* Fix yulif not updating his text when a building is finished

##0.5.8
* Fix crash when attempting to make some recipes in the mixer

##0.5.7
* Improve the removal of snow.
* If you notice lag, feel free to turn off "Remove snow faster" config option
* Validate some things before syncing player stats
* Make jade check for flower ore dictionary prefix
* Add a few ore dictionary registrations
* Added name tags to the animal ranch/poultry farm
* Fix bug where npcs don't gift recipes
* Fix the beetroot state handler
* Fix melons and pumpkins being unsellable
* Fix render of certain items in the recipe book
* Fix blood mage question button
* Fix torches able to block entering a mine

##0.5.6
* Save the mine portal locations to the dimension, not level.dat
* Increased the chicken/sheep/cows health
* Fixed some potential crashes with crops and also with crops returning
* Fixed vanilla crops not changing colours when dying
* Fixed inconsistent result of capability handler
* Fixed gifting your last item leaving a ghost stack of size 0
* Fixed decorative dirt breaking instantly
* Fixed the command /hf returning empty line
* Fixed Blast resistance of mine dirt
* Fixed bed restoring hunger without actually sleeping

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
