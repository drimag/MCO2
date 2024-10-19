package com.mobdeve.s20.dimagiba.rafael.mco2

class TreasureHunt {
    var description: String
        private set
    var poster: User
        private set
    var date: CustomDate
        private set
    var participants: MutableList<User> = mutableListOf()
        private set
    var winners: MutableList<User> = mutableListOf()
        private set
    var imageId: Int? = null
        private set

    constructor(description: String, poster: User, date: CustomDate, imageId: Int) {
        this.description = description
        this.poster = poster
        this.date = date
        this.imageId = imageId
    }

    constructor(description: String, poster: User, date: CustomDate) {
        this.description = description
        this.poster = poster
        this.date = date
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