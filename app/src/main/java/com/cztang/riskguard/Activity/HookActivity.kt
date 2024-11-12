package com.cztang.riskguard.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cztang.riskguard.Domain.CommonDomain
import com.cztang.riskguard.UiUtil.ItemDecoration
import com.cztang.riskguard.databinding.ActivityHookBinding
import pokercc.android.expandablerecyclerview.ExpandableItemAnimator
import pokercc.android.expandablerecyclerview.sample.markets.HookAdapter


class HookActivity : AppCompatActivity() {

    private var isExpanded = false

    // 延迟初始化，lazy 延迟初始化的目的是在需要使用 binding 时才进行初始化，从而避免在 onCreate 方法中直接初始化。
    private val binding by lazy { ActivityHookBinding.inflate(layoutInflater) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 创建 HookDomain 的 ArrayList
        val items = ArrayList<CommonDomain>().apply {
            add(CommonDomain("Root Info", listOf("123")))
            add(CommonDomain("Root Info", listOf("123")))
            add(CommonDomain("Root Info", listOf("123")))
            add(CommonDomain("Root Info", listOf("123")))
            add(CommonDomain("Root Info", listOf("123")))

        }
        with(binding.recyclerViewHook) {
            adapter = HookAdapter(items)
            itemAnimator = ExpandableItemAnimator(this, animChildrenItem = true)
            addItemDecoration(ItemDecoration())
            layoutManager = LinearLayoutManager(context)
        }

        binding.viewChangeHook.setOnClickListener {
            val adapter = binding.recyclerViewHook.adapter as? HookAdapter
            if (isExpanded) {
                adapter?.collapseAllGroups()
                binding.viewChangeHook.text = "Expand All"
            } else {
                adapter?.expandAllGroups()
                binding.viewChangeHook.text = "Collapse All"
            }
            isExpanded = !isExpanded
        }
    }
}