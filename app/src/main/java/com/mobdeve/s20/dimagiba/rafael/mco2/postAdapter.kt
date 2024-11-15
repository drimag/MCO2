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
        myViewHolder.itemView.setOnClickListener {
            val intent : Intent = Intent(myViewHolder.itemView.context, TreasureActivity::class.java)

            intent.putExtra("username", viewBinding.usernameTv.text.toString())
            intent.putExtra("description", viewBinding.descriptionTv.text.toString())
            intent.putExtra("pirates", viewBinding.participantsTv.text.toString())
            intent.putExtra("plundered", viewBinding.winnersTv.text.toString())
            val drawable = viewBinding.userImageIv.drawable as BitmapDrawable
            val bitmap = drawable.bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            intent.putExtra("pfp", byteArray)

            if (viewBinding.treasureImageIv.drawable != null ) {
                val postImage_drawable = viewBinding.treasureImageIv.drawable as BitmapDrawable
                val postImage_bitmap = postImage_drawable.bitmap
                val postImage_stream = ByteArrayOutputStream()
                postImage_bitmap.compress(Bitmap.CompressFormat.PNG, 100, postImage_stream)
                val postImage_byteArray = postImage_stream.toByteArray()
                intent.putExtra("postImage", postImage_byteArray)
            }

            // Fix date and loc (separate two in layout)
//          intent.putExtra(TreasureActivity.DATE_KEY, viewBinding.dateTv.text.toString())

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