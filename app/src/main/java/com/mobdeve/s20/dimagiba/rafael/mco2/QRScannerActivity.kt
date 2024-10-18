package com.mobdeve.s20.dimagiba.rafael.mco2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class QRScannerActivity : AppCompatActivity() {

    private lateinit var startScanningButton: Button
    private lateinit var scanResult: TextView

    // Register the activity result launcher


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        scanResult = findViewById(R.id.scan_info)

        val options = ScanOptions().setOrientationLocked(true).setPrompt("Scan a QR code")
        qrCodeLauncher.launch(options)
    }

    private val qrCodeLauncher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents == null) {
                scanResult.text = "Scan canceled"
            } else {
                scanResult.text = "QR Code Scanned: ${result.contents}"
                // Future logic to handle the treasure claim will go here
            }
        }
}
