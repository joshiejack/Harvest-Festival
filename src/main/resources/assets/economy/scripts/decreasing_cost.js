function getCost(sub_listing, stock_amount, stock_mechanic, seeded_random) {
    var original_value = sub_listing.gold();
    var stock_maximum = stock_mechanic.getMaximum();
    return original_value * (1 + (stock_amount / stock_maximum));
}