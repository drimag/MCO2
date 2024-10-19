package com.mobdeve.s20.dimagiba.rafael.mco2

class User {
    var userName: String
        private set
    var displayName: String
        private set
    var profileImageId: Int
        private set
    var posts: MutableList<User> = mutableListOf()
        private set
    var joinedHunts: MutableList<TreasureHunt> = mutableListOf()
        private set
    var foundHunts: MutableList<TreasureHunt> = mutableListOf()
        private set


    constructor(userName: String, displayName: String, profileImageId: Int) {
        this.userName = userName
        this.displayName = displayName
        this.profileImageId = profileImageId
    }

}
