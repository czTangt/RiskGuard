package com.cztang.riskguard.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cztang.device.CpuInfo
import com.cztang.device.Device
import com.cztang.device.RootInfo
import com.cztang.device.SerialInfo
import com.cztang.device.SystemInfo
import com.cztang.riskguard.Domain.CommonDomain
import com.cztang.riskguard.UiUtil.ItemDecoration
import com.cztang.riskguard.databinding.ActivityDeviceBinding
import pokercc.android.expandablerecyclerview.ExpandableItemAnimator
import pokercc.android.expandablerecyclerview.sample.markets.DeviceAdapter
import pokercc.android.expandablerecyclerview.sample.markets.HookAdapter

class DeviceActivity : AppCompatActivity() {

    private var isExpanded = false

    // 延迟初始化，lazy 延迟初始化的目的是在需要使用 binding 时才进行初始化，从而避免在 onCreate 方法中直接初始化。
    private val binding by lazy { ActivityDeviceBinding.inflate(layoutInflater) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 创建 DeviceDomain 的 ArrayList
        val items = ArrayList<CommonDomain>().apply {
            add(CommonDomain("Root Info", listOf(RootInfo(this@DeviceActivity).rootInfo)))
            add(CommonDomain("Cpu Info", listOf(CpuInfo(this@DeviceActivity).cpuInfo)))
            add(CommonDomain("Serial Info", listOf(SerialInfo(this@DeviceActivity).androidId)))
            add(
                CommonDomain(
                    "System Info",
                    listOf(
                        SystemInfo(this@DeviceActivity).buildInfo,
                        SystemInfo(this@DeviceActivity).baseband
                    )
                )
            )
            add(CommonDomain("HardDisk Info", listOf(Device(this@DeviceActivity).hardDiskInfor)))
            add(CommonDomain("Kernel Info", listOf(Device(this@DeviceActivity).kernelInfor)))
        }
        with(binding.recyclerViewDevice) {
            adapter = DeviceAdapter(items)
            itemAnimator = ExpandableItemAnimator(this, animChildrenItem = true)
            addItemDecoration(ItemDecoration())
            layoutManager = LinearLayoutManager(context)
        }

        binding.viewChangeDevice.setOnClickListener {
            val adapter = binding.recyclerViewDevice.adapter as? DeviceAdapter
            if (isExpanded) {
                adapter?.collapseAllGroups()
                binding.viewChangeDevice.text = "Expand All"
            } else {
                adapter?.expandAllGroups()
                binding.viewChangeDevice.text = "Collapse All"
            }
            isExpanded = !isExpanded
        }
    }
}