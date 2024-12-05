package com.mobdeve.s20.dimagiba.rafael.mco2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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

    fun verifyTreasure(treasureRef: DocumentReference) {
        treasureRef.update("verified", true)
            .addOnSuccessListener {
                Toast.makeText(this, "Verified!", Toast.LENGTH_LONG).show()
                val intent : Intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error verifying treasure", e)
            }
    }

    // Helper function to update the treasure's winners and participants
    fun updateTreasureParticipantsAndWinners(treasureRef: DocumentReference, userRef: DocumentReference, userId: String, treasureID: String) {
        treasureRef.update("winners", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                treasureRef.update("participants", FieldValue.arrayRemove(userId))
                    .addOnSuccessListener {
                        updateUserHuntLists(userRef, treasureID)
                        Toast.makeText(this, "Treasure Claimed!", Toast.LENGTH_LONG).show()
                        val intent : Intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error removing user from participants", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding user to winners", e)
            }
    }

    // Helper function to update the user's hunt lists
    fun updateUserHuntLists(userRef: DocumentReference, treasureID: String) {
        userRef.update("foundHunts", FieldValue.arrayUnion(treasureID))
            .addOnSuccessListener {
                userRef.update("joinedHunts", FieldValue.arrayRemove(treasureID))
                    .addOnSuccessListener {
                        scanResult.text = "Treasure hunt completed successfully!"
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error removing treasure from joinedHunts", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding treasure to foundHunts", e)
            }
    }

    private val qrCodeLauncher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            if (result.contents == null) {
                scanResult.text = "Scan canceled"
            } else {
                scanResult.text = "QR Code Scanned: ${result.contents}"
                val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                val db = FirebaseFirestore.getInstance()
                val userId = sharedPreferences.getString("userId", null) // Retrieve currentuser ID
                val treasureID = result.contents; // scanned returns a treasureID

                // if ID is a treasure hunt found in the "posts" list of the current user in the "Users" collection:
                // look for a treasure in "Treasures" collection with same ID and change verified from false to true

                // else if ID is a treasure hunt found in the "Treasures" collection:
                // add the userID to the "winners" list of the treasure in the treasure collection
                // remove the user from the "participants" list of the treasure

                // add the treasureID to the user's "foundHunts" list in the user collection
                // remove the treasureID to the user's "joinedHunts" list in the user collection

                if (userId == null || treasureID == null) {
                    scanResult.text = "Invalid user or treasure ID."
                    return@registerForActivityResult // TODO: what does this do
                }

                val userRef = db.collection("Users").document(userId)
                val treasureRef = db.collection("Treasures").document(treasureID)

                userRef.get().addOnSuccessListener { userSnapshot ->
                    if (!userSnapshot.exists()) {
                        scanResult.text = "User not found in Firestore."
                        return@addOnSuccessListener
                    }

                    val posts = userSnapshot.get("posts") as? List<String>
                    if (posts?.contains(treasureID) == true) { // the scanned post is the user's
                        // this means they are attempting to verify posting
                        verifyTreasure(treasureRef)

                    } else {
                        // update winners and participants since its not their post, they are a participant (possible also they just found it)
                        treasureRef.get().addOnSuccessListener { treasureSnapshot ->
                            if (!treasureSnapshot.exists()) {
                                scanResult.text = "Treasure ID not found in Treasures collection."
                                return@addOnSuccessListener
                            }

                            updateTreasureParticipantsAndWinners(treasureRef, userRef, userId, treasureID)

                        }.addOnFailureListener { e ->
                            Log.e("Firestore", "Error retrieving treasure", e)
                        }
                    }
                }.addOnFailureListener { e ->
                    Log.e("Firestore", "Error retrieving user", e)
                }
            }
        }


}
