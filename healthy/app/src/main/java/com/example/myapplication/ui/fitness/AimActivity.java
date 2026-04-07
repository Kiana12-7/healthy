package com.example.myapplication.ui.fitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.myapplication.R;

public class AimActivity extends AppCompatActivity {

    private TextView tvRecommendText;
    private CardView cardLoseWeight, cardBuildMuscle, cardKeepHealth;
    private TextView tvRecommendLose, tvRecommendBuild, tvRecommendKeep;
    private Button btnNext;

    private String selectedGoal = "";
    private float bmiValue = 0f;
    private String bodyShape; // 接收从BodilyForm传递的体型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aim);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

        tvRecommendText = findViewById(R.id.tvRecommendText);
        cardLoseWeight = findViewById(R.id.cardLoseWeight);
        cardBuildMuscle = findViewById(R.id.cardBuildMuscle);
        cardKeepHealth = findViewById(R.id.cardKeepHealth);
        tvRecommendLose = findViewById(R.id.tvRecommendLose);
        tvRecommendBuild = findViewById(R.id.tvRecommendBuild);
        tvRecommendKeep = findViewById(R.id.tvRecommendKeep);
        btnNext = findViewById(R.id.btnNext);

        // 接收体型
        bodyShape = getIntent().getStringExtra("body_shape");
        if (bodyShape == null) bodyShape = "直筒型";

        // 读取 BMI
        SharedPreferences sharedPref = getSharedPreferences("health_app", MODE_PRIVATE);
        bmiValue = sharedPref.getFloat("bmi", 21.0f);

        setupByBMI();

        cardLoseWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGoal = "瘦身减重";
                updateCardSelection(cardLoseWeight);
            }
        });

        cardBuildMuscle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGoal = "塑形/增肌";
                updateCardSelection(cardBuildMuscle);
            }
        });

        cardKeepHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGoal = "保持健康";
                updateCardSelection(cardKeepHealth);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGoal.isEmpty()) {
                    Toast.makeText(AimActivity.this, "请选择一个运动目标", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedGoal.equals("保持健康")) {
                        // 直接跳转到运动偏好界面
                        Intent intent = new Intent(AimActivity.this, PreferenceActivity.class);
                        startActivity(intent);
                    } else {
                        // 跳转到重点改善部位界面，并传递体型
                        Intent intent = new Intent(AimActivity.this, FocusAreaActivity.class);
                        intent.putExtra("body_shape", bodyShape);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void setupByBMI() {
        String recommendText;
        CardView recommendedCard = null;
        TextView recommendedTag = null;

        if (bmiValue < 18.5) {
            recommendText = "你的BMI较低，推荐你优先增肌";
            recommendedCard = cardBuildMuscle;
            recommendedTag = tvRecommendBuild;
        } else if (bmiValue >= 24) {
            recommendText = "你的BMI偏高，推荐你优先减重";
            recommendedCard = cardLoseWeight;
            recommendedTag = tvRecommendLose;
        } else {
            recommendText = "你的BMI正常，推荐你选择保持健康";
            recommendedCard = cardKeepHealth;
            recommendedTag = tvRecommendKeep;
        }

        tvRecommendText.setText(recommendText);

        if (recommendedTag != null) {
            recommendedTag.setVisibility(View.VISIBLE);
        }

        if (recommendedCard != null) {
            selectedGoal = getGoalFromCard(recommendedCard);
            updateCardSelection(recommendedCard);
        }
    }

    private String getGoalFromCard(CardView card) {
        if (card == cardLoseWeight) return "瘦身减重";
        if (card == cardBuildMuscle) return "塑形/增肌";
        if (card == cardKeepHealth) return "保持健康";
        return "";
    }

    private void updateCardSelection(CardView selectedCard) {
        cardLoseWeight.setCardBackgroundColor(getColor(android.R.color.white));
        cardBuildMuscle.setCardBackgroundColor(getColor(android.R.color.white));
        cardKeepHealth.setCardBackgroundColor(getColor(android.R.color.white));
        selectedCard.setCardBackgroundColor(getColor(R.color.selected_card_bg));
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