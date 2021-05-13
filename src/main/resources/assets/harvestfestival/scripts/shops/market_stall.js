function getCost(sub_listing, stock_amount, stock_mechanic, seeded_random) {
    var original_cost = 999999999;
    var department = shops.get("general_store_seeds");
    var other_listing = department.listing(sub_listing.id());
    if (other_listing != null) {
        original_cost = other_listing.sublisting(0).gold();
    } else original_cost = shipping.value(sub_listing.item());

    if (original_cost == 0) {
        original_cost = 75 + seeded_random.nextInt(150);
    }

    var modified = original_cost * (1.5 + (seeded_random.nextDouble() * (1 + seeded_random.nextInt(5))));
    return Math.ceil(modified / 10) * 10;
}