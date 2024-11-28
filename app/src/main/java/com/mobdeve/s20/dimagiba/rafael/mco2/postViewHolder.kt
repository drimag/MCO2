package com.mobdeve.s20.dimagiba.rafael.mco2

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.TreasurePostLayoutBinding

class postViewHolder (private val viewBinding: TreasurePostLayoutBinding): RecyclerView.ViewHolder(viewBinding.root) {
/*
    fun bindData(post: TreasureHunt) {
        this.viewBinding.usernameTv.text = post.poster.userName

        post.poster.profileImageId.let {
            Glide.with(this.viewBinding.userImageIv)
                .load(it)
                .circleCrop()
                .into(this.viewBinding.userImageIv)
        }

        this.viewBinding.userImageIv.tag = post.poster.profileImageId

        if (post.isVerified) {
            this.viewBinding.needsVerificationIcon.visibility = View.GONE
        } else {
            this.viewBinding.needsVerificationIcon.visibility = View.VISIBLE
        }

        post.imageId?.let { this.viewBinding.treasureImageIv.setImageResource(it) }
        this.viewBinding.treasureImageIv.tag = post.imageId
        this.viewBinding.locationDateTv.text = "${post.location.getCity()} · ${post.date.toStringNoYear()}"
        this.viewBinding.fullLocationTv.text = "${post.location.getFullLocation()}"
        this.viewBinding.fullDateTv.text = "${post.date.toStringFull()}"
        this.viewBinding.foundDateTv.text = "${post.foundDate?.toStringFull()}"
        this.viewBinding.descriptionTv.text = post.description
        this.viewBinding.participantsTv.text = "${post.participants.size}"
        this.viewBinding.winnersTv.text = "${post.winners.size}"
    }

 */

    @SuppressLint("SetTextI18n")
    fun bindData(post: TreasureHunt) {
        // Set poster's username
        this.viewBinding.usernameTv.text = post.postername

        // Load poster's profile image if available
        post.imageId?.let {
            Glide.with(this.viewBinding.userImageIv)
                .load(it)
                .circleCrop()
                .into(this.viewBinding.userImageIv)
        }
        this.viewBinding.userImageIv.tag = post.imageId

        // Show or hide verification icon
        if (post.isVerified) {
            this.viewBinding.needsVerificationIcon.visibility = View.GONE
        } else {
            this.viewBinding.needsVerificationIcon.visibility = View.VISIBLE
        }

        // Set treasure image if available
        post.imageId?.let {
            this.viewBinding.treasureImageIv.setImageResource(it)
        }
        this.viewBinding.treasureImageIv.tag = post.imageId

        // Set location and date details
        this.viewBinding.locationDateTv.text = "${getCityFromLocation(post.location)} · ${getFormattedDate(post.date)}"
        this.viewBinding.fullLocationTv.text = post.location
        this.viewBinding.fullDateTv.text = post.date
        this.viewBinding.foundDateTv.text = post.foundDate ?: "Not found yet"

        // Set description and participant/winner counts
        this.viewBinding.descriptionTv.text = post.description
        this.viewBinding.participantsTv.text = "${post.participants.size}"
        this.viewBinding.winnersTv.text = "${post.winners.size}"
    }

    private fun getCityFromLocation(location: String): String {
        // Example implementation to extract city from location
        return location.split(",").getOrNull(0) ?: "Unknown"
    }

    private fun getFormattedDate(date: String): String {
        // Example implementation to format date
        return date // Adj
    }


}