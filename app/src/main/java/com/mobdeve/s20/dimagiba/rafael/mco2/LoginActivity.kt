package com.mobdeve.s20.dimagiba.rafael.mco2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toRegisterBtn = findViewById<Button>(R.id.to_register_bt)

        toRegisterBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val loginBtn = findViewById<Button>(R.id.login_submit_bt)
        val loginUsername = findViewById<TextView>(R.id.login_username_et)
        val loginPassword = findViewById<TextView>(R.id.login_password_et)


        loginBtn.setOnClickListener {


            //import here for checking of firebase account

            val username = loginUsername.text.toString()
            val password = loginPassword.text.toString()

            loginUser(username, password)

            //Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
        }
    }


    private fun loginUser(userName: String, password: String) {
        val db = FirebaseFirestore.getInstance()

        // Query Firestore for the user by username
        db.collection("Users")
            .whereEqualTo("userName", userName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    // Get the document containing the user's details
                    val document = querySnapshot.documents[0]
                    val storedPassword = document.getString("password") // Retrieve the password stored in Firestore

                    // Check if the entered password matches the stored password
                    if (storedPassword == password) {
                        // Password matches, login is successful
                        Toast.makeText(this, "Correct password", Toast.LENGTH_SHORT).show()
                        // Proceed to the next activity (MainActivity or home screen)
                    } else {
                        // Password mismatch
                        Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Username not found
                    Toast.makeText(this, "Username not found!", Toast.LENGTH_SHORT).show()
                    //Log.e("LoginUser", "Username not found")
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error Getting Data", Toast.LENGTH_SHORT).show()
            }
    }
}