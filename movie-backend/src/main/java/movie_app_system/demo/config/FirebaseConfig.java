package movie_app_system.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = new ClassPathResource("serviceAccountKey.json").getInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);

                System.out.println("\n=================================================");
                System.out.println("FIREBASE ADMIN SDK ĐÃ KHỞI TẠO THÀNH CÔNG! 🎉");
                System.out.println("=================================================\n");
            }
        } catch (IOException e) {
            System.err.println("\nLỗi khởi tạo Firebase: Không tìm thấy hoặc lỗi file serviceAccountKey.json!");
            System.err.println("Chi tiết lỗi: " + e.getMessage() + "\n");
        }
    }

    @Bean
    public FirebaseDatabase firebaseDatabase() {
        return FirebaseDatabase.getInstance("https://movie-app-system-d6696-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }
}