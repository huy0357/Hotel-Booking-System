# 🏨 HBS - Hotel Booking System

Hệ thống backend đặt phòng khách sạn mô phỏng thực tế (giống Booking/Agoda), xây dựng bằng Spring Boot.

---

## 🚀 Features

- 🔐 Authentication & Authorization (JWT)
  - Role: `ADMIN`, `HOTEL_OWNER`, `CUSTOMER`

- 🏨 Hotel & Room Management
  - CRUD khách sạn, phòng, loại phòng

- 📅 Booking System
  - Đặt phòng theo thời gian thực
  - Kiểm tra trùng lịch phòng

- 💳 Payment System
- ⭐ Review & Rating

- ⚡ Redis Cache (tối ưu hiệu năng)
- 📧 Email xác nhận booking
- 🖼️ Upload ảnh khách sạn / phòng
- 🐳 Docker support

---

## 🗄️ Database Design

### Tables
- User
- Role
- Hotel
- Room
- RoomType
- Booking
- Payment
- Review
