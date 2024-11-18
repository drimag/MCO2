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

class foundPostAdapter (private val data: ArrayList<TreasureHunt>, private val context: Context) : Adapter<postViewHolder>() {

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
            val intent : Intent = Intent(myViewHolder.itemView.context, FoundTreasureActivity::class.java)

            intent.putExtra("username_found", viewBinding.usernameTv.text.toString())
            intent.putExtra("description_found", viewBinding.descriptionTv.text.toString())
            intent.putExtra("pirates_found", viewBinding.participantsTv.text.toString())
            intent.putExtra("plundered_found", viewBinding.winnersTv.text.toString())
            intent.putExtra("pfp_found", viewBinding.userImageIv.tag as? Int)
            intent.putExtra("postImage_found", viewBinding.treasureImageIv.tag as? Int)
            intent.putExtra("fullDate_found", viewBinding.fullDateTv.text.toString())
            intent.putExtra("fullLocation_found", viewBinding.fullLocationTv.text.toString())
            intent.putExtra("foundDate_found", viewBinding.foundDateTv.text.toString())

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