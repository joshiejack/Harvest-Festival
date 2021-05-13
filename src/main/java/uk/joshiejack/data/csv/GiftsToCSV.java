package uk.joshiejack.data.csv;

import uk.joshiejack.data.Data;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class GiftsToCSV implements Runnable {
    @Override
    public void run() {
        String path = "D:\\Data\\Minecraft Modding\\_Former\\HF 0.6\\src\\main\\java\\joshie\\harvest\\npcs\\gift";
        for (File file : Objects.requireNonNull(new File(path).listFiles())) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                StringBuilder output = new StringBuilder();
                output.append("Item,Quality\n");
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("stackRegistry.register") || line.contains("categoryRegistry.put")) {
                        String quality = StringUtils.substringBetween(line, "Quality.", ");").toLowerCase();
                        String name = "";
                        if (line.contains("stackRegistry")) {
                            if (line.contains("Ore")) {
                                name = StringUtils.substringBetween(line, "Ore.of(\"", "\"),");
                                Item item = Item.REGISTRY.getObject(new ResourceLocation("minecraft", name));
                                if (item != null) {
                                    name = "ore#" + name;
                                }
                            } else if (line.contains("getStackFromEnum")) {
                                String sub = StringUtils.substringBetween(line, ".getStackF", ",");
                                name = StringUtils.substringBetween(sub, ".", ")").toLowerCase();
                            } else if (line.contains(".")) {
                               name = StringUtils.substringBetween(line.replace(".register", ""), ".", ",").toLowerCase();
                            }
                            else name = "UNKNOWN";
                        } else if (line.contains("categoryRegistry")) {
                            name = "cat#" + StringUtils.substringBetween(line, "categoryRegistry.put(", ",").toLowerCase();
                        }

                        output.append(name);
                        output.append(",");
                        output.append(quality);
                        output.append("\n");
                    }
                }

                String file_name = "gifts_harvestfestival_" + file.getName().replace("Gifts", "").replace(".java", "").toLowerCase() + ".csv";
                Data.output(file_name, output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
