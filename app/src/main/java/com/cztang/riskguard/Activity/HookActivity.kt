package com.cztang.riskguard.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cztang.riskguard.databinding.ActivityHookBinding


class HookActivity : AppCompatActivity() {

    // 延迟初始化，lazy 延迟初始化的目的是在需要使用 binding 时才进行初始化，从而避免在 onCreate 方法中直接初始化。
    private val binding by lazy { ActivityHookBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}