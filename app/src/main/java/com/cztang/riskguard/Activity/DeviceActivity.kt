package com.cztang.riskguard.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cztang.riskguard.databinding.ActivityDeviceBinding
import pokercc.android.expandablerecyclerview.ExpandableItemAnimator
import pokercc.android.expandablerecyclerview.sample.markets.DeviceAdapter
import com.cztang.riskguard.Domain.DeviceItemDecoration

class DeviceActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DeviceActivity::class.java))
        }
    }

    // 延迟初始化 MarketsActivityBinding，用于视图绑定。
    private val binding by lazy { ActivityDeviceBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding.recyclerView) {
            adapter = DeviceAdapter()
            itemAnimator = ExpandableItemAnimator(this, animChildrenItem = true)
            addItemDecoration(DeviceItemDecoration())
            layoutManager = LinearLayoutManager(context)
        }

    }
}