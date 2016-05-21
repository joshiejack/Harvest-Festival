#Who
- Y = Yulife
- G = Girafi
- J = Joshie

### To Do
- Register Vanilla Items and HFItems as gift types
- Make NPCs leave for work early, and leave for home early
- Add Schedules for NPCs, where they visit specific buildings at certain times
- Schedule Based Conversations
- Add Extra Chat Messages, and Conversations for all NPCs
- Add the mining Dimension - J
    - Add Breakable rocks that drop specific things on different floors when broken with a hammer
    - Different Hammers should be able to break different rock sizes, with different amount of hits
    - Would be cool to hold down the hammer with right click, which makes the player jump then do a smash instead of 'mining it'
    - Add Portal blocks to mines that teleport you to the dimension
    - Add Portal blocks that teleport you further down floors
    - Add Portal blocks that look like ladders that players can place, that will take you down one floor of the mine
    - Add an Overlay while in the dimension that tells you the name of the mine aka Spring Mine or Winter Mine
    - Add an Overlay that tells you what floor you are on
    - Add Free Standing ladders
    - Generate rooms in dimensions and keep track with the mine tracker
        - Different size and shaped rooms
- Make all cooking equipment render things that they are cooking
- Change Blueprints to not be a block but an item that you hover and click where you want them placed - J
- Coin Render on hotbar
- Add Axe Item - J
    - Basic (Breaks Small Twigs)
    - Copper (Breaks Medium Twigs)
    - Silver (Breaks Small Stumps)
    - Gold (Breaks Large Twigs)
    - Mystril (Breaks Medium Stumps)
    - Cursed/Blessed (Breaks Large Stumps)
    - Mythic (Fells whole trees)
- Add Basic Items to spawn that can be gathered (Expanded Later)- J
    - Rocks
        - Small - > Gives 1 Stone
        - Medium - > Gives 4 Stone
        - Large > Gives 10 Stone
    - Stumps
        - Small - > Gives 3 Wood
        - Medium - > Gives 6 Wood
        - Large > Gives 12 Wood
    - Breakable Twigs
        - Small > Gives 1 Wood
        - Medium > Gives 2 Wood
        - Large > Gives 4 Wood
- Change Ondra to Ashlee, make female - Y
- Loot Table JSON
    - See https://github.com/joshiejack/Harvest-Festival/blob/4fc81be862b9686cf00a3b8b1d262a8b7106aaaf/src/main/java/joshie/harvest/core/lib/LootStrings.java
    - See https://github.com/joshiejack/Harvest-Festival/blob/4fc81be862b9686cf00a3b8b1d262a8b7106aaaf/src/main/java/joshie/harvest/buildings/loader/HFBuildings.java#L51
- Change Items to Item Enums + Change Item Registry Helper
- Change Crops, Seeds, NPCS to use ForgeControlledNameSpace and use metadata instead of NBT where appropriate
- Add Sprinkler Block

###(S)ounds, (E)ffects, (A)nimations
- Frying Pan
    -S - Sizzling
- Pot
    -S Bubbling
    -E Bubbles
- Oven
    -S Fire Flicker
    -S Timer Ping when Done
    -A Open Door when placing stuff in
- Fridge
    -S Hummmm
    -E Open Door
- Counter
    -S Chopping
    -A Player Chopping
- Mixer
    -S Whirring
    -E Items Spin

### To Check
- Incubators will hatch chickens - J
- Can Spawn the Goddess by dropping relevant items in goddess water - J
- ASM For Snow - J
- Sickles do their jobs - J
- Troughs can be filled - J
- Cows and Sheep will eat grass crop and from trough automatically - J
- Animals will get sick if not fed for a few days and die - J
- Animals will give better products when cared for (brushing, talking, milking/shearing, treats) - J
- Animal Pregnancy is working correctly (Impregnanted via miracle potion, takes few days to hatch) - J
- All Cooking utensils work correctly - J
- All Recipes can be successfully made - J
- Stamina and Fatigue is doing the correct things - J
- Cursed Tools are giving cursed effects - J
- Gathering is saving

### To Fix
- Make Withered versions of crops be darker - J
- Location of the flower in the spring season overlay - Y
- Syncing Town Information to Players
- When you can purchase buildings from a town

### Rendering
- Make Blueprints Render in World
- Make Blueprints Item Render
- Make Spawn NPC Render the NPC
- Mine Floor Connected Textures and Random Texture Overlays - Y, J?
- Mine Wall Random Textures - Y
- Make Spawn Cow Render the Cow
- Make Spawn Sheep Render the Sheep
- Make Spawn Chicken Render the Chicken
- Mine Wall/Floor in inventory renders
- Goddess Water Texture
- Render Food/Liquids in Mixer correctly, render food on counter correctly - J

### Textures
- Crop Grass Better Texture - Y
- Farmland Texture (Wet and Dry), Maybe just a slightly darker version of vanilla?
- Textures for the NPCs which we stole + New Selection for Ondra > Ashlee - Y
- Optional (Entirely, Everything else is more important, this is just extra fluff if you want something to do ^_^)
    - These wild items: http://fogu.com/hm6/chan8/wilditems.php (Block and item form, since they have to be gathered)
    - Any meals that we could now make because of these gathering items
    - Anything from the mines (not the creatures of course e.g.)
    - http://fogu.com/hm6/chan5/mine4items.php
    - http://fogu.com/hm6/chan5/mine3items.php
    - http://fogu.com/hm6/chan5/mine2items.php
    - http://fogu.com/hm6/chan5/mine1items.php

### Models + Textures
- Rocks (Spring Mine (Ore Variation), Winter Mine Versions (Ore Variation), Overworld Rocks (Stone Only))
    - Large                                                
    - Medium                                               
    - Small                                                
- Branch - Y
    - Large
    - Medium
    - Small
- Stump - Y
    - Large
    - Medium
    - Small

### Questing Tasks
- Spawning the NPC Builder Quest
- Welcome to 'Harvest Festival' Quests
- Blacksmith Upgrade Tool Quests
- Priest Bless Tools Quests
- Unlock Mixer, Frying Pan, Pot, Oven for purchase quests
