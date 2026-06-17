package movie_app_system.demo.config;

import com.google.gson.*;
import movie_app_system.demo.api.KKPhimClient;
import movie_app_system.demo.dto.MovieDetailResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;

@Configuration
public class RetrofitBackendConfig {

    @Bean
    public KKPhimClient kkPhimClient() {

        JsonDeserializer<MovieDetailResponse> detailDeserializer = new JsonDeserializer<MovieDetailResponse>() {
            @Override
            public MovieDetailResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                MovieDetailResponse response = new MovieDetailResponse();

                if (jsonObject.has("status")) {
                    response.setStatus(jsonObject.get("status").getAsBoolean());
                }
                if (jsonObject.has("msg")) {
                    response.setMsg(jsonObject.get("msg").getAsString());
                }

                if (jsonObject.has("movie") && jsonObject.get("movie").isJsonObject()) {
                    MovieDetailResponse.MovieInfo movieInfo = context.deserialize(
                            jsonObject.get("movie"),
                            MovieDetailResponse.MovieInfo.class
                    );
                    response.setMovie(movieInfo);
                } else {
                    response.setMovie(null);
                }

                return response;
            }
        };

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MovieDetailResponse.class, detailDeserializer)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://phimapi.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(KKPhimClient.class);
    }
}