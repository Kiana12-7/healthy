package com.example.myapplication.ui.fitness;

import android.content.Intent;
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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSelections()) {
                    // 获取所有选中的值
                    String sportType = getSelectedSportType();
                    String duration = getSelectedDuration();
                    String requirement = getSelectedRequirement();
                    String equipment = getSelectedEquipment();

                    // 可以保存到 SharedPreferences 或传递给下一个界面
                    Toast.makeText(PreferenceActivity.this,
                            "运动类型：" + sportType + "\n时长：" + duration + "\n要求：" + requirement + "\n器械：" + equipment,
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(PreferenceActivity.this, AbilityActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PreferenceActivity.this, "请完整填写所有问题", Toast.LENGTH_SHORT).show();
                }
            }
        });
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