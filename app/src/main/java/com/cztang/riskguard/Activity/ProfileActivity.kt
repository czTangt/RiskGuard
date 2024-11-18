package com.cztang.riskguard.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cztang.riskguard.Adapter.AchievementAdapter
import com.cztang.riskguard.Domain.AchievementDomain
import com.cztang.riskguard.Domain.CommonDomain
import com.cztang.riskguard.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var adapterArchive: RecyclerView.Adapter<*>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化RecyclerView
        initRecyclerViewArchive()
    }

    private fun initRecyclerViewArchive() {
        val items = ArrayList<AchievementDomain>().apply {
            add(AchievementDomain("Device Info", listOf("It displays device information, including Root, CPU, Serial, System, HardDisk, and Kernel Info."), "icon_achievement1"))
            add(AchievementDomain("Hook Status", listOf("It displays the hook status of the device."),"icon_achievement2"))
        }

        binding.viewArchive.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterArchive = AchievementAdapter(items)
        binding.viewArchive.adapter = adapterArchive
    }
}