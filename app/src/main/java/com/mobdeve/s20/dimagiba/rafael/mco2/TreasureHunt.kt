package com.mobdeve.s20.dimagiba.rafael.mco2

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
    var date: String = "", // ISO 8601 format for dates
    var location: String = "", // e.g., "lat,lng" or structured object
    var imageId: Int? = null,
    var isFound: Boolean = false,
    var isVerified: Boolean = true,
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

    constructor(treasureID: String, Tdescription: String, posterID: String, Postername: String, Date: String, Location: String) : this() {

        this.id = treasureID
        this.postername = Postername
        this.posterId = posterID
        this.description = Tdescription
        this.date = Date
        this.location = Location

    }
}
