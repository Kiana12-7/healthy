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

public class PreferenceActivity extends AppCompatActivity {

    private RadioGroup rgSportType, rgDuration, rgRequirement, rgEquipment;
    private Button btnNext;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

        rgSportType = findViewById(R.id.rgSportType);
        rgDuration = findViewById(R.id.rgDuration);
        rgRequirement = findViewById(R.id.rgRequirement);
        rgEquipment = findViewById(R.id.rgEquipment);
        btnNext = findViewById(R.id.btnNext);

        sharedPref = getSharedPreferences("health_app", MODE_PRIVATE);

        // 初始按钮状态（未全选时禁用）
        updateButtonState();

        // 为每个 RadioGroup 设置监听器，当任一选项改变时更新按钮状态
        RadioGroup.OnCheckedChangeListener listener = (group, checkedId) -> updateButtonState();
        rgSportType.setOnCheckedChangeListener(listener);
        rgDuration.setOnCheckedChangeListener(listener);
        rgRequirement.setOnCheckedChangeListener(listener);
        rgEquipment.setOnCheckedChangeListener(listener);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSelections()) {
                    // 保存所有选择
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sport_type", getSelectedSportType());
                    editor.putString("duration", getSelectedDuration());
                    editor.putString("requirement", getSelectedRequirement());
                    editor.putString("equipment", getSelectedEquipment());
                    editor.apply();

                    // 跳转到运动能力界面
                    Intent intent = new Intent(PreferenceActivity.this, AbilityActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PreferenceActivity.this, "请完整填写所有问题", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 更新按钮状态：全选时启用并变绿，否则禁用并变灰
     */
    private void updateButtonState() {
        boolean allSelected = rgSportType.getCheckedRadioButtonId() != -1 &&
                rgDuration.getCheckedRadioButtonId() != -1 &&
                rgRequirement.getCheckedRadioButtonId() != -1 &&
                rgEquipment.getCheckedRadioButtonId() != -1;
        btnNext.setEnabled(allSelected);
        btnNext.setBackgroundTintList(ColorStateList.valueOf(
                allSelected ? getColor(R.color.button_enabled) : getColor(R.color.button_disabled)
        ));
    }

    private boolean validateSelections() {
        return rgSportType.getCheckedRadioButtonId() != -1 &&
                rgDuration.getCheckedRadioButtonId() != -1 &&
                rgRequirement.getCheckedRadioButtonId() != -1 &&
                rgEquipment.getCheckedRadioButtonId() != -1;
    }

    private String getSelectedSportType() {
        int id = rgSportType.getCheckedRadioButtonId();
        if (id == R.id.rbYoga) return "瑜伽";
        if (id == R.id.rbRunning) return "跑步";
        if (id == R.id.rbStrength) return "力量训练";
        if (id == R.id.rbSwimming) return "游泳";
        if (id == R.id.rbCycling) return "骑行";
        if (id == R.id.rbNone) return "以上都不需要";
        return "";
    }

    private String getSelectedDuration() {
        int id = rgDuration.getCheckedRadioButtonId();
        if (id == R.id.rb20min) return "20分钟左右";
        if (id == R.id.rb30min) return "30分钟左右";
        if (id == R.id.rb45min) return "45分钟左右";
        if (id == R.id.rb60min) return "60分钟左右";
        return "";
    }

    private String getSelectedRequirement() {
        int id = rgRequirement.getCheckedRadioButtonId();
        if (id == R.id.rbNoNoise) return "零噪音";
        if (id == R.id.rbNoJump) return "无跳跃";
        if (id == R.id.rbSmallSpace) return "小场地";
        if (id == R.id.rbNoRequirement) return "没有以上要求";
        return "";
    }

    private String getSelectedEquipment() {
        int id = rgEquipment.getCheckedRadioButtonId();
        if (id == R.id.rbNoEquipment) return "无器械";
        if (id == R.id.rbDumbbell) return "哑铃";
        if (id == R.id.rbResistanceBand) return "弹力带";
        if (id == R.id.rbYogaMat) return "瑜伽垫";
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