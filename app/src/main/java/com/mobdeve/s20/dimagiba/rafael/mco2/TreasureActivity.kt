package com.mobdeve.s20.dimagiba.rafael.mco2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class TreasureActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_treasure)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra("username")
        val usernameTv = findViewById<TextView>(R.id.treasureUsername)
        usernameTv.text = username

        val description = intent.getStringExtra("description")
        val descriptionTv = findViewById<TextView>(R.id.treasureDescription)
        descriptionTv.text = description

        val pirates = intent.getStringExtra("pirates")
        val piratesTv = findViewById<TextView>(R.id.pirateCount)
        piratesTv.text = pirates

        val plundered = intent.getStringExtra("plundered")
        val plunderedTv = findViewById<TextView>(R.id.plundered_treasure_tv)
        plunderedTv.text = plundered

        val fullDate = intent.getStringExtra("fullDate")
        val fullDateTv = findViewById<TextView>(R.id.startDate)
        fullDateTv.text = fullDate

        val fullLocation = intent.getStringExtra("fullLocation")
        val fullLocationTv = findViewById<TextView>(R.id.treasureLocation)
        fullLocationTv.text = fullLocation

        val pfp = intent.getIntExtra("pfp", 0)
        val pfpTv = findViewById<ImageView>(R.id.treasureImage)

        pfp.let {
            Glide.with(pfpTv)
                .load(it)
                .circleCrop()
                .into(pfpTv)
        }

        val postImage = intent.getIntExtra("post_image", 0)
        val postImageTv = findViewById<ImageView>(R.id.postImageIv)
        postImageTv.setImageResource(postImage)

        val cancelButton = findViewById<Button>(R.id.treasureLeave)

        cancelButton.setOnClickListener {
            finish()
        }

        val joinButton = findViewById<Button>(R.id.treasureJoin)

        joinButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
            val db = FirebaseFirestore.getInstance()
            val userId = sharedPreferences.getString("userId", null) // Retrieve user ID
            val treasureId = intent.getStringExtra("TREASURE_ID"); // TODO: find a way to retrieve current post

            if (userId != null && treasureId != null) {
                val batch = db.batch()

                // add user to participant list of the treasure post
                val treasureRef = db.collection("Treasures").document(treasureId)
                batch.update(treasureRef, "participants", FieldValue.arrayUnion(userId))

                // add treasure hunt to current user's joined Hunts
                val userRef = db.collection("Users").document(userId)
                batch.update(userRef, "joinedHunts", FieldValue.arrayUnion(treasureId))

                // commit
                batch.commit()
                    .addOnSuccessListener {
                        Toast.makeText(this, "You joined the Hunt!", Toast.LENGTH_SHORT).show()
                        Log.d("Firestore", "Batched write successful: User added to participants and treasure added to joinedHunts.")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to join hunt.", Toast.LENGTH_LONG).show()
                        Log.e("Firestore", "Batched write failed", e)
                    }
            } else {
                Toast.makeText(this, "User not logged in!", Toast.LENGTH_LONG).show()
            }

            finish()
        }

    }
}