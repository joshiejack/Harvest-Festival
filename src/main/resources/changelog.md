#0.6.0
##!Important
* Removed a couple internal registries so it may warn you when joining a world, this can be safely ignored

##NEW!
###Fishing
* Added fishing rods, obtainable when you meet jakob for the first time

#Additions + Changes to existing features
###Animals
* Animals now only get stressed every two hours they're left outside in the rain or at night
* You will now get relationship points every two hours if an animal is outside in the day and it isn't raining
* You will now lose relationship points every time you use medicine on an animal
* You will now get more points from interactions with animals
* Adjusted animal product sell values to be more balanced
* Cows and sheep will now eat vanilla tall grass

###Buildings
* Optimised building save format, should be smaller size
* Only load buildings in to RAM when they're rendered/built
* Building costs increased slightly, buildings reshuffled order
* You will now unlock the church when you have nine npcs in town
* You will now unlock the townhall when you have built nine buildings
* Added the "festival grounds building", can be purchased as soon as you have built poultry farm or animal ranch or general store

###Calendar
* Summers are more likely to be sunny (98% vs 95%), and typhoon chances are higher if sun fails (99% vs 75%)

###Cooking
* Vanilla recipes will now show up in the cookbook
* Added a bunch of a new recipes
* Books will now have additional pages when you have a lot of recipes
* Food stats have been rebalanced entirely, they're now calculated based on the basic ingredients with a modifier for certain meals
* Sell costs for meals are now a bit lower and more rebalanced
* You can now cook multiple meals at once, the cooking system was rewritten to allow for smarter utensils (i.e. putting two corn in an oven will make two baked corn)

###Crops
* Sprinklers won't consume water on rainy days
* Untied the crops from the registry for them, shouldn't affect anything
* Melons and Pumpkins will no longer regrow, and will instead replace the crop
* Crops will now be plantable on more than farmland, however they will only grow on things other than farmland when a mod adds support
* Rebalanced the prices/sell value of all crops
* Removed the year unlocks, and replace with quest based ones instead
* Take note that once any player has completed the quest, the crops can be purchased by anyone in that town
    * You will now unlock strawberries when you build the goddess pond
    * You will now unlock cabbages when you have shipped 1000 spring crops, pineapples 1000 summer and green peppers 1000 autumn

###Gathering
* More items will now spawn around the world (that sell for more than 1 gold!)

###Mining
* Fixed cursed tools being unobtainable
* You can now access the mine as soon as you have built a general store
* There will now be some "hole blocks in the mine", you will need to right click these with a ladder to place one down, to go further down the mine
* Adjusted mine generation slightly, should only affect newly generated ones
* Added gems, some will drop from junk rocks, most will drop from the new gem ore
* Added Diamond, Emerald, Ruby, Amethyst, Topaz nodes to the mine
* Added random gem nodes (Only appear in winter)
* Ore nodes will now drop more than one more rarely
* Reduced the amount of dark chicks, chickens and sheep that spawn
* Mines will now have a greater chance of producing ore in winter
* Copper will now only spawn from floor 10 and below
* The new tiers will now start when you go through a bottom portal instead of just before

###Shops
* New Shop GUI
* Removed the Blacksmith's store
* Miner store now only sells ladders and escape ropes
* Moved the crop farming tools to the general store
* Moved the animal farming tools to the animal ranch
* Moved the sprinkler to the carpenters store
* Fodder now costs 100 gold, but will last for 10 feeds
* Chicken Feed now costs 50 gold, but will last for 10 feeds

###Tools
* Tools will have [BROKEN] in front of their name, when they can't be used
* Hammers will now be able to smash more tiers of rocks
* Fixed Hoes being half the durability they were meant to
* Tools will now reach 100% before you use them all the way

####Fixes
* Gifting npcs on their birthday will now only work once on smp, instead of over multiple days
* My hoes will now work with BOP blocks
* Made shop scroll behave better
* Add used to upgrade tooltip to all ores
* Fix missing crosshairs when exiting an npc gui

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
* Fix jade giving you turnips instead of tutorial turnips when doing the tutorial
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
