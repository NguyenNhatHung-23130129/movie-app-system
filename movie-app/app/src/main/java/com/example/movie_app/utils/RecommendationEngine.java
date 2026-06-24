package com.example.movie_app.utils;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class RecommendationEngine {
    private List<List<Float>> embeddings;
    private List<String> movieIds;

    public RecommendationEngine(Context context) {
        loadData(context);
    }

    private void loadData(Context context) {
        try {
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("embeddings.json")));
            Type listType = new TypeToken<List<List<Float>>>(){}.getType();
            embeddings = gson.fromJson(reader, listType);

            movieIds = new ArrayList<>();
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(context.getAssets().open("movie_mapping.csv")));
            String line;
            csvReader.readLine();
            while ((line = csvReader.readLine()) != null) {
                movieIds.add(line.split(",")[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float cosineSimilarity(List<Float> v1, List<Float> v2) {
        float dotProduct = 0, normA = 0, normB = 0;
        for (int i = 0; i < v1.size(); i++) {
            dotProduct += v1.get(i) * v2.get(i);
            normA += v1.get(i) * v1.get(i);
            normB += v2.get(i) * v2.get(i);
        }
        return (float) (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
    }

    public List<String> getRecommendations(String currentMovieId, int topK) {
        int index = movieIds.indexOf(currentMovieId);
        if (index == -1) return new ArrayList<>();

        List<Float> targetVector = embeddings.get(index);
        PriorityQueue<Map.Entry<String, Float>> pq = new PriorityQueue<>(
                (a, b) -> Float.compare(b.getValue(), a.getValue())
        );

        for (int i = 0; i < embeddings.size(); i++) {
            if (i == index) continue;
            float sim = cosineSimilarity(targetVector, embeddings.get(i));
            pq.add(new AbstractMap.SimpleEntry<>(movieIds.get(i), sim));
        }

        List<String> results = new ArrayList<>();
        for (int i = 0; i < topK && !pq.isEmpty(); i++) {
            results.add(pq.poll().getKey());
        }
        return results;
    }
}