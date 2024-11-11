package com.cztang.riskguard.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cztang.device.CpuInfo
import com.cztang.device.Device
import com.cztang.device.RootInfo
import com.cztang.device.SerialInfo
import com.cztang.device.SystemInfo
import com.cztang.riskguard.Domain.DeviceDomain
import com.cztang.riskguard.UiUtil.DeviceItemDecoration
import com.cztang.riskguard.databinding.ActivityDeviceBinding
import pokercc.android.expandablerecyclerview.ExpandableItemAnimator
import pokercc.android.expandablerecyclerview.sample.markets.DeviceAdapter

class DeviceActivity : AppCompatActivity() {

    // 延迟初始化，lazy 延迟初始化的目的是在需要使用 binding 时才进行初始化，从而避免在 onCreate 方法中直接初始化。
    private val binding by lazy { ActivityDeviceBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 创建 DeviceDomain 的 ArrayList
        val items = ArrayList<DeviceDomain>().apply {
            add(DeviceDomain("Root Info", listOf(RootInfo(this@DeviceActivity).rootInfo)))
            add(DeviceDomain("Cpu Info", listOf(CpuInfo(this@DeviceActivity).cpuInfo)))
            add(DeviceDomain("Serial Info", listOf(SerialInfo(this@DeviceActivity).androidId)))
            add(DeviceDomain("System Info", listOf(SystemInfo(this@DeviceActivity).buildInfo, SystemInfo(this@DeviceActivity).baseband)))
            add(DeviceDomain("HardDisk Info", listOf(Device(this@DeviceActivity).hardDiskInfor)))
            add(DeviceDomain("Kernel Info", listOf(Device(this@DeviceActivity).kernelInfor)))
        }
        with(binding.recyclerView) {
            adapter = DeviceAdapter(items)
            itemAnimator = ExpandableItemAnimator(this, animChildrenItem = true)
            addItemDecoration(DeviceItemDecoration())
            layoutManager = LinearLayoutManager(context)
        }

    }
}