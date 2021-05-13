package uk.joshiejack.economy.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.economy.shop.*;
import uk.joshiejack.economy.shop.builder.ListingBuilder;
import uk.joshiejack.economy.shop.comparator.ComparatorImmutable;
import uk.joshiejack.economy.shop.condition.ConditionCompare;
import uk.joshiejack.economy.shop.condition.ConditionList;
import uk.joshiejack.economy.shop.input.InputMethod;
import uk.joshiejack.economy.shop.input.InputToShop;
import uk.joshiejack.economy.shop.inventory.StockMechanic;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Economy.MODID)
public class ShopLoader {
    @SubscribeEvent
    public static void onDatabaseCollected(DatabaseLoadedEvent.DataLoaded database) {
        //Update the database with the simple departments
        //Very simple listings AND Generic one for any types
        Listing.HANDLERS.forEach((type, handler) -> {
            database.table("very_simple_department_listings_" + type).rows().forEach(sdl -> registerSimpleListing(database.get(), sdl, sdl.get("type"), type, sdl.get(type), "unlimited"));
            database.table("simple_department_listings_" + type).rows().forEach(sdl -> registerSimpleListing(database.get(), sdl, sdl.get("id"), type, sdl.get(type), sdl.get("stock_mechanic")));
        });

        database.table("very_simple_department_listings").rows().forEach(sdl -> registerSimpleListing(database.get(), sdl, sdl.get("data"), sdl.get("type"), sdl.get("data"), "unlimited"));
        database.table("simple_department_listings").rows().forEach(sdl -> registerSimpleListing(database.get(), sdl, sdl.get("id"), sdl.get("type"), sdl.get("data"), sdl.get("stock_mechanic")));
    }

    private static void registerSimpleListing(Database database, Row sdl, String id, String type, String data, String stock_mechanic) {
        String department_id = sdl.get("department_id");
        long gold = sdl.getAsLong("gold");
        database.createTable("department_listings", "department_id", "id", "stock_mechanic", "cost_formula").insert(department_id, id, stock_mechanic, "default");
        database.createTable("sublistings", "department_id", "listing_id", "id", "type", "data", "gold", "weight").insert(department_id, id, "default", type, data, gold, 1);
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) { //LOW, So we appear after recipes have been added
        //Temporary Registry, Create all the comparators
        Map<String, Comparator> comparators = Maps.newHashMap(); //Temporary Registry
        //for all the type of comparators
        for (String type : Comparator.types()) {
            if (Comparator.get(type) instanceof ComparatorImmutable) {
                comparators.put(type, Comparator.get(type));
            } else {
                event.table("comparator_" + type).rows().forEach(comparator -> {
                    String comparator_id = comparator.id();
                    if (comparators.containsKey(comparator_id)) {
                        comparators.get(comparator_id).merge(comparator);
                    } else {
                        Comparator theComparator = Comparator.get(type);
                        if (theComparator != null) { //Pass the database as well as the row data for this
                            comparators.put(comparator_id, theComparator.create(comparator, comparator_id));
                        }
                    }
                });
            }
        }

        //Put all the immutable comparators in to the database

        //Create all the conditions
        Map<String, Condition> conditions = Maps.newHashMap(); //Temporary Registry
        for (String type: Condition.types()) {
            event.table("condition_" + type).rows().forEach(condition -> {
                String condition_id = condition.id();
                if (conditions.containsKey(condition_id)) {
                    conditions.get(condition_id).merge(condition);
                } else {
                    Condition theCondition = Condition.get(type);
                    if (theCondition != null) {
                        if (theCondition instanceof ConditionCompare)
                            conditions.put(condition_id, ((ConditionCompare) theCondition).create(condition, comparators));
                        else conditions.put(condition_id, theCondition.create(condition, condition_id));
                    }
                }
            });
        }

        conditions.entrySet().stream().filter(e -> e.getValue() instanceof ConditionList).forEach(entry ->
                ((ConditionList) entry.getValue()).initList(event.get(), entry.getKey(), conditions));

        //Create all the stock mechanics
        Map<String, StockMechanic> stock_mechanics = Maps.newHashMap();
        //WHERE DO WE NPE???
        event.table("stock_mechanics");
        event.table("stock_mechanics").rows();
        event.table("stock_mechanics").rows().forEach(r -> {});
        event.table("stock_mechanics").rows().forEach(r -> {
            stock_mechanics.put("OOOO", new StockMechanic(1, 2));
        });




        event.table("stock_mechanics").rows().forEach(stock_mechanic ->
                stock_mechanics.put(stock_mechanic.id(), new StockMechanic(stock_mechanic.get("max stock"), stock_mechanic.get("replenish rate"))));

        event.table("stock_mechanics").rows().forEach(stock_mechanic ->
                stock_mechanics.put(stock_mechanic.id(), new StockMechanic(stock_mechanic.get("max stock"), stock_mechanic.get("replenish rate"))));
        Map<String, Interpreter> cost_scripts = Maps.newHashMap();
        event.table("cost_formulae").rows().forEach(cost_formula ->
                cost_scripts.put(cost_formula.id(), Scripting.get(cost_formula.getScript())));

        event.table("shops").rows().forEach(shop -> {
            String shop_id = shop.id();
            String name = shop.name();
            String vendor_id = shop.get("vendor_id");
            InputMethod opening_method = InputMethod.valueOf(shop.get("opening_method").toString().toUpperCase(Locale.ENGLISH));
            Shop theShop = new Shop(shop_id, name);
            event.table("departments").where("shop_id=" + shop_id).forEach(department -> {
                String department_id = department.id();
                Department theDepartment = new Department(theShop, department_id, opening_method); //Add the department to the shop in creation
                if (!department.isEmpty("icon")) theDepartment.setIcon(department.icon());
                if (!department.isEmpty("name")) theDepartment.setName(department.name());
                Row vendor = event.table("vendors").fetch_where("id=" + vendor_id); //Register the vendor
                InputToShop.register(vendor.get("type"), vendor.get("data"), theDepartment); //to the input

                //Add the conditions for this shop
                event.table("shop_conditions").where("shop_id=" + shop_id)
                        .forEach(condition -> theDepartment.addCondition(conditions.get(condition.get("condition_id").toString())));
                event.table("department_conditions").where("shop_id=" + shop_id + "&department_id=" + department_id)
                        .forEach(condition -> theDepartment.addCondition(conditions.get(condition.get("condition_id").toString())));

                event.table("department_listings").where("department_id=" + department_id).forEach(listing -> {
                    String listing_id = listing.id();
                    String data_id = listing_id.contains("$") ? listing_id.split("\\$")[0] : listing_id;
                    List<Sublisting> sublistings = Lists.newArrayList();//handler.getObjectsFromDatabase(database.get(), department_id, data_id);
                    event.table("sublistings").where("department_id=" + department_id + "&listing_id=" + data_id).forEach(sublisting -> {
                        String original_sub_id = sublisting.id();
                        String original_sub_type = sublisting.get("type");
                        boolean builder = original_sub_type.endsWith("_builder");
                        String sub_type = original_sub_type.replace("_builder", ""); //Remove the builder
                        List<String> data_entries = builder ? ListingBuilder.BUILDERS.get(sublisting.get("data").toString()).items() : Lists.newArrayList(sublisting.get("data").toString());
                        for (int i = 0; i < data_entries.size(); i++) {
                            String sub_id = builder ? original_sub_id + "$" + i : original_sub_id;
                            ListingHandler handler = Listing.HANDLERS.get(sub_type);
                            if (handler == null)
                                Economy.logger.log(Level.ERROR, "Failed to find the listing handler type: " + sub_type + " for " + department_id + ": " + listing_id + " :" + sub_id);
                            else {
                                Sublisting theSublisting = new Sublisting(sub_id, handler, handler.getObjectFromDatabase(event.get(), data_entries.get(i)));
                                theSublisting.setGold(sublisting.getAsLong("gold"));
                                theSublisting.setWeight(sublisting.getAsDouble("weight"));
                                String material_id = sub_id.contains("$") ? sub_id.split("\\$")[0] : sub_id;
                                event.table("sublisting_materials").where("department_id=" + department_id + "&listing_id=" + data_id + "&sub_id=" + material_id).forEach(material -> {
                                    theSublisting.addMaterial(new MaterialCost(material.get("item"), material.get("amount")));
                                });

                                //Load in display overrides
                                Row display = event.table("sublisting_display_data").fetch_where("department_id=" + department_id + "&listing_id=" + data_id + "&sub_id=" + material_id);
                                if (!display.isEmpty("icon"))
                                    theSublisting.setDisplayIcon(StackHelper.getStacksFromString(display.get("icon")));
                                if (!display.isEmpty("name")) theSublisting.setDisplayName(display.get("name"));
                                if (!display.isEmpty("tooltip"))
                                    theSublisting.setTooltip(display.get("tooltip").toString().split("\n"));
                                sublistings.add(theSublisting);
                            }
                        }
                    });

                    if (sublistings.size() > 0 && sublistings.stream().allMatch(sublisting -> sublisting.getHandler().isValid(sublisting.getObject()))) {
                        StockMechanic stockMechanic = stock_mechanics.get(listing.get("stock_mechanic").toString());
                        Interpreter costScript = cost_scripts.get(listing.get("cost_formula").toString());
                        if (stockMechanic == null)
                            Economy.logger.log(Level.ERROR, "Failed to find the stock mechanic: " + listing.get("stock_mechanic") + " for " + department_id + ":" + listing_id);
                        else if (costScript == null)
                            Economy.logger.log(Level.ERROR, "Failed to find the cost script: " + listing.get("cost_formula") + " for " + department_id + ":" + listing_id);
                        else {
                            Listing theListing = new Listing(theDepartment, listing_id, sublistings, costScript, stockMechanic);
                            event.table("listing_conditions").where("department_id=" + department_id + "&listing_id=" + data_id)
                                    .forEach(condition -> {
                                        Condition cd = conditions.get(condition.get("condition_id").toString());
                                        if (cd == null) Economy.logger.error("Incorrect condition added as a listing condition with the id: "
                                                + condition.get("condition_id") + " with the listing_id " + listing_id + " in the department " + theDepartment.id());
                                        else theListing.addCondition(conditions.get(condition.get("condition_id").toString()));
                                    });
                            sublistings.forEach(s -> s.setListing(theListing));
                            Economy.logger.log(Level.INFO, "Successfully added the listing: " + listing_id + " for " + department_id);
                        }
                    } else if (sublistings.size() == 0) {
                        Economy.logger.log(Level.ERROR, "No sublistings were added for the listing: " + department_id + ":" + listing_id);
                    } else {
                        for (Sublisting sublisting : sublistings) {
                            if (!sublisting.getHandler().isValid(sublisting.getObject())) {
                                Economy.logger.log(Level.ERROR, "The sublisting: " + sublisting.id() + " created an invalid object for the listing: " + department_id + ":" + listing_id);
                            }
                        }
                    }
                });
            });
        });
    }
}
