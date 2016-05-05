package joshie.harvest.debug;

import joshie.harvest.HarvestFestival;
import org.apache.logging.log4j.Level;

import java.util.HashMap;

public class HFDebug {
    public static void preInit() {
        /*List<Data> crops = new ArrayList();
        crops.add(new Data("cucumber", 4));
        crops.add(new Data("strawberry", 4));
        crops.add(new Data("onion", 3));
        crops.add(new Data("corn", 5));
        crops.add(new Data("pineapple", 5));
        crops.add(new Data("eggplant", 4));
        crops.add(new Data("spinach", 3));
        crops.add(new Data("sweet_potato", 3));
        crops.add(new Data("green_pepper", 5));

        for (Data crop: crops) {
            StringBuilder state = new StringBuilder();
            state.append("{\n" +
                    "  \"variants\": {\n");

            //Stages
            for (int i = 1; i <= crop.amount; i++) {
                String stage = "{\n" +
                        "  \"parent\": \"block/crop\",\n" +
                        "  \"textures\": {\n" +
                        "    \"crop\": \"harvestfestival:blocks/crops/" + crop.name + "_" + i + "\"\n" +
                        "  }\n" +
                        "}";

                try {
                    File toSave = new File("C:\\Users\\joshie\\Desktop\\Test\\models\\block", crop.name + "_stage" + i + ".json\\");
                    if (!toSave.getParentFile().exists()) {
                        toSave.getParentFile().mkdirs();
                    }

                    Writer writer = new OutputStreamWriter(new FileOutputStream(toSave), "UTF-8");
                    writer.write(stage);
                    writer.close();
                } catch (Exception e) { e.printStackTrace(); }

                //Append
                state.append("    \"stage=" + i + "\": { \"model\": \"harvestfestival:" + crop.name + "_stage" + i + "\" }");
                if (crop.amount != i) state.append(",\n");
            }

            state.append("\n  }\n" +
                    "}");

            try {
                File file = new File("C:\\Users\\joshie\\Desktop\\Test\\blockstates", "crops_block_" + crop.name + ".json\\");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(state.toString());
                writer.close();
            } catch (Exception e) {}
        }

        System.out.print("fin"); */
    }

    public static void init() {
        /*
        for (IMealRecipe recipe : HFApi.COOKING.getRecipes()) {
            try {
                IMeal best = recipe.getMeal();
                String name = best.getUnlocalizedName().replace(".", "_");
                File file = new File("C:\\Users\\joshie\\Desktop\\Test", name + ".json\\");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                String json = "{\n" +
                        "  \"parent\": \"item/generated\",\n" +
                        "  \"textures\": {\n" +
                        "    \"layer0\": \"harvestfestival:items/meals/" + name + "\"\n" +
                        "  }\n" +
                        "}";

                Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                writer.write(json.toString());
                writer.close();

            } catch (Exception e) {}
        }

        System.out.println("fin"); */
    }

    private static class Data {
        String name;
        int amount;

        public Data(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }
    }

    public static HashMap<String, Long> timers = new HashMap<String, Long>();

    public static void start(String name) {
        timers.put(name, System.nanoTime());
    }

    public static void end(String name) {
        long current = System.nanoTime();
        long then = timers.get(name);
        HarvestFestival.LOGGER.log(Level.INFO, name + " took " + (current - then));
    }
}
