package com.mobdeve.s20.dimagiba.rafael.mco2

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

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
            Toast.makeText(this, "You joined the Hunt!", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}