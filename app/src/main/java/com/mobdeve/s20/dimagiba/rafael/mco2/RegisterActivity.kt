package com.mobdeve.s20.dimagiba.rafael.mco2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = FirebaseDatabase.getInstance().reference

        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toLoginBtn = findViewById<Button>(R.id.to_login_bt)
        val regUsername = findViewById<TextView>(R.id.register_username_et)
        val regPassword = findViewById<TextView>(R.id.register_password_et)

        toLoginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerBtn = findViewById<Button>(R.id.register_submit_bt)

        registerBtn.setOnClickListener {

            //send query to firebase for this
            val username = regUsername.text.toString()
            val password = regPassword.text.toString()

            registerUser(username, password) { success, message ->
                if (success) {
                    Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()

                    // Navigate to the next screen
                } else {
                    Toast.makeText(this, "Registration failed: $message", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    private fun registerUser(userName: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()


        // Generate a unique ID for the user
        val userId = db.collection("Users").document().id

        val user = User(
            id = userId,
            userName = userName,
            password = password
        )


        // Check if the username already exists
        db.collection("Users").whereEqualTo("userName", userName).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Username is available, save the user
                    db.collection("Users").document(userId).set(user)
                        .addOnSuccessListener {

                            val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userId", userId) // Store the user's ID
                            editor.putString("username", userName) // Store the username
                            editor.apply()

                            // Launch the MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear back stack
                            startActivity(intent)

                            onComplete(true, userId) // Notify success


                        }
                        .addOnFailureListener { exception ->
                            exception.printStackTrace()
                            onComplete(false, null) // Notify failure
                        }
                } else {
                    // Username already exists
                    onComplete(false, "Username already exists")
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onComplete(false, null)
            }
    }


}