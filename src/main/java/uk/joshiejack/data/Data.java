package uk.joshiejack.data;

import uk.joshiejack.data.command.DataCommandExport;
import uk.joshiejack.data.csv.ToolCSVGen;
import uk.joshiejack.data.wiki.NPCExport;
import uk.joshiejack.data.wiki.ShippingExport;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

@Mod(modid = "data", name = "Wiki")
public class Data {
    private Set<Runnable> execute(Set<Runnable> exe) {
        exe.add(new ShippingExport());
        exe.add(new ToolCSVGen());

        //Return
        return exe;
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        DataCommandExport.EXPORTERS.put("npc", new NPCExport());
    }

    public static void output(String file_name, StringBuilder output) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File("export", file_name)))) {
            out.write(output.toString().substring(0, output.toString().length() - 1));  //Remove the final \n
            //you are trying to write
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
