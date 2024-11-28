package com.mobdeve.s20.dimagiba.rafael.mco2

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView.Adapter
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

             */
            val intent : Intent = Intent(myViewHolder.itemView.context, TreasureActivity::class.java)

            //get the following from the firebaseDB
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