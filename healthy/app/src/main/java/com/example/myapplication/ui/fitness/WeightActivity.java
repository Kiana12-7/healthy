package com.example.myapplication.ui.fitness;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private EditText etHeightCm;
    private EditText etWeightKg;
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

        // 下一步按钮：校验必须填写身高和体重
        btnGoToThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heightStr = etHeightCm.getText().toString().trim();
                String weightStr = etWeightKg.getText().toString().trim();

                if (heightStr.isEmpty() || weightStr.isEmpty()) {
                    Toast.makeText(WeightActivity.this, "请填写身高和体重", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double heightCm = Double.parseDouble(heightStr);
                    double weightKg = Double.parseDouble(weightStr);
                    if (heightCm <= 0 || weightKg <= 0) {
                        Toast.makeText(WeightActivity.this, "身高和体重必须为正数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(WeightActivity.this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 校验通过，跳转到伤病选择界面
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
            ivStatusImage.setImageResource(R.drawable.normal); // 默认图片
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

            updateStatusImage(bmi);

            // 保存 BMI 到 SharedPreferences（供其他界面使用）
            SharedPreferences sharedPref = getSharedPreferences("health_app", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("bmi", (float) bmi);
            editor.putFloat("height", (float) heightCm);
            editor.putFloat("weight", (float) weightKg);
            editor.apply();

        } catch (NumberFormatException e) {
            tvBmiResult.setText("BMI: 输入无效");
            ivStatusImage.setImageResource(R.drawable.normal);
        }
    }

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