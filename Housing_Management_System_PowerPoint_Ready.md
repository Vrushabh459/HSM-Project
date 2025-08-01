---
title: Housing Management System
subtitle: A Comprehensive Spring Boot Application
author: Infoway Technologies Pvt. Ltd, Pune
date: January 2024
theme: default
---

# Housing Management System

## A Comprehensive Spring Boot Application

**Infoway Technologies Pvt. Ltd, Pune**

**Technology Stack:**
- Spring Boot 3.5.0
- Spring Security & JWT Authentication  
- Spring Data JPA with MySQL
- WebSocket for Real-time Communication
- MapStruct for Object Mapping

---

# Project Overview

## Housing Management System - Executive Summary

**Key Features:**
- Multi-tenant society management platform
- Role-based access control (Admin, Resident, Guard)
- Real-time notifications and communication
- Comprehensive maintenance billing system
- Visitor management and tracking
- Complaint management workflow

**Business Value:**
- Streamlined society operations
- Enhanced security and visitor tracking
- Automated billing and payment tracking
- Transparent complaint resolution
- Real-time communication platform

---

# Problem Definition & Scope

**Problem Statement:**
- Manual society management processes
- Lack of centralized communication
- Inefficient visitor tracking
- Complex maintenance billing
- Poor complaint resolution tracking

**Goals & Objectives:**
- Digitize society management operations
- Implement role-based access control
- Enable real-time communication
- Automate billing and payment tracking
- Provide comprehensive reporting dashboard

**Major Constraints & Outcomes:**
- Multi-tenant architecture support
- Scalable and secure solution
- User-friendly interface design
- Compliance with data protection regulations

---

# System Architecture Overview

## High-Level Architecture

**Frontend ↔ Backend ↔ Database**

**Technology Components:**
- Spring Boot 3.5.0 (Backend Framework)
- Spring Security + JWT (Authentication)
- Spring Data JPA (Data Access Layer)
- MySQL 8.0 (Database)
- WebSocket (Real-time Communication)
- MapStruct (Object Mapping)

---

# Database Architecture

## Core Entities

**Core Entities:**
- **Society** - Housing society master data
- **Building** - Building information within society
- **Flat** - Individual flat details
- **User** - System users (Admin, Resident, Guard)
- **FlatMember** - Residents and family members
- **FlatAllocation** - Flat ownership/rental assignments

**Key Relationships:**
- Society → Buildings (One-to-Many)
- Building → Flats (One-to-Many)
- Flat → FlatMembers (One-to-Many)
- User → FlatAllocations (One-to-Many)
- Flat → Complaints (One-to-Many)
- Flat → MaintenanceBills (One-to-Many)

---

# Database Schema - User Management

## User & Authentication Schema

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role ENUM('ADMIN', 'RESIDENT', 'GUARD') NOT NULL,
    society_id BIGINT,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

# Database Schema - Property Management

## Society & Property Schema

```sql
CREATE TABLE societies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    number_of_buildings INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE flats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flat_number VARCHAR(50) NOT NULL,
    floor_number INT NOT NULL,
    flat_type ENUM('ONE_BHK', 'TWO_BHK', 'THREE_BHK', 'FOUR_BHK', 'PENTHOUSE'),
    area DOUBLE NOT NULL,
    building_id BIGINT NOT NULL
);
```

---

# Authentication & Security Architecture

## JWT-Based Authentication System

**Security Components:**
- **JwtUtil** - Token generation and validation
- **JwtAuthFilter** - Request filtering and token extraction
- **CustomUserDetailsService** - User authentication service
- **SecurityConfig** - Security configuration and URL protection

**Authentication Flow:**
1. User Login Request → AuthController
2. Username/Password Validation → AuthService
3. JWT Token Generation → JwtUtil
4. Token Response → Client
5. Subsequent Requests → JwtAuthFilter
6. Token Validation → Access Granted/Denied

---

# User Roles & Permissions

## Role-Based Access Control Matrix

| Feature | ADMIN | RESIDENT | GUARD |
|---------|-------|----------|-------|
| Society Management | ✓ (Own) | ✗ | ✗ |
| Building Management | ✓ | ✗ | ✗ |
| Flat Management | ✓ | ✓ (Own) | ✗ |
| User Management | ✓ | ✗ | ✗ |
| Complaint Management | ✓ (All) | ✓ (Own) | ✓ (View) |
| Maintenance Bills | ✓ (Create/View) | ✓ (View/Pay) | ✗ |
| Visitor Management | ✓ (View All) | ✓ (Own Flat) | ✓ (Log/Approve) |
| Notice Management | ✓ (Create/Edit) | ✓ (View) | ✓ (View) |
| Reports & Analytics | ✓ | ✗ | ✗ |

---

# Society Management Module

## Society & Building Administration

**Core Functionalities:**
- Society registration and profile management
- Building creation and floor management
- Flat allocation and type configuration
- Administrative user assignment

**Key APIs:**
```java
POST /societies - Create new society
GET /societies/{id} - Get society details
PUT /societies/{id} - Update society information
DELETE /societies/{id} - Delete society

POST /buildings - Create new building
GET /buildings/society/{societyId} - Get buildings by society
```

**Business Rules:**
- Only ADMIN users can create societies
- Building deletion requires empty flats
- Society administrators limited to own society data

---

# Flat Management System

## Flat Allocation & Member Management

**Flat Management Features:**
- Flat creation with type and area specification
- Owner and tenant allocation tracking
- Family member registration
- Flat status and availability management

**FlatMember Registration Process:**
1. Member Registration Request → FlatMemberController
2. Flat Validation → FlatMemberService
3. User Association (if registered) → UserService
4. Approval Workflow → Admin Notification
5. Status Update → Real-time Notification

**Key Components:**
- **Flat Types**: 1BHK, 2BHK, 3BHK, 4BHK, Penthouse
- **Member Types**: Owner, Family Member, Tenant
- **Approval Workflow**: Admin approval required

---

# Complaint Management Workflow

## End-to-End Complaint Resolution

**Complaint Categories:**
- MAINTENANCE - Plumbing, electrical, structural issues
- SECURITY - Safety concerns, unauthorized access
- NOISE - Noise pollution complaints
- PARKING - Parking space disputes
- CLEANLINESS - Hygiene and sanitation issues
- OTHER - Miscellaneous complaints

**Complaint Lifecycle:**
```
PENDING → IN_PROGRESS → RESOLVED/REJECTED
```

**Workflow Process:**
1. Complaint Creation (Resident) → ComplaintController
2. Admin Notification → WebSocket notification
3. Status Updates → Real-time tracking
4. Resolution Documentation → Admin action
5. Closure Notification → Complainant notification

---

# Maintenance Billing System

## Automated Billing & Payment Tracking

**Billing Features:**
- Monthly maintenance bill generation
- Due date management and tracking
- Payment status monitoring
- Payment reference tracking
- Overdue notifications

**Bill Management Process:**
1. Bill Generation → Admin creates monthly bills
2. Resident Notification → WebSocket/Email notification
3. Payment Processing → External payment gateway
4. Status Update → Payment confirmation
5. Receipt Generation → Payment acknowledgment

---

# Visitor Management System

## Security & Access Control

**Visitor Tracking Features:**
- Visitor registration with contact details
- Purpose of visit documentation
- Entry and exit time tracking
- Flat resident approval workflow
- Guard monitoring and control

**Visitor Management Flow:**
1. Visitor Arrival → Guard Registration
2. Flat Notification → Resident approval request
3. Approval/Rejection → Real-time notification
4. Entry Authorization → Security clearance
5. Exit Tracking → Time-based monitoring

**Security Features:**
- Mandatory phone number verification
- Purpose validation requirements
- Time-based access control
- Resident approval mandatory
- Complete audit trail maintenance

---

# Real-time Communication System

## WebSocket-Based Notifications

**Communication Channels:**
- **Private Notifications** - Individual user messages
- **Society Broadcasts** - Society-wide announcements
- **Role-based Notifications** - Admin/Resident/Guard specific
- **Global Announcements** - System-wide messages

**WebSocket Configuration:**
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig {
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
}
```

**Notification Types:**
- Complaint status updates
- Maintenance bill notifications
- Visitor approval requests
- Society announcements
- Emergency alerts

---

# Notice Management System

## Information Broadcasting Platform

**Notice Features:**
- Priority-based notice classification
- Society-wide information broadcasting
- Category-based notice organization
- Expiration date management
- Read/unread status tracking

**Notice Priorities:**
- **HIGH** - Emergency and urgent announcements
- **MEDIUM** - Important society matters
- **LOW** - General information and updates

**Notice Workflow:**
1. Notice Creation → Admin/Management
2. Priority Assignment → Urgency classification
3. Society Broadcasting → WebSocket notification
4. Read Tracking → User engagement monitoring
5. Expiration Management → Automatic cleanup

---

# Data Transfer Objects (DTOs)

## API Data Contract Management

**Authentication DTOs:**
- **AuthRequest** - User registration data
- **LoginDTO** - Login credentials
- **JwtResponseDTO** - Authentication response with token

**Entity DTOs:**
- **UserDTO** - User profile information
- **SocietyDTO** - Society details
- **BuildingDTO** - Building information
- **FlatDTO** - Flat details with relationships
- **FlatMemberDTO** - Resident information

**Business Process DTOs:**
- **ComplaintDTO** - Complaint data with status
- **MaintenanceBillDTO** - Billing information
- **VisitorDTO** - Visitor tracking data
- **NoticeDTO** - Notice broadcasting data

**Benefits:**
- Clear API contracts
- Data validation and sanitization
- Separation of concerns
- Version management support

---

# Service Layer Architecture

## Business Logic Implementation

**Service Pattern Implementation:**
```java
public interface ComplaintService {
    List<ComplaintDTO> getAllComplaints();
    ComplaintDTO createComplaint(ComplaintDTO complaint, Long userId);
    ComplaintDTO updateComplaintStatus(Long id, ComplaintStatus status);
    ComplaintDTO resolveComplaint(Long id, String resolution);
}

@Service
@Transactional
public class ComplaintServiceImpl implements ComplaintService {
    // Business logic implementation
}
```

**Key Service Classes:**
- **AuthService** - Authentication and user management
- **ComplaintService** - Complaint workflow management
- **FlatService** - Flat and allocation management
- **VisitorService** - Visitor tracking and approval
- **MaintenanceBillService** - Billing and payment processing
- **NotificationService** - Real-time communication

---

# Data Access Layer (DAO)

## Repository Pattern Implementation

**JPA Repository Structure:**
```java
@Repository
public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<User> findBySocietyId(Long societyId);
    List<User> findByRole(UserRole role);
}

@Repository
public interface ComplaintDao extends JpaRepository<Complaint, Long> {
    List<Complaint> findByFlatBuildingSocietyId(Long societyId);
    List<Complaint> findByCreatedByUserId(Long userId);
    List<Complaint> findByStatus(ComplaintStatus status);
}
```

**Repository Benefits:**
- Automatic CRUD operations
- Custom query methods
- Database agnostic implementation
- Transaction management
- Lazy loading support

---

# MapStruct Object Mapping

## Efficient Entity-DTO Conversion

**Mapper Configuration:**
```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "societyId", source = "society.id")
    @Mapping(target = "societyName", source = "society.name")
    UserDTO toDTO(User user);
    
    @Mapping(target = "society", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO userDTO);
    
    List<UserDTO> toDTOList(List<User> users);
}
```

**Mapping Benefits:**
- Compile-time generation
- Type-safe conversions
- Performance optimization
- Boilerplate code reduction
- Debugging support

---

# Exception Handling Strategy

## Centralized Error Management

**Custom Exception Classes:**
```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
```

**Global Exception Handler:**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleResourceNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO(false, ex.getMessage()));
    }
}
```

---

# API Documentation & Testing

## RESTful API Design Principles

**API Endpoint Structure:**
```
Authentication:
POST /auth/register - User registration
POST /auth/login - User authentication

Society Management:
GET /societies - List societies
POST /societies - Create society
PUT /societies/{id} - Update society

Complaint Management:
GET /complaints - List complaints (role-based)
POST /complaints - Create complaint
PUT /complaints/{id}/status - Update status

Visitor Management:
GET /visitor-logs - List visitor logs
POST /visitor-logs - Log new visitor
PUT /visitor-logs/{id}/approve - Approve visitor
```

**Response Format:**
```json
{
    "success": true,
    "message": "Operation completed successfully",
    "data": { /* Response payload */ },
    "timestamp": "2024-01-15T10:30:00Z"
}
```

---

# Configuration Management

## Application Configuration Setup

**Database Configuration:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hsmsdb
spring.datasource.username=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

**Security Configuration:**
```properties
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=1800000
app.jwt.header=Authorization
app.jwt.prefix=Bearer 

app.websocket.endpoint=/ws
app.websocket.allowed-origins=*
```

**Environment Management:**
- Development, staging, and production profiles
- Environment-specific property files
- Secret management for sensitive data
- Logging configuration per environment

---

# Performance & Scalability

## System Optimization Strategies

**Database Optimization:**
- Proper indexing on frequently queried columns
- Lazy loading for entity relationships
- Connection pooling configuration
- Query optimization and monitoring

**Caching Strategy:**
- Redis integration for session management
- Application-level caching for static data
- Database query result caching
- CDN integration for static assets

**Security Measures:**
- JWT token-based stateless authentication
- Password encryption using BCrypt
- API rate limiting implementation
- CORS configuration for cross-origin security
- SQL injection prevention through JPA

**Scalability Features:**
- Microservices-ready architecture
- Load balancing support
- Database connection pooling
- Horizontal scaling capabilities
- Cloud deployment readiness

---

# Testing Strategy

## Quality Assurance Approach

**Testing Layers:**
```java
// Unit Testing - Service Layer
@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {
    
    @Mock
    private ComplaintDao complaintRepository;
    
    @InjectMocks
    private ComplaintServiceImpl complaintService;
    
    @Test
    void shouldCreateComplaint() {
        // Test implementation
    }
}

// Integration Testing - Controller Layer
@SpringBootTest
@AutoConfigureTestDatabase
class ComplaintControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldCreateComplaintSuccessfully() {
        // Integration test implementation
    }
}
```

**Testing Coverage:**
- Unit tests for service layer business logic
- Integration tests for API endpoints
- Security testing for authentication flows
- Database testing with test containers
- End-to-end testing for complete workflows

---

# Deployment Architecture

## Production Deployment Strategy

**Containerization with Docker:**
```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/housing-management-system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Docker Compose Setup:**
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - DB_HOST=mysql
    depends_on:
      - mysql
      - redis
  
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: hsmsdb
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
    volumes:
      - mysql_data:/var/lib/mysql
```

**Cloud Deployment Options:**
- AWS ECS/EKS for container orchestration
- RDS for managed database service
- ElastiCache for Redis caching
- Application Load Balancer for high availability

---

# Monitoring & Logging

## Production Monitoring Setup

**Application Monitoring:**
- Spring Boot Actuator endpoints
- Health checks and metrics collection
- Custom business metrics tracking
- Performance monitoring and alerting

**Logging Strategy:**
```java
logging.level.org.springframework.security=DEBUG
logging.level.com.app=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

**Monitoring Tools Integration:**
- Prometheus for metrics collection
- Grafana for visualization dashboards
- ELK Stack for log aggregation
- Application Performance Monitoring (APM)

**Key Metrics to Track:**
- API response times and throughput
- Database connection pool usage
- Memory and CPU utilization
- Business metrics (complaints, bills, visitors)
- Error rates and exception tracking

---

# Security Implementation

## Comprehensive Security Measures

**Authentication & Authorization:**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated())
                .build();
    }
}
```

**Security Features:**
- JWT-based stateless authentication
- Role-based method security
- Password encryption with BCrypt
- CORS configuration for API security
- Input validation and sanitization
- SQL injection prevention through JPA

**Data Protection:**
- Sensitive data encryption
- Personal information anonymization
- Audit logging for data access
- GDPR compliance considerations

---

# Future Enhancements

## Roadmap & Scalability Considerations

**Phase 2 Enhancements:**
- **Mobile Application** - Native iOS/Android apps
- **Payment Gateway Integration** - Online payment processing
- **Document Management** - File upload and storage
- **Reporting Dashboard** - Advanced analytics and insights
- **Email/SMS Notifications** - Multi-channel communication

**Phase 3 Advanced Features:**
- **IoT Integration** - Smart home device connectivity
- **AI-Powered Analytics** - Predictive maintenance insights
- **Blockchain Integration** - Transparent transaction recording
- **Multi-language Support** - Internationalization
- **Advanced Security** - Biometric authentication

**Technical Improvements:**
- Microservices architecture migration
- Event-driven architecture implementation
- Advanced caching strategies
- Performance optimization
- Auto-scaling capabilities

**Business Expansion:**
- Multi-society management for property managers
- Vendor management system
- Marketplace for society services
- Community social features

---

# Project Workflow Summary

## Complete System Workflow

**User Onboarding Flow:**
1. Admin Registration → Society Creation
2. Building & Flat Setup → Property Management
3. Resident Registration → Flat Allocation
4. Guard Registration → Security Setup
5. System Go-Live → Operations Begin

**Daily Operations Workflow:**

**Visitor Management:**
Guard Logs Visitor → Resident Approval → Entry/Exit Tracking

**Complaint Management:**
Resident Files Complaint → Admin Review → Resolution → Closure

**Billing Cycle:**
Admin Generates Bills → Resident Notification → Payment Processing → Receipt

**Notice Distribution:**
Admin Creates Notice → System Broadcast → Resident Acknowledgment

**Key Success Metrics:**
- 99.9% system uptime
- <2 second API response time
- Real-time notification delivery
- Secure data handling compliance
- User satisfaction ratings >4.5/5

**Project Value Delivered:**
- Digitized society management operations
- Improved resident communication and satisfaction
- Streamlined administrative processes
- Enhanced security and visitor tracking
- Automated billing and financial management

---

# Thank You

## Questions & Discussion

**Contact Information:**
- **Email:** support@infoway-technologies.com
- **Phone:** +91-XXX-XXX-XXXX
- **Website:** www.infoway-technologies.com

**GitHub Repository:** [Housing Management System](https://github.com/infoway/housing-management-system)

**Technology Stack Summary:**
- Spring Boot 3.5.0
- Spring Security + JWT
- MySQL 8.0
- WebSocket Communication
- Docker Containerization