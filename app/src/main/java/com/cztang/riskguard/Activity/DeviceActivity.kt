package com.cztang.riskguard.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cztang.device.CpuInfo
import com.cztang.device.SerialInfo
import com.cztang.device.SystemInfo
import com.cztang.riskguard.Domain.DeviceDomain
import com.cztang.riskguard.Domain.OngoingDomain
import com.cztang.riskguard.databinding.ActivityDeviceBinding
import pokercc.android.expandablerecyclerview.ExpandableItemAnimator
import pokercc.android.expandablerecyclerview.sample.markets.DeviceAdapter
import com.cztang.riskguard.UiUtil.DeviceItemDecoration

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

        // 创建 DeviceDomain 的 ArrayList
        val items = ArrayList<DeviceDomain>().apply {
            add(DeviceDomain("Cpu Info", CpuInfo(this@DeviceActivity).curCpuFreq))
            add(DeviceDomain("Serial Info", SerialInfo(this@DeviceActivity).androidId))
            add(DeviceDomain("System Info", SystemInfo(this@DeviceActivity).systemInfo))
        }
        with(binding.recyclerView) {
            adapter = DeviceAdapter(items)
            itemAnimator = ExpandableItemAnimator(this, animChildrenItem = true)
            addItemDecoration(DeviceItemDecoration())
            layoutManager = LinearLayoutManager(context)
        }

    }
}