package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipeapp.Adapters.IngAda;
import com.example.recipeapp.Adapters.InstruAdap;
import com.example.recipeapp.Adapters.SimiReciAdap;
import com.example.recipeapp.Listeners.InstruListen;
import com.example.recipeapp.Listeners.ReciClickListen;
import com.example.recipeapp.Listeners.ReciDetaListen;
import com.example.recipeapp.Listeners.SimiReciListen;
import com.example.recipeapp.Models.Ingredient;
import com.example.recipeapp.Models.InstructionsResponse;
import com.example.recipeapp.Models.RecipeDetailsResponse;
import com.example.recipeapp.Models.SimilarRecipeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReciDetaActivity extends AppCompatActivity {
    int id;
    TextView textView_meal_name, textView_meal_source,textView_meal_summary;
    ImageView imageView_meal_image;
    RecyclerView recycler_meal_ingredients, recycler_meal_similar, recycler_meal_instructions;
    ReqMng manager;
    ProgressDialog dialog;
    IngAda ingredientsAdapter;
    SimiReciAdap similarRecipeAdapter;
    InstruAdap instructionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reci_dets);

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new ReqMng(this);
        manager.getRecipeDetails(recipeDetailsListener,id);
        manager.getSimilaRecipes(similarRecipesListener, id);
        manager.getInstructions(instructionsListener, id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details...");
        dialog.show();

    }

    private void findViews() {
        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.textView_meal_source);
        textView_meal_summary = findViewById(R.id.textView_meal_summary);
        imageView_meal_image = findViewById(R.id.imageView_meal_image);
        recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
        recycler_meal_similar = findViewById(R.id.recycler_meal_similar);
        recycler_meal_instructions = findViewById(R.id.recycler_meal_instructions);
    }

    private final ReciDetaListen recipeDetailsListener = new ReciDetaListen() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceName);
            textView_meal_summary.setText(response.summary);
            Picasso.get().load(response.image).into(imageView_meal_image);

            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(new LinearLayoutManager(ReciDetaActivity.this, LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngAda(ReciDetaActivity.this, response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(ReciDetaActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final SimiReciListen similarRecipesListener = new SimiReciListen() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> response, String message) {
            recycler_meal_similar.setHasFixedSize(true);
            recycler_meal_similar.setLayoutManager(new LinearLayoutManager(ReciDetaActivity.this,LinearLayoutManager.HORIZONTAL, false));
            similarRecipeAdapter = new SimiReciAdap(ReciDetaActivity.this, response, recipeClickListener );
            recycler_meal_similar.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(ReciDetaActivity.this,message, Toast.LENGTH_SHORT).show();
        }
    };

    private final ReciClickListen recipeClickListener = new ReciClickListen() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(ReciDetaActivity.this, ReciDetaActivity.class)
                    .putExtra("id", id));
        }
    };
    private final InstruListen instructionsListener = new InstruListen() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_meal_instructions.setHasFixedSize(true);
            recycler_meal_instructions.setLayoutManager(new LinearLayoutManager(ReciDetaActivity.this, LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstruAdap(ReciDetaActivity.this, response);
            recycler_meal_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {
            // Handle error fetching instructions
            Toast.makeText(ReciDetaActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };


}