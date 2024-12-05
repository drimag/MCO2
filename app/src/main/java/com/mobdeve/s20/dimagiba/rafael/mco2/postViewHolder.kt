package com.mobdeve.s20.dimagiba.rafael.mco2

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s20.dimagiba.rafael.mco2.databinding.TreasurePostLayoutBinding
import java.util.Date
import java.util.Locale

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
        val context = viewBinding.root.context
        val pfp = getDrawableIdFromString(context,post.posterPfp?:"");
        // Load poster's profile image if available
        pfp.let {
            Glide.with(this.viewBinding.userImageIv)
                .load(it)
                .circleCrop()
                .into(this.viewBinding.userImageIv)
        }
        this.viewBinding.userImageIv.tag = pfp

        // Show or hide verification icon
        if (post.isVerified) {
            this.viewBinding.needsVerificationIcon.visibility = View.GONE
        } else {
            this.viewBinding.needsVerificationIcon.visibility = View.VISIBLE
        }

        // Set treasure image if available
        post.imageId.let {
            this.viewBinding.treasureImageIv.setImageResource(post.imageId)
        }
        this.viewBinding.treasureImageIv.tag = post.imageId

        // Set location and date details
        this.viewBinding.locationDateTv.text = "${getCityFromLocation(post.location)} · ${getFormattedDate(post.date)}"
        this.viewBinding.fullLocationTv.text = post.location
        this.viewBinding.fullDateTv.text = getLongerDate(post.date)
        this.viewBinding.foundDateTv.text = post.foundDate ?: "Not found yet"

        // Set description and participant/winner counts
        this.viewBinding.descriptionTv.text = post.description
        this.viewBinding.participantsTv.text = "${post.participants.size}"
        this.viewBinding.winnersTv.text = "${post.winners.size}"
    }

    private fun getCityFromLocation(location: String): String {
        // Example implementation to extract city from location
        val components = location.split(",").map { it.trim() }
        return if (components.size >= 2) {
            components[components.size - 2] // Return the second-to-last component
        } else {
            "Unknown location"
        }
    }

    private fun getFormattedDate(date: String): String {
        return try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            isoFormat.timeZone = android.icu.util.TimeZone.getTimeZone("Asia/Taipei")
            val parsedDate: Date = isoFormat.parse(date)!!

            val outputFormat = SimpleDateFormat("MMM d", Locale.US)
            outputFormat.format(parsedDate)
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    private fun getLongerDate(date: String): String {
        return try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            isoFormat.timeZone = android.icu.util.TimeZone.getTimeZone("Asia/Taipei")
            val parsedDate: Date = isoFormat.parse(date)!!

            val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
            outputFormat.format(parsedDate)
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    private fun getDrawableIdFromString(context: Context, drawableString: String): Int {
        if (drawableString.startsWith("R.drawable.")) {
            val resourceName = drawableString.substring("R.drawable.".length)
            return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        }

        return 0
    }

}