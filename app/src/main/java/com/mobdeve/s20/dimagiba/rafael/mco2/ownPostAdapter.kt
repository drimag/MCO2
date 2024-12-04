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

            if (viewBinding.needsVerificationIcon.visibility == View.VISIBLE) {
                intent.putExtra("needs_verification", true)
            }

            intent.putExtra("username", viewBinding.usernameTv.text.toString())
            intent.putExtra("description", viewBinding.descriptionTv.text.toString())
            intent.putExtra("pirates", viewBinding.participantsTv.text.toString())
            intent.putExtra("plundered", viewBinding.winnersTv.text.toString())
            intent.putExtra("pfp", viewBinding.userImageIv.tag as? Int)
            intent.putExtra("postImage", viewBinding.treasureImageIv.tag as? Int)
            intent.putExtra("fullDate", viewBinding.fullDateTv.text.toString())
            intent.putExtra("fullLocation", viewBinding.fullLocationTv.text.toString())

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

//    fun updateData(newPosts: List<TreasureHunt>) {
//        this.posts = newPosts
//        notifyDataSetChanged()
//    }

}