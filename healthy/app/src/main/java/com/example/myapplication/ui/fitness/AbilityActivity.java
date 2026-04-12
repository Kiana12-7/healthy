package com.example.myapplication.ui.fitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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

        // “我的信息”按钮始终绿色且可点击
        btnMyInfo.setEnabled(true);
        btnMyInfo.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_enabled)));
        btnMyInfo.setOnClickListener(v -> {
            Intent intent = new Intent(AbilityActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        // 初始按钮状态（未全选时禁用灰色）
        updateGenerateButtonState();

        // 为每个 RadioGroup 设置监听器，当任一选项改变时更新“生成计划”按钮状态
        RadioGroup.OnCheckedChangeListener listener = (group, checkedId) -> updateGenerateButtonState();
        rgPushup.setOnCheckedChangeListener(listener);
        rgSquat.setOnCheckedChangeListener(listener);
        rgSitups.setOnCheckedChangeListener(listener);
        rgStairs.setOnCheckedChangeListener(listener);

        // “生成计划”按钮点击事件
        btnGenerate.setOnClickListener(v -> {
            if (validateSelections()) {
                String pushup = getSelectedPushup();
                String squat = getSelectedSquat();
                String situps = getSelectedSitups();
                String stairs = getSelectedStairs();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("pushup", pushup);
                editor.putString("squat", squat);
                editor.putString("situp", situps);
                editor.putString("stairs", stairs);
                editor.apply();

                Toast.makeText(AbilityActivity.this,
                        "俯卧撑：" + pushup + "\n深蹲：" + squat + "\n仰卧起坐：" + situps + "\n爬楼：" + stairs,
                        Toast.LENGTH_LONG).show();

                // TODO: 跳转到生成计划界面
            } else {
                Toast.makeText(AbilityActivity.this, "请完整填写所有运动能力问题", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 更新“生成计划”按钮状态：全选时启用并变绿，否则禁用并变灰
     */
    private void updateGenerateButtonState() {
        boolean allSelected = rgPushup.getCheckedRadioButtonId() != -1 &&
                rgSquat.getCheckedRadioButtonId() != -1 &&
                rgSitups.getCheckedRadioButtonId() != -1 &&
                rgStairs.getCheckedRadioButtonId() != -1;
        btnGenerate.setEnabled(allSelected);
        btnGenerate.setBackgroundTintList(ColorStateList.valueOf(
                allSelected ? getColor(R.color.button_enabled) : getColor(R.color.button_disabled)
        ));
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