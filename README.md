# Tiệm Ngọt 108 - Dự án Web Fullstack (Spring Boot + Thymeleaf)

Dự án "Tiệm Ngọt 108" là một ứng dụng web fullstack được phát triển bằng Spring Boot. Ứng dụng này tích hợp cả logic nghiệp vụ backend và giao diện người dùng frontend thông qua việc sử dụng Thymeleaf để render các trang HTML động và phục vụ các tài nguyên tĩnh. Nó cung cấp một nền tảng thương mại điện tử hoàn chỉnh cho cửa hàng bánh kẹo trực tuyến, với các tính năng quản lý sản phẩm, đơn hàng, người dùng và bảo mật JWT.

## Mục lục

- Giới thiệu dự án
- Tính năng
- Công nghệ sử dụng
- Cấu trúc thư mục
- Bắt đầu
  - Điều kiện tiên quyết
  - Cài đặt và Xây dựng
  - Thiết lập cơ sở dữ liệu
  - Chạy ứng dụng

## Giới thiệu dự án

Dự án "Tiệm Ngọt 108" là một ứng dụng web thương mại điện tử đầy đủ tính năng, phát triển theo kiến trúc Fullstack sử dụng Spring Boot cho phần Backend và kết hợp Thymeleaf cùng Vanilla JavaScript, Bootstrap cho phần Frontend. Ứng dụng cung cấp một nền tảng mua sắm trực tuyến cho cửa hàng bánh kẹo, cho phép khách hàng thực hiện các tác vụ như duyệt sản phẩm, quản lý giỏ hàng, đặt hàng và quản lý tài khoản cá nhân. Ngoài ra, hệ thống còn tích hợp giao diện quản trị viên mạnh mẽ để quản lý sản phẩm, đơn hàng, người dùng và các chương trình khuyến mãi (voucher). Ứng dụng được xây dựng với mục tiêu mang lại trải nghiệm người dùng mượt mà, trực quan và đảm bảo an toàn thông tin với các cơ chế bảo mật hiện đại như JWT.

## Tính năng

-   **Xác thực & Ủy quyền (Authentication & Authorization)**:
    -   Đăng ký tài khoản người dùng mới.
    -   Đăng nhập an toàn sử dụng JSON Web Tokens (JWT) để xác thực người dùng và quản lý phiên.
    -   Quản lý hồ sơ cá nhân cho người dùng.
    -   Hệ thống phân quyền chi tiết (Role-Based Access Control - RBAC) với các vai trò `Admin` và `User`.
-   **Quản lý Sản phẩm**:
    -   Xem danh sách sản phẩm, duyệt theo danh mục và tìm kiếm sản phẩm.
    -   Hiển thị chi tiết từng sản phẩm.
    -   **Tính năng Admin**: Thêm mới, chỉnh sửa, xóa sản phẩm khỏi hệ thống.
-   **Quản lý Giỏ hàng**:
    -   Thêm sản phẩm vào giỏ hàng.
    -   Cập nhật số lượng sản phẩm trong giỏ.
    -   Xóa sản phẩm khỏi giỏ hàng.
    -   Xem tổng giá trị đơn hàng trong giỏ.
-   **Quản lý Đơn hàng**:
    -   Đặt hàng từ giỏ hàng đã chọn.
    -   Xem lịch sử và trạng thái các đơn hàng đã đặt.
    -   **Tính năng Admin**: Cập nhật trạng thái của các đơn hàng (ví dụ: đang xử lý, đã giao, đã hủy).
-   **Quản lý Mã giảm giá (Voucher)**:
    -   Khách hàng có thể áp dụng mã giảm giá khi thanh toán để nhận ưu đãi.
    -   **Tính năng Admin**: Quản lý toàn diện các mã giảm giá, bao gồm tạo mới, chỉnh sửa chi tiết (mã, giá trị, loại giảm giá PERCENTAGE/FIXED_AMOUNT, ngày hiệu lực `validFrom`, ngày hết hạn `validTo`, giới hạn sử dụng).
-   **Cơ sở dữ liệu**:
    -   Sử dụng MySQL làm hệ quản trị cơ sở dữ liệu chính.
    -   Quản lý phiên bản lược đồ cơ sở dữ liệu tự động với Flyway Migrations.

## Công nghệ sử dụng

Dự án này được xây dựng trên nền tảng Spring Boot và sử dụng các công nghệ chính sau:

-   **Java:** Phiên bản 21
-   **Spring Boot:** 3.3.3
    -   **Spring Web:** Xây dựng các API RESTful và quản lý request/response.
    -   **Spring Security:** Cung cấp các tính năng bảo mật mạnh mẽ, bao gồm xác thực và phân quyền dựa trên JWT.
    -   **Spring Data JPA:** Đơn giản hóa việc tương tác với cơ sở dữ liệu thông qua các Repository và Hibernate.
    -   **Spring Validation:** Hỗ trợ xác thực dữ liệu đầu vào.
    -   **Thymeleaf:** Công cụ tạo mẫu phía máy chủ, được sử dụng để phục vụ các tệp HTML (templates) cho trình duyệt. Tuy nhiên, phần lớn nội dung động trên các trang được xử lý bởi JavaScript phía client sau khi trang được tải.
-   **Bootstrap:** Phiên bản 5.3.3, framework CSS phổ biến để xây dựng giao diện người dùng đáp ứng và hiện đại.
-   **Vanilla JavaScript:** Được sử dụng để xử lý tương tác phía client, tải dữ liệu động thông qua API và quản lý các sự kiện DOM.
-   **Google Fonts:** Cung cấp phông chữ 'Nunito Sans' cho thiết kế giao diện.
-   **Maven:** Công cụ quản lý dự án và xây dựng tự động.
-   **MySQL:** Hệ quản trị cơ sở dữ liệu quan hệ được sử dụng để lưu trữ dữ liệu ứng dụng.
-   **Flyway:** Công cụ quản lý và tự động hóa các phiên bản cơ sở dữ liệu (database migrations).
-   **JJWT:** Thư viện Java chuyên dụng để tạo và xác thực JSON Web Tokens.
-   **Lombok:** Thư viện giúp giảm thiểu mã lặp lại (boilerplate code) bằng cách tự động tạo getters, setters, constructors, v.v.
-   **Tài nguyên tĩnh:** Custom CSS (`style.css`), hình ảnh được quản lý trong thư mục `src/main/resources/static/` để xây dựng giao diện người dùng.

## Cấu trúc thư mục

Dự án này tuân theo cấu trúc ứng dụng Spring Boot tiêu chuẩn, tích hợp cả phần backend và các thành phần frontend (Thymeleaf templates, static assets) trong một module duy nhất:

```
candyshop/
├── src/main/java/com/candyshop/
│   ├── CandyshopApplication.java    // Điểm khởi đầu chính của ứng dụng Spring Boot
│   ├── config/                     // Các lớp cấu hình ứng dụng, ví dụ: SecurityConfig cho bảo mật
│   ├── controller/                 // Các lớp xử lý yêu cầu HTTP (RESTful APIs và View Controllers)
│   │   ├── admin/                  // Các controller dành riêng cho giao diện quản trị
│   ├── dto/                        // Các đối tượng truyền dữ liệu (Data Transfer Objects)
│   ├── entity/                     // Các thực thể JPA ánh xạ tới các bảng cơ sở dữ liệu
│   ├── exception/                  // Các lớp xử lý ngoại lệ tùy chỉnh của ứng dụng
│   ├── init/                       // Các lớp khởi tạo dữ liệu ban đầu
│   ├── repository/                 // Các giao diện Spring Data JPA để thao tác với cơ sở dữ liệu
│   ├── security/                   // Các lớp liên quan đến bảo mật JWT và cấu hình Spring Security
│   └── service/                    // Các lớp chứa logic nghiệp vụ cốt lõi của ứng dụng
├── src/main/resources/
│   ├── application.yml             // Tệp cấu hình chính của ứng dụng (cổng, database, v.v.)
│   ├── db/migration/               // Các script SQL của Flyway để quản lý phiên bản cơ sở dữ liệu
│   ├── static/                     // Chứa các tài nguyên tĩnh của frontend (CSS, hình ảnh)
│   │   ├── style.css               // Tệp CSS chính
│   │   └── images/                 // Thư mục chứa hình ảnh tĩnh
│   └── templates/                  // Chứa các tệp mẫu HTML của Thymeleaf (giao diện người dùng)
│       ├── about.html              // Mẫu HTML cho trang giới thiệu
│       ├── admin-customers.html    // Mẫu HTML cho quản lý khách hàng
│       ├── admin-orders.html       // Mẫu HTML cho quản lý đơn hàng
│       ├── admin-products.html     // Mẫu HTML cho quản lý sản phẩm
│       ├── admin-vouchers.html     // Mẫu HTML cho quản lý mã giảm giá
│       ├── cart.html               // Mẫu HTML cho trang giỏ hàng
│       ├── checkout.html           // Mẫu HTML cho trang thanh toán
│       ├── index.html              // Mẫu HTML cho trang chủ
│       ├── login.html              // Mẫu HTML cho trang đăng nhập
│       ├── orders.html             // Mẫu HTML cho trang đơn hàng của người dùng
│       ├── product-detail.html     // Mẫu HTML cho trang chi tiết sản phẩm
│       ├── profile.html            // Mẫu HTML cho trang hồ sơ người dùng
│       └── register.html           // Mẫu HTML cho trang đăng ký
├── pom.xml                         // Tệp cấu hình dự án Maven
└── README.md                       // Tệp tài liệu README của dự án
```

## Bắt đầu

Làm theo các hướng dẫn sau để thiết lập và chạy dự án "Tiệm Ngọt 108" trên máy cục bộ của bạn.

### Điều kiện tiên quyết

Trước khi bắt đầu, hãy đảm bảo bạn đã cài đặt các công cụ sau:

-   **Java Development Kit (JDK):** Phiên bản 21 hoặc mới hơn.
-   **Apache Maven:** Phiên bản 3.6.0 hoặc mới hơn.
-   **MySQL Server:** Phiên bản 8.0 hoặc mới hơn.
-   Một môi trường phát triển tích hợp (IDE) như IntelliJ IDEA, VS Code hoặc Eclipse với các plugin hỗ trợ Java và Spring Boot được khuyến nghị.

### Cài đặt và Xây dựng

1.  **Clone kho lưu trữ:**
    Nếu bạn chưa có mã nguồn, hãy clone dự án từ GitHub:
    ```bash
    git clone https://github.com/KTD1108/Web_candyshop.git
    cd candyshop
    ```

2.  **Xây dựng dự án với Maven:**
    Sử dụng Maven để tải các dependencies và đóng gói ứng dụng:
    ```bash
    mvn clean install
    ```
    Lệnh này sẽ biên dịch mã nguồn, chạy các bài kiểm tra và tạo ra một tệp JAR trong thư mục `target/`.

### Thiết lập cơ sở dữ liệu

1.  **Tạo cơ sở dữ liệu MySQL:**
    Tạo một cơ sở dữ liệu mới trong MySQL Server của bạn. Tên mặc định mà ứng dụng mong đợi là `candyshop`.
    ```sql
    CREATE DATABASE candyshop;
    ```

2.  **Cấu hình `application.yml`:**
    Mở tệp `src/main/resources/application.yml` và cập nhật thông tin kết nối cơ sở dữ liệu để phù hợp với cài đặt MySQL của bạn.
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/candyshop?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
        username: root
        password: your_mysql_password # Thay thế bằng mật khẩu MySQL của bạn
        driver-class-name: com.mysql.cj.jdbc.Driver
      jpa:
        hibernate:
          ddl-auto: update # Chỉ sử dụng trong môi trường phát triển. Trong sản xuất, hãy dùng 'none' hoặc 'validate' cùng Flyway.
        show-sql: true
        properties:
          hibernate:
            format_sql: true
      flyway:
        enabled: true
        locations: classpath:db/migration
    ```
    **Quan trọng:** Thay thế `your_mysql_password` bằng mật khẩu người dùng MySQL của bạn.

3.  **Flyway Migrations**:
    Các script di chuyển cơ sở dữ liệu Flyway (`src/main/resources/db/migration/*.sql`) sẽ tự động được thực thi khi ứng dụng khởi động lần đầu, tạo ra các bảng và dữ liệu ban đầu. Nếu bạn cần chạy thủ công (ví dụ để kiểm tra hoặc reset):
    ```bash
    mvn flyway:migrate
    ```

### Chạy ứng dụng

Có một số cách để khởi động ứng dụng "Tiệm Ngọt 108":

1.  **Từ IDE của bạn:**
    Mở dự án trong IDE đã cấu hình. Điều hướng đến tệp `src/main/java/com/candyshop/CandyshopApplication.java` và chạy phương thức `main`.

2.  **Sử dụng Maven:**
    Từ thư mục gốc của dự án, chạy lệnh Maven sau:
    ```bash
    mvn spring-boot:run
    ```

3.  **Từ tệp JAR đã đóng gói:**
    Sau khi bạn đã chạy `mvn clean install`, một tệp JAR thực thi sẽ được tạo trong thư mục `target/` (ví dụ: `candyshop-1.0.0.jar`). Bạn có thể chạy nó bằng lệnh:
    ```bash
    java -jar target/candyshop-1.0.0.jar
    ```

Sau khi ứng dụng khởi động thành công, bạn có thể truy cập vào giao diện web tại: `http://localhost:8080` .

