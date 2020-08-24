#0.6.0
##!Important
* Removed a couple internal registries so it may warn you when joining a world, this can be safely ignored
* Changed default server length from 365 to 300, for calendar related reasons, recommended you do so too

#Additions + Changes to existing features
###Animals
* Animals now only get stressed every two hours they're left outside in the rain or at night
* You will now get relationship points every two hours if an animal is outside in the day and it isn't raining
* You will now lose relationship points every time you use medicine on an animal
* You will now get more points from interactions with animals
* Adjusted animal product sell values to be more balanced
* Cows and sheep will now eat vanilla tall grass
* Cleaning will now stop working when the animal is fully cleaned
* The Brush will only take damage when you've fully brushed the animal
* Brushing takes less hunger and less time
* Feeding animals incorrect treats or hitting them will cause a relationship penalty
* Changed relationship maximum to 27,000RP
* Animals relationship are now treated more like happiness. They are tied to the animal instead of the player. This means you'll get the larger products no matter who milks a cow for example. Just as long as they're happy.
* Cows, Chickens and Sheep should be a little smarter, and try to find a location that's inside when it's raining, night or winter, and go outside when its day, sunny and not winter
* Animals will now search for feeding trays/troughs/feeding trays within a 64 block radius, so ensure they can access them
* Change collision of nests and feeding trays
* Fodder will now visually show how much is in the trough rather than just full or empty
* The way size of animal products is redesigned. It's now a random chance for larger products, with the higher relationship increasing this chance.
* You'll start getting medium products at three hearts and large at six hearts
* Chickens will now be invulnerable while riding a players head
* Cows and sheep will become sick less frequently

###Buildings
* Optimised building save format, should be smaller size
* Building costs increased slightly, buildings reshuffled order, some cost other things
* Removed the basement from the general store as it caused confusion
* You will now unlock the church when you have nine npcs in town
* You will now unlock the townhall when you have built nine buildings
* Added the "park building", can be purchased as soon as you have built poultry farm or animal ranch or general store, it's intended to be used for festivals
* Added demolish to the carpenters 'sell' menu. Will destroy that building.

###CraftTweaker
* Add support for blacklisting hoes/seeds/items being giftable
* Add support for adding modded crops

###Cooking
* Vanilla recipes will now show up in the cookbook
* Added a bunch of a new recipes
* Books will now have additional pages when you have a lot of recipes
* Food stats have been rebalanced entirely, they're now calculated based on the basic ingredients with a modifier for certain meals
* Sell costs for meals are now a bit lower and more rebalanced
* You can now cook multiple meals at once, the cooking system was rewritten to allow for smarter utensils (i.e. putting two corn in an oven will make two baked corn)
* Ingredients can be used to cook recipes directly from the fridge

###Crops
* Added Old Sprinkler, waters a 3x3 area available from the Carpenter from Summer 7
* You now unlock the normal sprinkler by becoming friends with Yulif
* Sprinklers won't consume water by default, can be reenabled in config
* Sprinklers will only animate for 15 minutes in the morning
* Sprinklers will only water crops in the morning, not as soon as you put water in them
* Untied the crops from the registry for them, shouldn't affect anything
* Melons and Pumpkins will no longer regrow, and will instead replace the crop
* Crops will now be plantable on more than farmland, however they will only grow on things other than farmland when a mod adds support
* Rebalanced the crop prices and changed some stats
* Changed how/when some crops are unlocked
* Take note that once any player has completed the quest, the crops can be purchased by anyone in that town
* When crops die, they chance a chance to turn in to weeds, wood, or rocks
* You can now break dead crops normally, no need to use a sickle
* Added a 'shipping basket', you wear it on your head and it will autoship things you pick up
* Nether Wart is no longer an obtainable HF crop. I'll re-add it when I add greenhouses. (Vanilla works again)
* Industrial Hemp is no longer a HF crop. Was slightly buggy. Weill re-add when I rewrite how crops work.

###Festivals - NEW!
* Added new years festival, celebrate with food
* Added cooking festival, celebrate your cooking skills. Winners will have cooked meals sell for 1.1x more.
* Added cow festival, winners cows will will always produce large milk!
* Added chicken festival, winners chickens will always produce large eggs!
* Added harvest festival, make friends with the villagers by contributing your best to a communal soup
* Added sheep festival, winners sheep will always produce large wool!
* Added starry night festival, celebrate the winter with your loved ones
* Added new years eve festival, celebrate with a meal then watch some fireworks to bring in the new year

###Fishing - NEW!
* Added fishing rods, obtainable when you meet Jakob for the first time
* Added various fish and junk that can be caught
* Different fish per season and water type (OCEAN, RIVER, LAKE, POND)
* Added bait, attach to your rods to increase speed
* Added fish trap, seed with bait to catch fish/junk
* Added hatchery, will duplicate fish

###Fruit Trees - NEW!
* Added fruit trees. The ones added are:
  * Apple (Unlocks Autumn year 2)
  * Banana (Unlocks Summer year 3)
  * Grape (Unlocks Autumn year 3)
  * Orange (Unlocks Summer year 2)
  * Peach (Unlocks Summer year 3)
* Fruit tree seeds can be bought in the general store

###Gathering
* More items will now spawn around the world (that sell for more than 1 gold!)
* Items will no longer spawn inside of towns, they will only spawn from 32 to 128 blocks away
* Added support so that blacklisting seeds works for BOP Short/Medium Grass etc.
* Added support so that gathering items will spawn on BOP Grass

###Knowledge - NEW!
* Added a knowledge book, things you can find inside are:
  * Collections for Shipping, Fishing, Mining and Cooking
  * Relationship Data for Animals and NPCs
  * Notes page, where everything you learn from NPCs is kept track of
  * Quest page, where you can see what quests you are currently working on
* Added a calendar! The clockmaker will give you one when you meet him
  * Can see the birthdays of all the npcs you've met as well as the dates festival are 'expected' to occur (you'll receive letter day before)
* Added Mailbox
  * NPCs will send letters to invite you to start festivals

###Mining
* Fixed cursed tools being unobtainable
* You can now access the mine as soon as you have built a general store
* There will now be some "hole blocks in the mine", you will need to right click these with a ladder to place one down, to go further down the mine
* Adjusted mine generation slightly, should only affect newly generated ones
* Added gems, some will drop from junk rocks, most will drop from the new gem ore
* Added Amethyst nodes to the copper and greater levels of the mine
* Added Topaz nodes to the silver and greater levels of the mine
* Added Emerald and Ruby nodes to the gold and greater levels of the mine
* Added Diamond nodes to the mystil and greater levels of the mine
* Added Random gem nodes (Only appear in winter) to the copper and greater levels of the mine
* Added Elevators for the mine. Allows you to teleport down to deeper depths instantly (for a setup cost!)
* Ore nodes will now drop more than one more rarely
* Reduced the amount of dark chicks, chickens and sheep that spawn
* Dark chicks will drop black feathers, Dark chickens will drop black feathers and raw chicken, Dark sheep will drop mutton and black wool, and Dark cows will drop black leather and beef
* Mines will now have a greater chance of producing ore in winter
* The new tiers will now start when you go through a bottom portal instead of just before

###NPCs
* Blacklisted damageable items by default, unless a gift type is assigned to them
* NPCs will now like or dislike more things than before
* Added a few more quests mostly related to friendship with the npcs, that give you things like secret shops or secret notes!
* Throwing an item in to the goddess pond will now gift her that item, and summon her (instead of just adding RP) (So be careful)
* Gifting NPCS gifts they don't like won't make them hate you so much
* Changed relationship maximum to 50,000RP, as well as the amount of RP you get for each gift type
    * Awesome from 800 to 1000
    * Good from 400 to 500
    * Decent from 300 to 200
    * Dislike from -400 to -200
    * Bad from -600 to -400
    * Terrible from -1000 to -800
* Disabled the blue feather until i do marriage properly, there's no benefit and it didn't really even work properly
* Jade will now only give you up to 10 seeds every 5 days
* Jade will now sometimes give you hardy turnip seeds instead of a normal crop
* Various npcs have had their schedule changed, so they may be found at different buildings at different times
* Blacksmith will only start work on tools during operating hours.
* Blacksmith will take four days to upgrade tools if you have him start work on tuesday or wednesdays as he rests on thursdays.
* Blacksmith prices for upgrades have increased. Copper was cheaper than a new tool before...
* Can now right click to go back to the previous chat page
* NPCS will teleport when taking damage from being in a wall

###Quests
* Added a quest board, which comes when you have the townhall, you can purchase a new one from carpenter when townhall is built
* Added random delivery and slaying quests available from the quest board object

###Shops
* New Shop GUI
* Removed the Blacksmith's store
* Miner store now sells ladders and escape ropes, and will also sell copper, silver and gold once you have mined some
* Moved the crop farming tools to the general store
* Moved the animal farming tools to the animal ranch
* Moved the sprinkler to the carpenters store
* Fodder now costs 100 gold, but will last for 5 feeds
* Chicken Feed now costs 50 gold, but will last for 10 feeds
* You are now able to sell some items to the stores, but only a certain amount each day
* Shops are now able to support a "stock value", aka how many items you can buy of this item each day
* You no longer need to purchase a counter for the mixer to be unlocked for purchase
* Added some secret unlockable shops
* Added a shop to johan for his trades, instead of using the quest system, he also nows sells coins, which ship for their purchase value
* Added a daytime clockmaker shop to tiberius, selling clocks, calendars, maps, compass
* Added a shop to jakob, selling the new fish stuff
* Poultry Farm is now open from 6am-12pm instead of 5am-11am

###Tools
* Tools will have [BROKEN] in front of their name, when they can't be used
* Hammers will now be able to smash more tiers of rocks
* Rebalanced max damage
    * Basic = 128
    * Copper = 256
    * Silver = 768
    * Gold = 1152
    * Mystril = 3456
    * Cursed = Gold
    * Blessed = 6912
    * Mythic = 13824
* Tools will now reach 100% before you use them all the way
* Change Basic tools repair material to Junk Ore
* Hammers will now mine larger areas of stone
* Axes will now chop down entire trees
* Sickles will now break large areas of grass
* Certain tiers of watering can have had their ranges changed (mostly++)
* Added config option to prevent respawning at a bed when you are starving
* Added config option to kill the player instead of respawning at the bed
* Added config option for fainting when the exhausted timer is expiring (changed default to false)

####Fixes
* Gifting npcs on their birthday will now only work once on smp, instead of over multiple days
* Made shop scroll behave better
* Add used to upgrade tooltip to all ores
* Fix missing crosshairs when exiting an npc gui
* Fixed sickles always breaking blocks even when broken
* Fixed bug where fodder could be placed in a fridge
* Fix chicks spawning in random locations around the incubator
* Fix milkers and brushes being stackable
* You'll know spawn at the bottom of the mining hill stairs
* Fixed NPC spawner
* Fixed NPCs not always respawning next day, if they died
* Fixed mouseover in shops showing the wrong item just bought, in certain cases
* Fixed hunger not being refilled when sleeping, when Morpheus is installed on a server
* Other minor fixes and changes

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
