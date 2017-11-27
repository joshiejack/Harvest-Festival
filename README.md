![](src/main/resources/assets/harvestfestival/logo.png)

Harvest Festival is a mod heavily inspired by the Harvest Moon series of games. 
Adding crops and animals, that need to be cared for in order to make the most out of them. 
Village and relationship building with the introduction of new npcs and the builder, 
who will help you build a town for the people, as well as guide you on your way. 
Also adding seasons that affect day length, daily weather, cooking and a new way to mine.

More information about Harvest Festival and downloads can be found on [CurseForge](https://minecraft.curseforge.com/projects/harvest-festival)

If you have any questions, feel free to join the [Harvest Festival Discord](https://discord.gg/MRZAyze)

Adding Harvest Festival to your buildscript
---
Add to your build.gradle:
```gradle
repositories {
  maven {
    // url of the maven that hosts Harvest Festival files
    url "http://girafi.dk/maven/"
  }
}

dependencies {
  // compile against Harvest Festival
  deobfCompile "uk.joshiejack.harvest:harvest_festival_${mc_version}:${mc_version}-${hf_version}"
}
```

`${mc_version}` & `${hf_version}` can be found [here](http://girafi.dk/maven/uk/joshiejack/harvest/), check the file name of the version you want.