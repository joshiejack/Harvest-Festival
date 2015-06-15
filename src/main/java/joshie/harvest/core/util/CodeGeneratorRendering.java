package joshie.harvest.core.util;

import java.io.File;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.io.FileUtils;

public class CodeGeneratorRendering {
    public void getCode() {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("package joshie.harvest.blocks.render;\n\n");
            builder.append("import joshie.harvest.util.RenderBase;\n");
            builder.append("import net.minecraft.init.Blocks;\n");
            builder.append("import net.minecraftforge.common.util.ForgeDirection;\n\n");
            builder.append("import org.lwjgl.opengl.GL11;\n\n");
            builder.append("public class RenderFridge extends RenderBase {\n");
            builder.append("    @Override\n");
            builder.append("    public void renderBlock() {\n");
            
            String input = FileUtils.readFileToString(new File("render-input.txt"));
            builder.append("        if (dir == ForgeDirection.WEST || isItem()) {\n");
            builder.append(input);
            builder.append("\n        } else if (dir == ForgeDirection.NORTH) {");
            builder.append("\n" + input.replaceAll("\\((.*),\\s(.*),\\s(.*),\\s(.*),\\s(.*),\\s(.*)\\)", "\\($3, $2, $1, $6, $5, $4\\)"));
            builder.append("\n        } else if (dir == ForgeDirection.EAST) {");

            //Remove RenderBlock and ); and spaces
            StringBuilder second = new StringBuilder("");
            //Split the String up by comma list
            String[] split = input.split(",|;");

            int pos = 0;
            double one = 0D;
            double two = 0D;
            double three = 0D;
            double four = 0D;
            double five = 0D;
            double six = 0D;
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.DOWN);

            int round = 0;

            for (String s : split) {
                if (s.contains("setTexture")) {
                    second.append(s + ";");
                    continue;
                }

                String trimmed = s.replace("renderBlock(", "").replace(")", "");
                Double value = Double.parseDouble(trimmed); //Convert the internal to a double

                switch (pos) {
                    case 0:
                        one = value;
                        break;
                    case 1:
                        two = value;
                        break;
                    case 2:
                        three = value;
                        break;
                    case 3:
                        four = value;
                        break;
                    case 4:
                        five = value;
                        break;
                    case 5:
                        six = value;
                        break;
                }

                pos++;

                if (pos == 6) {
                    //Now that we have all six numbers of this sequence filled out, we should fix them
                    StringBuilder append = new StringBuilder("\n           renderBlock(");
                    append.append("" + df.format((1D - four)) + "D, ");
                    append.append("" + two + "D, ");
                    append.append("" + three + "D, ");
                    append.append("" + df.format((1D - one)) + "D, ");
                    append.append("" + five + "D, ");
                    append.append("" + six + "D);");

                    second.append(append);

                    pos = 0;
                    round++;
                }
            }

            builder.append(second);
            builder.append("\n        } else if (dir == ForgeDirection.SOUTH) {");
            builder.append(second.toString().replaceAll("\\((.*),\\s(.*),\\s(.*),\\s(.*),\\s(.*),\\s(.*)\\)", "\\($3, $2, $1, $6, $5, $4\\)"));
            builder.append("\n        }\n");
            builder.append("    }\n");
            builder.append("}\n");

            //now that we have done WEST and NORTH

            PrintWriter writer = new PrintWriter("render-generator.log", "UTF-8");
            writer.print(builder.toString());

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
