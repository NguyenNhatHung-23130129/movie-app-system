package movie_app_system.demo.config;

import movie_app_system.demo.api.KKPhimClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitBackendConfig {

    @Bean
    public KKPhimClient kkPhimClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://phimapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(KKPhimClient.class);
    }
}