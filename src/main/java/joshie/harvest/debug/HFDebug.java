package joshie.harvest.debug;

import joshie.harvest.core.config.General;

public class HFDebug {
    public static void preInit() {
        if (General.DEBUG_MODE) {
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
    }

    private static class Data {
        String name;
        int amount;

        public Data(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }
    }
}
