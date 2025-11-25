# FriendSpark

## About app

An app that allows user to find friends and events nearby based on user’s interests.

When potential user registers a personal account in app, he/she then is able to search for people or events nearly to find potential friends or events.

The app doesn't show the exact geolocation of people.

## Identification of the problem

### 1. What’s the main goal of the app?

The main goal of FriendSpark is to facilitate meaningful connections by helping users discover and interact with potential friends nearby who share similar interests or hobbies. It focuses on creating a safe, privacy-conscious platform where users can build platonic relationships through profile matching, interest-based filtering, and event discovery, without revealing exact geolocation data.

### 2. What problem does the app solve?

FriendSpark solves the challenge of social isolation and difficulty in forming new friendships in adulthood or after life changes (e.g., moving to a new city). It addresses the lack of easy, low-pressure ways to meet like-minded people locally, reducing reliance on chance encounters or awkward in-person networking.

### 3. Who faces this problem, and how does it impact their daily lives?

This problem primarily affects young adults (18-35), expats, remote workers, and newcomers to cities who struggle with building social circles beyond work or family. It impacts daily lives by leading to loneliness, reduced mental well-being, and missed opportunities for shared activities like hobbies or events, often resulting in more screen time on passive social media without real-world connections.

### 4. Are there any existing solutions to this problem, and how can your app improve on them?

Existing solutions include Bumble BFF (for platonic matching), Hey! VINA (women-focused friend-finding), Meetup (event-based groups), Nextdoor (neighborhood networking), and Zingr (location-based discovery). FriendSpark can improve by emphasizing privacy through approximate geohashing (unlike exact location sharing in some apps), integrating event discovery with friend matching in one seamless flow, and offering cross-platform support (Android, iOS, web, desktop) for broader accessibility. It could also enhance matching algorithms with hobby-specific filters to reduce mismatched connections, addressing complaints about superficial swiping in apps like Bumble BFF.

### 5. How will this app make users’ lives easier, more efficient, or more enjoyable?

FriendSpark will make lives easier by providing a quick, interest-driven way to find local connections, saving time on manual searches or awkward meetups. It enhances efficiency through filtered searches and event suggestions, reducing the effort to plan social activities. For enjoyment, it fosters genuine interactions based on shared hobbies, helping users build fulfilling relationships and participate in fun events, ultimately boosting happiness and combating loneliness.

### 6. How does this app align with the company’s larger mission and values?

Assuming your project represents a personal or startup mission focused on fostering authentic human connections in a digital world (as implied by the app's emphasis on privacy and interest-based matching), FriendSpark aligns by promoting inclusive, safe social networking that values user privacy and real-world engagement over superficial interactions. It supports values like community-building, accessibility (multiplatform), and innovation (geohashing for anonymity), contributing to a broader goal of reducing social isolation through technology.

### Important notes

- Mandatory: Android, iOS, Web
- Optional: Windows, MacOS, Linux

## Functionality

- Search for potential friends nearby
- Use a filter to find people with certain interests or hobbies
- Create profile:
    - Required: photo, range of age, name, interests or hobbies
    - Optional: address
- View user profile details: name, photo, interests or hobbies
- Search for events that are taking place or will take place soon
- View event details:
    - Name
    - Description
    - Photo
    - Duration
    - Repeat
    - Prices (if paid)

## Must have features

- Goggle/Apple login
- Multi-language (start with EN, RU)
- Light/Dark theme

## Possible features

- Feature voting: user can add (if not exists yet) a new possible feature and explanation why it should be added
- Subscribe to the new events or people: when new user or event appears nearby, user will be notified about it

# Design

See: https://www.figma.com/resource-library/how-to-design-an-app/ as reference guide

## Screens

1. **Sign-In**: Email/password/interests fields, "Sign In" button and Goggle/Apple buttons
2. **Log-In**: Email/password, "Log In" button and Goggle/Apple buttons
3. **Home**: Menu Bar, Nearby friends list, events cards, search FAB
4. **Profile**:Photo, name, age range, interests tags
5. **Search**: Menu Bar, Filters (interests, distance), results grid

## Pictures

Different pictures where two and more persons (m/w, m/m, w/w, etc) are doing what they love: running, walking, hiking, playing computer games, etc.

[Untitled](https://www.notion.so/2835fb66f399804a9f12d2b6c2cf1eba?pvs=21)

# MVP Implementation Priority

**Phase 1 (Weeks 1-4):**
•	User registration/authentication
•	Basic profile creation
•	Simple proximity search
•	Basic mobile app with core screens
**Phase 2 (Weeks 5-8):**
•	Interest-based filtering
•	Event creation and search
•	Enhanced profile features
•	Web application
**Phase 3 (Weeks 9-12):**
•	Advanced matching algorithms
•	Push notifications
•	Performance optimizations
•	Analytics integration

## User flow diagram

```mermaid
flowchart TD
    Start([App Launch]) --> Splash[Splash Screen]
    Splash --> CheckAuth{User Logged In?}
    
    CheckAuth -->|No| Onboard[Onboarding Screen]
    CheckAuth -->|Yes| Home[Home Screen]
    
    Onboard --> GetStarted(Click 'Get Started')
    GetStarted --> SignUp[Sign Up Screen]
    
    SignUp --> FillForm(Fill Registration Form)
    FillForm --> Submit(Click 'Submit')
    Submit --> Validate{Form Valid?}
    
    Validate -->|No| Error1[Show Error Message]
    Error1 --> SignUp
    
    Validate -->|Yes| Register{Registration Success?}
    Register -->|No| Error2[Show Error]
    Error2 --> SignUp
    
    Register -->|Yes| Welcome[Welcome Screen]
    Welcome --> Home
    
    Home --> Search(Click 'Search')
    Home --> Profile(Click 'Profile')
    Home --> Messages(Click 'Messages')
    
    Search --> SearchScreen[Search Screen]
    Profile --> ProfileScreen[Profile/Friend Details]
    Messages --> MessagesScreen[Messages Screen]
    
    ProfileScreen --> SendMsg(Click 'Send Message')
    ProfileScreen --> ViewEvents(Click 'View Events')
    
    SendMsg --> MessagesScreen
    ViewEvents --> EventsScreen[Events Screen]
    
    style Start fill:#13a4ec,stroke:#0c7bb3,color:#fff
    style Splash fill:#10b981,stroke:#059669,color:#fff
    style Home fill:#10b981,stroke:#059669,color:#fff
    style Onboard fill:#10b981,stroke:#059669,color:#fff
    style SignUp fill:#10b981,stroke:#059669,color:#fff
    style Welcome fill:#10b981,stroke:#059669,color:#fff
    style SearchScreen fill:#10b981,stroke:#059669,color:#fff
    style ProfileScreen fill:#10b981,stroke:#059669,color:#fff
    style MessagesScreen fill:#10b981,stroke:#059669,color:#fff
    style EventsScreen fill:#10b981,stroke:#059669,color:#fff
    style Error1 fill:#ef4444,stroke:#dc2626,color:#fff
    style Error2 fill:#ef4444,stroke:#dc2626,color:#fff
    style GetStarted fill:#f9f,stroke:#dc2626,color:#fff
    style Submit fill:#f9f,stroke:#dc2626,color:#fff
    style Search fill:#f9f,stroke:#dc2626,color:#fff
    style Profile fill:#f9f,stroke:#dc2626,color:#fff
    style Messages fill:#f9f,stroke:#dc2626,color:#fff
    style SendMsg fill:#f9f,stroke:#dc2626,color:#fff
    style ViewEvents fill:#f9f,stroke:#dc2626,color:#fff
    
    
   
```

## Component Communication Diagram

```mermaid
graph TD
    %% UI Layer (Presentation)
    subgraph Presentation Layer
        A["ProfileScreen<br>(Compose)"] -->|StateFlow| B[ProfileViewModel]
        C["SearchScreen<br>(Compose)"] -->|StateFlow| D[SearchViewModel]
        E["EventScreen<br>(Compose)"] -->|StateFlow| F[EventViewModel]
        B -->|Calls| G[CreateProfileUseCase]
        D -->|Calls| H[SearchNearbyUsersUseCase]
        F -->|Calls| I[FetchEventsUseCase]
    end

    %% Domain Layer
    subgraph Domain Layer
        G -->|Uses| J[UserRepository]
        H -->|Uses| J
        I -->|Uses| K[EventRepository]
        L["UserProfile<br>(Entity)"] -->|Used by| G
        L -->|Used by| H
        M["Event<br>(Entity)"] -->|Used by| I
    end

    %% Data Layer
    subgraph Data Layer
        J -->|Fetches| N["ApiService<br>(Ktor Client)"]
        J -->|Caches| O["LocalDatabase<br>(SQLDelight)"]
        K -->|Fetches| N
        K -->|Caches| O
        P[GeolocationService] -->|Provides GeoHash| J
    end

    %% Platform-Specific
    subgraph Platform-Specific
        Q["Android Location<br>(LocationManager)"] -->|actual| P
        R[iOS CoreLocation] -->|actual| P
    end

    %% External Services
    subgraph External Services
        N -->|HTTP| S["Spring Boot Backend<br>(REST APIs)"]
        N -->|Uploads| T["Cloud Storage<br>(AWS S3)"]
    end

    %% State Flow
    B -->|Emits StateFlow| A
    D -->|Emits StateFlow| C
    F -->|Emits StateFlow| E

    %% Notes
    classDef layer fill:#f9f,stroke:#333,stroke-width:2px;
    class A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P layer;
    class Q,R fill:#bbf,stroke:#333,stroke-width:2px;
    class S,T fill:#dfd,stroke:#333,stroke-width:2px;
```

```mermaid
graph TD
    %% === Frontend (KMP) Section ===
    subgraph KMP_Frontend["Kotlin Multiplatform Frontend"]
        subgraph ComposeApp["ComposeApp Module"]
            UI["UI: @Composable Screens<br>(SignIn, Home, Profile, Search)"]
            ViewModels["ViewModels: SignInViewModel, HomeViewModel<br>(Koin Injected)"]
            Navigation[Navigation: Voyager/Decompose]
        end

        subgraph Shared["Shared Module"]
            Domain["Domain: Entities (UserProfile, Event)<br>Use Cases"]
            Data["Data: Repositories, Ktor Client<br>SQLDelight (Local DB)"]
            DI[DI: Koin Modules]
        end

        subgraph Android["Android Module"]
            AndroidEntry[Android Entry: MainActivity]
            Permissions[Permissions: Location, Notifications]
            WorkManager[WorkManager: Sync Tasks]
        end

        subgraph iOS["iOS Module"]
            iOSEntry[iOS Entry: MainViewController]
            CoreLocation[CoreLocation: Geolocation]
            BackgroundTasks[Background Tasks: Sync]
        end

        UI --> ViewModels
        ViewModels --> Data
        Data --> DI
        AndroidEntry --> UI
        iOSEntry --> UI
        Permissions --> Data
        WorkManager --> Data
        CoreLocation --> Data
        BackgroundTasks --> Data
    end

    %% === Backend (Spring Boot) Section ===
    subgraph SpringBoot_Backend["Spring Boot Backend API"]
        subgraph Presentation["Presentation Layer"]
            UserController[UserController: /api/users/nearby]
            AuthController[AuthController: /api/auth/login]
            EventController[EventController: /api/events]
        end

        subgraph Service["Service Layer"]
            UserService[UserService: Business Logic]
            AuthService[AuthService: JWT Generation]
            EventService[EventService: Event Management]
        end

        subgraph DataAccess["Data Access Layer"]
            UserRepository[UserRepository: JPA Queries]
            EventRepository[EventRepository: JPA Queries]
        end

        subgraph Security["Security Layer"]
            SpringSecurity[Spring Security: JWT Filter]
            SecurityConfig[SecurityConfig: @EnableWebSecurity]
        end

        UserController --> UserService
        AuthController --> AuthService
        EventController --> EventService
        UserService --> UserRepository
        AuthService --> UserRepository
        EventService --> EventRepository
        UserController --> Security
        AuthController --> Security
        EventController --> Security
    end

    %% === Database Section ===
    subgraph Database["PostgreSQL Database"]
        UsersTable[users: id, email, name, interests, geohash]
        EventsTable[events: id, title, location_geohash, date, creator_id]
        UserEventsTable[user_events: user_id, event_id, rsvp_status]
        BlockedUsersTable[blocked_users: blocker_id, blocked_id]
        
        UsersTable --> UserRepository
        EventsTable --> EventRepository
        UserEventsTable --> UserRepository
        UserEventsTable --> EventRepository
        BlockedUsersTable --> UserRepository
    end

    %% === External Integrations Section ===
    subgraph Integrations["External Integrations"]
        GeohashService[Geohash Service]
        FCM[FCM: Push Notifications]
    end

    %% === Data Flow ===
    UI -->|"HTTP Request (GET /api/users/nearby)"| UserController
    Data -->|HTTP Request| AuthController
    Data -->|HTTP Request| EventController
    UserRepository -->|SQL Query| UsersTable
    EventRepository -->|SQL Query| EventsTable
    UserService -->|Geohash Logic| GeohashService
    UserService -->|Notification Trigger| FCM
    Data -->|Local Sync| UsersTable
    Data -->|Local Sync| EventsTable

    %% === Notes ===
    classDef frontend fill:#f9f,stroke:#333,stroke-width:2px;
    classDef backend fill:#bbf,stroke:#333,stroke-width:2px;
    classDef database fill:#bfb,stroke:#333,stroke-width:2px;
    classDef integrations fill:#ffb,stroke:#333,stroke-width:2px;
    class KMP_Frontend,ComposeApp,Shared,Android,iOS frontend;
    class SpringBoot_Backend,Presentation,Service,DataAccess,Security backend;
    class Database,UsersTable,EventsTable,UserEventsTable,BlockedUsersTable database;
    class Integrations,GeohashService,FCM integrations;
```

## Final Architecture Diagram

```mermaid
graph TB
    subgraph "Clients – One Kotlin UI codebase"
        Desktop[Compose Desktop<br>Windows / macOS / Linux]
        Web[Compose Web<br>HTML + Canvas/WasmGC]
        Android[Compose Android]
        iOS["Compose iOS<br>(via Skiko)"]
        Desktop --> SharedUI
        Web --> SharedUI
        Android --> SharedUI
        iOS --> SharedUI
    end

    subgraph "Shared Kotlin Multiplatform"
        SharedUI[Compose Multiplatform UI<br>+ Voyager Navigation]
        SharedDomain[Domain Logic<br>Use Cases, Entities]
        SharedData[Repositories<br>Ktor Client + SQLDelight]
        SharedDI[Koin]
    end

    subgraph "Backend – Spring Boot (Kotlin)"
        API[Spring Boot 3<br>REST + JWT]
        Service[Services]
        Repo[JPA Repositories]
        DB[(PostgreSQL + PostGIS)]
        Redis[(Redis Cache<br>optional)]
        FCM[FCM / APNs<br>Push]
    end

    SharedData -->|HTTPS + JSON| API
    API --> Service --> Repo --> DB
    Service --> Redis
    Service --> FCM

    classDef client fill:#E3F2FD,stroke:#1976D2
    classDef shared fill:#F1F8E9,stroke:#689F38
    classDef backend fill:#FFF3E0,stroke:#EF6C00
    class Desktop,Web,Android,iOS client
    class SharedUI,SharedDomain,SharedData,SharedDI shared
    class API,Service,Repo,DB,Redis,FCM backend
```

```mermaid
graph TD
    subgraph "Frontend (Kotlin Multiplatform App)"
        KMP_UI["A(Compose UI: Mobile / Desktop / Web)"]
        KMP_SHARED("B[KMP Shared Module: ViewModel, Repository, API Client]")
        KMP_UI -->|UI Actions| KMP_SHARED
    end

    subgraph External Services
        FIREBASE("C(Firebase Authentication)")
        SPRING_BACKEND["D(Spring Boot API Server)"]
        DB["E (Application Database)"]
    end

    %% --- Authentication Flow (Via Firebase) ---
    subgraph Authentication Flow
        style FIREBASE fill:#FFC107,stroke:#333,stroke-width:2px
        style KMP_SHARED fill:#D0E6F0
        KMP_SHARED -->|"1. Sign In / Register (Credentials)"| FIREBASE
        FIREBASE -->|2. Verify & Issue ID Token| KMP_SHARED
        KMP_SHARED -->|"3. Store Token Locally (Secure Storage)"| KMP_SHARED
    end

    %% --- Authorized Data Flow (Via Spring Boot) ---
    subgraph Authorized Data Flow
        style SPRING_BACKEND fill:#4CAF50,stroke:#333,stroke-width:2px
        style KMP_SHARED fill:#D0E6F0
        KMP_SHARED -->|"4. API Request w/ Firebase ID Token (Bearer)"| SPRING_BACKEND
        SPRING_BACKEND -->|5. Validate Token & Get User ID| FIREBASE
        FIREBASE -->|6. Token Valid & User ID| SPRING_BACKEND
        SPRING_BACKEND -->|7. Business Logic / Data Access| DB
        DB -->|8. Data Result| SPRING_BACKEND
        SPRING_BACKEND -->|9. Authorized API Response| KMP_SHARED
        KMP_SHARED -->|10. Update UI State| KMP_UI
    end
   
```

## Class Diagram

```mermaid
classDiagram
    direction TB

    %% ================== BACKEND (Spring Boot + Kotlin) ==================
    namespace Backend {
        class User {
            +UUID id
            +String firebaseUid
            +String email
            +String name
            +List~String~ interests
            +String geohash
            +Double lat
            +Double lng
            +LocalDateTime createdAt
        }

        class Event {
            +UUID id
            +String title
            +String description
            +String geohash
            +Double lat
            +Double lng
            +LocalDateTime date
            +UUID creatorId
            +List~User~ attendees
        }

        class UserEvent {
            +UUID userId
            +UUID eventId
            +RsvpStatus status
        }

        class BlockedUser {
            +UUID blockerId
            +UUID blockedId
        }

        class UserController {
            +getNearbyUsers()
            +getEventsNearby()
            +createEvent()
            +rsvpEvent()
            +blockUser()
        }

        class EventController

        class AuthController {
            +validateToken()
        }

        class JwtAuthenticationFilter {
            -FirebaseAuth firebaseAuth
            +doFilterInternal()
        }

        class FirebaseToken {
            +verify() FirebaseToken
        }

        class AuthenticatedUserIdProvider {
            +getAuthenticatedUserId() String
        }

        class FirebaseConfig {
            +firebaseAuth() FirebaseAuth
        }

        class SecurityConfig {
            +securityFilterChain()
        }
    }

    %% ================== FRONTEND (KMP - Compose Multiplatform) ==================
    namespace Frontend {
        class OnboardingScreen {
            +Content()
        }

        class RegisterScreen {
            +Content()
        }

        class LoginScreen {
            +Content()
        }

        class HomeScreen {
            +NearbyUsersList()
            +NearbyEventsList()
        }

        class ProfileScreen {
            +EditInterests()
            +UpdateLocation()
        }

        class EventDetailScreen

        class AppNavigator {
            +Navigator(OnboardingScreen)
        }

        class FirebaseAuthManager {
            +signInWithEmail()
            +signInWithGoogle()
            +getIdToken() String
        }

        class ApiClient {
            +getNearbyUsers(token: String)
            +getNearbyEvents(token: String)
            +createEvent()
        }
    }

    %% ================== RELATIONSHIPS ==================
    User "1" --> "0..*" Event : creates
    User "1" --> "0..*" UserEvent : participates in
    User "1" --> "0..*" BlockedUser : blocks
    Event "1" --> "0..*" UserEvent : has attendees

    UserController --> User
    EventController --> Event
    JwtAuthenticationFilter --> FirebaseToken
    JwtAuthenticationFilter --> FirebaseAuth
    AuthenticatedUserIdProvider --> SecurityContextHolder
    SecurityConfig --> JwtAuthenticationFilter

    OnboardingScreen --> FirebaseAuthManager
    RegisterScreen --> FirebaseAuthManager
    HomeScreen --> ApiClient
    ProfileScreen --> ApiClient
    ApiClient --> UserController
    ApiClient --> EventController

    %% ================== ENUMS ==================
    class RsvpStatus {
        <<enumeration>>
        GOING
        INTERESTED
        NOT_GOING
    }

    UserEvent --> RsvpStatus

 
```

## Authentication/Authorization Flow

```mermaid
sequenceDiagram
    participant User
    participant KMP_UI as Compose UI (Mobile/Web)
    participant KMP_REPO as KMP Shared Module (Repository)
    participant FIREBASE as Firebase Auth Client SDK
    participant SPRING_API as Spring Boot Backend

    Note over User, FIREBASE: 1. Registration / Login

    User->>KMP_UI: 1. Enters Email/Password
    KMP_UI->>KMP_REPO: 2. call authenticateUser(credentials)
    KMP_REPO->>FIREBASE: 3. delegate sign-up/in request (e.g., Firebase SDK)
    FIREBASE-->>FIREBASE: 4. Verify Credentials & Generate Token
    FIREBASE-->>KMP_REPO: 5. return Firebase ID Token (JWT)

    Note over KMP_REPO, KMP_REPO: 6. Securely Save Token (e.g., KMP-Settings / Keychain)

    KMP_REPO-->>KMP_UI: 7. return Success / Failure Status
    KMP_UI->>User: 8. Show Main Screen or Error

    Note over User, SPRING_API: 9. Subsequent Authorized Data Request

    KMP_UI->>KMP_REPO: 10. call fetchUserData()
    KMP_REPO->>KMP_REPO: 11. retrieve ID Token from secure storage
    KMP_REPO->>SPRING_API: 12. HTTP Request with Token in "Authorization: Bearer <ID_Token>" Header
    SPRING_API->>FIREBASE: 13. Spring Boot: Validate Token via Firebase Admin SDK
    FIREBASE-->>SPRING_API: 14. Token Valid / User UID
    SPRING_API-->>KMP_REPO: 15. return Authorized Data
    KMP_REPO-->>KMP_UI: 16. Update State / Show Data
```

### Helpful commands:

Backend CRUD + Auth (JWT):

```bash
./gradlew :backend:bootRun
```

Desktop app with Onboarding + Register:

```bash
./gradlew :composeApp:run
```

Web version (same code):

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

Add Android + iOS targets (zero UI changes):

```bash
./gradlew :composeApp:iosX64Binaries
```