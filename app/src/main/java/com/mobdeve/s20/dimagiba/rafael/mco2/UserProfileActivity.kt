package com.mobdeve.s20.dimagiba.rafael.mco2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.ActivityUserProfileBinding
import org.w3c.dom.Text

class UserProfileActivity : AppCompatActivity() {

    /*
    companion object {
        private var data = ArrayList<TreasureHunt>()
        private var own_data = ArrayList<TreasureHunt>()
        private var joined_data = ArrayList<TreasureHunt>()
        private var found_data = ArrayList<TreasureHunt>()

        val user1 = User("chopper", R.drawable.chopper)
        val user2 = User("kuromi", R.drawable.kuromi)
        val user3 = User("luffy", R.drawable.luffy)

        val post1 = TreasureHunt("find my treasure guys heheheheh", user1, CustomDate(2020, 10, 9), Location("Taguig"), isVerified = false)
        val post2 = TreasureHunt("wala magawa hehehe", user1, CustomDate(2020, 10, 4), Location("Taguig"))
        val post3 = TreasureHunt("first time in Pateros", user1, CustomDate(2020, 6, 15), Location("Pateros"))

        val post4 = TreasureHunt("DI NYO TO MAHAHANAP LEGIT", user2, CustomDate(2020, 10, 9), Location("Makati"))
        val post5 = TreasureHunt("sali n kau guys <3", user3, CustomDate(2020, 9, 2), Location("Makati"))

        val post6 = TreasureHunt("pahanap ng one piece :P", user3, CustomDate(2020, 4, 1), Location("Manila"), foundDate = CustomDate(2024, 10, 10))
        init {
            // Add all posts to the data list
            own_data.add(post1)
            own_data.add(post2)
            own_data.add(post3)

            joined_data.add(post4)
            joined_data.add(post5)

            found_data.add(post6)
        }
    }

     */

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: postAdapter
    private lateinit var ownPostAdapter: ownPostAdapter
    private lateinit var joinedPostAdapter: joinedPostAdapter
    private lateinit var foundPostAdapter: foundPostAdapter
    private lateinit var usernameText: TextView

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        if (::ownPostAdapter.isInitialized) {
            ownPostAdapter.notifyDataSetChanged()
        }
        if (::joinedPostAdapter.isInitialized) {
            joinedPostAdapter.notifyDataSetChanged()
        }
        if (::foundPostAdapter.isInitialized) {
            foundPostAdapter.notifyDataSetChanged()
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.filter_self_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.pending -> {
                    // Handle filtering by day
//                    Toast.makeText(this, "Showing Pending Posts (Logic not Implemented)", Toast.LENGTH_SHORT).show()
                    updateOwnAdaptor("Pending Posts")
                    val filterTextView: TextView = findViewById(R.id.filter_text_tv)
                    filterTextView.text = "Pending Posts"
                    true
                }
                R.id.verified -> {
                    // Handle filtering by month
//                    Toast.makeText(this, "Showing Verified Posts (Logic not Implemented)", Toast.LENGTH_SHORT).show()
                    val filterTextView: TextView = findViewById(R.id.filter_text_tv)
                    filterTextView.text = "Verified Posts"
                    updateOwnAdaptor("Verified Posts")
                    true
                }
                R.id.All -> {
                    // Handle filtering by year
//                    Toast.makeText(this, "Showing All Posts (Logic not Implemented)", Toast.LENGTH_SHORT).show()
                    val filterTextView: TextView = findViewById(R.id.filter_text_tv)
                    updateOwnAdaptor("All Posts")
                    filterTextView.text = "All Posts"
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        //getting reference to database
        val db = FirebaseFirestore.getInstance()

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null) // Retrieve user ID
        val username = sharedPreferences.getString("username", null) // Retrieve username
        val pfpString = sharedPreferences.getString("pfp", null) // Retrieve username
        val pfp = pfpString?.let { getDrawableIdFromString(it) };

        val userPostsList = ArrayList<TreasureHunt>()

        //for own users post
        if (userId != null) {
            db.collection("Treasures")
                .whereEqualTo("posterId", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->

                    userPostsList.clear()
                    for (document in querySnapshot.documents) {
                        val treasure = document.toObject(TreasureHunt::class.java)?.copy(id = document.id)
                        if (treasure != null) {
                            userPostsList.add(treasure)
                        }
                    }
                    // Notify your adapter after updating the list
                    //change adapter for user posts
                    ownPostAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    Toast.makeText(this, "Error fetching user posts: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewBinding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val usernameText = findViewById<TextView>(R.id.user_profile_username_tv)
        usernameText.text = username

        R.drawable.chopper.let {
            Glide.with(viewBinding.userProfilePicIv)
                .load(pfp)
                .circleCrop()
                .into(viewBinding.userProfilePicIv)
        }

        this.recyclerView = viewBinding.userProfilePostsRv

        //change own data to from database


        this.ownPostAdapter = ownPostAdapter(userPostsList, this)
        this.recyclerView.adapter = ownPostAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        val userPostsBtn: Button = findViewById<Button>(R.id.user_profile_user_posts_bt)
        val userJoinedPostsBtn: Button = findViewById<Button>(R.id.user_profile_joined_posts_bt)
        val userFoundPostsBtn: Button = findViewById<Button>(R.id.user_profile_found_posts_bt)

        val logoutBtn: ImageButton = findViewById<ImageButton>(R.id.user_profile_logout_bt)
        val backBtn: ImageButton = findViewById<ImageButton>(R.id.user_profile_back_bt)
        val postsDropdown: LinearLayout = findViewById<LinearLayout>(R.id.user_profile_post_filter)

        backBtn.setOnClickListener {
            finish()
        }

        logoutBtn.setOnClickListener {
            confirmLogout()
        }

        userPostsBtn.setOnClickListener {
            select(userPostsBtn)
            deselect(userJoinedPostsBtn)
            deselect(userFoundPostsBtn)
            postsDropdown.visibility = View.VISIBLE
            val filterTextView: TextView = findViewById(R.id.filter_text_tv)
            filterTextView.text = "All Posts"
            //change own_data to list of from db


            this.ownPostAdapter = ownPostAdapter(userPostsList, this)
            this.recyclerView.adapter = ownPostAdapter
            this.recyclerView.layoutManager = LinearLayoutManager(this)
        }

        userJoinedPostsBtn.setOnClickListener {
            deselect(userPostsBtn)
            select(userJoinedPostsBtn)
            deselect(userFoundPostsBtn)
            postsDropdown.visibility = View.INVISIBLE

            //change joined_data to list of from db

            fetchJoinedTreasures { joinedData ->
                // Update the RecyclerView with the fetched data
                this.joinedPostAdapter = joinedPostAdapter(joinedData, this)
                this.recyclerView.adapter = joinedPostAdapter
                this.recyclerView.layoutManager = LinearLayoutManager(this)
            }
/*
            this.joinedPostAdapter = joinedPostAdapter(joined_data, this)
            this.recyclerView.adapter = joinedPostAdapter
            this.recyclerView.layoutManager = LinearLayoutManager(this)

 */
        }

        userFoundPostsBtn.setOnClickListener {
            deselect(userPostsBtn)
            deselect(userJoinedPostsBtn)
            select(userFoundPostsBtn)
            postsDropdown.visibility = View.INVISIBLE


            fetchFinishedTreasures { foundList ->
                // Update the RecyclerView with the fetched data
                this.foundPostAdapter = foundPostAdapter(foundList, this)
                this.recyclerView.adapter = foundPostAdapter
                this.recyclerView.layoutManager = LinearLayoutManager(this)
            }
            //change found_data to list of from d

        }
        postsDropdown.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun confirmLogout() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log Out")
            .setMessage("Are you sure you want to log out of this account?")
            .setPositiveButton("Yes") {dialog, _ ->

                //clear session data
                val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("No") {dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun select(button: Button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.white))
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.gold))
    }

    fun deselect(button: Button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.cool_grey))
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun fetchJoinedTreasures(onComplete: (ArrayList<TreasureHunt>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null) // Retrieve user ID
        //val username = sharedPreferences.getString("username", null) // Retrieve username

        if (userId != null) {
            db.collection("Treasures")
                .whereArrayContains("participants", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val joinedTreasuresList = ArrayList<TreasureHunt>()

                    for (document in querySnapshot.documents) {
                        val treasure = document.toObject(TreasureHunt::class.java)?.copy(id = document.id)
                        if (treasure != null) {
                            joinedTreasuresList.add(treasure)
                        }
                    }

                    // Pass the list to the callback
                    onComplete(joinedTreasuresList)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error fetching joined treasures: ${exception.message}", Toast.LENGTH_SHORT).show()
                    onComplete(ArrayList()) // Return an empty list on error
                }
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            onComplete(ArrayList()) // Return an empty list if no user is logged in
        }
    }

    private fun fetchFinishedTreasures(onComplete: (ArrayList<TreasureHunt>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null) // Retrieve user ID
        //val username = sharedPreferences.getString("username", null) // Retrieve username


        if (userId != null) {
            db.collection("Treasures")
                .whereArrayContains("winners", userId) // Check if the user is in the winners list
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val finishedTreasuresList = ArrayList<TreasureHunt>()

                    for (document in querySnapshot.documents) {
                        val treasure = document.toObject(TreasureHunt::class.java)?.copy(id = document.id)
                        if (treasure != null) {
                            finishedTreasuresList.add(treasure)
                        }
                    }

                    // Pass the list to the callback
                    onComplete(finishedTreasuresList)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error fetching finished treasures: ${exception.message}", Toast.LENGTH_SHORT).show()
                    onComplete(ArrayList()) // Return an empty ArrayList on error
                }
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            onComplete(ArrayList()) // Return an empty ArrayList if no user is logged in
        }
    }
    private fun getDrawableIdFromString(drawableString: String): Int {
        if (drawableString.startsWith("R.drawable.")) {
            val resourceName = drawableString.substring("R.drawable.".length)
            return resources.getIdentifier(resourceName, "drawable", packageName)
        } else {
            throw IllegalArgumentException("Invalid drawable string: $drawableString")
        }
    }

    private fun updateOwnAdaptor(filter: String){
        val db = FirebaseFirestore.getInstance()
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null) // Retrieve user ID

        val userPostsList = ArrayList<TreasureHunt>()

        if (userId != null) {
            db.collection("Treasures")
                .whereEqualTo("posterId", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->

                    userPostsList.clear()
                    for (document in querySnapshot.documents) {
                        val treasure = document.toObject(TreasureHunt::class.java)?.copy(id = document.id)
                        if(filter == "Pending Posts" && treasure != null && !treasure.isVerified) {
                            userPostsList.add(treasure)
                        }
                        else if (filter == "Verified Posts" && treasure != null && treasure.isVerified) {
                            userPostsList.add(treasure)
                        }
                        else if (filter == "All Posts" && treasure != null){
                            userPostsList.add(treasure)
                        }
                    }
                    // Notify your adapter after updating the list
                    //change adapter for user posts
                    ownPostAdapter.notifyDataSetChanged()
                    this.ownPostAdapter = ownPostAdapter(userPostsList, this)
                    this.recyclerView.adapter = ownPostAdapter
                    this.recyclerView.layoutManager = LinearLayoutManager(this)
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    Toast.makeText(this, "Error fetching user posts: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

}
