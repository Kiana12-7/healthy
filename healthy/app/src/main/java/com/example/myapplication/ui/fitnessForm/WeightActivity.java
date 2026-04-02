package com.example.myapplication.ui.fitnessForm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class WeightActivity extends AppCompatActivity {

    private EditText etHeightCm;
    private EditText etWeightKg;
    private TextView tvBmiResult;
    private ImageView ivStatusImage;
    private Button btnGoToThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        // 初始化 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        etHeightCm = findViewById(R.id.etHeightCm);
        etWeightKg = findViewById(R.id.etWeightKg);
        tvBmiResult = findViewById(R.id.tvBmiResult);
        ivStatusImage = findViewById(R.id.ivStatusImage);
        btnGoToThird = findViewById(R.id.btnGoToThird);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                calculateAndDisplayBmi();
            }
        };

        etHeightCm.addTextChangedListener(textWatcher);
        etWeightKg.addTextChangedListener(textWatcher);

        btnGoToThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeightActivity.this, WoundActivity.class);
                startActivity(intent);
            }
        });
    }

    private void calculateAndDisplayBmi() {
        String heightStr = etHeightCm.getText().toString().trim();
        String weightStr = etWeightKg.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            tvBmiResult.setText("BMI: --");
            // 无输入时，可设置默认图片或隐藏图片
            ivStatusImage.setImageResource(R.drawable.normal); // 可选：设置默认图
            return;
        }

        try {
            double heightCm = Double.parseDouble(heightStr);
            double weightKg = Double.parseDouble(weightStr);

            if (heightCm <= 0 || weightKg <= 0) {
                tvBmiResult.setText("BMI: 请输入正数");
                ivStatusImage.setImageResource(R.drawable.normal);
                return;
            }

            double heightM = heightCm / 100.0;
            double bmi = weightKg / (heightM * heightM);
            String bmiText = String.format("BMI: %.1f", bmi);
            tvBmiResult.setText(bmiText);

            // 根据 BMI 显示对应的图片
            updateStatusImage(bmi);

        } catch (NumberFormatException e) {
            tvBmiResult.setText("BMI: 输入无效");
            ivStatusImage.setImageResource(R.drawable.normal);
        }
    }

    /**
     * 根据 BMI 值更新状态图片
     * 标准：偏瘦 < 18.5，正常 18.5~24，过胖 >= 24
     */
    private void updateStatusImage(double bmi) {
        if (bmi < 18.5) {
            ivStatusImage.setImageResource(R.drawable.thin);
        } else if (bmi >= 24) {
            ivStatusImage.setImageResource(R.drawable.fat);
        } else {
            ivStatusImage.setImageResource(R.drawable.normal);
        }
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