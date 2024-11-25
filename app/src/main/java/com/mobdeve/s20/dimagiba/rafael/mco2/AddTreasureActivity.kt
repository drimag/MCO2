package com.mobdeve.s20.dimagiba.rafael.mco2

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder



class AddTreasureActivity : AppCompatActivity() {

    private lateinit var cancelBtn: Button
    private lateinit var postBtn : Button
    private lateinit var treasureEt : EditText
    private lateinit var userPFP : ImageView
    private lateinit var generateQR: Button
    private lateinit var QRCode: ImageView

    companion object {
        const val TREASURE_CONTENT_KEY = "TREASURE_CONTENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_treasure)

        this.cancelBtn = findViewById<Button>(R.id.cancelBtn)
        this.postBtn = findViewById<Button>(R.id.postBtn)
        this.treasureEt = findViewById<EditText>(R.id.treasureET)
        this.userPFP = findViewById<ImageView>(R.id.addTreasureUserImageIV)
        this.generateQR = findViewById<Button>(R.id.button2)
        this.QRCode = findViewById<ImageView>(R.id.QRCode)


        val pfp = intent.getIntExtra("userPFP", 0)

        pfp.let {
            Glide.with(userPFP)
                .load(it)
                .circleCrop()
                .into(userPFP)
        }

        this.cancelBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

        this.postBtn.setOnClickListener(View.OnClickListener { view ->
            if (this.treasureEt.text.toString().isNotEmpty()){
                val intent : Intent = Intent()
                intent.putExtra(AddTreasureActivity.TREASURE_CONTENT_KEY, this.treasureEt.text.toString())

                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this@AddTreasureActivity,
                    "Please make your tweet has text",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        this.generateQR.setOnClickListener(View.OnClickListener {

            val qrContent = "https://example.com"
            //change QR code to link to treasure

            val qrBitmap = generateQRCode(qrContent)
            qrBitmap?.let {
                this.QRCode.setImageBitmap(it)
            }

        })


    }

    fun generateQRCode(content: String): Bitmap? {
        return try {
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400)
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}