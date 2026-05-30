package movie_app_system.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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
                System.out.println("🎉 FIREBASE ADMIN SDK ĐÃ KHỞI TẠO THÀNH CÔNG! 🎉");
                System.out.println("=================================================\n");
            }
        } catch (IOException e) {
            System.err.println("\n❌ Lỗi khởi tạo Firebase: Không tìm thấy hoặc lỗi file serviceAccountKey.json!");
            System.err.println("Chi tiết lỗi: " + e.getMessage() + "\n");
        }
    }
}