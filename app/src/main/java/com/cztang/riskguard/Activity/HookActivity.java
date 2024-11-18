package com.cztang.riskguard.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cztang.hook.Hook;
import com.cztang.riskguard.databinding.ActivityHookBinding;

public class HookActivity extends AppCompatActivity {

    private ActivityHookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化按钮
        initButtonView();
    }

    private void initButtonView() {
        // 配置 Hook 检测按钮
        binding.buttonHook.setOnClickListener(v -> {
            String hookInfo = new Hook(this).getHookInfo();
            binding.outputHook.setText(hookInfo);
        });

        // 配置返回按钮
        binding.backHook.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
