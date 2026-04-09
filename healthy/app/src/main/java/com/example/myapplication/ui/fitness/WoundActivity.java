package com.example.myapplication.ui.fitness;

import android.content.Intent;
import android.content.SharedPreferences;   // 添加这一行
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.myapplication.R;

public class WoundActivity extends AppCompatActivity {

    private CardView cardKnee, cardWaist;
    private Button btnNext;
    private boolean isKneeSelected = false;
    private boolean isWaistSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wound);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        cardKnee = findViewById(R.id.cardKnee);
        cardWaist = findViewById(R.id.cardWaist);
        btnNext = findViewById(R.id.btnNext);

        cardKnee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isKneeSelected = !isKneeSelected;
                updateCardBackground(cardKnee, isKneeSelected);
            }
        });

        cardWaist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWaistSelected = !isWaistSelected;
                updateCardBackground(cardWaist, isWaistSelected);
            }
        });

        btnNext.setOnClickListener(v -> {
            StringBuilder selected = new StringBuilder();
            if (isKneeSelected) selected.append("膝盖 ");
            if (isWaistSelected) selected.append("腰部 ");
            String injuries = selected.length() == 0 ? "无伤病情况" : selected.toString().trim();

            SharedPreferences sharedPref = getSharedPreferences("health_app", MODE_PRIVATE);
            sharedPref.edit().putString("injuries", injuries).apply();

            Intent intent = new Intent(WoundActivity.this, BodilyFormActivity.class);
            startActivity(intent);
        });
    }

    private void updateCardBackground(CardView card, boolean isSelected) {
        if (isSelected) {
            card.setCardBackgroundColor(getColor(R.color.selected_color));
        } else {
            card.setCardBackgroundColor(getColor(android.R.color.white));
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