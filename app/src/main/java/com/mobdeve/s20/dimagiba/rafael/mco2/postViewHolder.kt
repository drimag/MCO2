package com.mobdeve.s20.dimagiba.rafael.mco2

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.TreasurePostLayoutBinding

class postViewHolder (private val viewBinding: TreasurePostLayoutBinding): RecyclerView.ViewHolder(viewBinding.root) {

    fun bindData(post: TreasureHunt) {
        this.viewBinding.usernameTv.text = post.poster.userName

        post.poster.profileImageId.let {
            Glide.with(this.viewBinding.userImageIv)
                .load(it)
                .circleCrop()
                .into(this.viewBinding.userImageIv)
        }

        post.imageId?.let { this.viewBinding.treasureImageIv.setImageResource(it) }
        this.viewBinding.dateTv.text = post.date.toStringNoYear()
        this.viewBinding.descriptionTv.text = post.description
        this.viewBinding.participantsTv.text = "${post.participants.size} Pirates"
        this.viewBinding.winnersTv.text = "${post.winners.size} Plundered"
    }


}