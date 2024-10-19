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

        val username = intent.getStringExtra("username1")
        val usernameTv = findViewById<TextView>(R.id.treasureUsername)
        usernameTv.text = username

        val description = intent.getStringExtra("description1")
        val descriptionTv = findViewById<TextView>(R.id.treasureDescription)
        descriptionTv.text = description

        val profilePicIV = findViewById<ImageView>(R.id.treasureImage)
        val byteArray = intent.getByteArrayExtra("pfp1")

        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
        profilePicIV.setImageBitmap(bitmap)

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