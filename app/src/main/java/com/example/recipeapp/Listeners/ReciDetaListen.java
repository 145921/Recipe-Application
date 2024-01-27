package com.example.recipeapp.Listeners;

import com.example.recipeapp.Models.RecipeDetailsResponse;

public interface ReciDetaListen {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
