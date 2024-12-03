package com.mobdeve.s20.dimagiba.rafael.mco2

/*
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

 */


data class User(
    var id: String = "",
    var password: String = "",
    var userName: String = "",
    var profileImageId: String = "R.drawable.default_pfp",
    var posts: List<String> = listOf(), // List of TreasureHunt IDs
    var joinedHunts: List<String> = listOf(), // List of TreasureHunt IDs
    var foundHunts: List<String> = listOf() // List of TreasureHunt IDs
) {
    // Firebase-compatible methods
    fun addPost(postId: String) {
        posts = posts + postId
    }

    fun removePost(postId: String) {
        posts = posts - postId
    }

    fun joinHunt(huntId: String) {
        joinedHunts = joinedHunts + huntId
    }

    fun leaveHunt(huntId: String) {
        joinedHunts = joinedHunts - huntId
    }

    fun addFoundHunt(huntId: String) {
        foundHunts = foundHunts + huntId
    }

    constructor(userName: String, profileImageId: String) : this() {
        this.userName = userName
        this.profileImageId = profileImageId
    }
}

