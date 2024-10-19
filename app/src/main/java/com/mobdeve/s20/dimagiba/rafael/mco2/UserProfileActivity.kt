package com.mobdeve.s20.dimagiba.rafael.mco2

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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

    companion object {
        private var data = ArrayList<TreasureHunt>()
        private var own_data = ArrayList<TreasureHunt>()
        private var joined_data = ArrayList<TreasureHunt>()
        private var found_data = ArrayList<TreasureHunt>()

        val user1 = User("chopper", R.drawable.chopper)
        val user2 = User("kuromi", R.drawable.kuromi)
        val user3 = User("luffy", R.drawable.luffy)

        val post1 = TreasureHunt("find my treasure guys heheheheh", user1, CustomDate(2020, 10, 9), Location("Taguig"))
        val post2 = TreasureHunt("wala magawa hehe", user1, CustomDate(2020, 10, 4), Location("Taguig"))
        val post3 = TreasureHunt("first time in Pateros", user1, CustomDate(2020, 6, 15), Location("Pateros"))

        val post4 = TreasureHunt("DI NYO TO MAHAHANAP LEGIT", user2, CustomDate(2020, 10, 9), Location("Makati"))
        val post5 = TreasureHunt("sali n kau guys <3", user3, CustomDate(2020, 9, 2), Location("Makati"))

        val post6 = TreasureHunt("pahanap ng one piece :P", user3, CustomDate(2020, 4, 1), Location("Manila"))
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: postAdapter

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.filter_self_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.pending -> {
                    // Handle filtering by day
                    Toast.makeText(this, "Showing Pending Posts (Logic not Implemented)", Toast.LENGTH_SHORT).show()
                    val filterTextView: TextView = findViewById(R.id.filter_text_tv)
                    filterTextView.text = "Pending Posts"
                    true

                }
                R.id.verified -> {
                    // Handle filtering by month
                    Toast.makeText(this, "Showing Verified Posts (Logic not Implemented)", Toast.LENGTH_SHORT).show()
                    val filterTextView: TextView = findViewById(R.id.filter_text_tv)
                    filterTextView.text = "Verified Posts"
                    true
                }
                R.id.All -> {
                    // Handle filtering by year
                    Toast.makeText(this, "Showing All Posts (Logic not Implemented)", Toast.LENGTH_SHORT).show()
                    val filterTextView: TextView = findViewById(R.id.filter_text_tv)
                    filterTextView.text = "All Posts"
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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

        R.drawable.chopper.let {
            Glide.with(viewBinding.userProfilePicIv)
                .load(it)
                .circleCrop()
                .into(viewBinding.userProfilePicIv)
        }

        this.recyclerView = viewBinding.userProfilePostsRv
        this.postAdapter = postAdapter(own_data, this)
        this.recyclerView.adapter = postAdapter
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
            this.postAdapter = postAdapter(own_data, this)
            this.recyclerView.adapter = postAdapter
            this.recyclerView.layoutManager = LinearLayoutManager(this)
        }

        userJoinedPostsBtn.setOnClickListener {
            deselect(userPostsBtn)
            select(userJoinedPostsBtn)
            deselect(userFoundPostsBtn)
            postsDropdown.visibility = View.INVISIBLE
            this.postAdapter = postAdapter(joined_data, this)
            this.recyclerView.adapter = postAdapter
            this.recyclerView.layoutManager = LinearLayoutManager(this)
        }

        userFoundPostsBtn.setOnClickListener {
            deselect(userPostsBtn)
            deselect(userJoinedPostsBtn)
            select(userFoundPostsBtn)
            postsDropdown.visibility = View.INVISIBLE
            this.postAdapter = postAdapter(found_data, this)
            this.recyclerView.adapter = postAdapter
            this.recyclerView.layoutManager = LinearLayoutManager(this)
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
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gold))
    }

    fun deselect(button: Button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.light_gold))
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }
}
