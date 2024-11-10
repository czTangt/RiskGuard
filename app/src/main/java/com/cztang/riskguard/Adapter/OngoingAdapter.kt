package com.cztang.riskguard.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cztang.riskguard.Activity.DeviceActivity
import com.cztang.riskguard.Activity.MainActivity
import com.cztang.riskguard.Domain.OngoingDomain
import com.cztang.riskguard.R

class OngoingAdapter(private val items: ArrayList<OngoingDomain>) : RecyclerView.Adapter<OngoingAdapter.ViewHolder>() {

    private var context: Context? = null

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context).inflate(R.layout.viewholder_ongoing, parent, false)
        return ViewHolder(inflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.date.text = items[position].data
        holder.progressBarPercent.text = items[position].progressPercent.toString() + "%"

        val drawableResourceId = holder.itemView.resources.getIdentifier(
            items[position].picPath, "drawable", context?.packageName
        )

        context?.let {
            Glide.with(it)
                .load(drawableResourceId)
                .into(holder.pic)
        }

        holder.progressBar.progress = items[position].progressPercent

        // Configure background color for position 0 as dark, others as light
        if (position == 0) {
            holder.layout.setBackgroundResource(R.drawable.bg_dark)
            context?.getColor(R.color.white)?.let { holder.title.setTextColor(it) }
            context?.getColor(R.color.white)?.let { holder.date.setTextColor(it) }
            context?.getColor(R.color.white)?.let { holder.progressText.setTextColor(it) }
            context?.getColor(R.color.white)?.let { holder.progressBarPercent.setTextColor(it) }
            holder.pic.setColorFilter(getColor(context!!, R.color.white), PorterDuff.Mode.SRC_IN)
            holder.progressBar.progressTintList = context?.getColor(R.color.white)
                ?.let { ColorStateList.valueOf(it) }
        } else {
            holder.layout.setBackgroundResource(R.drawable.bg_light)
            context?.getColor(R.color.dark_blue)?.let { holder.title.setTextColor(it) }
            context?.getColor(R.color.dark_blue)?.let { holder.date.setTextColor(it) }
            context?.getColor(R.color.dark_blue)?.let { holder.progressText.setTextColor(it) }
            context?.getColor(R.color.dark_blue)?.let { holder.progressBarPercent.setTextColor(it) }
            holder.pic.setColorFilter(getColor(context!!, R.color.dark_blue), PorterDuff.Mode.SRC_IN)
            holder.progressBar.progressTintList = context?.getColor(R.color.dark_blue)
                ?.let { ColorStateList.valueOf(it) }
        }

        // Set click event listener, click different items to jump to different Activities
        holder.itemView.setOnClickListener {
            val intent = if (position == 0) Intent(context, DeviceActivity::class.java) else Intent(context, MainActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.titleText_ongoing)
        var date: TextView = itemView.findViewById(R.id.dataText_ongoing)
        var progressBarPercent: TextView = itemView.findViewById(R.id.percentText_ongoing)
        var progressText: TextView = itemView.findViewById(R.id.progressText_ongoing)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar_ongoing)
        var pic: ImageView = itemView.findViewById(R.id.pic_ongoing)
        var layout: ConstraintLayout = itemView.findViewById(R.id.Component_ongoing)
    }
}