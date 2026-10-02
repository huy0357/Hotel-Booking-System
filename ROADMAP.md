# 🏨 HOTEL BOOKING SYSTEM — LỘ TRÌNH CHINH PHỤC MID-LEVEL BACKEND ENGINEER
> **Phương châm:** Tự tay code chay 100% Backend • Học sâu bản chất • Không đốt cháy giai đoạn • Sẵn sàng phỏng vấn công ty Product / Global.

---

## 📌 TỔNG QUAN HỆ THỐNG CÔNG NGHỆ (TECH MATRIX)

| Nhóm kỹ năng | Công nghệ & Công cụ thực chiến | Vị trí áp dụng trong dự án |
| :--- | :--- | :--- |
| **Ngôn ngữ & Nền tảng** | Java 17, JVM Internals, Multithreading | Toàn bộ core logic, xử lý đồng thời khi đặt phòng |
| **Cơ sở dữ liệu** | MySQL 8.0, Flyway Migration, HikariCP | Thiết kế schema, quan hệ, migration theo version |
| **Tương tác dữ liệu** | JDBC, JdbcTemplate, Hibernate, Spring Data JPA | Từ truy vấn raw JDBC, tối ưu N+1, Specification đến Projection |
| **Framework nền tảng** | Spring Core, DI, IoC, Spring Boot 3.x | Quản lý vòng đời bean, auto-config, profiles |
| **Bảo mật & Auth** | Spring Security 6, JWT, BCrypt, OAuth2 Google | Đăng ký, đăng nhập, phân quyền RBAC, mạng xã hội |
| **Lưu trữ & Dịch vụ ngoài** | MinIO (chuẩn AWS S3), JavaMailSender, Thymeleaf | Upload ảnh phòng/avatar, gửi mail xác nhận bất đồng bộ |
| **Cache & Khóa phân tán** | Redis, Redisson, Spring Data Redis | Cache danh mục, TTL giữ phòng 10 phút, Distributed Lock |
| **Kiến trúc hướng sự kiện** | Apache Kafka, Transactional Outbox Pattern | Xử lý sự kiện sau đặt phòng, tách rời Email & Audit log |
| **Kiến trúc & Design Pattern** | Strategy, Factory, Builder, Observer, AOP | Tính giá phòng, áp mã voucher, ghi audit log |
| **Kiểm thử chất lượng** | JUnit 5, Mockito, Testcontainers, k6 | Unit test, Integration test DB thực tế, Stress test đa luồng |
| **Đo lường & Vận hành** | Actuator, Prometheus, Grafana, Logback, MDC | Tracing request (`traceId`), giám sát metric server |
| **DevOps & Triển khai** | Docker, Docker Compose, GitHub Actions CI/CD | Đóng gói container chuẩn production, pipeline tự động |

---

## 🗺️ CHI TIẾT LỘ TRÌNH TỪNG GIAI ĐOẠN & CHỨC NĂNG

---

### GIAI ĐOẠN 1: NỀN TẢNG DỮ LIỆU, XỬ LÝ LỖI & CORE ARCHITECTURE
> **Mục tiêu:** Xây dựng móng vững chắc cho hệ thống, thiết lập chuẩn format API và kiểm soát phiên bản cơ sở dữ liệu.

#### 1. Quản lý cấu trúc Database với Flyway
* **Nghiệp vụ:** Tạo các bảng cơ bản: `roles`, `users`, `user_roles`, `refresh_tokens`.
* **Tech thực chiến:**
  - **SQL & Database:** DDL chuẩn (Data Definition Language), ràng buộc khoá chính (PK), khoá ngoại (FK), Index trên `email`.
  - **Flyway Migration:** Script `V1__init_auth_schema.sql`. Quản lý lịch sử thay đổi schema bằng code, nói KHÔNG với `ddl-auto=update`.
  - **JDBC Connection:** Cấu hình HikariCP kết nối MySQL qua `application.properties`.

#### 2. Exception Handling & Mã lỗi tập trung
* **Nghiệp vụ:** Trả về thông báo lỗi nhất quán khi vi phạm logic (trùng email, mật khẩu sai, không tìm thấy tài nguyên).
* **Tech thực chiến:**
  - **Validation & Exception Handling:** `@RestControllerAdvice`, `@ExceptionHandler`.
  - **BusinessException:** Unchecked Exception kế thừa `RuntimeException`, mang theo `ResponseCode` và đối số động (`args`).
  - **I18n (Internationalization):** `MessageSource` dịch mã lỗi thành thông điệp đa ngôn ngữ dựa trên `LocaleContextHolder`.
  - **DTO Pattern:** Đóng gói toàn bộ response hệ thống vào `ResponseDto<T>` thống nhất format (`success`, `statusCode`, `message`, `data`, `metaData`).

---

### GIAI ĐOẠN 2: XÁC THỰC, PHÂN QUYỀN & BẢO MẬT (AUTH & SECURITY)
> **Mục tiêu:** Nắm chắc luồng Spring Security Filter Chain, cơ chế Token State-less và phân quyền Role-Based Access Control (RBAC).

#### 1. Đăng ký & Đăng nhập (Local Authentication)
* **Nghiệp vụ:**
  - Đăng ký tài khoản (GUEST): Validate định dạng email, mật khẩu mạnh, họ tên, số điện thoại.
  - Đăng nhập: Trả về cặp `AccessToken` (15 phút) và `RefreshToken` (7 ngày).
* **Tech thực chiến:**
  - **Bean Validation:** `@Valid`, `@NotBlank`, `@Email`, `@Size`, `@Pattern` (Regex số điện thoại).
  - **Spring Security 6:** `SecurityFilterChain`, `AuthenticationManager`, `DaoAuthenticationProvider`, `BCryptPasswordEncoder`.
  - **Java Core:** Enum định nghĩa các Role (`ROLE_GUEST`, `ROLE_STAFF`, `ROLE_ADMIN`).
  - **DTO & MapStruct:** `RegisterRequest`, `LoginRequest`, `AuthResponse`, MapStruct mapper chuyển đổi Entity $\leftrightarrow$ DTO.

#### 2. JWT & Refresh Token Rotation
* **Nghiệp vụ:** Xác thực request gửi lên, cấp lại token mới khi token cũ hết hạn, bảo vệ chống đánh cắp token.
* **Tech thực chiến:**
  - **JJWT (Java JWT):** Tạo Claims, ký HMAC-SHA256, parse và validate expiration.
  - **Security Filter:** `OncePerRequestFilter` (chặn kiểm tra header `Authorization: Bearer <token>`), nạp `UsernamePasswordAuthenticationToken` vào `SecurityContextHolder`.
  - **Advanced Security:** Refresh Token Rotation (mỗi lần refresh sẽ hủy token cũ và cấp token mới, phát hiện hành vi dùng lại token bị lộ để khóa tài khoản ngay lập tức).

#### 3. Phân quyền API (Authorization) & Social Login
* **Nghiệp vụ:**
  - Khách chỉ xem phòng và đặt phòng của chính mình.
  - Lễ tân (STAFF) quản lý check-in/out. Admin toàn quyền.
  - Đăng nhập nhanh bằng tài khoản Google.
* **Tech thực chiến:**
  - **Method Security:** Kích hoạt `@EnableMethodSecurity`, dùng `@PreAuthorize("hasRole('ADMIN')")` hoặc `@PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")`.
  - **OAuth2 Client:** `spring-boot-starter-oauth2-client`, cấu hình Google Client ID/Secret, trích xuất Google Profile và merge tài khoản vào DB.

---

### GIAI ĐOẠN 3: QUẢN LÝ KHÁCH SẠN, LOẠI PHÒNG & MEDIA (CATALOG & CRUD)
> **Mục tiêu:** Làm chủ quan hệ JPA phức tạp, dập tắt lỗi N+1 Query, tích hợp dịch vụ lưu trữ Object Storage chuẩn AWS S3.

#### 1. Thiết kế thực thể & Quan hệ phức hợp
* **Nghiệp vụ:** `Hotel` có nhiều `RoomType`. Mỗi `RoomType` có nhiều `Room`. `RoomType` liên kết N-N với `Amenity` (tiện nghi: Wifi, Bể bơi,...).
* **Tech thực chiến:**
  - **Hibernate & JPA Mappings:** `@OneToMany`, `@ManyToOne`, `@ManyToMany` kèm `@JoinTable`.
  - **Cascade & OrphanRemoval:** Kiểm soát xóa phòng theo loại phòng một cách chặt chẽ.
  - **Audit Trailing JPA:** Kế thừa `BaseEntity` với `@CreatedDate`, `@LastModifiedDate`, cấu hình `AuditorAware` để tự lưu ID người tạo/sửa.
  - **Soft Delete:** Triển khai cờ `deleted_at`, sử dụng `@SQLDelete` và `@SQLRestriction("deleted_at IS NULL")`.

#### 2. Tối ưu hóa truy vấn & Dập tắt N+1 Query
* **Nghiệp vụ:** Lấy danh sách khách sạn kèm loại phòng và tiện ích để hiển thị lên trang chủ.
* **Tech thực chiến:**
  - **Basic Query Optimization:** Soi log SQL, nhận diện lỗi N+1 Query (1 query cha kéo theo N query con).
  - **Advanced Spring Data JPA:** Dùng `@EntityGraph(attributePaths = {"roomTypes", "amenities"})` hoặc JPQL `JOIN FETCH`.
  - **Projection:** Dùng Class-based Projection hoặc Interface-based Projection để chỉ lấy đúng những cột cần thiết, giảm tải dung lượng RAM.
  - **Pagination:** Phân trang với `Pageable`, `PageRequest`, phân biệt chi phí giữa `Page<T>` (kèm `COUNT(*)`) và `Slice<T>`.

#### 3. Upload ảnh phòng & Khách sạn lên MinIO (S3 Compatible)
* **Nghiệp vụ:** Admin tải ảnh bìa, ảnh chi tiết cho từng loại phòng.
* **Tech thực chiến:**
  - **File Upload:** Nhận `MultipartFile`, kiểm tra dung lượng (< 5MB), validate MIME type (`image/jpeg`, `image/png`).
  - **MinIO / AWS S3 SDK:** Tương tác với MinIO container qua S3 Java Client (`software.amazon.awssdk:s3`).
  - **Advanced S3 Feature:** Tạo **Presigned URL** (cấp link tạm thời để Client tự upload trực tiếp lên S3/MinIO giúp giảm tải tải băng thông Backend).

---

### GIAI ĐOẠN 4: TÌM KIẾM PHÒNG TRỐNG NÂNG CAO (SEARCH ENGINE)
> **Mục tiêu:** Xử lý bài toán logic cốt lõi của ngành du lịch/khách sạn: Lọc phòng không bị trùng lịch theo dải ngày.

#### 1. Xây dựng bộ lọc động (Dynamic Query Filter)
* **Nghiệp vụ:** Khách tìm theo: Thành phố, Ngày đến (`checkIn`), Ngày đi (`checkOut`), Số khách, Khoảng giá từ-đến, Tiện nghi mong muốn.
* **Tech thực chiến:**
  - **Spring Data JPA Specification:** Xây dựng `RoomSpecification` sử dụng `CriteriaBuilder`, `Predicate` để ghép nối các điều kiện `AND`/`OR` động.
  - **Java Core Stream & java.time:** Xử lý `LocalDate`, `ChronoUnit.DAYS.between(checkIn, checkOut)` để tính số đêm.

#### 2. Thuật toán kiểm tra lịch trống & Tối ưu Database Index
* **Nghiệp vụ:** Phòng hợp lệ là phòng KHÔNG có bất kỳ đơn đặt phòng nào trùng lặp dải ngày (`is_active = true`).
* **Tech thực chiến:**
  - **SQL Logic Overlapping:**
    ```sql
    WHERE NOT (b.check_in_date >= :requestedCheckOut OR b.check_out_date <= :requestedCheckIn)
    ```
  - **Native SQL vs Specification:** Tự tay viết bằng 2 cách, so sánh hiệu năng thực tế.
  - **Database Indexing:** Dùng `EXPLAIN ANALYZE`, đánh Composite Index `(room_id, check_in_date, check_out_date)` để biến câu query từ Full Table Scan thành Index Range Scan.

---

### GIAI ĐOẠN 5: ĐẶT PHÒNG, GIAO DỊCH & CHỐNG OVERBOOKING (CONCURRENCY)
> **Mục tiêu:** Đây là **trái tim kỹ thuật** của dự án. Luyện sâu Transaction Isolation, Pessimistic / Optimistic Lock và Distributed Lock chống Race Condition.

#### 1. Tạo đơn đặt phòng & Snapshot giá
* **Nghiệp vụ:** Khách chọn 1 hoặc nhiều phòng, hệ thống chốt giá tại thời điểm đặt (`price_per_night`), sinh mã booking duy nhất, trạng thái `PENDING`.
* **Tech thực chiến:**
  - **Transaction Management:** `@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)`.
  - **Design Pattern (Builder):** Xây dựng đối tượng `Booking` phức tạp gồm nhiều `BookingRoom` và `PriceSnapshot`.
  - **Clean Code (Fail-Fast):** Kiểm tra phòng trống, ngày hợp lệ, nếu sai ném `BusinessException(ResponseCode.ROOM_NOT_AVAILABLE)` ngay lập tức.

#### 2. Kỹ thuật chống trùng phòng (Anti-Overbooking - Concurrency Control)
* **Nghiệp vụ:** 100 khách hàng cùng bấm đặt phòng cuối cùng của khách sạn vào cùng 1 tích tắc.
* **Tech thực chiến:**
  - **Pessimistic Locking (Khóa bi quan):** 
    - Viết query trong Repository: `@Lock(LockModeType.PESSIMISTIC_WRITE)` $\rightarrow$ Sinh lệnh SQL `SELECT ... FOR UPDATE` chặn các luồng khác chờ đến khi commit transaction.
  - **Optimistic Locking (Khóa lạc quan):**
    - Thêm cột `@Version private Long version;` vào Entity Room. Bắt ngoại lệ `OptimisticLockingFailureException` để thông báo phòng đã được người khác đặt trước.
  - **Advanced Redis (Redisson Distributed Lock):**
    - Áp dụng `RLock lock = redissonClient.getLock("room-lock:" + roomId);` giải quyết triệt để bài toán khi hệ thống scale nhiều server instance.
  - **Multithreading Testing:** Viết test case dùng `ExecutorService`, `CountDownLatch(100)` để mô phỏng 100 thread bắn request song song kiểm chứng không bao giờ bị overbooking.

#### 3. Giữ chỗ 10 phút & Tự động hủy (Hold Room Timeout)
* **Nghiệp vụ:** Đơn ở trạng thái `PENDING` chỉ được giữ chỗ trong 10 phút. Nếu quá hạn không thanh toán sẽ chuyển sang `EXPIRED` và tự nhả phòng.
* **Tech thực chiến:**
  - **Spring Scheduler:** `@EnableScheduling`, viết method `@Scheduled(fixedRate = 60000)` quét các booking có `hold_expires_at < NOW()`.
  - **Redis TTL (Key Expiration):** Đẩy mã booking vào Redis với TTL = 600 giây, sử dụng Redis Keyspace Notifications để bắt sự kiện hết hạn và giải phóng phòng theo thời gian thực.

---

### GIAI ĐOẠN 6: THANH TOÁN, KHUYẾN MÃI & DESIGN PATTERNS
> **Mục tiêu:** Áp dụng các Design Pattern kinh điển (Strategy, Factory) để code linh hoạt, bảo trì tốt theo chuẩn SOLID.

#### 1. Module Tính giá & Khuyến mãi (Promotion Engine)
* **Nghiệp vụ:** Tính tổng tiền phòng dựa theo: Giá gốc theo mùa, Voucher giảm theo %, Voucher giảm cố định, Giảm cho khách VIP.
* **Tech thực chiến:**
  - **Strategy Pattern:** Interface `DiscountStrategy` với các class triển khai: `PercentageDiscountStrategy`, `FixedAmountDiscountStrategy`, `SeasonalDiscountStrategy`.
  - **Factory Pattern:** `DiscountStrategyFactory` tự động chọn Strategy phù hợp dựa trên loại Voucher người dùng nhập.
  - **Java Core:** Dùng `BigDecimal` cho toàn bộ tính toán tiền tệ, tuyệt đối không dùng `double/float` để tránh sai số dấu phẩy động.

#### 2. Xử lý Thanh toán & Idempotency Key
* **Nghiệp vụ:** Tạo giao dịch thanh toán (Mô phỏng VNPay/Momo hoặc Tiền mặt tại quầy).
* **Tech thực chiến:**
  - **Idempotency Key:** Sử dụng header `Idempotency-Key` lưu trong Redis/DB để nếu khách bấm 2 lần hoặc cổng thanh toán gọi webhook 2 lần, hệ thống nhận diện và không trừ tiền lặp lại.
  - **State Machine:** Quản lý luồng chuyển đổi trạng thái nghiêm ngặt: `PENDING` $\rightarrow$ `PAID` $\rightarrow$ `CHECKED_IN` $\rightarrow$ `CHECKED_OUT` (hoặc `CANCELLED`).

---

### GIAI ĐOẠN 7: BẤT ĐỒNG BỘ, KAFKA & TRANSACTIONAL OUTBOX PATTERN
> **Mục tiêu:** Xây dựng kiến trúc hướng sự kiện (Event-Driven), dập tắt tình trạng nghẽn server và giải quyết bài toán Dual-Write kinh điển.

#### 1. Gửi Email thông báo bất đồng bộ
* **Nghiệp vụ:** Sau khi đặt phòng hoặc thanh toán thành công, gửi email HTML chứa mã đặt phòng, chi tiết phòng và hướng dẫn nhận phòng.
* **Tech thực chiến:**
  - **JavaMailSender & Thymeleaf:** Render template HTML có gắn logo và bảng chi tiết giá.
  - **`@Async` & Thread Pool:** Cấu hình `ThreadPoolTaskExecutor` chuyên dụng (CorePoolSize, MaxPoolSize, QueueCapacity) để việc gửi email chạy ngầm, không làm tăng thời gian phản hồi (Response Time) của khách.

#### 2. Apache Kafka & Transactional Outbox Pattern
* **Nghiệp vụ:** Sự kiện đặt phòng thành công cần phát đi cho các hệ thống: Gửi email, Ghi nhận điểm thưởng, Cập nhật thống kê doanh thu.
* **Tech thực chiến:**
  - **Bài toán Dual-Write:** Không thể vừa commit DB vừa gọi Kafka trực tiếp (nếu Kafka sập thì DB đã commit dẫn đến lệch dữ liệu).
  - **Transactional Outbox Pattern:** Ghi event vào bảng `outbox_events` trong cùng một `@Transactional` của nghiệp vụ Booking.
  - **Kafka Producer Worker:** Một tiến trình nền đọc các event chưa gửi trong bảng `outbox_events` và publish lên Kafka Topic `booking-events`.
  - **Kafka Consumer:** Các consumer lắng nghe topic độc lập (`EmailConsumer`, `AnalyticsConsumer`), cấu hình **Consumer Group**, xử lý **Idempotent Consumer** và cấu hình **Dead Letter Queue (DLQ)** phòng trường hợp retry quá số lần.

---

### GIAI ĐOẠN 8: CACHING NÂNG CAO, AOP AUDIT & RATE LIMITING
> **Mục tiêu:** Tăng tốc độ đọc dữ liệu lên gấp 10 lần với Redis Caching, bảo vệ server chống tấn công và ghi vết toàn diện bằng Spring AOP.

#### 1. Tối ưu hiệu năng Caching với Redis
* **Nghiệp vụ:** Danh sách khách sạn hot, danh mục loại phòng, tiện nghi được người dùng truy cập liên tục.
* **Tech thực chiến:**
  - **Spring Cache:** `@Cacheable(value = "hotels", key = "#id")`, `@CacheEvict(allEntries = true)` khi Admin cập nhật dữ liệu.
  - **Cache-Aside Pattern:** Đọc từ Cache trước, miss mới vào DB rồi nạp lại vào Cache.
  - **Chống lỗi Caching kinh điển:** Tránh **Cache Avalanche** (cho TTL ngẫu nhiên), tránh **Cache Penetration** (cache cả kết quả rỗng `null`).

#### 2. Audit Trail & Đo lường thời gian thực thi bằng AOP
* **Nghiệp vụ:** Tự động ghi lại lịch sử: Ai đã sửa giá phòng? Ai huỷ đơn? Hàm nào chạy quá 500ms?
* **Tech thực chiến:**
  - **Spring AOP:** Viết `@Aspect`, `@Around`, `@Pointcut` nhắm vào toàn bộ tầng Service.
  - **MDC (Mapped Diagnostic Context):** Tự động sinh `traceId` (UUID) gán vào từng request từ Filter để khi đọc log qua file/Kibana, dễ dàng trace toàn bộ vết request từ Controller đến DB.

#### 3. Rate Limiting (Chống Spam / Brute-Force)
* **Nghiệp vụ:** Giới hạn mỗi IP chỉ được gọi API tìm kiếm tối đa 60 lần/phút; chỉ được thử đăng nhập sai 5 lần/phút.
* **Tech thực chiến:**
  - **Redis Lua Script:** Thực thi thuật toán **Token Bucket** hoặc **Sliding Window Log** trong Redis để đảm bảo tính nguyên tử (Atomic).

---

### GIAI ĐOẠN 9: BÁO CÁO THỐNG KÊ, SQL NÂNG CAO & BATCH PROCESSING
> **Mục tiêu:** Luyện câu lệnh SQL phân tích phức tạp phục vụ Dashboard Admin.

#### 1. Dashboard Báo cáo Doanh thu & Tỷ lệ lấp đầy
* **Nghiệp vụ:**
  - Doanh thu theo tháng / quý / năm.
  - Tỷ lệ lấp đầy phòng (Occupancy Rate) = `(Số đêm đã đặt / Tổng số đêm có thể phục vụ) * 100`.
  - Top 5 khách sạn có doanh thu cao nhất.
* **Tech thực chiến:**
  - **Advanced SQL:** `GROUP BY`, `HAVING`, `SUM`, `COUNT`, `DATE_FORMAT`, Window Functions (`ROW_NUMBER()`, `DENSE_RANK()`).
  - **Native Query & DTO Interface Projection:** Hứng kết quả thống kê phức tạp vào DTO Interface không liên quan đến Entity.
  - **Spring JDBC (`JdbcTemplate`):** Sử dụng `JdbcTemplate` khi cần tối ưu hiệu năng tối đa cho các câu truy vấn báo cáo nặng hàng triệu dòng dữ liệu.

---

### GIAI ĐOẠN 10: KIỂM THỬ TOÀN DIỆN (TESTING STRATEGY)
> **Mục tiêu:** Tự tin với chất lượng code thông qua bộ kiểm thử tự động, không còn nỗi sợ "sửa chỗ này hỏng chỗ khác".

#### 1. Unit Testing & Mocking
* **Tech thực chiến:**
  - **JUnit 5:** `@Test`, `@ParameterizedTest`, `@DisplayName`, AssertJ assertions.
  - **Mockito:** `@Mock`, `@InjectMocks`, `when().thenReturn()`, `verify()`, `assertThrows(BusinessException.class, ...)`.
  - Kiểm thử 100% các logic nghiệp vụ trong `UserService`, `BookingService`, `PromotionService`.

#### 2. Integration Testing với Testcontainers
* **Tech thực chiến:**
  - **Testcontainers:** Khởi chạy container MySQL và Redis thật sự trong Docker khi chạy test (thay vì dùng H2 Database in-memory vốn có cú pháp SQL khác biệt so với MySQL).
  - **`@SpringBootTest` & `@AutoConfigureMockMvc`:** Giả lập toàn bộ luồng gọi API từ HTTP Request đến khi lưu dữ liệu vào DB thật.

#### 3. Load Testing (Kiểm thử tải)
* **Tech thực chiến:**
  - Sử dụng công cụ **k6** hoặc **Apache JMeter**.
  - Viết kịch bản bắn 500 Virtual Users (VU) đặt phòng đồng thời để kiểm tra thời gian đáp ứng (p95, p99 response time) và độ chịu tải của Connection Pool HikariCP.

---

### GIAI ĐOẠN 11: ĐO LƯỜNG, DOCKER & CI/CD PIPELINE
> **Mục tiêu:** Đóng gói ứng dụng chuẩn công nghiệp, triển khai tự động lên môi trường Cloud.

#### 1. API Documentation & Monitoring
* **Tech thực chiến:**
  - **Springdoc OpenAPI (Swagger UI):** Cấu hình tự sinh trang `/swagger-ui.html` với đầy đủ mô tả Request/Response, Header Authorization Bearer.
  - **Spring Boot Actuator:** Mở các endpoint `/actuator/health`, `/actuator/metrics`.
  - **Prometheus & Grafana:** Thu thập số liệu CPU, RAM, JVM Heap, HikariCP Active Connections và hiển thị lên Dashboard Grafana trực quan.

#### 2. Containerization với Docker đa tầng (Multi-Stage Build)
* **Tech thực chiến:**
  - Viết `Dockerfile` Multi-Stage: Stage 1 build jar với Maven, Stage 2 chạy với Eclipse Temurin JRE Alpine siêu nhẹ (< 150MB), tạo non-root user để tăng tính bảo mật.
  - Viết `docker-compose.yml` tích hợp 6 dịch vụ đồng bộ:
    1. `hotel-booking-app` (Spring Boot)
    2. `mysql-db` (MySQL 8.0)
    3. `redis-cache` (Redis 7.x)
    4. `minio-storage` (MinIO Object Storage)
    5. `kafka-broker` (Apache Kafka)
    6. `prometheus-grafana` (Giám sát hệ thống)

#### 3. Tự động hóa CI/CD với GitHub Actions
* **Tech thực chiến:**
  - Thiết lập `.github/workflows/ci-cd.yml`:
    - Mỗi khi push code hoặc mở Pull Request: Tự động chạy `mvn test` với Testcontainers.
    - Kiểm tra chất lượng code (SonarCloud/Linter).
    - Tự động đóng gói Docker Image và push lên **Docker Hub** hoặc **GitHub Container Registry (GHCR)**.

---

## 🎯 BẢNG THEO DÕI TIẾN ĐỘ THỰC HIỆN (CHECKLIST)

- [x] **Bước 0:** Thiết lập dự án, chuẩn hóa `pom.xml` (Java 17, Spring Boot).
- [x] **Bước 1:** Dựng bộ khung `BusinessException`, `ResponseCode` & `GlobalExceptionHandler`.
- [ ] **Bước 2:** Viết Flyway Migration `V1__init_auth_schema.sql` (bảng `users`, `roles`, `user_roles`).
- [ ] **Bước 3:** Entity `User` & `Role` (Mô hình hóa quan hệ Many-to-Many).
- [ ] **Bước 4:** Xây dựng Module Auth (Đăng ký tài khoản, mã hóa mật khẩu, kiểm tra trùng email).
- [ ] **Bước 5:** Xây dựng Spring Security Filter Chain & Đăng nhập cấp JWT (Access + Refresh Token).
- [ ] **Bước 6:** Tích hợp Flyway tạo danh mục Khách sạn, Loại phòng, Phòng & Tiện nghi (`V2__catalog.sql`).
- [ ] **Bước 7:** CRUD Khách sạn, Phòng, Phân trang & Xử lý N+1 Query (`@EntityGraph`).
- [ ] **Bước 8:** Dựng MinIO Docker & Module Upload ảnh phòng (Presigned URL).
- [ ] **Bước 9:** Tìm kiếm & Lọc phòng trống theo dải ngày (Specification & Indexing).
- [ ] **Bước 10:** Core Đặt phòng & Chống Overbooking (Pessimistic Lock & Redis Redisson Lock).
- [ ] **Bước 11:** Module Tính giá & Khuyến mãi (Strategy Pattern & Factory Pattern).
- [ ] **Bước 12:** Thanh toán & Idempotency Key.
- [ ] **Bước 13:** Bất đồng bộ: Gửi Email xác nhận đặt phòng (`@Async` & Thymeleaf).
- [ ] **Bước 14:** Transactional Outbox Pattern & Apache Kafka.
- [ ] **Bước 15:** Caching danh mục với Redis & AOP Audit Log (`traceId`).
- [ ] **Bước 16:** Báo cáo doanh thu & SQL nâng cao (`JdbcTemplate` / Native Query).
- [ ] **Bước 17:** Viết Unit Test & Integration Test với Testcontainers.
- [ ] **Bước 18:** Swagger UI, Actuator, Prometheus & Grafana.
- [ ] **Bước 19:** Đóng gói Docker Multi-stage & `docker-compose.yml` hoàn chỉnh.
- [ ] **Bước 20:** Thiết lập Pipeline CI/CD với GitHub Actions.
