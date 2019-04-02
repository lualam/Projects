package com.example.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BloodGroupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_group);
        ButterKnife.bind(this);

        back.setOnClickListener(this);
        tv_a_plus.setOnClickListener(this);
        tv_a_minus.setOnClickListener(this);
        tv_b_plus.setOnClickListener(this);
        tv_b_minus.setOnClickListener(this);
        tv_ab_plus.setOnClickListener(this);
        tv_ab_minus.setOnClickListener(this);
        tv_o_plus.setOnClickListener(this);
        tv_o_minus.setOnClickListener(this);
    }

    public void sendResult(View view, String bloodGroup) {
        view.setBackgroundResource(R.drawable.blood_group_item_selected);
        ((TextView) view).setTextColor(Color.WHITE);
        Intent intent = new Intent();
        intent.putExtra("bloodgroup", bloodGroup);
        setResult(RESULT_OK, intent);
        finish();
    }



    @Override
    public void onClick(View view) {
        if (view == back) {
            setResult(RESULT_CANCELED);
            onBackPressed();
        } else if (view == tv_a_plus) {
            sendResult(view, "A+");
        } else if (view == tv_a_minus) {
            sendResult(view, "A-");
        } else if (view == tv_b_plus) {
            sendResult(view, "B+");
        } else if (view == tv_b_minus) {
            sendResult(view, "B-");
        } else if (view == tv_ab_plus) {
            sendResult(view, "AB+");
        } else if (view == tv_ab_minus) {
            sendResult(view, "AB-");
        } else if (view == tv_o_plus) {
            sendResult(view, "O+");
        } else if (view == tv_o_minus) {
            sendResult(view, "O-");
        }
    }

    @BindView(R.id.blood_group_back)
    ImageView back;
    @BindView(R.id.tv_a_plus)
    TextView tv_a_plus;
    @BindView(R.id.tv_a_minus)
    TextView tv_a_minus;
    @BindView(R.id.tv_b_plus)
    TextView tv_b_plus;
    @BindView(R.id.tv_b_minus)
    TextView tv_b_minus;
    @BindView(R.id.tv_ab_plus)
    TextView tv_ab_plus;
    @BindView(R.id.tv_ab_minus)
    TextView tv_ab_minus;
    @BindView(R.id.tv_o_plus)
    TextView tv_o_plus;
    @BindView(R.id.tv_o_minus)
    TextView tv_o_minus;
}
