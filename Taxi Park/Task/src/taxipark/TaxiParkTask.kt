package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        this.allDrivers.minus(this.trips.map { it.driver }.toSet())

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        if (minTrips < 1) {
            this.allPassengers
        } else {
            this.trips.flatMap { t -> t.passengers.map { p -> p to t } }
                    .groupBy { it.first }
                    .filter { (p, t) -> t.size >= minTrips }
                    .keys
        }

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        this.trips.filter { t -> t.driver == driver }
                .flatMap { t -> t.passengers.map { p -> p to driver } }
                .groupBy { it.first }
                .filter { (p, d) -> d.size > 1 }
                .keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        this.trips.flatMap { t -> t.passengers.map { p -> p to t } }
                .groupBy { it.first }
                .mapValues { (p, t) -> t.map { it.second } }
                .mapValues { (p, t) -> t.partition { t -> (t.discount ?: 0.0) > 0.0 } }
                .filter { (p, results) -> results.first.size > results.second.size }
                .keys

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val linearBuckets = this.trips.map { t -> t.duration }
            .groupBy { it/10 }
            .maxBy { (b, d) -> d.size }
            ?.key

    return linearBuckets?.let {
        val start = it * 10
        val end = start + 9
        IntRange(start, end)
    }
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (this.trips.isEmpty()) {
        return false
    } else {
        val sortedDriverIncome = this.trips.groupBy { it.driver }
                .mapValues { (d, t) -> t.sumByDouble { it.cost } }
                .entries
                .sortedBy { it.value }

        val income80 = this.trips.sumByDouble { it.cost } * 0.8
        val driver20 = this.allDrivers.size / 5

        return sortedDriverIncome.takeLast(driver20).sumByDouble { it.value } >= income80
    }
}