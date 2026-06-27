package movie_app_system.demo.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import movie_app_system.demo.api.KKPhimClient;
import movie_app_system.demo.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.util.*;

@Service
public class MovieSyncService {

    private static final Logger logger = LoggerFactory.getLogger(MovieSyncService.class);
    private static final String IMAGE_DOMAIN = "https://phimimg.com/";

    @Autowired
    private KKPhimClient kkPhimClient;

    @Autowired
    private FirebaseDatabase firebaseDatabase;

    @Scheduled(fixedRate = 3600000)
    public void syncNewMovies() {
        DatabaseReference moviesRef = firebaseDatabase.getReference("movies");
        DatabaseReference categoryIndexRef = firebaseDatabase.getReference("by_category");
        DatabaseReference countryIndexRef = firebaseDatabase.getReference("by_country");
        DatabaseReference typeIndexRef = firebaseDatabase.getReference("by_type");
        DatabaseReference yearIndexRef = firebaseDatabase.getReference("by_year");
        DatabaseReference metadataRef = firebaseDatabase.getReference("metadata");

        logger.info("[SYNC] Bắt đầu quy trình đồng bộ và đánh chỉ mục...");

        for (int page = 1; page <= 3; page++) {
            try {
                Response<KKPhimListResponse> listResponse = kkPhimClient.getNewMovies(page).execute();
                if (!listResponse.isSuccessful() || listResponse.body() == null) continue;

                for (MovieItem item : listResponse.body().getItems()) {
                    try {
                        Response<MovieDetailResponse> detailRes = kkPhimClient.getMovieDetail(item.getSlug()).execute();

                        if (detailRes.isSuccessful() && detailRes.body() != null && detailRes.body().getMovie() != null) {
                            var detail = detailRes.body().getMovie();
                            String slug = detail.getSlug();

                            Map<String, Object> data = new HashMap<>();
                            data.put("id", detail.getId());
                            data.put("name", detail.getName());
                            data.put("slug", slug);
                            data.put("poster_url", formatImageUrl(detail.getPosterUrl()));
                            data.put("year", detail.getYear());
                            data.put("type", detail.getType());
                            data.put("category", convertToMapList(detail.getCategory()));
                            data.put("country", convertToMapList(detail.getCountry()));
                            moviesRef.child(slug).setValueAsync(data);

                            if (detail.getCategory() != null) {
                                for (Category c : detail.getCategory()) {
                                    categoryIndexRef.child(c.getSlug()).child("movies").child(slug).setValueAsync(true);
                                    categoryIndexRef.child(c.getSlug()).child("name").setValueAsync(c.getName());
                                    metadataRef.child("categories").child(c.getSlug()).setValueAsync(c.getName());
                                }
                            }
                            if (detail.getCountry() != null) {
                                for (Country c : detail.getCountry()) {
                                    countryIndexRef.child(c.getSlug()).child("movies").child(slug).setValueAsync(true);
                                    countryIndexRef.child(c.getSlug()).child("name").setValueAsync(c.getName());
                                    metadataRef.child("countries").child(c.getSlug()).setValueAsync(c.getName());
                                }
                            }

                            typeIndexRef.child(detail.getType()).child(slug).setValueAsync(true);
                            yearIndexRef.child(String.valueOf(detail.getYear())).child(slug).setValueAsync(true);

                            logger.info("[SYNC] Đã đồng bộ và đánh chỉ mục thành công: {}", slug);
                        }
                    } catch (Exception e) {
                        logger.error("Lỗi sync chi tiết {}: {}", item.getSlug(), e.getMessage());
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                logger.error("Lỗi đồng bộ trang {}: {}", page, e.getMessage());
            }
        }
    }

    private <T> List<Map<String, String>> convertToMapList(List<T> list) {
        List<Map<String, String>> result = new ArrayList<>();
        if (list == null) return result;
        for (T item : list) {
            Map<String, String> map = new HashMap<>();
            if (item instanceof Category) {
                Category c = (Category) item;
                map.put("name", c.getName());
                map.put("slug", c.getSlug());
            } else if (item instanceof Country) {
                Country c = (Country) item;
                map.put("name", c.getName());
                map.put("slug", c.getSlug());
            }
            result.add(map);
        }
        return result;
    }

    private String formatImageUrl(String url) {
        if (url == null || url.isEmpty()) return "";
        return url.startsWith("http") ? url : IMAGE_DOMAIN + url;
    }
}