package com.mobdeve.s20.dimagiba.rafael.mco2

class Location {
    private var city: String
    private var region: String

    constructor(city: String) {
        this.city = city
        this.region = "Metro Manila"
    }

    constructor(city: String, region: String) {
        this.city = city
        this.region = region
    }

    fun getCity(): String {
        return this.city
    }

    companion object {
        private val cityString = arrayOf(
            "Caloocan",
            "Las Pinas",
            "Makati",
            "Malabon",
            "Mandaluyong",
            "Manila",
            "Marikina",
            "Muntinlupa",
            "Navotas",
            "Paranaque",
            "Pasay",
            "Pasig",
            "Pateros",
            "Quezon City",
            "San Juan",
            "Taguig",
            "Valenzuela"
        )
    }
}