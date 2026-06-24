# HBS-Hotel-Booking-System
🏨 Hotel Booking System (Spring Boot)

Hệ thống backend đặt phòng khách sạn mô phỏng thực tế (giống Booking/Agoda), xây dựng bằng Spring Boot.

🚀 Features
🔐 Authentication & Authorization (JWT)
Role: ADMIN, HOTEL_OWNER, CUSTOMER
🏨 Hotel & Room Management
CRUD khách sạn, phòng, loại phòng
📅 Booking System
Đặt phòng theo thời gian thực
Kiểm tra trùng lịch phòng
💳 Payment Handling
⭐ Review & Rating
⚡ Redis Cache (tối ưu hiệu năng)
📧 Email xác nhận booking
🖼️ Upload ảnh khách sạn / phòng
🐳 Docker support
🗄️ Database Design
User
Role
Hotel
Room
RoomType
Booking
Payment
Review
Relationships
1 Hotel → N Rooms
1 Room → N Bookings
1 User → N Bookings
📅 Business Rule

Không cho đặt phòng nếu trùng lịch:

(start1 < end2) && (start2 < end1)
🧱 Tech Stack
Java 17 + Spring Boot
Spring Security + JWT
Spring Data JPA
Redis
MySQL / PostgreSQL
Docker
🛠️ Modules
Auth Service
Hotel Service
Booking Service
Payment Service
Email Service
📌 Goal
Hiểu kiến trúc backend thực tế
Thiết kế database chuẩn production
Xử lý business logic doanh nghiệp
Làm quen Redis, Security, Docker
