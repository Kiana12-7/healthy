package com.example.myapplication.ui.fitness;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.remote.FitnessFormDataSource;
import com.example.myapplication.data.remote.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

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

        rgPushup.setOnCheckedChangeListener((group, checkedId) -> saveAllSelections());
        rgSquat.setOnCheckedChangeListener((group, checkedId) -> saveAllSelections());
        rgSitups.setOnCheckedChangeListener((group, checkedId) -> saveAllSelections());
        rgStairs.setOnCheckedChangeListener((group, checkedId) -> saveAllSelections());

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
                    SharedPreferences sp = getSharedPreferences("health_app", MODE_PRIVATE);
                    String description = buildUserProfileForAI(sp);
                    // 你需要根据实际情况构造请求体，这里简化
                    Call<ResponseBody> call = RetrofitClient.INSTANCE.getFitnessFormService().save(description);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AbilityActivity.this, "计划生成成功", Toast.LENGTH_SHORT).show();

                                // 跳转
                                Intent intent = new Intent(AbilityActivity.this, MainActivity.class);
                                intent.putExtra("select_today", true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AbilityActivity.this, "服务器错误: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(AbilityActivity.this, "网络异常: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AbilityActivity.this, "请完整填写所有运动能力问题", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String buildUserProfileForAI(SharedPreferences sp) {
        float height = sp.getFloat("height", 0f);
        float weight = sp.getFloat("weight", 0f);
        String injuries = sp.getString("injuries", "无伤病情况");
        String bodyShape = sp.getString("body_shape", "未选择");
        String aimGoal = sp.getString("aim_goal", "未选择");
        String focusArea = sp.getString("focus_area", "未选择");
        String currentStatus = sp.getString("current_body_status", "未选择");
        String targetStatus = sp.getString("target_body_status", "未选择");
        String sportType = sp.getString("sport_type", "未选择");
        String duration = sp.getString("duration", "未选择");
        String requirement = sp.getString("requirement", "未选择");
        String equipment = sp.getString("equipment", "未选择");
        String pushUp = sp.getString("pushup", "未选择");
        String squat = sp.getString("squat", "未选择");
        String sitUp = sp.getString("situp", "未选择");
        String stairs = sp.getString("stairs", "未选择");

        String profile = "我目前的身高是 " + height + " 厘米，体重 " + weight + " 公斤。\n" +
                "伤病情况：" + injuries + "。\n" +
                "体型：" + bodyShape + "。\n" +
                "运动目标：" + aimGoal + "，重点改善部位是 " + focusArea + "。\n" +
                "体态改善意向：希望从“" + currentStatus + "”改善到“" + targetStatus + "”。\n" +
                "运动偏好：除了常规训练，还愿意做 " + sportType + "，每天能投入的运动时长约为 " + duration + "。\n" +
                "计划要求：" + requirement + "，允许使用的器械有：" + equipment + "。\n" +
                "当前运动能力：\n" +
                "- 俯卧撑：" + pushUp + "\n" +
                "- 深蹲：" + squat + "\n" +
                "- 仰卧起坐：" + sitUp + "\n" +
                "- 爬楼梯（或类似有氧）：" + stairs + "\n" +
                "请根据以上信息为我制定一份个性化的健身计划。";

        return profile;
    }

    private void saveAllSelections() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("pushup", getSelectedPushup());
        editor.putString("squat", getSelectedSquat());
        editor.putString("situp", getSelectedSitups());
        editor.putString("stairs", getSelectedStairs());
        editor.apply();
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