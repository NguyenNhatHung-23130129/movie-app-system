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
2. Bấm nút **Sync Project with Gradle Files** (Hình con voi ở góc trên bên phải) để nạp các thư viện cấu hình kiến trúc (Retrofit, Firebase, Lifecycle ViewModel, LiveData...).
3. Chọn máy ảo hoặc cắm điện thoại thật để **Run**.

#### LƯU Ý ĐẶC BIỆT KHI CẤU HÌNH IP GỌI API (`RetrofitClient.java`):
* **Nếu dùng máy ảo (Emulator):** Giữ nguyên `BASE_URL = "http://10.0.2.2:8080/"`.
* **Nếu test bằng điện thoại thật (Physical Device):** 1. Bắt buộc điện thoại và máy tính phải **kết nối chung một mạng Wi-Fi**.
  2. Mở `cmd` trên máy tính, gõ `ipconfig` để lấy số **IPv4 Address** (Ví dụ: `192.168.1.5`).
  3. Sửa lại trong file thành: `BASE_URL = "http://192.168.1.5:8080/"`.

---

## BẢN ĐỒ CODE - VỊ TRÍ ĐỂ BẠN VIẾT TÍNH NĂNG TIẾP THEO

Để dự án không bị xung đột (conflict) code và đi đúng cấu trúc hệ thống, các thành viên tuyệt đối phải tuân thủ việc viết code đúng các package đã được quy hoạch sẵn:

### Tại Cục Backend (`movie-backend`)
Mọi file code logic mới sẽ nằm trong package `com.movie_app_system.demo`:

* **`controller/`**: Nơi viết các **API Endpoints** (Định nghĩa URL để App Android gọi vào).
* **`service/`**: Nơi xử lý **Logic nghiệp vụ chính** (Đăng ký, đăng nhập, đồng bộ dữ liệu...).
* **`repository/`**: Nơi tương tác, truy vấn dữ liệu trực tiếp với Database Firebase (Firestore).
* **`dto/`**: Nơi định nghĩa các đối tượng đóng gói dữ liệu để xuất bản sang Frontend (`MovieResponse.java`, `MovieItem.java`, `Pagination.java`).
* *Lưu ý:* Package `config/` đã cấu hình sẵn Spring Security (CORS) và FirebaseConfig, không chỉnh sửa nếu không có sự đồng ý của chủ project.

### Tại Cục Frontend (`movie-app`) - CHUẨN KIẾN TRÚC MVVM
Mọi file Java điều khiển tính năng sẽ nằm trong package `com.example.movie_app`:

* **`network/`**: Nơi quản lý kết nối mạng (`RetrofitClient.java`, `ApiService.java`). Mọi hàm gọi API (GET, POST...) từ Backend bắt buộc phải khai báo trong `ApiService`.
* **`models/`**: Lớp **Model** chứa cấu trúc hứng dữ liệu JSON trả về từ Backend (Cặp song sinh khớp với DTO của Backend).
* **`repository/`**: Lớp **Repository** chịu trách nhiệm xử lý nguồn dữ liệu (Gọi API kết nối Backend hoặc Firebase). Giao diện không được gọi trực tiếp lớp này.
* **`viewmodel/`**: Lớp **ViewModel** giữ vai trò làm bộ não xử lý logic của màn hình, cung cấp dữ liệu dạng `LiveData` cho tầng hiển thị. **Cấm tuyệt đối import các thư viện UI hoặc Context vào đây.**
* **`adapters/`**: Nơi viết các Adapter (như `MovieAdapter`) để đổ dữ liệu danh sách lên thanh cuộn `RecyclerView`.
* **Tầng hiển thị (View)**: Các file `Activity/Fragment` viết trực tiếp tại package gốc. Chỉ làm nhiệm vụ xử lý giao diện (như bắt sự kiện click nút) và `observe` (lắng nghe) dữ liệu từ ViewModel bắn ra để hiển thị lên màn hình. **Cấm viết logic gọi mạng tại đây!**
* **Giao diện XML (`app/src/main/res/layout/`)**: Nơi thiết kế vẽ giao diện app (Ví dụ: `activity_main.xml`, `item_movie.xml`).

---
_Lưu ý trước khi code, nhớ `git pull` bản mới nhất về. Khi hoàn thành một tính năng, hãy commit kèm mô tả rõ ràng nhé! Chúc bạn code mượt không bug!_
