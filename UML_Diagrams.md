# Housing Management System - UML Diagrams & Architecture

## 1. Entity Relationship Diagram (ERD)

```mermaid
erDiagram
    SOCIETY ||--o{ BUILDING : contains
    SOCIETY ||--o{ USER : manages
    BUILDING ||--o{ FLAT : has
    FLAT ||--o{ FLAT_ALLOCATION : assigns
    FLAT ||--o{ FLAT_MEMBER : resides
    FLAT ||--o{ COMPLAINT : reports
    FLAT ||--o{ MAINTENANCE_BILL : generates
    FLAT ||--o{ VISITOR : visits
    USER ||--o{ FLAT_ALLOCATION : owns
    USER ||--o{ VISITOR : logs
    FLAT_MEMBER ||--o{ COMPLAINT : creates
    FLAT_MEMBER ||--o{ VISITOR : approves

    SOCIETY {
        Long id PK
        String name
        String address
        Integer numberOfBuildings
        LocalDateTime createdAt
    }

    BUILDING {
        Long id PK
        String name
        Integer totalFloors
        Long societyId FK
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    FLAT {
        Long id PK
        String flatNumber
        Integer floorNumber
        FlatType flatType
        Double area
        Long buildingId FK
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    USER {
        Long id PK
        String name
        String email
        String password
        String phone
        UserRole role
        Long societyId FK
        Boolean enabled
        LocalDateTime createdAt
    }

    FLAT_MEMBER {
        Long id PK
        String name
        String phone
        String email
        String relationship
        Boolean isOwner
        Long flatId FK
        Long userId FK
        Boolean approved
        LocalDateTime createdAt
    }

    FLAT_ALLOCATION {
        Long id PK
        Long userId FK
        Long flatId FK
        AllocationStatus status
        LocalDateTime allocatedAt
    }

    COMPLAINT {
        Long id PK
        String title
        String description
        ComplaintCategory category
        ComplaintStatus status
        Long flatId FK
        Long createdById FK
        LocalDateTime createdAt
        LocalDateTime resolvedAt
        String resolution
    }

    MAINTENANCE_BILL {
        Long id PK
        String billNumber
        LocalDate billDate
        LocalDate dueDate
        BigDecimal amount
        Boolean paid
        LocalDate paymentDate
        String paymentReference
        Long flatId FK
        String description
        LocalDateTime createdAt
    }

    VISITOR {
        Long id PK
        String name
        String phone
        String purpose
        LocalDateTime entryTime
        LocalDateTime exitTime
        Long visitingFlatId FK
        VisitorStatus status
        Long loggedById FK
        Boolean approved
        LocalDateTime approvalTime
        Long approvedById FK
        LocalDateTime createdAt
    }

    NOTICE {
        Long id PK
        String title
        String content
        NoticePriority priority
        Long societyId FK
        Long createdById FK
        LocalDateTime createdAt
        LocalDateTime expiresAt
    }
```

## 2. Class Diagram - Core Domain Model

```mermaid
classDiagram
    class Society {
        -Long id
        -String name
        -String address
        -Integer numberOfBuildings
        -List~Building~ buildings
        -List~User~ admins
        -LocalDateTime createdAt
        +getId() Long
        +getName() String
        +getBuildings() List~Building~
    }

    class Building {
        -Long id
        -String name
        -int totalFloors
        -Society society
        -List~Flat~ flats
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +getId() Long
        +getName() String
        +getFlats() List~Flat~
    }

    class Flat {
        -Long id
        -String flatNumber
        -Integer floorNumber
        -FlatType flatType
        -Double area
        -Building building
        -Set~FlatAllocation~ flatAllocations
        -Set~Complaint~ complaints
        -FlatMember member
        -List~MaintenanceBill~ maintenanceBills
        +getId() Long
        +getFlatNumber() String
        +getBuilding() Building
    }

    class User {
        -Long id
        -String name
        -String email
        -String password
        -String phone
        -UserRole role
        -Society society
        -Set~FlatAllocation~ flatAllocations
        -boolean enabled
        +getId() Long
        +getEmail() String
        +getRole() UserRole
        +getAuthorities() Collection~GrantedAuthority~
    }

    class FlatMember {
        -Long id
        -String name
        -String phone
        -String email
        -String relationship
        -boolean isOwner
        -Flat flat
        -User user
        -boolean approved
        +getId() Long
        +isOwner() boolean
        +getFlat() Flat
    }

    class Complaint {
        -Long id
        -String title
        -String description
        -ComplaintCategory category
        -ComplaintStatus status
        -Flat flat
        -FlatMember createdBy
        -LocalDateTime createdAt
        -LocalDateTime resolvedAt
        -String resolution
        +getId() Long
        +getStatus() ComplaintStatus
        +resolve(String resolution) void
    }

    class MaintenanceBill {
        -Long id
        -String billNumber
        -LocalDate billDate
        -LocalDate dueDate
        -BigDecimal amount
        -boolean paid
        -LocalDate paymentDate
        -String paymentReference
        -Flat flat
        +getId() Long
        +isPaid() boolean
        +markAsPaid(String reference) void
    }

    class Visitor {
        -Long id
        -String name
        -String phone
        -String purpose
        -LocalDateTime entryTime
        -LocalDateTime exitTime
        -Flat visitingFlat
        -VisitorStatus status
        -User loggedBy
        -boolean approved
        -FlatMember approvedBy
        +getId() Long
        +approve(FlatMember approver) void
        +markExit() void
    }

    %% Relationships
    Society ||--o{ Building : contains
    Building ||--o{ Flat : has
    Flat ||--o{ FlatMember : resides
    User ||--o{ FlatMember : registers
    Flat ||--o{ Complaint : reports
    Flat ||--o{ MaintenanceBill : generates
    Flat ||--o{ Visitor : visits
    FlatMember ||--o{ Complaint : creates
    User ||--o{ Visitor : logs

    %% Enums
    class UserRole {
        <<enumeration>>
        ADMIN
        RESIDENT
        GUARD
    }

    class FlatType {
        <<enumeration>>
        ONE_BHK
        TWO_BHK
        THREE_BHK
        FOUR_BHK
        PENTHOUSE
    }

    class ComplaintStatus {
        <<enumeration>>
        PENDING
        IN_PROGRESS
        RESOLVED
        REJECTED
    }

    class ComplaintCategory {
        <<enumeration>>
        MAINTENANCE
        SECURITY
        NOISE
        PARKING
        CLEANLINESS
        OTHER
    }

    class VisitorStatus {
        <<enumeration>>
        PENDING
        APPROVED
        REJECTED
        IN_PREMISES
        EXITED
    }
```

## 3. Use Case Diagram

```mermaid
graph TD
    Admin([Admin])
    Resident([Resident])
    Guard([Guard])
    System([Housing Management System])

    %% Admin Use Cases
    Admin --> UC1[Manage Society]
    Admin --> UC2[Manage Buildings]
    Admin --> UC3[Manage Flats]
    Admin --> UC4[Manage Users]
    Admin --> UC5[Handle Complaints]
    Admin --> UC6[Generate Bills]
    Admin --> UC7[Create Notices]
    Admin --> UC8[View Reports]

    %% Resident Use Cases
    Resident --> UC9[Register Family Members]
    Resident --> UC10[File Complaints]
    Resident --> UC11[View Bills]
    Resident --> UC12[Pay Bills]
    Resident --> UC13[Approve Visitors]
    Resident --> UC14[View Notices]
    Resident --> UC15[Update Profile]

    %% Guard Use Cases
    Guard --> UC16[Log Visitors]
    Guard --> UC17[Monitor Entries]
    Guard --> UC18[View Visitor Status]
    Guard --> UC19[Emergency Alerts]

    %% System Features
    UC1 --> System
    UC2 --> System
    UC3 --> System
    UC4 --> System
    UC5 --> System
    UC6 --> System
    UC7 --> System
    UC8 --> System
    UC9 --> System
    UC10 --> System
    UC11 --> System
    UC12 --> System
    UC13 --> System
    UC14 --> System
    UC15 --> System
    UC16 --> System
    UC17 --> System
    UC18 --> System
    UC19 --> System
```

## 4. Sequence Diagram - User Authentication Flow

```mermaid
sequenceDiagram
    participant Client
    participant AuthController
    participant AuthService
    participant UserDao
    participant JwtUtil
    participant SecurityContext

    Client->>AuthController: POST /auth/login {email, password}
    AuthController->>AuthService: login(LoginDTO)
    
    AuthService->>UserDao: findByEmail(email)
    UserDao-->>AuthService: User entity
    
    AuthService->>AuthService: validatePassword(password)
    
    alt Password Valid
        AuthService->>SecurityContext: setAuthentication()
        AuthService->>JwtUtil: generateToken(user)
        JwtUtil-->>AuthService: JWT Token
        
        AuthService-->>AuthController: JwtResponseDTO
        AuthController-->>Client: 200 OK {token, userInfo}
    else Password Invalid
        AuthService-->>AuthController: AuthenticationException
        AuthController-->>Client: 401 Unauthorized
    end
```

## 5. Sequence Diagram - Complaint Management Flow

```mermaid
sequenceDiagram
    participant Resident
    participant ComplaintController
    participant ComplaintService
    participant ComplaintDao
    participant NotificationService
    participant WebSocketController
    participant Admin

    Resident->>ComplaintController: POST /complaints {complaint data}
    ComplaintController->>ComplaintService: createComplaint(complaintDTO, userId)
    
    ComplaintService->>ComplaintDao: save(complaint)
    ComplaintDao-->>ComplaintService: saved complaint
    
    ComplaintService->>NotificationService: sendComplaintNotification(complaint)
    NotificationService->>WebSocketController: sendAdminNotification(notification)
    WebSocketController-->>Admin: Real-time notification
    
    ComplaintService-->>ComplaintController: ComplaintDTO
    ComplaintController-->>Resident: 201 Created
    
    Note over Admin: Admin reviews complaint
    Admin->>ComplaintController: PUT /complaints/{id}/status {IN_PROGRESS}
    ComplaintController->>ComplaintService: updateComplaintStatus(id, status)
    
    ComplaintService->>ComplaintDao: updateStatus(id, status)
    ComplaintService->>NotificationService: sendStatusUpdateNotification()
    NotificationService->>WebSocketController: sendPrivateNotification(resident)
    WebSocketController-->>Resident: Status update notification
```

## 6. Sequence Diagram - Visitor Management Flow

```mermaid
sequenceDiagram
    participant Guard
    participant VisitorController
    participant VisitorService
    participant VisitorDao
    participant NotificationService
    participant WebSocketController
    participant Resident

    Guard->>VisitorController: POST /visitor-logs {visitor data}
    VisitorController->>VisitorService: logVisitor(visitorDTO)
    
    VisitorService->>VisitorDao: save(visitor)
    VisitorDao-->>VisitorService: saved visitor
    
    VisitorService->>NotificationService: sendApprovalRequest(visitor)
    NotificationService->>WebSocketController: sendPrivateNotification(resident)
    WebSocketController-->>Resident: Visitor approval request
    
    VisitorService-->>VisitorController: VisitorDTO
    VisitorController-->>Guard: 201 Created
    
    Note over Resident: Resident approves/rejects visitor
    Resident->>VisitorController: PUT /visitor-logs/{id}/approve
    VisitorController->>VisitorService: approveVisitor(id, flatMemberId)
    
    VisitorService->>VisitorDao: updateApprovalStatus(id, approved)
    VisitorService->>NotificationService: sendApprovalNotification()
    NotificationService->>WebSocketController: sendPrivateNotification(guard)
    WebSocketController-->>Guard: Approval confirmation
```

## 7. Activity Diagram - Bill Generation Process

```mermaid
graph TD
    Start([Start: Monthly Bill Generation])
    
    A[Admin selects billing period]
    B[System retrieves all active flats]
    C[Calculate maintenance amount per flat]
    D{Flat has special charges?}
    E[Add additional charges]
    F[Generate bill number]
    G[Create maintenance bill record]
    H[Save bill to database]
    I[Send notification to residents]
    J{More flats to process?}
    K[Generate billing report]
    End([End: All bills generated])

    Start --> A
    A --> B
    B --> C
    C --> D
    D -->|Yes| E
    D -->|No| F
    E --> F
    F --> G
    G --> H
    H --> I
    I --> J
    J -->|Yes| C
    J -->|No| K
    K --> End
```

## 8. Component Diagram - System Architecture

```mermaid
graph TB
    subgraph "Presentation Layer"
        WEB[Web Interface]
        API[REST API]
        WS[WebSocket Endpoints]
    end

    subgraph "Security Layer"
        AUTH[Authentication Filter]
        JWT[JWT Token Manager]
        RBAC[Role-Based Access Control]
    end

    subgraph "Business Layer"
        AS[Auth Service]
        SS[Society Service]
        FS[Flat Service]
        CS[Complaint Service]
        VS[Visitor Service]
        BS[Billing Service]
        NS[Notification Service]
    end

    subgraph "Data Access Layer"
        UD[User DAO]
        SD[Society DAO]
        FD[Flat DAO]
        CD[Complaint DAO]
        VD[Visitor DAO]
        BD[Bill DAO]
    end

    subgraph "Data Layer"
        DB[(MySQL Database)]
        CACHE[(Redis Cache)]
    end

    subgraph "External Systems"
        EMAIL[Email Service]
        SMS[SMS Gateway]
        PAYMENT[Payment Gateway]
    end

    %% Connections
    WEB --> API
    API --> AUTH
    AUTH --> JWT
    AUTH --> RBAC
    
    API --> AS
    API --> SS
    API --> FS
    API --> CS
    API --> VS
    API --> BS
    
    WS --> NS
    
    AS --> UD
    SS --> SD
    FS --> FD
    CS --> CD
    VS --> VD
    BS --> BD
    
    UD --> DB
    SD --> DB
    FD --> DB
    CD --> DB
    VD --> DB
    BD --> DB
    
    NS --> CACHE
    NS --> EMAIL
    NS --> SMS
    BS --> PAYMENT
```

## 9. Deployment Diagram

```mermaid
graph TB
    subgraph "Client Tier"
        BROWSER[Web Browser]
        MOBILE[Mobile App]
    end

    subgraph "Load Balancer"
        LB[Application Load Balancer]
    end

    subgraph "Application Tier"
        subgraph "Container Cluster"
            APP1[Spring Boot App Instance 1]
            APP2[Spring Boot App Instance 2]
            APP3[Spring Boot App Instance 3]
        end
    end

    subgraph "Data Tier"
        subgraph "Database Cluster"
            MASTER[(MySQL Master)]
            SLAVE[(MySQL Read Replica)]
        end
        REDIS[(Redis Cache)]
    end

    subgraph "Monitoring & Logging"
        PROMETHEUS[Prometheus]
        GRAFANA[Grafana]
        ELK[ELK Stack]
    end

    subgraph "External Services"
        EMAIL_SVC[Email Service]
        SMS_SVC[SMS Gateway]
        PAYMENT_SVC[Payment Gateway]
    end

    %% Connections
    BROWSER --> LB
    MOBILE --> LB
    
    LB --> APP1
    LB --> APP2
    LB --> APP3
    
    APP1 --> MASTER
    APP2 --> MASTER
    APP3 --> MASTER
    
    APP1 --> SLAVE
    APP2 --> SLAVE
    APP3 --> SLAVE
    
    APP1 --> REDIS
    APP2 --> REDIS
    APP3 --> REDIS
    
    APP1 --> EMAIL_SVC
    APP2 --> SMS_SVC
    APP3 --> PAYMENT_SVC
    
    APP1 --> PROMETHEUS
    APP2 --> PROMETHEUS
    APP3 --> PROMETHEUS
    
    PROMETHEUS --> GRAFANA
    APP1 --> ELK
    APP2 --> ELK
    APP3 --> ELK
```

## 10. State Diagram - Complaint Lifecycle

```mermaid
stateDiagram-v2
    [*] --> Pending : Complaint Filed
    
    Pending --> InProgress : Admin Assigns
    Pending --> Rejected : Admin Rejects
    
    InProgress --> Resolved : Issue Fixed
    InProgress --> Pending : More Info Needed
    InProgress --> Rejected : Cannot Resolve
    
    Resolved --> [*] : Complaint Closed
    Rejected --> [*] : Complaint Closed
    
    note right of Pending
        Complaint submitted by resident
        Awaiting admin review
    end note
    
    note right of InProgress
        Admin investigating/fixing
        Status updates sent to resident
    end note
    
    note right of Resolved
        Issue resolved with solution
        Resident notified
    end note
    
    note right of Rejected
        Complaint deemed invalid
        or cannot be resolved
    end note
```

## 11. Data Flow Diagram - Level 0 (Context Diagram)

```mermaid
graph TD
    subgraph "External Entities"
        ADMIN[Society Admin]
        RESIDENT[Residents]
        GUARD[Security Guard]
        VISITOR[Visitors]
    end

    HMS[Housing Management System]

    %% Data Flows
    ADMIN -->|Society Data, User Management| HMS
    HMS -->|Reports, Notifications| ADMIN

    RESIDENT -->|Complaints, Profile Updates| HMS
    HMS -->|Bills, Notices, Status Updates| RESIDENT

    GUARD -->|Visitor Logs, Security Reports| HMS
    HMS -->|Visitor Approvals, Alerts| GUARD

    VISITOR -->|Entry Requests| HMS
    HMS -->|Access Status| VISITOR
```

## 12. Package Diagram - Code Organization

```mermaid
graph TB
    subgraph "com.app"
        subgraph "controller"
            AUTH_CTRL[AuthController]
            SOC_CTRL[SocietyController]
            COMP_CTRL[ComplaintController]
            VIS_CTRL[VisitorController]
            WS_CTRL[WebSocketController]
        end

        subgraph "service"
            AUTH_SVC[AuthService]
            SOC_SVC[SocietyService]
            COMP_SVC[ComplaintService]
            VIS_SVC[VisitorService]
            NOTIF_SVC[NotificationService]
        end

        subgraph "dao"
            USER_DAO[UserDao]
            SOC_DAO[SocietyDao]
            COMP_DAO[ComplaintDao]
            VIS_DAO[VisitorDao]
        end

        subgraph "model"
            USER[User]
            SOCIETY[Society]
            COMPLAINT[Complaint]
            VISITOR[Visitor]
        end

        subgraph "dto"
            USER_DTO[UserDTO]
            SOC_DTO[SocietyDTO]
            COMP_DTO[ComplaintDTO]
            VIS_DTO[VisitorDTO]
        end

        subgraph "security"
            JWT_UTIL[JwtUtil]
            JWT_FILTER[JwtAuthFilter]
            SEC_CONFIG[SecurityConfig]
        end

        subgraph "config"
            WS_CONFIG[WebSocketConfig]
            CORS_CONFIG[CorsConfig]
        end

        subgraph "exception"
            GLOBAL_HANDLER[GlobalExceptionHandler]
            CUSTOM_EX[Custom Exceptions]
        end

        subgraph "mapper"
            USER_MAPPER[UserMapper]
            COMP_MAPPER[ComplaintMapper]
            VIS_MAPPER[VisitorMapper]
        end
    end

    %% Dependencies
    controller --> service
    service --> dao
    dao --> model
    controller --> dto
    service --> dto
    mapper --> model
    mapper --> dto
    controller --> security
    service --> security
```