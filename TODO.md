#Who
- Y = Yulife
- G = Girafi
- J = Joshie

### To Do
- Register Vanilla Items and HFItems as gift types
- Make NPCs leave for work early, and leave for home early
- Add Schedules for NPCs, where they visit specific buildings at certain times
- Schedule Based Conversations
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
- Make the rotation of Blueprints make more sense - J
- Change Blueprints to not be a block but an item that you hover and click where you want them place - J
- Add Extra Chat Messages, and Conversations for all NPCs
- Coin Render only in cities
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
- Change Meals to use ResourceLocation Identifiers
- Change NPCs to use ResourceLocation Identifiers
- Change Items to  Item Enums + Change Item Registry Helper
- Change Seeds, Buildings etc to use ForgeControlledNameSpace and use metadata instead of NBT where appropriate
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
- NPCs walk home, if they fail they should teleport - J
- Incubators will hatch chickens - J
- Can Spawn the Goddess with the Flower Pot Trick - J
- Can Spawn the Goddess with the Goddess Flower Trick - J
- Can Spawn the Goddess by dropping relevant items in goddess water - J
- All Crops render correctly - Y
- All Crops grow correctly - J
- All Crops drop the correct items - J
- ASM For Snow - J
- Hoes, Sickles, Watering Cans do their jobs - J
- Troughs can be filled - J
- Cows and Sheep will eat grass crop and from trough automatically - J
- Animals will get sick if not fed for a few days and die - J
- Animals will give better products when cared for (brushing, talking, milking/shearing, treats) - J
- Animal Pregnancy is working correctly (Impregnanted via miracle potion, takes few days to hatch) - J
- Buying Entities works correctly - J
- All Cooking utensils work correctly - J
- All Recipes can be successfully made - J
- Stamina and Fatigue is doing the correct things - J
- Cursed Tools are giving cursed effects - J

### To Fix
- Rendering of Shops - J
- NPC Chat not closing after conversation is finished - J
- Make Withered versions of crops be darker - J
- Make crops get destroyed on losing the block below - J
- Location of the flower in the spring season overlay - Y
- Give the custom farmland block a name and texture/model - Y
- Syncing Town Information to Players
- When you can purchase buildings from a town

### Rendering
- Make Blueprints Render in World
- Make Blueprints Item Render
- Make Spawn Building Blueprints Render their Icon
- Make Spawn NPC Render the NPC
- Mine Floor Connected Textures and Random Texture Overlays - Y, J?
- Mine Wall Random Textures - Y
- Make Spawn Cow Render the Cow
- Make Spawn Sheep Render the Sheep
- Make Spawn Chicken Render the Chicken
- Mine Wall/Floor in world Rendering and Cookware in inventory renders
- Render Food/Liquids in Mixer correctly, render food on counter correctly - J

### Textures
- Crop Grass Better Texture - Y
- Winter Mine version - Y
- Textures for the NPCs which we stole + New Selection for Ondra > Ashlee - Y

### Models + Textures
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Same Models just different textures
- Rocks (Spring Mine (Ore Variation), Winter Mine Versions (Ore Variation), Overworld Rocks (Stone Only))
    - Large                                                ^
    - Medium                                               ^
    - Small                                                ^
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>^
- Branch - Y
    - Large
    - Medium
    - Small
- Stump - Y
    - Large
    - Medium
    - Small
-Food Trough - Y
    #For feeding animals
    - Empty Trough
        - Z Axis Rotation
            - Left Side
            - Middle Section
            - Right Side
         - X Axis Rotation
            - Left Side
            - Middle Section
            - Right Side
    - Full Trough
        - Z Axis Rotation
            - Left Side
            - Middle Section
            - Right Side
         - X Axis Rotation
            - Left Side
            - Middle Section
            - Right Side
- Sprinkler Block
    - Just a simple shape in the centre, water effects will be done by J
    - Was thinking against it BUT, I'd rather have sprinklers running on a server than do a team system.

### Questing Tasks
- Spawning the NPC Builder Quest
- Welcome to 'Harvest Festival' Quests
- Blacksmith Upgrade Tool Quests
- Priest Bless Tools Quests
- Unlock Mixer, Frying Pan, Pot, Oven for purchase quests