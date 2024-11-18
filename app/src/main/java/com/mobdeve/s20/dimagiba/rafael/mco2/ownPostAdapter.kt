package com.mobdeve.s20.dimagiba.rafael.mco2

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.ActivityPendingPostBinding
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.TreasurePostLayoutBinding
import java.io.ByteArrayOutputStream

class ownPostAdapter (private val data: ArrayList<TreasureHunt>, private val context: Context) : Adapter<postViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewHolder {
        // Initialize the ViewBinding of an item's layout
        val viewBinding: TreasurePostLayoutBinding = TreasurePostLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val myViewHolder = postViewHolder(viewBinding)

        // Provide logic for clicking on anywhere on the entire itemView of the ViewHolder
        // TODO:: i think this is where we put the post details
        myViewHolder.itemView.setOnClickListener {
            val intent : Intent = Intent(myViewHolder.itemView.context, OwnTreasureActivity::class.java)

            intent.putExtra("username_own", viewBinding.usernameTv.text.toString())
            intent.putExtra("description_own", viewBinding.descriptionTv.text.toString())
            intent.putExtra("pirates_own", viewBinding.participantsTv.text.toString())
            intent.putExtra("plundered_own", viewBinding.winnersTv.text.toString())
            intent.putExtra("pfp_own", viewBinding.userImageIv.tag as? Int)
            intent.putExtra("postImage_own", viewBinding.treasureImageIv.tag as? Int)
            intent.putExtra("fullDate_own", viewBinding.fullDateTv.text.toString())
            intent.putExtra("fullLocation_own", viewBinding.fullLocationTv.text.toString())

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