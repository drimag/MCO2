package com.mobdeve.s20.dimagiba.rafael.mco2

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FoundTreasureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_found_treasure)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra("username_found")
        val usernameTv = findViewById<TextView>(R.id.treasureUsername)
        usernameTv.text = username

        val description = intent.getStringExtra("description_found")
        val descriptionTv = findViewById<TextView>(R.id.treasureDescription)
        descriptionTv.text = description

        val pirates = intent.getStringExtra("pirates_found")
        val piratesTv = findViewById<TextView>(R.id.pirateCount)
        piratesTv.text = pirates

        val plundered = intent.getStringExtra("plundered_found")
        val plunderedTv = findViewById<TextView>(R.id.plundered_treasure_tv)
        plunderedTv.text = plundered

        val fullDate = intent.getStringExtra("fullDate_found")
        val fullDateTv = findViewById<TextView>(R.id.startDate)
        fullDateTv.text = fullDate

        val fullLocation = intent.getStringExtra("fullLocation_found")
        val fullLocationTv = findViewById<TextView>(R.id.treasureLocation)
        fullLocationTv.text = fullLocation

        val foundDate = intent.getStringExtra("foundDate_found")
        val foundDateTv = findViewById<TextView>(R.id.treasureFoundDate)
        foundDateTv.text = foundDate

        val defaultArray = byteArrayOf()

        val profilePicIV = findViewById<ImageView>(R.id.treasureImage)
        val byteArray = intent.getByteArrayExtra("pfp_found") ?: defaultArray

        if (byteArray.isNotEmpty()){
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            profilePicIV.setImageBitmap(bitmap)
        }


        val postPicIV = findViewById<ImageView>(R.id.postImageIv)
        val postByteArray = intent.getByteArrayExtra("postImage_found") ?: defaultArray


        if (postByteArray.isNotEmpty()){

            val postBitmap = BitmapFactory.decodeByteArray(postByteArray, 0, postByteArray.size)
            postPicIV.setImageBitmap(postBitmap)
        }

        val cancelButton = findViewById<Button>(R.id.treasureLeave)

        cancelButton.setOnClickListener {
            finish()
        }

    }
}