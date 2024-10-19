package com.mobdeve.s20.dimagiba.rafael.mco2

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.TreasurePostLayoutBinding

class postAdapter (private val data: ArrayList<TreasureHunt>) : Adapter<postViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewHolder {
        // Initialize the ViewBinding of an item's layout
        val tweetViewBinding: TreasurePostLayoutBinding = TreasurePostLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val myViewHolder = postViewHolder(tweetViewBinding)

        // Provide logic for clicking on anywhere on the entire itemView of the ViewHolder
        // TODO:: i think this is where we put the post details
        myViewHolder.itemView.setOnClickListener {
//            val intent : Intent = Intent(myViewHolder.itemView.context, TweetDetailsActivity::class.java)
//
//            intent.putExtra(TweetDetailsActivity.USERNAME_KEY, tweetViewBinding.usernameTv.text.toString())
//            intent.putExtra(TweetDetailsActivity.USERHANDLE_KEY, tweetViewBinding.userHandleTv.text.toString())
//            intent.putExtra(TweetDetailsActivity.CAPTION_KEY, tweetViewBinding.tweetContentTv.text.toString())
//            intent.putExtra(TweetDetailsActivity.LIKES_KEY, tweetViewBinding.likesTv.text.toString())
//            val imageId = tweetViewBinding.userImageIv.tag as? Int
//            intent.putExtra(TweetDetailsActivity.PFP_KEY, imageId)
//            intent.putExtra(TweetDetailsActivity.DATE_KEY, tweetViewBinding.dateTv.text.toString())
//
//            this.myActivityResultLauncher.launch(intent)
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