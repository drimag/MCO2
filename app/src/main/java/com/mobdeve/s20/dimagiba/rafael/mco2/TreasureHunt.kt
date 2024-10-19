package com.mobdeve.s20.dimagiba.rafael.mco2

class TreasureHunt {
    var description: String
        private set
    var imageId: Int
        private set
    var poster: User
        private set
    var date: CustomDate
        private set
    var participants: MutableList<User> = mutableListOf()
        private set
    var winners: MutableList<User> = mutableListOf()
        private set

    constructor(description: String, imageId: Int, poster: User, date: CustomDate) {
        this.description = description
        this.imageId = imageId
        this.poster = poster
        this.date = date
    }

    fun addWinner(user: User) {
        winners.add(user)
    }

    fun removeWinner(user: User) {
        winners.remove(user)
    }

    fun addParticipant(user: User) {
        participants.add(user)
    }

    fun removeParticipant(user: User) {
        participants.remove(user)
    }
}