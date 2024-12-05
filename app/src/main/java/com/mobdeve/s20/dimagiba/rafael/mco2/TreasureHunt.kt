package com.mobdeve.s20.dimagiba.rafael.mco2

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*
class TreasureHunt(
    var description: String,
    var poster: User,
    var date: CustomDate,
    var location: Location,
    var imageId: Int? = null,
    var isFound: Boolean? = false,
    var isVerified: Boolean? = true,
    var foundDate: CustomDate? = null
) {

    var participants: MutableList<User> = mutableListOf()
        private set
    var winners: MutableList<User> = mutableListOf()
        private set

    init {
        poster.addPost(this.toString())
    }

    fun addWinner(winner: User) {
        winners.add(winner)
    }

    fun removeWinner(winner: User) {
        winners.remove(winner)
    }

    fun addParticipant(participant: User) {
        participants.add(participant)
    }

    fun removeParticipant(participant: User) {
        participants.remove(participant)
    }
}

 */


data class TreasureHunt(
    var id: String = "",
    var description: String = "",
    var posterId: String = "",
    var postername: String = "",
    var posterPfp: String = "",
    var date: String = "", // ISO 8601 format for dates
    var location: String = "", // e.g., "lat,lng" or structured object
    var imageId: Int = 0,
    var isFound: Boolean = false,
    var isVerified: Boolean = false,
    var foundDate: String? = null,
    var participants: List<String> = listOf(), // List of user IDs
    var winners: List<String> = listOf() // List of user IDs
) {
    // Firebase-compatible methods
    fun addParticipant(participantId: String) {
        participants = participants + participantId
    }

    fun removeParticipant(participantId: String) {
        participants = participants - participantId
    }

    fun addWinner(winnerId: String) {
        winners = winners + winnerId
    }

    fun removeWinner(winnerId: String) {
        winners = winners - winnerId
    }

    constructor(treasureID: String, Tdescription: String, posterID: String, Postername: String, Date: String, Location: String, posterPfp: String) : this() {

        this.id = treasureID
        this.postername = Postername
        this.posterId = posterID
        this.description = Tdescription
        this.date = Date
        this.location = Location
        this.posterPfp = posterPfp
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun toLocalDate(): LocalDate? {
        return try {
            date.let {
                LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME).toLocalDate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
