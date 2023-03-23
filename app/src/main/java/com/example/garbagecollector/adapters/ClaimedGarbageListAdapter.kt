package com.example.garbagecollector.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.garbagecollector.R
import com.example.garbagecollector.model.Location
import com.example.garbagecollector.util.DateFormatter

class ClaimedGarbageListAdapter(private var locationData: List<Location>) :
    RecyclerView.Adapter<ClaimedGarbageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.claimed_garbage_list_item, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locationItem = locationData[position]
        holder.garbagePublishDate.text = DateFormatter.formatDate(locationItem.createDate)
        holder.claimedGarbageAddress.text =
            "${locationItem.name}, ${locationItem.city}, ${locationItem.postalCode.toString()}"
        holder.claimedGarbagePhoto.setImageBitmap(locationItem.photo)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val garbagePublishDate: TextView = itemView.findViewById(R.id.garbage_posted_date)
        val claimedGarbagePhoto: ImageView = itemView.findViewById(R.id.posted_garbage_photo)
        val claimedGarbageAddress: TextView = itemView.findViewById(R.id.posted_garbage_address)
    }

    override fun getItemCount(): Int {
        return locationData.size
    }
}