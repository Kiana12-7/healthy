package com.example.myapplication.ui.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.myapplication.R;

public class FocusAreaActivity extends AppCompatActivity {

    private ImageView ivBodyImage;
    private CardView cardShoulderArm, cardChest, cardAbdomen, cardHipsLegs, cardFullBody;
    private Button btnNext;
    private String selectedArea = "";

    private TextView tvRecommendShoulder, tvRecommendChest, tvRecommendAbdomen, tvRecommendHipsLegs, tvRecommendFullBody;
    private CardView[] cards;
    private TextView[] recommendTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_area);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

        ivBodyImage = findViewById(R.id.ivBodyImage);
        cardShoulderArm = findViewById(R.id.cardShoulderArm);
        cardChest = findViewById(R.id.cardChest);
        cardAbdomen = findViewById(R.id.cardAbdomen);
        cardHipsLegs = findViewById(R.id.cardHipsLegs);
        cardFullBody = findViewById(R.id.cardFullBody);
        btnNext = findViewById(R.id.btnNext);

        tvRecommendShoulder = findViewById(R.id.tvRecommendShoulder);
        tvRecommendChest = findViewById(R.id.tvRecommendChest);
        tvRecommendAbdomen = findViewById(R.id.tvRecommendAbdomen);
        tvRecommendHipsLegs = findViewById(R.id.tvRecommendHipsLegs);
        tvRecommendFullBody = findViewById(R.id.tvRecommendFullBody);

        cards = new CardView[]{cardShoulderArm, cardChest, cardAbdomen, cardHipsLegs, cardFullBody};
        recommendTags = new TextView[]{tvRecommendShoulder, tvRecommendChest, tvRecommendAbdomen, tvRecommendHipsLegs, tvRecommendFullBody};

        String shape = getIntent().getStringExtra("selected_shape");
        if (shape == null) shape = "直筒型";

        setBodyImage(shape);
        String recommended = getRecommendedArea(shape);
        highlightRecommended(recommended);

        setupCardClick(cardShoulderArm, "肩臂");
        setupCardClick(cardChest, "胸部");
        setupCardClick(cardAbdomen, "腰腹");
        setupCardClick(cardHipsLegs, "臀腿");
        setupCardClick(cardFullBody, "全身");

        for (CardView card : cards) {
            if (getAreaFromCard(card).equals(recommended)) {
                updateCardSelection(card);
                selectedArea = recommended;
                break;
            }
        }

        btnNext.setOnClickListener(v -> {
            if (selectedArea.isEmpty()) {
                Toast.makeText(FocusAreaActivity.this, "请选择一个重点改善部位", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPref = getSharedPreferences("health_app", MODE_PRIVATE);
                sharedPref.edit().putString("focus_area", selectedArea).apply();

                Intent intent = new Intent(FocusAreaActivity.this, BodyPartFocusActivity.class);
                intent.putExtra("part", selectedArea);
                startActivity(intent);
            }
        });
        // 在 onCreate 中找到 btnNext 后添加：
        btnNext.setEnabled(true);
        btnNext.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.button_enabled)));
    }

    private void setBodyImage(String shape) {
        int imageRes;
        switch (shape) {
            case "梨型":
                imageRes = R.drawable.pear_form;
                break;
            case "沙漏型":
                imageRes = R.drawable.funnel_form;
                break;
            case "苹果型":
                imageRes = R.drawable.apple_form;
                break;
            case "直筒型":
            default:
                imageRes = R.drawable.stright_form;
                break;
        }
        ivBodyImage.setImageResource(imageRes);
    }

    private String getRecommendedArea(String shape) {
        switch (shape) {
            case "苹果型":
                return "腰腹";
            case "梨型":
                return "臀腿";
            case "沙漏型":
                return "肩臂";
            case "直筒型":
            default:
                return "全身";
        }
    }

    private void highlightRecommended(String area) {
        for (TextView tag : recommendTags) {
            tag.setVisibility(View.GONE);
        }
        switch (area) {
            case "肩臂":
                tvRecommendShoulder.setVisibility(View.VISIBLE);
                break;
            case "胸部":
                tvRecommendChest.setVisibility(View.VISIBLE);
                break;
            case "腰腹":
                tvRecommendAbdomen.setVisibility(View.VISIBLE);
                break;
            case "臀腿":
                tvRecommendHipsLegs.setVisibility(View.VISIBLE);
                break;
            case "全身":
                tvRecommendFullBody.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupCardClick(CardView card, final String area) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedArea = area;
                updateCardSelection(card);
            }
        });
    }

    private void updateCardSelection(CardView selectedCard) {
        for (CardView card : cards) {
            card.setCardBackgroundColor(getColor(android.R.color.white));
        }
        selectedCard.setCardBackgroundColor(getColor(R.color.selected_card_bg));
    }

    private String getAreaFromCard(CardView card) {
        if (card == cardShoulderArm) return "肩臂";
        if (card == cardChest) return "胸部";
        if (card == cardAbdomen) return "腰腹";
        if (card == cardHipsLegs) return "臀腿";
        if (card == cardFullBody) return "全身";
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