package uk.joshiejack.penguinlib.events;

import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.data.database.Table;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Collection;

public class DatabaseLoadedEvent extends Event {
    private final Database database;
    public DatabaseLoadedEvent(Database database) {
        this.database = database;
    }

    @Deprecated
    public Collection<Row> getData(String name) {
        return database.table(name).rows();
    }

    public Table table(String table) {
        return database.table(table);
    }

    public Database get() {
        return database;
    }

    /** Called before custom items are registered **/
    public static class Pre extends Event {}

    public static class LoadComplete extends DatabaseLoadedEvent {
        public LoadComplete(Database database) {
            super(database);
        }
    }

    public static class DataLoaded extends DatabaseLoadedEvent {
        public DataLoaded(Database database) {
            super(database);
        }
    }

    public static class ConfigLoad extends DatabaseLoadedEvent {
        public ConfigLoad(Database database) { super(database); }
    }
}
