package com.mobdeve.s20.dimagiba.rafael.mco2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.graphics.Bitmap
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.firestore.FirebaseFirestore


class AddTreasureActivity : AppCompatActivity() {

    private lateinit var cancelBtn: Button
    private lateinit var postBtn : Button
    private lateinit var treasureEt : EditText
    private lateinit var userPFP : ImageView
    private lateinit var generateQR: Button
    private lateinit var QRCode: ImageView
    private lateinit var setTreas: ImageView
    private lateinit var treasureLocation: TextView
    private lateinit var treasureLocationBtn: LinearLayout
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var treasureID: String

    private val setLocationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val newLocation = result.data?.getStringExtra(MapActivity.NEW_LOCATION) ?: "LocationError"
            treasureLocation.text = newLocation
            treasureEt.isEnabled = true;
            generateQR.isEnabled = true;
        }
    }

    companion object {
        const val TREASURE_CONTENT_KEY = "TREASURE_CONTENT"
        const val LOCATION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val db = FirebaseFirestore.getInstance()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        analytics = Firebase.analytics
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_treasure)

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val pfpString = sharedPreferences.getString("pfp", null) // Retrieve username
        val pfp = pfpString?.let { getDrawableIdFromString(it) };

        this.cancelBtn = findViewById<Button>(R.id.cancelBtn)
        this.postBtn = findViewById<Button>(R.id.postBtn)
        this.treasureEt = findViewById<EditText>(R.id.treasureET)
        this.userPFP = findViewById<ImageView>(R.id.addTreasureUserImageIV)
        this.generateQR = findViewById<Button>(R.id.button2)
        this.QRCode = findViewById<ImageView>(R.id.QRCode)
        this.treasureLocationBtn = findViewById<LinearLayout>(R.id.treasure_location_ll)
        this.treasureLocation = findViewById<TextView>(R.id.treasureLocation)

        generateQR.isEnabled = false;


        //val pfp = intent.getIntExtra("userPFP", 0)

        R.drawable.chopper.let {
            Glide.with(userPFP)
                .load(pfp)
                .circleCrop()
                .into(userPFP)
        }

        this.cancelBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

        this.treasureLocationBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            this.setLocationLauncher.launch(intent)
        })

        this.postBtn.setOnClickListener(View.OnClickListener { view ->
            if (this.treasureEt.text.toString().isNotEmpty() && this.treasureID.isNotEmpty()){
                val intent : Intent = Intent()
                intent.putExtra(AddTreasureActivity.TREASURE_CONTENT_KEY, this.treasureEt.text.toString())

                val userId = sharedPreferences.getString("userId", null) // Retrieve user ID
                val username = sharedPreferences.getString("username", null) // Retrieve username
                val customDate = CustomDate()
                val formattedDate = customDate.toISO8601String()

                val treasureHunt = TreasureHunt(
                    treasureID = treasureID,
                    Tdescription = this.treasureEt.text.toString(),
                    posterID = userId.toString(),
                    Postername = username.toString(),
                    Date = formattedDate,
                    posterPfp = pfpString?:"null",
                    Location = treasureLocation.text.toString() // Example lat, lng coordinates
                )


// Save the TreasureHunt object to Firestore
                db.collection("Treasures").document(treasureHunt.id).set(treasureHunt)
                    .addOnSuccessListener {
                        // Successfully added the treasure hunt to Firestore
                        Log.d("Firestore", "Treasure hunt added successfully!")
                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        Log.e("Firestore", "Error adding treasure hunt", e)
                    }
                //instantiate the class, get the customdate, location etc
                //add it to the firebase

                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this@AddTreasureActivity,
                    "Please make your tweet has text/Generate a QR code to print.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        this.generateQR.setOnClickListener(View.OnClickListener {

            //val qrContent = "https://example.com"
            //change QR code to link to treasure

            //val qrBitmap = generateQRCode(qrContent)
            //qrBitmap?.let {
                //this.QRCode.setImageBitmap(it)
            //}

            //STORE THE ID OF THE TREASURE IN THE QR CODE INSTEAD OF LOCATION SINCE PRINTING IT PALA
            treasureID = db.collection("Treasure").document().id
            val qrBitmap = generateQRCode(treasureID)
            qrBitmap?.let {
                // Display the generated QR code on ImageView
                QRCode.setImageBitmap(it)
            }


            //checkLocationPermissionAndGenerateQR()

            //checking analytics if it connects
/*
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(FirebaseAnalytics.Param.ITEM_ID, "123");
                param(FirebaseAnalytics.Param.ITEM_NAME, "test");
                param(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            }

 */
            generateQR.isEnabled = false

        })


    }
    private fun checkLocationPermissionAndGenerateQR() {
        // Check for permission to access fine and coarse location
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        } else {
            // Permissions granted, proceed to get the location
            getLastLocation { location ->
                location?.let {
                    // Create QR code content with location info
                    val qrContent = "Lat: ${it.latitude}, Lon: ${it.longitude}"

                    // Show Toast with location coordinates
                    Toast.makeText(this@AddTreasureActivity, qrContent, Toast.LENGTH_LONG).show()

                    // Generate QR code with location information
                    val qrBitmap = generateQRCode(qrContent)
                    qrBitmap?.let {
                        // Display the generated QR code on ImageView
                        QRCode.setImageBitmap(it)
                    }
                } ?: run {
                    // Handle case when location is not available
                    Toast.makeText(this, "Failed to fetch location", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(callback: (android.location.Location?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    callback(location) // pass the location object to the callback
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error getting location: ${it.message}", Toast.LENGTH_SHORT).show()
                callback(null)
            }
    }


    private fun generateQRCode(content: String): Bitmap? {
        return try {
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400)
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get location
                checkLocationPermissionAndGenerateQR()
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getDrawableIdFromString(drawableString: String): Int {
        if (drawableString.startsWith("R.drawable.")) {
            val resourceName = drawableString.substring("R.drawable.".length)
            return resources.getIdentifier(resourceName, "drawable", packageName)
        } else {
            throw IllegalArgumentException("Invalid drawable string: $drawableString")
        }
    }
}