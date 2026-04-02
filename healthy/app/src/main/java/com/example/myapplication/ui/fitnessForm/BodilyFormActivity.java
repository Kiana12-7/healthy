package com.example.myapplication.ui.fitnessForm;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class BodilyFormActivity extends AppCompatActivity {

    private ImageView ivBodyShape;
    private Button btnStraight, btnPear, btnFunnel, btnApple;
    private Button btnNext;
    private String selectedShape = "直筒型"; // 默认选中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodily_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        ivBodyShape = findViewById(R.id.ivBodyShape);
        btnStraight = findViewById(R.id.btnStraight);
        btnPear = findViewById(R.id.btnPear);
        btnFunnel = findViewById(R.id.btnFunnel);
        btnApple = findViewById(R.id.btnApple);
        btnNext = findViewById(R.id.btnNext);

        // 设置点击事件
        btnStraight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShape = "直筒型";
                ivBodyShape.setImageResource(R.drawable.stright_form);
                updateButtonStyle(btnStraight);
            }
        });

        btnPear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShape = "梨型";
                ivBodyShape.setImageResource(R.drawable.pear_form);
                updateButtonStyle(btnPear);
            }
        });

        btnFunnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShape = "漏斗型";
                ivBodyShape.setImageResource(R.drawable.funnel_form);
                updateButtonStyle(btnFunnel);
            }
        });

        btnApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShape = "苹果型";
                ivBodyShape.setImageResource(R.drawable.apple_form);
                updateButtonStyle(btnApple);
            }
        });

        // 默认选中直筒型（高亮样式）
        updateButtonStyle(btnStraight);

        // 下一步按钮：跳转到第五个界面（暂时空置，可以创建 FifthActivity 或提示完成）
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BodilyFormActivity.this, "你选择了" + selectedShape + "体型", Toast.LENGTH_SHORT).show();
                // 这里可以跳转到后续界面，例如：
                // Intent intent = new Intent(BodilyFormActivity.this, FifthActivity.class);
                // startActivity(intent);
                // 暂时不跳转，仅提示
            }
        });
    }

    /**
     * 更新按钮样式：选中的按钮高亮，其他恢复默认
     */
    private void updateButtonStyle(Button selectedButton) {
        // 重置所有按钮背景色
        btnStraight.setBackgroundTintList(getColorStateList(R.color.default_button_bg));
        btnPear.setBackgroundTintList(getColorStateList(R.color.default_button_bg));
        btnFunnel.setBackgroundTintList(getColorStateList(R.color.default_button_bg));
        btnApple.setBackgroundTintList(getColorStateList(R.color.default_button_bg));

        // 设置选中按钮背景色（例如浅蓝色）
        selectedButton.setBackgroundTintList(getColorStateList(R.color.selected_button_bg));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}