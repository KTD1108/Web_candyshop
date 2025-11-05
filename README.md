# CandyShop - Full-Stack E-commerce Application

Đây là một ứng dụng web thương mại điện tử full-stack cho một cửa hàng kẹo, được xây dựng bằng Java, Spring Boot cho backend và HTML/CSS đơn giản cho frontend.

## ✨ Tính năng

*   **Xác thực người dùng:** Đăng ký và đăng nhập cho người dùng với xác thực dựa trên JWT.
*   **Phân quyền:** Các vai trò riêng biệt cho Người dùng (User) và Quản trị viên (Admin).
*   **Quản lý sản phẩm:** Admin có thể thêm, sửa, xóa sản phẩm.
*   **Duyệt sản phẩm:** Người dùng có thể xem sản phẩm theo danh mục.
*   **Giỏ hàng:** Người dùng có thể thêm sản phẩm vào giỏ hàng, cập nhật số lượng và xóa sản phẩm.
*   **Thanh toán:** Người dùng có thể đặt hàng từ các mặt hàng trong giỏ hàng.
*   **Lịch sử đơn hàng:** Người dùng có thể xem lịch sử các đơn hàng đã đặt.
*   **Bảng điều khiển Admin:** Giao diện riêng cho admin để quản lý sản phẩm, đơn hàng và người dùng.

## 🛠️ Công nghệ sử dụng

### Backend
*   **Java 21**
*   **Spring Boot 3.3.3**
    *   **Spring Web:** Để xây dựng các REST API.
    *   **Spring Security:** Để xử lý xác thực và phân quyền với JWT.
    *   **Spring Data JPA:** Để tương tác với cơ sở dữ liệu.
*   **MySQL:** Hệ quản trị cơ sở dữ liệu quan hệ.
*   **Flyway:** Công cụ quản lý và tự động hóa việc di trú (migration) cơ sở dữ liệu.
*   **Maven:** Công cụ quản lý và xây dựng dự án.
*   **Lombok:** Thư viện giúp giảm thiểu code soạn sẵn (boilerplate).

### Frontend
*   HTML
*   CSS

## 🚀 Bắt đầu

### Điều kiện tiên quyết
*   **JDK 21** hoặc mới hơn.
*   **Maven 3.8** hoặc mới hơn.
*   **MySQL Server** đang chạy.

### Hướng dẫn cài đặt và khởi chạy

1.  **Clone repository:**
    ```bash
    git clone <URL_CUA_REPOSITORY>
    cd candyshop
    ```

2.  **Cấu hình cơ sở dữ liệu:**
    *   Mở tệp `src/main/resources/application.yml`.
    *   Đảm bảo rằng bạn có một database MySQL tên là `candyshop`. Flyway sẽ tự động tạo các bảng.
    *   Cập nhật `username` và `password` cho phù hợp với cấu hình MySQL của bạn:
      ```yaml
      spring:
        datasource:
          url: jdbc:mysql://localhost:3306/candyshop?createDatabaseIfNotExist=true
          username: root # <-- THAY ĐỔI NẾU CẦN
          password: root # <-- THAY ĐỔI NẾU CẦN
      ```

3.  **Cấu hình JWT Secret:**
    *   Trong cùng tệp `application.yml`, thay đổi giá trị `secret` trong phần `app.jwt` thành một chuỗi ngẫu nhiên và an toàn dài ít nhất 32 ký tự.
      ```yaml
      app:
        jwt:
          secret: "CHANGE_ME_TO_A_LONG_RANDOM_SECRET_32+_CHARS" # <-- THAY ĐỔI GIÁ TRỊ NÀY
      ```

4.  **Build và chạy ứng dụng:**
    *   Sử dụng Maven để build và chạy ứng dụng Spring Boot:
    ```bash
    mvn spring-boot:run
    ```
    *   Backend API sẽ có sẵn tại `http://localhost:8080`.

5.  **Truy cập Frontend:**
    *   Mở các tệp `.html` trong thư mục `frontend` trực tiếp bằng trình duyệt của bạn để tương tác với ứng dụng.

---
*Tệp README này được tạo tự động.*
