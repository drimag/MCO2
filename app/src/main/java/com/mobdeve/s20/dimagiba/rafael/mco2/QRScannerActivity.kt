package com.mobdeve.s20.dimagiba.rafael.mco2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class QRScannerActivity : AppCompatActivity() {

    private lateinit var scanResult: TextView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        scanResult = findViewById(R.id.scan_info)
        toolbar = findViewById(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            finish() // Finish the activity and go back
        }

        val options = ScanOptions()
            .setPrompt("Scan a QR code")
            .setOrientationLocked(true)

        qrCodeLauncher.launch(options)
    }

    private val qrCodeLauncher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents == null) {
                scanResult.text = "Scan canceled"
            } else {
                scanResult.text = "QR Code Scanned: ${result.contents}"
                // Future logic to handle the treasure claim will go here
                Toast.makeText(this,scanResult.toString(), Toast.LENGTH_SHORT)

                //get the information from the QR code, which should only include the ID of the treasure
                //if the ID of the treasure matches that of the treasure

                // if ID is a treasure hunt from another user in the database:
                // user might already be a winner,
                // add the userID to the winner list of the treasure, use the savedpreferences for that
                // remove the user from the participants
                // add the treasureID to the userID list of foundtreasure
                // remove the treasureID from the users list of joinedtreasure

                // if ID is a treasure hunt of the user that they are trying to verify:
                // change state of the post to verified?
                //



            }
        }
}
