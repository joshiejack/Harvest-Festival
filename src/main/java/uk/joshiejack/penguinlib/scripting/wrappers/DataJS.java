package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.util.Patterns;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("unused")
public class DataJS extends AbstractJS<NBTTagCompound> {
    public DataJS(NBTTagCompound tag) {
        super(tag);
    }


    public boolean load(String name, boolean bool) {
        NBTTagCompound object = penguinScriptingObject;
        return object.hasKey(name, 1) ? object.getBoolean(name) : bool;
    }

    public double load(String name, double dbl) {
        NBTTagCompound object = penguinScriptingObject;
        return object.hasKey(name, 6) ? object.getDouble(name) : dbl;
    }

    public int load(String name, int num) {
        NBTTagCompound object = penguinScriptingObject;
        return object.hasKey(name, 3) ? object.getInteger(name) : num;
    }

    public String load(String name, String str) {
        NBTTagCompound object = penguinScriptingObject;
        return object.hasKey(name, 8) ? object.getString(name) : str;
    }

    public boolean has(String name) {
        return penguinScriptingObject.hasKey(name);
    }

    public void save(String name, boolean bool) {
        save(name, (Object) bool);
    }

    public void save(String name, int number) {
        save(name, (Object) number);
    }

    public void save(String name, double dubble) {
        save(name, (Object) dubble);
    }

    public void save(String name, float ffff) {
        save(name, (double) ffff);
    }

    public void save(String name, Object var) {
        NBTTagCompound object = penguinScriptingObject;
        if (Patterns.BOOLEAN_PATTERN.matcher(var.toString()).matches()) {
            object.setBoolean(name, Boolean.parseBoolean(var.toString()));
        } else if (Patterns.DOUBLE_PATTERN.matcher(var.toString()).matches()) {
            object.setDouble(name, Double.parseDouble(var.toString()));
        } else if (Patterns.INTEGER_PATTERN.matcher(var.toString()).matches()) {
            object.setInteger(name, Integer.parseInt(var.toString()));
        } else object.setString(name, var.toString());
    }

    public enum Type {
        BOOLEAN, DOUBLE, INTEGER, STRING
    }
}
