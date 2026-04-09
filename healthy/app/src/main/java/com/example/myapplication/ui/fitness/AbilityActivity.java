package com.example.myapplication.ui.fitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class AbilityActivity extends AppCompatActivity {

    private RadioGroup rgPushup, rgSquat, rgSitups, rgStairs;
    private Button btnGenerate;
    private Button btnMyInfo;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ability);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

        rgPushup = findViewById(R.id.rgPushup);
        rgSquat = findViewById(R.id.rgSquat);
        rgSitups = findViewById(R.id.rgSitups);
        rgStairs = findViewById(R.id.rgStairs);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnMyInfo = findViewById(R.id.btnMyInfo);

        sharedPref = getSharedPreferences("health_app", MODE_PRIVATE);

        // “我的信息”按钮：跳转到统计页面
        btnMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AbilityActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        // “生成计划”按钮：保存数据并提示（后续可跳转到计划展示）
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSelections()) {
                    // 获取所有选中的值
                    String pushup = getSelectedPushup();
                    String squat = getSelectedSquat();
                    String situps = getSelectedSitups();
                    String stairs = getSelectedStairs();

                    // 保存到 SharedPreferences
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("pushup", pushup);
                    editor.putString("squat", squat);
                    editor.putString("situp", situps);
                    editor.putString("stairs", stairs);
                    editor.apply();

                    // 显示保存成功提示（后续可替换为跳转）
                    Toast.makeText(AbilityActivity.this,
                            "俯卧撑：" + pushup + "\n深蹲：" + squat + "\n仰卧起坐：" + situps + "\n爬楼：" + stairs,
                            Toast.LENGTH_LONG).show();

                    // TODO: 跳转到生成计划界面（如计划展示）
                    // Intent intent = new Intent(AbilityActivity.this, PlanActivity.class);
                    // startActivity(intent);
                } else {
                    Toast.makeText(AbilityActivity.this, "请完整填写所有运动能力问题", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateSelections() {
        return rgPushup.getCheckedRadioButtonId() != -1 &&
                rgSquat.getCheckedRadioButtonId() != -1 &&
                rgSitups.getCheckedRadioButtonId() != -1 &&
                rgStairs.getCheckedRadioButtonId() != -1;
    }

    private String getSelectedPushup() {
        int id = rgPushup.getCheckedRadioButtonId();
        if (id == R.id.pushupNone) return "从来没试过，或1个也做不了";
        if (id == R.id.pushupLess10) return "10个以内";
        if (id == R.id.pushup10to20) return "10-20个";
        if (id == R.id.pushup20to30) return "20-30个";
        if (id == R.id.pushupAbove30) return "30个以上";
        return "";
    }

    private String getSelectedSquat() {
        int id = rgSquat.getCheckedRadioButtonId();
        if (id == R.id.squatNone) return "从来没试过，或1个也做不了";
        if (id == R.id.squatLess10) return "10个以内";
        if (id == R.id.squat10to20) return "10-20个";
        if (id == R.id.squat20to30) return "20-30个";
        if (id == R.id.squatAbove30) return "30个以上";
        return "";
    }

    private String getSelectedSitups() {
        int id = rgSitups.getCheckedRadioButtonId();
        if (id == R.id.situpsNone) return "从来没试过，或1个也做不了";
        if (id == R.id.situpsLess10) return "10个以内";
        if (id == R.id.situps10to20) return "10-20个";
        if (id == R.id.situps20to30) return "20-30个";
        if (id == R.id.situpsAbove30) return "30个以上";
        return "";
    }

    private String getSelectedStairs() {
        int id = rgStairs.getCheckedRadioButtonId();
        if (id == R.id.stairsVeryTired) return "很累，甚至有点喘不过气";
        if (id == R.id.stairsTired) return "比较累，呼吸也比较局促";
        if (id == R.id.stairsSlightly) return "呼吸有点局促，但可以很快恢复";
        if (id == R.id.stairsAlmostNone) return "几乎没有感觉";
        if (id == R.id.stairsVeryEasy) return "非常轻松，完全没有感觉";
        return "";
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