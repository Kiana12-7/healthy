package com.example.myapplication.ui.fitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class BodyPartFocusActivity extends AppCompatActivity {

    private LinearLayout llCurrentOptions;
    private LinearLayout llTargetOptions;
    private Button btnNext;
    private TextView tvTitle;

    private String part; // 部位名称
    private String[] labels;          // 选项标签（固定顺序）
    private int[] uncheckResources;
    private int[] checkResources;

    private int currentSelectedIndex = -1;
    private int targetSelectedIndex = -1;

    // 保存改善后每个选项的View，用于动态启用/禁用
    private List<View> targetOptionViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_part_focus);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

        llCurrentOptions = findViewById(R.id.llCurrentOptions);
        llTargetOptions = findViewById(R.id.llTargetOptions);
        btnNext = findViewById(R.id.btnNext);
        tvTitle = findViewById(R.id.tvTitle);

        part = getIntent().getStringExtra("part");
        if (part == null) part = "全身";

        setupPartData(); // 根据部位设置 labels 和图片资源
        tvTitle.setText("定制我的" + part);

        // 创建当前选项视图
        setupCurrentOptions();
        // 创建改善后选项视图（初始全部可用）
        setupTargetOptions();

        // 初始按钮状态（未选择时禁用）
        updateButtonState();

        btnNext.setOnClickListener(v -> {
            if (currentSelectedIndex == -1 || targetSelectedIndex == -1) {
                Toast.makeText(BodyPartFocusActivity.this, "请选择当前体态和改善后目标", Toast.LENGTH_SHORT).show();
            } else {
                String current = labels[currentSelectedIndex];
                String target = labels[targetSelectedIndex];
                SharedPreferences sharedPref = getSharedPreferences("health_app", MODE_PRIVATE);
                sharedPref.edit()
                        .putString("current_body_status", current)
                        .putString("target_body_status", target)
                        .apply();

                Intent intent = new Intent(BodyPartFocusActivity.this, PreferenceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupPartData() {
        // 新顺序：松弛赘肉, 纤细, 匀称, 紧致, 健壮
        labels = new String[]{"松弛赘肉", "纤细", "匀称", "紧致", "健壮"};

        // 资源映射：fat(松弛), thin(纤细), normal(匀称), good(紧致), great(健壮)
        switch (part) {
            case "肩臂":
                uncheckResources = new int[]{
                        R.drawable.shoulder_arm_fat_uncheck,
                        R.drawable.shoulder_arm_thin_uncheck,
                        R.drawable.shoulder_arm_normal_uncheck,
                        R.drawable.shoulder_arm_good_uncheck,
                        R.drawable.shoulder_arm_great_uncheck
                };
                checkResources = new int[]{
                        R.drawable.shoulder_arm_fat_check,
                        R.drawable.shoulder_arm_thin_check,
                        R.drawable.shoulder_arm_normal_check,
                        R.drawable.shoulder_arm_good_check,
                        R.drawable.shoulder_arm_great_check
                };
                break;
            case "胸部":
                uncheckResources = new int[]{
                        R.drawable.chest_fat_uncheck,
                        R.drawable.chest_thin_uncheck,
                        R.drawable.chest_normal_uncheck,
                        R.drawable.chest_good_uncheck,
                        R.drawable.chest_great_uncheck
                };
                checkResources = new int[]{
                        R.drawable.chest_fat_check,
                        R.drawable.chest_thin_check,
                        R.drawable.chest_normal_check,
                        R.drawable.chest_good_check,
                        R.drawable.chest_great_check
                };
                break;
            case "腰腹":
                uncheckResources = new int[]{
                        R.drawable.abdomen_fat_uncheck,
                        R.drawable.abdomen_thin_uncheck,
                        R.drawable.abdomen_normal_uncheck,
                        R.drawable.abdomen_good_uncheck,
                        R.drawable.abdomen_great_uncheck
                };
                checkResources = new int[]{
                        R.drawable.abdomen_fat_check,
                        R.drawable.abdomen_thin_check,
                        R.drawable.abdomen_normal_check,
                        R.drawable.abdomen_good_check,
                        R.drawable.abdomen_great_check
                };
                break;
            case "臀腿":
                uncheckResources = new int[]{
                        R.drawable.hips_legs_fat_uncheck,
                        R.drawable.hips_legs_thin_uncheck,
                        R.drawable.hips_legs_normal_uncheck,
                        R.drawable.hips_legs_good_uncheck,
                        R.drawable.hips_legs_great_uncheck
                };
                checkResources = new int[]{
                        R.drawable.hips_legs_fat_check,
                        R.drawable.hips_legs_thin_check,
                        R.drawable.hips_legs_normal_check,
                        R.drawable.hips_legs_good_check,
                        R.drawable.hips_legs_great_check
                };
                break;
            case "全身":
            default:
                uncheckResources = new int[]{
                        R.drawable.whole_body_fat_uncheck,
                        R.drawable.whole_body_thin_uncheck,
                        R.drawable.whole_body_normal_uncheck,
                        R.drawable.whole_body_good_uncheck,
                        R.drawable.whole_body_great_uncheck
                };
                checkResources = new int[]{
                        R.drawable.whole_body_fat_check,
                        R.drawable.whole_body_thin_check,
                        R.drawable.whole_body_normal_check,
                        R.drawable.whole_body_good_check,
                        R.drawable.whole_body_great_check
                };
                break;
        }
    }

    private void setupCurrentOptions() {
        for (int i = 0; i < labels.length; i++) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_body_shape, llCurrentOptions, false);
            ImageView iv = itemView.findViewById(R.id.ivBodyImage);
            TextView tv = itemView.findViewById(R.id.tvBodyLabel);
            tv.setText(labels[i]);
            iv.setImageResource(uncheckResources[i]);

            final int index = i;
            itemView.setOnClickListener(v -> {
                if (currentSelectedIndex == index) return;
                // 更新当前选中
                updateCurrentSelection(index);
                // 根据新的当前选项，更新改善后选项的可用性
                updateTargetOptionsAvailability();
                // 更新按钮状态
                updateButtonState();
            });
            llCurrentOptions.addView(itemView);
        }
    }

    private void setupTargetOptions() {
        for (int i = 0; i < labels.length; i++) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_body_shape, llTargetOptions, false);
            ImageView iv = itemView.findViewById(R.id.ivBodyImage);
            TextView tv = itemView.findViewById(R.id.tvBodyLabel);
            tv.setText(labels[i]);
            iv.setImageResource(uncheckResources[i]);

            final int index = i;
            itemView.setOnClickListener(v -> {
                // 检查该选项是否允许被选择（根据当前选中的改善前）
                if (isTargetAllowed(index)) {
                    if (targetSelectedIndex == index) return;
                    updateTargetSelection(index);
                    // 更新按钮状态
                    updateButtonState();
                } else {
                    Toast.makeText(BodyPartFocusActivity.this, "当前体态无法直接改善为此目标", Toast.LENGTH_SHORT).show();
                }
            });
            llTargetOptions.addView(itemView);
            targetOptionViews.add(itemView);
        }
        // 初始时没有改善前选中，所有改善后禁用或置灰
        enableTargetOptions(false);
    }

    /**
     * 根据当前选中的改善前索引，判断改善后索引是否允许
     */
    private boolean isTargetAllowed(int targetIndex) {
        if (currentSelectedIndex == -1) return false;
        String current = labels[currentSelectedIndex];
        String target = labels[targetIndex];
        switch (current) {
            case "松弛赘肉":
            case "纤细":
                // 允许：匀称、紧致、健壮
                return target.equals("匀称") || target.equals("紧致") || target.equals("健壮");
            case "匀称":
                // 允许：紧致、健壮
                return target.equals("紧致") || target.equals("健壮");
            case "紧致":
                // 允许：纤细
                return target.equals("健壮");
            case "健壮":
                // 只能选健壮
                return target.equals("健壮");
            default:
                return false;
        }
    }

    /**
     * 更新改善后所有选项的启用/禁用状态，并重置改善后选中（如果当前选中不再允许）
     */
    private void updateTargetOptionsAvailability() {
        for (int i = 0; i < targetOptionViews.size(); i++) {
            View view = targetOptionViews.get(i);
            boolean allowed = isTargetAllowed(i);
            view.setEnabled(allowed);
            // 视觉反馈：降低透明度表示不可选
            view.setAlpha(allowed ? 1.0f : 0.4f);
        }
        // 如果当前改善后选中的索引不再允许，清除改善后选中
        if (targetSelectedIndex != -1 && !isTargetAllowed(targetSelectedIndex)) {
            // 清除改善后选中状态
            View oldItem = llTargetOptions.getChildAt(targetSelectedIndex);
            if (oldItem != null) {
                ImageView oldIv = oldItem.findViewById(R.id.ivBodyImage);
                oldIv.setImageResource(uncheckResources[targetSelectedIndex]);
            }
            targetSelectedIndex = -1;
            // 清除后也要更新按钮状态
            updateButtonState();
        }
    }

    /**
     * 启用或禁用所有改善后选项（初始状态）
     */
    private void enableTargetOptions(boolean enable) {
        for (View view : targetOptionViews) {
            view.setEnabled(enable);
            view.setAlpha(enable ? 1.0f : 0.4f);
        }
        if (!enable) {
            // 清除改善后选中
            if (targetSelectedIndex != -1) {
                View oldItem = llTargetOptions.getChildAt(targetSelectedIndex);
                if (oldItem != null) {
                    ImageView oldIv = oldItem.findViewById(R.id.ivBodyImage);
                    oldIv.setImageResource(uncheckResources[targetSelectedIndex]);
                }
                targetSelectedIndex = -1;
            }
        }
    }

    private void updateCurrentSelection(int newIndex) {
        // 清除旧选中
        if (currentSelectedIndex != -1) {
            View oldItem = llCurrentOptions.getChildAt(currentSelectedIndex);
            if (oldItem != null) {
                ImageView oldIv = oldItem.findViewById(R.id.ivBodyImage);
                oldIv.setImageResource(uncheckResources[currentSelectedIndex]);
            }
        }
        // 设置新选中
        View newItem = llCurrentOptions.getChildAt(newIndex);
        if (newItem != null) {
            ImageView newIv = newItem.findViewById(R.id.ivBodyImage);
            newIv.setImageResource(checkResources[newIndex]);
        }
        currentSelectedIndex = newIndex;
    }

    private void updateTargetSelection(int newIndex) {
        // 清除旧选中
        if (targetSelectedIndex != -1) {
            View oldItem = llTargetOptions.getChildAt(targetSelectedIndex);
            if (oldItem != null) {
                ImageView oldIv = oldItem.findViewById(R.id.ivBodyImage);
                oldIv.setImageResource(uncheckResources[targetSelectedIndex]);
            }
        }
        // 设置新选中
        View newItem = llTargetOptions.getChildAt(newIndex);
        if (newItem != null) {
            ImageView newIv = newItem.findViewById(R.id.ivBodyImage);
            newIv.setImageResource(checkResources[newIndex]);
        }
        targetSelectedIndex = newIndex;
    }

    /**
     * 更新“下一步”按钮状态：当前和改善后都选中时启用并变绿，否则禁用并变灰
     */
    private void updateButtonState() {
        boolean isValid = currentSelectedIndex != -1 && targetSelectedIndex != -1;
        btnNext.setEnabled(isValid);
        btnNext.setBackgroundTintList(ColorStateList.valueOf(
                isValid ? getColor(R.color.button_enabled) : getColor(R.color.button_disabled)
        ));
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