package joshie.harvest.quests;

import joshie.harvest.api.quests.Quest;

public class Quests {
    public static final Quest TUTORIAL_SUPERMARKET = QuestHelper.getQuest("tutorial.supermarket");
    public static final Quest TUTORIAL_UPGRADING = QuestHelper.getQuest("tutorial.upgrading");

    //Quest order based on meeting npcs and relations
    public static final Quest ABI_MEET = QuestHelper.getQuest("meeting.abi");
    public static final Quest ASHLEE_MEET = QuestHelper.getQuest("tutorial.chicken");
    public static final Quest ASHLEE_5K = QuestHelper.getQuest("recipe.corn.baked");
    public static final Quest BRANDON_5K = QuestHelper.getQuest("recipe.soup.rice");
    public static final Quest CANDICE_5K = QuestHelper.getQuest("recipe.milk.hot");
    public static final Quest CLOE_5K = QuestHelper.getQuest("recipe.noodles");
    public static final Quest DANIERU_5K = QuestHelper.getQuest("recipe.butter");
    public static final Quest DANIERU_10K = QuestHelper.getQuest("recipe.egg.scrambled");
    public static final Quest GODDESS_MEET = QuestHelper.getQuest("tutorial.intro");
    public static final Quest GODDESS_5K = QuestHelper.getQuest("recipe.jam.strawberry");
    public static final Quest GODDESS_10K = QuestHelper.getQuest("recipe.milk.strawberry");
    public static final Quest JACOB_MEET = QuestHelper.getQuest("meeting.jacob");
    public static final Quest JACOB_5K = QuestHelper.getQuest("recipe.fish.stew");
    public static final Quest JACOB_10K = QuestHelper.getQuest("recipe.fish.grilled");
    public static final Quest JADE_MEET = QuestHelper.getQuest("tutorial.crops");
    public static final Quest JADE_5K = QuestHelper.getQuest("recipe.toast");
    public static final Quest JADE_10K = QuestHelper.getQuest("recipe.toast.french");
    public static final Quest JADE_15K = QuestHelper.getQuest("friendship.jade.flowers");
    public static final Quest JADE_20K = QuestHelper.getQuest("recipe.jam.apple");
    public static final Quest JENNI_5K = QuestHelper.getQuest("recipe.salad");
    public static final Quest JIM_MEET = QuestHelper.getQuest("tutorial.cow");
    public static final Quest JIM_5K = QuestHelper.getQuest("recipe.fishsticks");
    public static final Quest JOHAN_MEET = QuestHelper.getQuest("meeting.johan");
    public static final Quest JOHAN_5K = QuestHelper.getQuest("recipe.candied.potato");
    public static final Quest JOHAN_10K = QuestHelper.getQuest("recipe.fries.french");
    public static final Quest KATLIN_MEET = QuestHelper.getQuest("meeting.katlin");
    public static final Quest KATLIN_5K = QuestHelper.getQuest("recipe.porridge");
    public static final Quest KATLIN_10K = QuestHelper.getQuest("recipe.stew");
    public static final Quest LIARA_MEET = QuestHelper.getQuest("tutorial.cafe");
    public static final Quest LIARA_5K = QuestHelper.getQuest("recipe.chocolate.hot");
    public static final Quest LIARA_7K = QuestHelper.getQuest("recipe.cookies");
    public static final Quest TIBERIUS_5K = QuestHelper.getQuest("recipe.dinnerroll");
    public static final Quest TIBERIUS_10K = QuestHelper.getQuest("recipe.doughnut");
    public static final Quest YULIF_MEET = QuestHelper.getQuest("tutorial.carpenter");
    public static final Quest TOMAS_5K = QuestHelper.getQuest("recipe.latte.vegetable");
    public static final Quest YULIF_5K = QuestHelper.getQuest("recipe.juice.pineapple");
    public static final Quest YULIF_10K = QuestHelper.getQuest("recipe.cake.chocolate");

    //Buildings
    public static final Quest BUILDING_CAFE = QuestHelper.getQuest("building.cafe");
    public static final Quest BUILDING_BLACKSMITH = QuestHelper.getQuest("building.blacksmith");
    public static final Quest BUILDING_FISHER = QuestHelper.getQuest("building.fisher");
    public static final Quest BUILDING_FESTIVALS = QuestHelper.getQuest("building.festivals");

    //Dummy Quests for unlocking things
    public static final Quest SEEDS_STRAWBERRY = QuestHelper.getQuest("seeds.strawberry");
    public static final Quest SEEDS_SWEET_POTATO = QuestHelper.getQuest("seeds.sweetpotato");
    public static final Quest SEEDS_TREES1 = QuestHelper.getQuest("seeds.trees1");
    public static final Quest SPRINKLER = QuestHelper.getQuest("item.sprinkler");
    public static final Quest HATCHERY = QuestHelper.getQuest("item.hatchery");
    public static final Quest FLOWER_BUYER = QuestHelper.getQuest("buyer.flowers");
    public static final Quest BUY_FLOWER = QuestHelper.getQuest("trader.flowers");
}
