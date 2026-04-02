package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

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

        // 设置卡片点击事件
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

        // 下一步按钮
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 收集选中的伤病
                StringBuilder selected = new StringBuilder();
                if (isKneeSelected) selected.append("膝盖 ");
                if (isWaistSelected) selected.append("腰部 ");

                Intent intent = new Intent(WoundActivity.this, BodilyFormActivity.class);
                intent.putExtra("selected_injuries", selected.toString().trim());
                startActivity(intent);
            }
        });
    }

    /**
     * 更新卡片的背景颜色来表示选中状态
     * @param card 卡片视图
     * @param isSelected 是否选中
     */
    private void updateCardBackground(CardView card, boolean isSelected) {
        if (isSelected) {
            card.setCardBackgroundColor(getColor(R.color.selected_color)); // 需要定义颜色资源
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