package com.friendspark.backend.util

object Geohash {
    private const val BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz"
    private val BASE32_MAP = BASE32.mapIndexed { i, c -> c to i }.toMap()

    /**
     * Encodes latitude/longitude to geohash with given precision (1–12)
     * FriendSpark uses precision = 12 → ~1 meter accuracy
     */
    fun encode(latitude: Double, longitude: Double, precision: Int = 12): String {
        require(precision in 1..12) { "Precision must be between 1 and 12" }
        require(latitude in -90.0..90.0) { "Invalid latitude" }
        require(longitude in -180.0..180.0) { "Invalid longitude" }

        var latMin = -90.0
        var latMax = 90.0
        var lonMin = -180.0
        var lonMax = 180.0

        val bits = BooleanArray(precision * 5)
        var bit = 0
        var even = true

        while (bit < precision * 5) {
            if (even) {
                val mid = (lonMin + lonMax) / 2
                if (longitude >= mid) {
                    bits[bit++] = true
                    lonMin = mid
                } else {
                    bits[bit++] = false
                    lonMax = mid
                }
            } else {
                val mid = (latMin + latMax) / 2
                if (latitude >= mid) {
                    bits[bit++] = true
                    latMin = mid
                } else {
                    bits[bit++] = false
                    latMax = mid
                }
            }
            even = !even
        }

        val sb = StringBuilder()
        var i = 0
        while (i < bits.size) {
            var value = 0
            repeat(5) { offset ->
                if (i + offset < bits.size && bits[i + offset]) {
                    value = value or (1 shl (4 - offset))
                }
            }
            sb.append(BASE32[value])
            i += 5
        }
        return sb.toString()
    }

    // Optional: decode back to coordinates (useful for debugging)
    fun decode(geohash: String): Pair<Double, Double> {
        var latMin = -90.0
        var latMax = 90.0
        var lonMin = -180.0
        var lonMax = 180.0
        var even = true

        for (c in geohash) {
            val value = BASE32_MAP[c] ?: error("Invalid geohash character: $c")
            repeat(5) { i ->
                val bit = (value shr (4 - i)) and 1 == 1
                if (even) {
                    val mid = (lonMin + lonMax) / 2
                    if (bit) lonMin = mid else lonMax = mid
                } else {
                    val mid = (latMin + latMax) / 2
                    if (bit) latMin = mid else latMax = mid
                }
                even = !even
            }
        }
        return (latMin + latMax) / 2 to (lonMin + lonMax) / 2
    }
}