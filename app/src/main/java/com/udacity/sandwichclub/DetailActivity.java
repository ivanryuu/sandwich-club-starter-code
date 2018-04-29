package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.description_tv)
    TextView descriptionTV;

    @BindView(R.id.origin_tv)
    TextView originTV;

    @BindView(R.id.also_known_tv)
    TextView alsoKnownAsTV;

    @BindView(R.id.ingredients_tv)
    TextView ingredientsTV;

    @BindView(R.id.image_iv)
    ImageView ingredientsIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.placeholder)
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        populateTV(alsoKnownAsTV, TextUtils.join(", ", sandwich.getAlsoKnownAs().toArray()));

        populateTV(originTV, sandwich.getPlaceOfOrigin());

        populateTV(descriptionTV, sandwich.getDescription());

        populateTV(ingredientsTV, TextUtils.join("\n", sandwich.getIngredients().toArray()));
    }

    private void populateTV(TextView textView, String text) {
        if (TextUtils.isEmpty(text)) {
            text = getResources().getString(R.string.no_data_message);
        }
        textView.setText(text);
    }
}
