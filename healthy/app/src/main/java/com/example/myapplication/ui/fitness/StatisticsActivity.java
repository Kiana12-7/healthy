package com.example.myapplication.ui.fitness;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class StatisticsActivity extends AppCompatActivity {

    private TableLayout tableStats;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("我的信息");
        }

        tableStats = findViewById(R.id.tableStats);
        btnBack = findViewById(R.id.btnBack);

        loadAndDisplayData();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadAndDisplayData() {
        SharedPreferences sp = getSharedPreferences("health_app", MODE_PRIVATE);

        // 页面2
        float height = sp.getFloat("height", 0f);
        float weight = sp.getFloat("weight", 0f);
        float bmi = sp.getFloat("bmi", 0f);
        addRow("身高(cm)", height == 0 ? "未填写" : String.valueOf(height));
        addRow("体重(kg)", weight == 0 ? "未填写" : String.valueOf(weight));
        addRow("BMI", bmi == 0 ? "未填写" : String.format("%.1f", bmi));

        // 页面3
        String injuries = sp.getString("injuries", "无伤病情况");
        addRow("伤病情况", injuries);

        // 页面4
        String bodyShape = sp.getString("body_shape", "未选择");
        addRow("体型", bodyShape);

        // 页面5
        String aimGoal = sp.getString("aim_goal", "未选择");
        addRow("运动目标", aimGoal);

        // 页面6+7
        String focusArea = sp.getString("focus_area", "未选择");
        addRow("重点改善部位", focusArea);
        String currentStatus = sp.getString("current_body_status", "未选择");
        String targetStatus = sp.getString("target_body_status", "未选择");
        addRow("改善前体态", currentStatus);
        addRow("改善后体态", targetStatus);

        // 页面8
        String sportType = sp.getString("sport_type", "未选择");
        String duration = sp.getString("duration", "未选择");
        String requirement = sp.getString("requirement", "未选择");
        String equipment = sp.getString("equipment", "未选择");
        addRow("额外运动类型", sportType);
        addRow("每日运动时长", duration);
        addRow("计划要求", requirement);
        addRow("允许器械", equipment);

        // 页面9
        String pushup = sp.getString("pushup", "未选择");
        String squat = sp.getString("squat", "未选择");
        String situp = sp.getString("situp", "未选择");
        String stairs = sp.getString("stairs", "未选择");
        addRow("俯卧撑能力", pushup);
        addRow("深蹲能力", squat);
        addRow("仰卧起坐能力", situp);
        addRow("爬楼疲劳度", stairs);
    }

    private void addRow(String label, String value) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.setPadding(8, 8, 8, 8);

        TextView tvLabel = new TextView(this);
        tvLabel.setText(label);
        tvLabel.setPadding(8, 8, 8, 8);
        tvLabel.setTextSize(14f);
        tvLabel.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvValue = new TextView(this);
        tvValue.setText(value);
        tvValue.setPadding(8, 8, 8, 8);
        tvValue.setTextSize(14f);
        tvValue.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));

        row.addView(tvLabel);
        row.addView(tvValue);
        tableStats.addView(row);

        // 添加分隔线（可选）
        View divider = new View(this);
        divider.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(0xFFCCCCCC);
        tableStats.addView(divider);
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