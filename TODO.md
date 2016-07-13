### NPCS, Conversations & AI
- Add Schedules for NPCs, where they visit specific buildings at certain times
- Schedule Based Conversations
- Add Extra Chat Messages, and Conversations for all NPCs

### Mining Dimension Stuff
- Add the hole block on the bottom of each stack of floors, so that you can teleport to the next stack of mines
- Add an Overlay while in the dimension that tells you the name of the mine aka Spring Mine or Winter Mine
- Add an Overlay that tells you what floor you are on

### Functionality
- Fridge can have stacks of up to 512
- Make Mythic Axe Fell Whole Trees
- Make Axes have an AOE that will break all branches/stumps
- Cursed Tools have negative effect
- Syncing Town Information to Players
- When you can purchase buildings from a town
- Make tools consume hunger/add exhaustion when used
- Add custom potion effect called "Fatigued", added to when they have less than three haunches
- Fatigue potion effect will tick down, it can be removed by eating
- Add Config option to restore your hunger when you sleep (off by default)
- When a player is fatigued they will walk slower, and sometimes lose their vision
- Add Custom potion effect called "Exhausted", added when a player has one haunch or less
- Player will walk even slower, have even more frequest vision loss, and will sometimes be nauseous
- When the exhausted potion effect completes, the player will be sent back to their spawn bed,
  where they will be immediately put to sleep, with their hunger restored (effectively they die BUT they keep their inventory)

### Rendering/Modeling
- HarvestChicken
- Change Blueprints to so that you can hover and click where you want them placed with a preview of the building
- Allow repositioning of coin/seasons huds

### Recipe Rewrite
- Recipes have an item which is a page for a book, which is how you learn to do new recipes
- Add pages to your recipe book item
- Select a recipe in the recipe book
- Have all the items required in your inventory and automatically get placed in utensils
- You can always perform every recipe, you'll just learn every recipe evnetually

### Tedium
- Register HFItems as gift types
- Loot Table JSON
- See https://github.com/joshiejack/Harvest-Festival/blob/4fc81be862b9686cf00a3b8b1d262a8b7106aaaf/src/main/java/joshie/harvest/core/lib/LootStrings.java
- See https://github.com/joshiejack/Harvest-Festival/blob/4fc81be862b9686cf00a3b8b1d262a8b7106aaaf/src/main/java/joshie/harvest/buildings/loader/HFBuildings.java#L51

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
- Smashing Rocks
    -S Smashing
    -E Particle effects
- Chopping Wood
    -S Chopping
    -E Particle effects
- Harvest Goddess
    -S Magical Spawning
    -E Particle Effect
- Priest Blessing
    - S Particles when he gives you it back
    - E Magical Sound when he gives you it back

### To Fix
- Workout how to use the weighting system in conjunction with forge states, so i don't have to use my own hacked together weighting system...
- Goddess Water Texture
- Location of the flower in the spring season overlay

### Textures
- Crop Grass Better Texture
- Textures for the NPCs which we stole + New Selection for Ondra > Ashlee
- Texture for Johan the Trader

### Jewels & Jewel Rock
- Make Render for Jewel Rock - J
- Add Diamonds and Emeralds as spawnable from Jewel Rock
### Textures For them
- Amethyst
- Agate
- Fluorite
- Peridot
- Topaz
- Ruby
- Sandrose
- Moonstone
- Alexandrite
- Pink Diamond
- Orichalc
- Adamantite

### Questing Tasks
- Spawning the NPC Builder Quest
- Welcome to 'Harvest Festival' Quests
- Blacksmith Upgrade Tool Quests
- Unlock Mixer, Frying Pan, Pot, Oven for purchase quests
