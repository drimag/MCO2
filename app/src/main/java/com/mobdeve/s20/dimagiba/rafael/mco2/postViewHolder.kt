package com.mobdeve.s20.dimagiba.rafael.mco2

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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
        this.viewBinding.locationDateTv.text = "${post.location.getCity()} · ${post.date.toStringNoYear()}"
        this.viewBinding.fullLocationTv.text = "${post.location.getFullLocation()}"
        this.viewBinding.fullDateTv.text = "${post.date.toStringFull()}"
        this.viewBinding.descriptionTv.text = post.description
        this.viewBinding.participantsTv.text = "${post.participants.size}"
        this.viewBinding.winnersTv.text = "${post.winners.size}"
    }


}