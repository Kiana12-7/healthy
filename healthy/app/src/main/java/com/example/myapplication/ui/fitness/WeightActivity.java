package com.example.myapplication.ui.fitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class WeightActivity extends AppCompatActivity {

    private EditText etHeightCm, etWeightKg;
    private TextView tvBmiResult;
    private ImageView ivStatusImage;
    private Button btnGoToThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

        etHeightCm = findViewById(R.id.etHeightCm);
        etWeightKg = findViewById(R.id.etWeightKg);
        tvBmiResult = findViewById(R.id.tvBmiResult);
        ivStatusImage = findViewById(R.id.ivStatusImage);
        btnGoToThird = findViewById(R.id.btnGoToThird);

        // 初始按钮状态
        updateButtonState();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculateAndDisplayBmi();
                updateButtonState();  // 每次输入后更新按钮状态
            }
        };
        etHeightCm.addTextChangedListener(textWatcher);
        etWeightKg.addTextChangedListener(textWatcher);

        btnGoToThird.setOnClickListener(v -> {
            String heightStr = etHeightCm.getText().toString().trim();
            String weightStr = etWeightKg.getText().toString().trim();

            if (heightStr.isEmpty() || weightStr.isEmpty()) {
                Toast.makeText(WeightActivity.this, "请填写身高和体重", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                double h = Double.parseDouble(heightStr);
                double w = Double.parseDouble(weightStr);
                if (h <= 0 || w <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                Toast.makeText(WeightActivity.this, "请输入有效数字", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(WeightActivity.this, WoundActivity.class));
        });
    }

    private void updateButtonState() {
        boolean isValid = !etHeightCm.getText().toString().trim().isEmpty() &&
                !etWeightKg.getText().toString().trim().isEmpty();
        btnGoToThird.setEnabled(isValid);
        btnGoToThird.setBackgroundTintList(ColorStateList.valueOf(
                isValid ? getColor(R.color.button_enabled) : getColor(R.color.button_disabled)
        ));
    }

    private void calculateAndDisplayBmi() {
        // 原有代码不变（省略）
        String heightStr = etHeightCm.getText().toString().trim();
        String weightStr = etWeightKg.getText().toString().trim();
        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            tvBmiResult.setText("BMI: --");
            ivStatusImage.setImageResource(R.drawable.normal);
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
            tvBmiResult.setText(String.format("BMI: %.1f", bmi));
            if (bmi < 18.5) ivStatusImage.setImageResource(R.drawable.thin);
            else if (bmi >= 24) ivStatusImage.setImageResource(R.drawable.fat);
            else ivStatusImage.setImageResource(R.drawable.normal);

            SharedPreferences sp = getSharedPreferences("health_app", MODE_PRIVATE);
            sp.edit().putFloat("bmi", (float) bmi)
                    .putFloat("height", (float) heightCm)
                    .putFloat("weight", (float) weightKg).apply();
        } catch (NumberFormatException e) {
            tvBmiResult.setText("BMI: 输入无效");
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