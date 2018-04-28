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

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

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
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        populateTV(R.id.also_known_label_tv, R.id.also_known_tv,
                TextUtils.join(", ", sandwich.getAlsoKnownAs().toArray()));

        populateTV(R.id.origin_label_tv, R.id.origin_tv, sandwich.getPlaceOfOrigin());

        populateTV(R.id.description_label_tv, R.id.description_tv, sandwich.getDescription());

        populateTV(R.id.ingredients_label_tv, R.id.ingredients_tv,
                TextUtils.join("\n", sandwich.getIngredients().toArray()));
    }

    private void populateTV(int labelId, int valueId, String text) {
        TextView labelTV = findViewById(labelId);
        TextView valueTV = findViewById(valueId);
        if (TextUtils.isEmpty(text)) {
            labelTV.setVisibility(View.GONE);
            valueTV.setVisibility(View.GONE);
        } else {
            labelTV.setVisibility(View.VISIBLE);
            valueTV.setVisibility(View.VISIBLE);
            valueTV.setText(text);
        }
    }
}
