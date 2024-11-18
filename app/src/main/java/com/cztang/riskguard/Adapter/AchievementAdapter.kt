package com.cztang.riskguard.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cztang.riskguard.Domain.AchievementDomain
import com.cztang.riskguard.R

class AchievementAdapter(private val items: ArrayList<AchievementDomain>) :
    RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AchievementAdapter.ViewHolder {
        context = parent.context
        val inflater =
            LayoutInflater.from(context).inflate(R.layout.viewholder_achievement, parent, false)
        return ViewHolder(inflater)
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: AchievementAdapter.ViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.status.text = items[position].data[0]
        val drawableResourceId = holder.itemView.resources.getIdentifier(
            items[position].picPath, "drawable", context?.packageName
        )

        context?.let {
            Glide.with(it)
                .load(drawableResourceId)
                .into(holder.pic)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.titleText_achievement)
        var status: TextView = itemView.findViewById(R.id.statusText_achievement)
        var pic: ImageView = itemView.findViewById(R.id.pic_achievement)
    }
}