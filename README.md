# Movie Streaming System (Hệ Thống Xem Phim Trực Tuyến)

Chào mừng bạn đã tham gia vào dự án! Hệ thống đã được thiết lập sẵn cấu hình kết nối API lấy phim tự động từ đối tác **KKPhim**, tích hợp **Spring Security** và cơ sở dữ liệu đám mây **Firebase**.

Dự án gồm 2 phần độc lập:
* **`movie-backend`**: Xử lý logic nghiệp vụ và dữ liệu (Spring Boot - Java 21).
* **`movie-app`**: Giao diện người dùng trên điện thoại (Android Studio - Java, SDK 34+).

---

## HƯỚNG DẪN LẤY KEY CẤU HÌNH FIREBASE (BẮT BUỘC)

Vì lý do bảo mật, các file chứa khóa bí mật đã được đưa vào `.gitignore` và không đẩy lên GitHub. **Mình (Chủ project) đã tạo sẵn Firebase và cấp quyền Admin rồi**, bạn không cần phải lên web tạo lại nữa. Hãy nhắn tin trực tiếp cho mình để lấy 2 file cấu hình, sau đó tự tay bỏ vào máy của bạn theo đúng vị trí sau:

### 1. Đối với Backend (`movie-backend`)
* **File cần xin mình:** `serviceAccountKey.json` (Khóa Admin SDK).
* **Vị trí bỏ vào:** Bạn hãy copy file này và dán trực tiếp vào thư mục:  
  `movie-backend/src/main/resources/`

### 2. Đối với Frontend (`movie-app`)
* **File cần xin mình:** `google-services.json` (Cấu hình Client Android).
* **Vị trí bỏ vào:** Bạn hãy copy file này và dán trực tiếp vào thư mục:  
  `movie-app/app/`

---

## Cách Chạy Dự Án Lần Đầu Sau Khi Nhận Key

### Bước 1: Khởi chạy Backend
1. Mở thư mục `movie-backend` bằng **IntelliJ IDEA**.
2. Chờ Maven tải xong các dependency, sau đó mở file `DemoApplication.java` (`src/main/java/com/movie_app_system/demo/`) và bấm **Run**.
3. **Link test API lấy phim mới nhất (Local):** `http://localhost:8080/api/v1/movies/latest?page=1`

### Bước 2: Khởi chạy Frontend
1. Mở thư mục `movie-app` bằng **Android Studio**.
2. Bấm nút **Sync Project with Gradle Files** (Hình con voi ở góc trên bên phải) để nạp file cấu hình và thư viện.
3. Chọn máy ảo hoặc cắm điện thoại thật để **Run**.
4. *Lưu ý khi gọi API:* Nếu bạn dùng máy ảo Android, hãy dùng IP `http://10.0.2.2:8080/` làm Base URL để nó hiểu là đang gọi về Backend chạy ở Localhost máy tính.

---

## BẢN ĐỒ CODE - VỊ TRÍ ĐỂ BẠN VIẾT TÍNH NĂNG TIẾP THEO

Để dự án không bị xung đột (conflict) code và đi đúng cấu trúc Base đã dựng, bạn hãy vào đúng các package được quy hoạch sẵn dưới đây để viết tiếp tính năng:

### Tại Cục Backend (`movie-backend`)
Mọi file code logic mới sẽ nằm trong package `com.movie_app_system.demo`:

* **`controller/`**: Nơi bạn viết các **API Endpoints** (Định nghĩa đường dẫn URL để App Android gọi vào).
* **`service/`**: Nơi bạn viết **Logic nghiệp vụ** xử lý (Ví dụ: logic đăng ký, đăng nhập, tìm kiếm phim,...).
* **`repository/`**: Nơi viết code tương tác, truy vấn dữ liệu trực tiếp với Database Firebase (Firestore).
* **`model/`**: Nơi định nghĩa các đối tượng dữ liệu (Entity/DTO) như `Movie.java`, `User.java`.
* *Lưu ý:* Package `config/` đã cấu hình sẵn Spring Security (CORS) và FirebaseConfig, bạn không cần chỉnh sửa gì thêm ở đây.

### Tại Cục Frontend (`movie-app`)
Mọi file Java điều khiển tính năng sẽ nằm trong package `com.example.movie_app`:

* **`network/`**: Nơi chứa `RetrofitClient.java` và `ApiService.java`. Nếu cần gọi API GET/POST mới từ Backend, hãy vào file `ApiService` để khai báo hàm.
* **`models/`**: Nơi tạo các class hứng dữ liệu JSON trả về từ Backend (Ví dụ: Class ánh xạ dữ liệu Phim, dữ liệu User).
* **`adapters/`**: Nơi viết các class `MovieAdapter` để đổ danh sách dữ liệu phim lên thanh cuộn `RecyclerView`.
* **Giao diện Java (Activities/Fragments)**: Code Java điều khiển màn hình (như xử lý bấm nút, chuyển màn hình) viết trực tiếp ở thư mục gốc của package này.
* **Giao diện XML (`app/src/main/res/layout/`)**: Vào đây để thiết kế, vẽ giao diện (Ví dụ: `activity_main.xml`, `item_movie.xml`).

---
_Lưu ý trước khi code, nhớ `git pull` bản mới nhất về. Khi hoàn thành một tính năng, hãy commit kèm mô tả rõ ràng nhé! Chúc bạn code mượt không bug!_
