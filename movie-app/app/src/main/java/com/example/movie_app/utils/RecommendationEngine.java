package com.example.movie_app.utils;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class RecommendationEngine {
    private float[][] embeddings;
    private List<String> movieIds;
    private Map<String, Integer> movieIndexMap;

    public RecommendationEngine(Context context) {
        loadData(context);
    }

    private void loadData(Context context) {
        try {
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("embeddings.json")));
            List<List<Float>> rawEmbeddings = gson.fromJson(reader, new TypeToken<List<List<Float>>>(){}.getType());

            int rows = rawEmbeddings.size();
            int cols = rawEmbeddings.get(0).size();
            embeddings = new float[rows][cols];
            for (int i = 0; i < rows; i++) {
                float norm = 0;
                for (float val : rawEmbeddings.get(i)) norm += val * val;
                norm = (float) Math.sqrt(norm);

                for (int j = 0; j < cols; j++) {
                    embeddings[i][j] = (norm != 0) ? rawEmbeddings.get(i).get(j) / norm : 0;
                }
            }

            movieIds = new ArrayList<>();
            movieIndexMap = new HashMap<>();
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(context.getAssets().open("movie_mapping.csv")));
            csvReader.readLine();
            String line;
            int idx = 0;
            while ((line = csvReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String id = parts[0].trim();
                    movieIds.add(id);
                    movieIndexMap.put(id, idx++);
                }
            }
            Log.d("DEBUG_ENGINE", "Load data thành công. Movies: " + movieIds.size());
        } catch (Exception e) {
            Log.e("DEBUG_ENGINE", "Lỗi load file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private float dotProduct(float[] v1, float[] v2) {
        float sum = 0;
        for (int i = 0; i < v1.length; i++) sum += v1[i] * v2[i];
        return sum;
    }

    public List<String> getRecommendations(List<String> watchedMovieIds, int topK) {
        if (embeddings == null || watchedMovieIds == null || watchedMovieIds.isEmpty()) return new ArrayList<>();

        float[] userProfile = new float[embeddings[0].length];
        int count = 0;
        for (String id : watchedMovieIds) {
            Integer idx = movieIndexMap.get(id);
            if (idx != null) {
                for (int i = 0; i < userProfile.length; i++) {
                    userProfile[i] += embeddings[idx][i];
                }
                count++;
            }
        }

        if (count == 0) return new ArrayList<>();

        for (int i = 0; i < userProfile.length; i++) {
            userProfile[i] /= count;
        }

        float minSimilarity = 0.2f;

        PriorityQueue<Map.Entry<String, Float>> pq = new PriorityQueue<>(
                (a, b) -> Float.compare(b.getValue(), a.getValue())
        );

        for (int i = 0; i < embeddings.length; i++) {
            float sim = dotProduct(userProfile, embeddings[i]);
            if (sim > minSimilarity) {
                pq.add(new AbstractMap.SimpleEntry<>(movieIds.get(i), sim));
            }
        }

        List<String> results = new ArrayList<>();
        while (results.size() < topK && !pq.isEmpty()) {
            String id = pq.poll().getKey();
            if (!watchedMovieIds.contains(id)) {
                results.add(id);
            }
        }
        return results;
    }
}