package com.mobdeve.s20.dimagiba.rafael.mco2

class User {
    var userName: String
        private set
    var profileImageId: Int
        private set
    var posts: MutableList<TreasureHunt> = mutableListOf()
        private set
    var joinedHunts: MutableList<TreasureHunt> = mutableListOf()
        private set
    var foundHunts: MutableList<TreasureHunt> = mutableListOf()
        private set

    constructor(userName: String, profileImageId: Int) {
        this.userName = userName
        this.profileImageId = profileImageId
    }

    //this should be called in the constructor of treasure hunt
    fun addPost(post: TreasureHunt){
        posts.add(post)
    }

    fun removePost(post: TreasureHunt){
        posts.remove(post)
    }
}

