package com.mobdeve.s20.dimagiba.rafael.mco2

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.TreasurePostLayoutBinding
import java.io.ByteArrayOutputStream

class postAdapter (private val data: ArrayList<TreasureHunt>, private val context: Context) : Adapter<postViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewHolder {
        // Initialize the ViewBinding of an item's layout
        val viewBinding: TreasurePostLayoutBinding = TreasurePostLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val myViewHolder = postViewHolder(viewBinding)

        // Provide logic for clicking on anywhere on the entire itemView of the ViewHolder
        // TODO:: i think this is where we put the post details
        //get the item attributes and set it here

        myViewHolder.itemView.setOnClickListener {

            //check if user has completed it or not and if it is, go to found page, if not, go
            //to joined treasure page

            /* if(user has completed activity) {
                val intent : Intent = Intent(myViewHolder.itemView.context, FoundTreasureActivity::class.java
            } else {
                val intent : Intent = Intent(myViewHolder.itemView.context, TreasureActivity::class.java)
            }
            JoinedTreasureActivity::class.java
             */
            val position = myViewHolder.adapterPosition

            val treasureHunt = data[position]
            val treasureId = treasureHunt.id
            val treasurePoster = treasureHunt.posterId
            val winners = treasureHunt.winners
            val participants = treasureHunt.participants

            // Access SharedPreferences to retrieve the current user's ID
            val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("userId", null)

            if (userId == null) {
                Toast.makeText(context, "User not logged in!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Determine the intent based on the user's role
            val intent: Intent = when {
                userId == treasurePoster -> {
                    // User is the poster
                    Intent(myViewHolder.itemView.context, OwnTreasureActivity::class.java)
                }
                winners.contains(userId) -> {
                    // User is a winner
                    Intent(myViewHolder.itemView.context, FoundTreasureActivity::class.java)
                }
                participants.contains(userId) -> {
                    // User is a participant
                    Intent(myViewHolder.itemView.context, JoinedTreasureActivity::class.java)
                }
                else -> {
                    // Default case
                    Intent(myViewHolder.itemView.context, TreasureActivity::class.java)
                }
            }

            // Pass data to the next activity
            intent.putExtra("TREASURE_ID", treasureId)
            intent.putExtra("USER_ID", userId)

            //get the following from the firebaseDB
//            intent.putExtra("treasureID", data[position].id) // the treasure ID not sure yet if needed
            intent.putExtra("username", viewBinding.usernameTv.text.toString())
            intent.putExtra("description", viewBinding.descriptionTv.text.toString())
            intent.putExtra("pirates", viewBinding.participantsTv.text.toString())
            intent.putExtra("plundered", viewBinding.winnersTv.text.toString())
            intent.putExtra("pfp", viewBinding.userImageIv.tag as? Int)
            intent.putExtra("post_image", viewBinding.treasureImageIv.tag as? Int)
            intent.putExtra("fullDate", viewBinding.fullDateTv.text.toString())
            intent.putExtra("fullLocation", viewBinding.fullLocationTv.text.toString())

            //check if user has completed it or not and if it is, go to found page, if not, go
            //to joined treasure page

            context.startActivity(intent)
        }
        return myViewHolder
    }

    override fun onBindViewHolder(holder: postViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}