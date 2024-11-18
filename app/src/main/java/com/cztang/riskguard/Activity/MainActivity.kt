package com.cztang.riskguard.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cztang.riskguard.Adapter.OngoingAdapter
import com.cztang.riskguard.Domain.OngoingDomain
import com.cztang.riskguard.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterOngoing: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化RecyclerView
        initRecyclerView()
        // 底部导航
        bottomnavigation()
    }

    private fun bottomnavigation() {
        binding.homeMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.profileMain.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.backMain.setOnClickListener {
            finishAffinity()
            exitProcess(0)
        }
    }

    private fun initRecyclerView() {
        val items = ArrayList<OngoingDomain>().apply {
            add(OngoingDomain("Device Info", "Nov 8, 2024", 80, "icon_device"))
            add(OngoingDomain("Hook Status", "Nov 12, 2024", 90, "icon_hook"))
            add(OngoingDomain("3Food App", "Jun 12, 2023", 0, "icon_folder"))
            add(OngoingDomain("4Food App", "Jun 12, 2023", 0, "icon_network"))
        }
        binding.viewOngoing.layoutManager = GridLayoutManager(this, 2)
        adapterOngoing = OngoingAdapter(items)
        binding.viewOngoing.adapter = adapterOngoing
    }
}