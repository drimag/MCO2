package com.mobdeve.s20.dimagiba.rafael.mco2

class TreasureHunt(
    var description: String,
    var poster: User,
    var date: CustomDate,
    var location: Location,
    var imageId: Int? = null
) {

    var participants: MutableList<User> = mutableListOf()
        private set
    var winners: MutableList<User> = mutableListOf()
        private set

    init {
        poster.addPost(this)
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