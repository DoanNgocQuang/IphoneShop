# IphoneShop - Backend API

A comprehensive Spring Boot REST API for an iPhone e-commerce platform with admin dashboard, inventory management, and user authentication.

## 🎯 Features

- **User Management**: Registration, login, authentication, and profile management
- **Product Management**: iPhone catalog with variants, images, and categories
- **Shopping Cart**: Add/remove items, view cart, checkout functionality
- **Order Management**: Order creation, tracking, and history
- **Admin Dashboard**: Sales analytics, user management, product management
- **Security**: JWT authentication, CORS configuration, role-based access control
- **File Upload**: Support for product images and files
- **API Documentation**: Integrated Swagger/OpenAPI documentation
- **Database Seeding**: Automatic initial data population

## 🛠️ Tech Stack

- **Framework**: Spring Boot 4.x.x
- **Language**: Java 25
- **Build Tool**: Maven
- **Database**: MySQL/MariaDB
- **Authentication**: JWT (JSON Web Tokens)
- **Documentation**: Swagger/OpenAPI 3.0
- **ORM**: Spring Data JPA / Hibernate

## 📋 Prerequisites

- Java JDK 17 or higher
- Maven 3.8+
- MySQL 8.0+ or compatible database
- Git (optional)

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd backend
```

### 2. Configure Database

Update `src/main/resources/application.yaml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/iphoneshop
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: create-drop
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

or the OpenAPI JSON:

```
http://localhost:8080/v3/api-docs
```

## 📁 Project Structure

```
src/main/java/MyIphoneShop/demo/
├── config/              # Configuration classes
│   ├── CorsConfig.java          # CORS configuration
│   ├── DatabaseSeeder.java      # Initial data loading
│   ├── SecurityConfig.java      # Spring Security setup
│   ├── SwaggerConfig.java       # Swagger/OpenAPI config
│   └── WebConfig.java
├── controller/          # REST API endpoints
│   ├── AdminDashboardController.java
│   ├── AdminUserController.java
│   ├── AuthController.java
│   ├── CartController.java
│   ├── CategoryController.java
│   ├── FileUploadController.java
│   ├── IphoneController.java
│   ├── OrderController.java
│   └── UserProfileController.java
├── dto/                 # Data Transfer Objects
│   ├── AuthResponse.java
│   ├── CartResponse.java
│   ├── IphoneResponse.java
│   ├── OrderResponse.java
│   └── ... (other DTOs)
├── entity/              # JPA Entity classes
│   ├── Address.java
│   ├── Cart.java
│   ├── Category.java
│   ├── Iphone.java
│   ├── IphoneVariant.java
│   ├── Order.java
│   └── ... (other entities)
├── repository/          # Spring Data JPA repositories
├── security/            # JWT and security utilities
├── service/             # Business logic services
└── MyIphoneShopApplication.java  # Main Spring Boot class
```

## 🔐 Authentication

The API uses JWT (JSON Web Tokens) for authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## 📝 Environment Variables

Optional environment variables for deployment:

```
DATABASE_URL=jdbc:mysql://localhost:3306/iphoneshop
DATABASE_USERNAME=root
DATABASE_PASSWORD=password
JWT_SECRET=your_secret_key
JWT_EXPIRATION=86400000
```

## 🐛 Troubleshooting

### Connection refused to database

- Ensure MySQL is running
- Check database URL and credentials in `application.yaml`
- Verify the database exists or let Hibernate create it

### JWT token expired

- Tokens expire after the configured duration (default: 24 hours)
- Login again to get a new token

### CORS errors

- Check `CorsConfig.java` for allowed origins
- Ensure frontend URL is whitelisted

## 📦 Building for Production

```bash
./mvnw clean package
# or on Windows:
mvnw.cmd clean package
```

This creates a JAR file in the `target/` directory that can be deployed.

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## 📄 License

This project is provided as-is for educational purposes.

## 📞 Contact & Support

For issues or questions, please open an issue on the repository.

---

**Happy coding!** 🚀
