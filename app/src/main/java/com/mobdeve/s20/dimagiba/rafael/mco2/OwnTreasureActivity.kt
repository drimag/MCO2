package com.mobdeve.s20.dimagiba.rafael.mco2

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class OwnTreasureActivity : AppCompatActivity() {

    private val verificationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            finish()
            val builder = AlertDialog.Builder(this@OwnTreasureActivity)
            builder.setTitle("Hooray!")
            builder.setMessage("Your treasure post has been verified! Other users can now join in your treasure hunt!")

            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Close the dialog when OK is clicked
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pending_post)
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

//        val pfp = intent.getIntExtra("pfp_own", 0)
        val pfp = intent.getIntExtra("pfp", 0)
        val pfpTv = findViewById<ImageView>(R.id.treasureImage)

        pfp.let {
            Glide.with(pfpTv)
                .load(it)
                .circleCrop()
                .into(pfpTv)
        }

        val postImage = intent.getIntExtra("postImage", 0)
        val postImageTv = findViewById<ImageView>(R.id.postImageIv)
        postImageTv.setImageResource(postImage)

        val cancelButton = findViewById<Button>(R.id.treasureLeave)

        cancelButton.setOnClickListener {
            finish()
        }


        //do you need this?
        val joinButton = findViewById<Button>(R.id.treasureVerify)

        joinButton.setOnClickListener {
            val intent = Intent(this, QRScannerActivity::class.java)
            verificationLauncher.launch(intent)
            //Toast.makeText(this, "Logic not yet implemented", Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, "You joined the Hunt!", Toast.LENGTH_SHORT).show()
//            finish()
        }

    }
}