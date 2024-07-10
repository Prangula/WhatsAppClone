package com.example.whatsappclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone.R
import com.example.whatsappclone.models.ContactData
import de.hdodenhof.circleimageview.CircleImageView

class ContactAdapter
    :RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

        inner class ViewHolder(itemView: View)
            :RecyclerView.ViewHolder(itemView){
            val chatItemIv:CircleImageView= itemView.findViewById(R.id.chat_item_iv)
            val chatItemName: TextView = itemView.findViewById(R.id.chat_item_name)
            val chatItemMessage: TextView = itemView.findViewById(R.id.chat_item_message)
            val chatItemTime: TextView = itemView.findViewById(R.id.chat_item_time)
            }

    private val diffCallBack = object : DiffUtil.ItemCallback<ContactData>(){
        override fun areItemsTheSame(
            oldItem: ContactData,
            newItem: ContactData
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem:ContactData,
            newItem: ContactData
        ): Boolean {
            return newItem == oldItem
        }

    }

    private val differ = AsyncListDiffer(this,diffCallBack)
    var items:List<ContactData>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_item,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            chatItemName.text = item.name
            chatItemTime.text = getTimeAgo(item.date.time)
            chatItemMessage.text = item.message
            Glide
                .with(holder.itemView)
                .load(item.image)
                .into(chatItemIv)
        }


    }
    private fun getTimeAgo(previousTime: Long): String {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - previousTime

        val seconds = timeDifference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days >= 365 -> "${days / 365} Y"
            days >= 30 -> "${days / 30} M"
            days >= 1 -> "$days D"
            hours >= 1 -> "$hours H"
            minutes >= 1 -> "$minutes M"
            else -> "just now"
        }
    }
}