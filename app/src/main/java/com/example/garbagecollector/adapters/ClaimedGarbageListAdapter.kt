package com.example.garbagecollector.adapters

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.garbagecollector.R
import com.example.garbagecollector.repository.web.dto.LocationDto
import com.example.garbagecollector.util.DateFormatter

class ClaimedGarbageListAdapter(private var locationData: List<LocationDto>) :
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
        //Format received date
        holder.garbagePublishDate.text = DateFormatter.convertDateFormat(locationItem.creationDate.toString())
        holder.claimedGarbageAddress.text =
            "${locationItem.name}, ${locationItem.city}, ${locationItem.postalCode.toString()}"
        //Decode the string into byte array
        val byteArray = Base64.decode(locationItem.photo, Base64.DEFAULT)
        //Decode to Bitmap to set it
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        holder.claimedGarbagePhoto.setImageBitmap(bitmap)
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