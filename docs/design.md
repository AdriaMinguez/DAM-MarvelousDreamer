# Marvelous Dreamer — Design Document

## What is this app?

Marvelous Dreamer is an Android travel planner built with **Jetpack Compose** and **Material3**. It lets users organise trips, browse day-by-day itineraries, track budgets, search and book hotels via a REST API, and explore a photo gallery for each journey.

---

## Architecture

The app follows **MVVM** (Model–View–ViewModel) with unidirectional data flow and dependency injection via **Hilt**:

- **Model** — Room entities in `data/local/entity/`, domain classes in `domain/`, and API DTOs in `data/remote/dto/`.
- **View** — Composable functions in `ui/screens/`. Each screen receives data as parameters and exposes events via lambda callbacks.
- **ViewModel** — `TripViewModel`, `AuthViewModel`, `HotelViewModel` and `GalleryViewModel` in `ui/viewmodel/`, injected by Hilt. They expose state via `StateFlow` and delegate to repositories.
- **Repository** — `TripRepositoryImpl`, `AuthRepository`, `HotelRepositoryImpl` and `GalleryRepository` in `data/repository/`. They abstract the data source (Room DAOs, Firebase Auth, Retrofit API).
- **Data Source** — Room Database (`AppDatabase`) with DAOs for trips, activities, users, access logs, reservations and images. Firebase Authentication for login/register. Retrofit for hotel REST API.

Data flow: **UI → ViewModel → Repository → DAO/Firebase/Retrofit → SQLite/Cloud/API**

The app uses a **single Activity** (`MainActivity`, annotated with `@AndroidEntryPoint`) with Jetpack Navigation Compose handling all transitions inside a `NavHost`.

---

## Package Structure

```
com.example.marvelousdreamer/
├── MarvelousDreamerApp.kt          # @HiltAndroidApp
├── di/
│   └── AppModule.kt                # Hilt DI module (DB, DAOs, repos, Retrofit)
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt          # Room Database (v2, 6 entities)
│   │   ├── Converters.kt           # TypeConverters
│   │   ├── dao/                     # TripDao, ActivityDao, UserDao, AccessLogDao,
│   │   │                            # ReservationDao, ImageDao
│   │   └── entity/                  # TripEntity, ActivityEntity, UserEntity,
│   │                                # AccessLogEntity, ReservationEntity, ImageEntity
│   ├── remote/                      # Sprint 04 — Retrofit
│   │   ├── api/
│   │   │   └── HotelApiService.kt   # Retrofit API interface
│   │   ├── dto/                     # HotelDto, RoomDto, ReservationDto, etc.
│   │   └── mapper/
│   │       └── DtoMappers.kt        # DTO ↔ Domain mapping
│   ├── repository/                  # TripRepositoryImpl, AuthRepository,
│   │                                # HotelRepositoryImpl, GalleryRepository
│   └── preferences/
│       └── UserPreferencesManager.kt
├── domain/
│   ├── Trip.kt, Activity.kt, ActivityType.kt, TripRepository.kt, User.kt
│   ├── model/                       # Hotel, Room, Reservation, ReserveRequest
│   └── repository/
│       └── HotelRepository.kt
├── utils/
│   └── FileHelpers.kt               # Image storage helpers
└── ui/
    ├── MainActivity.kt
    ├── screens/                      # All composable screens
    ├── navigation/                   # NavGraph.kt, Routes.kt
    ├── viewmodel/                    # TripViewModel, AuthViewModel,
    │                                 # HotelViewModel, GalleryViewModel
    └── themes/                       # Color.kt, Theme.kt, Type.kt
```

---

## Navigation

Routes are defined as constants in `Routes.kt`. Dynamic routes use path parameters:

```
trip_detail/{tripId}
trip_gallery/{tripId}
edit_trip/{tripId}
add_activity/{tripId}
edit_activity/{tripId}/{activityId}
```

**Auth flow (Sprint 03):** On app start, the splash screen checks if the user is logged in via Firebase. If not, the user is redirected to the Login screen. After login/register, the user goes to Home. Logout returns to Login.

**Hotel flow (Sprint 04):** Bottom nav has Home, Hotels, Bookings, Settings. Hotels leads to search screen (city + dates), then hotel detail with rooms and images, then booking. Bookings shows all local reservations with delete option.

**Back navigation rules:**
- From trip detail → always goes to **Home**
- From trip gallery → goes back to the **specific trip**
- From login/register → no back to Home (must authenticate)
- From hotel detail → back to search results
- Everything else → `popBackStack()`

The bottom bar is hidden on Splash, Terms, auth screens, CRUD forms and hotel detail.

---

## Domain Model

![Domain Class Diagram](domain_model.png)

---

## REST API (Sprint 04)

The app connects to a hotel reservation API at `http://15.224.84.148:8090` using **Retrofit** with Gson converter. Group ID: `G11`.

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/hotels/{group_id}/availability?start_date=&end_date=&city=` | Search available hotels |
| POST | `/hotels/{group_id}/reserve` | Book a room |
| GET | `/hotels/{group_id}/reservations` | List group reservations |
| DELETE | `/reservations/{res_id}` | Cancel a reservation |

### Request/Response models

- **HotelDto**: id, name, address, rating, image_url, rooms[]
- **RoomDto**: id, room_type, price, images[]
- **ReserveRequestDto**: hotel_id, room_id, start_date, end_date, guest_name, guest_email
- **ReservationResponseDto**: message, nights, reservation (with hotel + room details)

Images are loaded via **Coil** using the API base URL + relative image paths.

---

## Database Schema

The app uses **Room** (SQLite) for local persistence. The database contains 6 tables (v2):

### Tables

#### `trips`
| Column      | Type    | Constraints       | Description              |
|-------------|---------|-------------------|--------------------------|
| id          | TEXT    | PRIMARY KEY       | UUID or "hotel_xxx"      |
| title       | TEXT    | NOT NULL          | Trip name                |
| description | TEXT    |                   | Trip description         |
| destination | TEXT    |                   | Destination city/country |
| startDate   | INTEGER | NOT NULL          | Epoch day (LocalDate)    |
| endDate     | INTEGER | NOT NULL          | Epoch day (LocalDate)    |
| budget      | REAL    |                   | Budget in EUR            |
| notes       | TEXT    |                   | Free text notes          |
| userId      | TEXT    |                   | Firebase UID (owner)     |

#### `activities`
| Column      | Type    | Constraints                          | Description           |
|-------------|---------|--------------------------------------|-----------------------|
| id          | TEXT    | PRIMARY KEY                          | UUID                  |
| tripId      | TEXT    | FOREIGN KEY → trips(id) ON DELETE CASCADE | Parent trip      |
| title       | TEXT    | NOT NULL                             | Activity name         |
| description | TEXT    |                                      | Activity description  |
| date        | INTEGER | NOT NULL                             | Epoch day (LocalDate) |
| time        | INTEGER | NOT NULL                             | Second of day (LocalTime) |
| location    | TEXT    |                                      | Place name            |
| cost        | REAL    |                                      | Cost in EUR           |
| type        | TEXT    |                                      | ActivityType enum     |

#### `users`
| Column       | Type    | Constraints | Description                |
|--------------|---------|-------------|----------------------------|
| id           | TEXT    | PRIMARY KEY | Firebase UID               |
| login        | TEXT    | NOT NULL    | Email address              |
| username     | TEXT    | NOT NULL    | Display name               |
| birthdate    | INTEGER |             | Epoch millis               |
| address      | TEXT    |             | Street address             |
| country      | TEXT    |             | Country name               |
| phone        | TEXT    |             | Phone number               |
| acceptEmails | INTEGER |             | Boolean (0/1)              |

#### `access_log`
| Column    | Type    | Constraints              | Description            |
|-----------|---------|--------------------------|------------------------|
| id        | INTEGER | PRIMARY KEY AUTOINCREMENT| Auto ID                |
| userId    | TEXT    | NOT NULL                 | Firebase UID           |
| action    | TEXT    | NOT NULL                 | "LOGIN" or "LOGOUT"   |
| timestamp | INTEGER | NOT NULL                 | System.currentTimeMillis() |

#### `reservations` (Sprint 04)
| Column        | Type    | Constraints | Description              |
|---------------|---------|-------------|--------------------------|
| id            | TEXT    | PRIMARY KEY | API reservation ID       |
| tripId        | TEXT    | NOT NULL    | Linked trip ID           |
| hotelId       | TEXT    |             | Hotel ID from API        |
| hotelName     | TEXT    |             | Hotel name               |
| hotelAddress  | TEXT    |             | Hotel address            |
| hotelImageUrl | TEXT    |             | Hotel image URL          |
| roomId        | TEXT    |             | Room ID from API         |
| roomType      | TEXT    |             | Room type name           |
| price         | REAL    |             | Price per night          |
| roomImages    | TEXT    |             | Comma-separated image URLs |
| startDate     | TEXT    |             | Check-in date            |
| endDate       | TEXT    |             | Check-out date           |
| guestName     | TEXT    |             | Guest name               |
| guestEmail    | TEXT    |             | Guest email              |
| userId        | TEXT    |             | Firebase UID (owner)     |

#### `images` (Sprint 04)
| Column | Type    | Constraints                               | Description     |
|--------|---------|-------------------------------------------|-----------------|
| id     | INTEGER | PRIMARY KEY AUTOINCREMENT                 | Auto ID         |
| tripId | TEXT    | FOREIGN KEY → trips(id) ON DELETE CASCADE | Parent trip     |
| uri    | TEXT    | NOT NULL                                  | Local file URI  |

### Relationships

- **trips ← activities**: One-to-many. Cascade delete.
- **trips ← images**: One-to-many. Cascade delete: removing a trip deletes all its photos.
- **trips ← reservations**: One-to-one. Each hotel booking creates a trip with id "hotel_xxx".
- **users → trips**: One-to-many. Only the logged-in user's trips are shown.
- **users → access_log**: One-to-many. Every login/logout event is recorded.

### TypeConverters

- `LocalDate` ↔ `Long` (epoch day)
- `LocalTime` ↔ `Int` (second of day)
- `ActivityType` ↔ `String` (enum name)

### Data Validation

- Trip title must be unique per user (checked via `isTitleDuplicate` query)
- Username must be unique (checked via `isUsernameTaken` query)
- Trip start date must be before end date (validated in ViewModel)
- Activity date must be within trip date range (validated in ViewModel)
- All date fields use DatePicker components (no free text input)