function getQualityFromNumber(input) {
    var chance = random(0, 99);
    var gold = 100 - input;
    var silver = gold / 2;
    if (chance > gold) return "harvestfestival:gold";
    else if (chance > silver) return "harvestfestival:silver";
    else return "harvestfestival:normal";
}

function getQualityFromFertilizer(fertilizer) {
    if (fertilizer > 0) {
        return getQualityFromNumber(fertilizer);
    } else {
        var chance = random(0, 99);
        if (chance < 1) return "harvestfestival:gold";
        else if (chance < 10) return "harvestfestival:silver";
        else return "harvestfestival:normal";
    }
}

function getQualityFromTreeAge(age, year_length) {
    if (age >= year_length * 3) return "harvestfestival:mystril";
    else if (age >= year_length * 2) return "harvestfestival:gold";
    else if (age >= year_length) return "harvestfestival:silver";
    else return "harvestfestival:normal";
}