package com.mobdeve.s20.dimagiba.rafael.mco2

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OwnTreasureActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pending_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra("username_own")
        val usernameTv = findViewById<TextView>(R.id.treasureUsername)
        usernameTv.text = username

        val description = intent.getStringExtra("description_own")
        val descriptionTv = findViewById<TextView>(R.id.treasureDescription)
        descriptionTv.text = description

        val pirates = intent.getStringExtra("pirates_own")
        val piratesTv = findViewById<TextView>(R.id.pirateCount)
        piratesTv.text = pirates

        val plundered = intent.getStringExtra("plundered_own")
        val plunderedTv = findViewById<TextView>(R.id.plundered_treasure_tv)
        plunderedTv.text = plundered

        val fullDate = intent.getStringExtra("fullDate_own")
        val fullDateTv = findViewById<TextView>(R.id.startDate)
        fullDateTv.text = fullDate

        val fullLocation = intent.getStringExtra("fullLocation_own")
        val fullLocationTv = findViewById<TextView>(R.id.treasureLocation)
        fullLocationTv.text = fullLocation

        val needsVerification = intent.getBooleanExtra("needs_verification", false)
        val needsVerificationHeader = findViewById<LinearLayout>(R.id.notVerifiedHeader)
        val verifyButton = findViewById<Button>(R.id.treasureVerify)

        if (needsVerification) {
            needsVerificationHeader.visibility = View.VISIBLE
            verifyButton.visibility = View.VISIBLE
        } else {
            needsVerificationHeader.visibility = View.GONE
            verifyButton.visibility = View.GONE
        }

        val defaultArray = byteArrayOf()

        val profilePicIV = findViewById<ImageView>(R.id.treasureImage)
        val byteArray = intent.getByteArrayExtra("pfp_own") ?: defaultArray

        if (byteArray.isNotEmpty()){
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            profilePicIV.setImageBitmap(bitmap)
        }


        val postPicIV = findViewById<ImageView>(R.id.postImageIv)
        val postByteArray = intent.getByteArrayExtra("postImage_own") ?: defaultArray


        if (postByteArray.isNotEmpty()){

            val postBitmap = BitmapFactory.decodeByteArray(postByteArray, 0, postByteArray.size)
            postPicIV.setImageBitmap(postBitmap)
        }

        val cancelButton = findViewById<Button>(R.id.treasureLeave)

        cancelButton.setOnClickListener {
            finish()
        }

        val joinButton = findViewById<Button>(R.id.treasureVerify)

        joinButton.setOnClickListener {
            val intent = Intent(this, QRScannerActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Logic not yet implemented", Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, "You joined the Hunt!", Toast.LENGTH_SHORT).show()
//            finish()
        }

    }
}