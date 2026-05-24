# Sprint 04 – Execution & Review

## 1. Resultados obtenidos

El Sprint 04 se ha completado satisfactoriamente. Se ha integrat Retrofit per connectar l'app amb l'API REST d'hotels, s'han implementat pantalles de cerca, reserva i gestió de reserves amb imatges, s'ha creat una galeria d'imatges per trip amb emmagatzematge local, i s'ha corregit l'arquitectura d'AuthRepository afegint la interfície de domini que faltava al Sprint 03.

---

## 2. Tareas completadas

| ID   | Completada | Comentarios |
|------|------------|-------------|
| T1.1 | ✅ | Retrofit + Gson + OkHttp configurat amb Hilt DI |
| T1.2 | ✅ | DTOs, domain models, API interface (MVVM) |
| T1.3 | ✅ | HotelRepository interface + HotelRepositoryImpl |
| T1.4 | ✅ | 6 unit tests amb MockWebServer |
| T2.1 | ✅ | Pantalla de cerca amb selector de ciutat i DatePickers |
| T2.2 | ✅ | Hotels i habitacions amb dades de l'API |
| T2.3 | ✅ | Reserva guardada localment com a trip + ReservationEntity |
| T2.4 | ✅ | Imatges d'hotels i habitacions amb Coil |
| T3.1 | ✅ | Image picker per adjuntar múltiples imatges |
| T3.2 | ✅ | Imatges guardades en storage intern + ImageEntity en Room |
| T3.3 | ✅ | Galeria al detall del trip (tab Gallery + preview) |
| T4.1 | ✅ | Pantalla de reserves amb trip associat |
| T4.2 | ✅ | Eliminació de reserves local i via API |
| T4.3 | ✅ | Imatges d'hotel i habitació a la llista de reserves |
| T4.4 | ✅ | Badge "🏨 Hotel reservation" a My Trips |

---

## 3. Desviaciones

**Correcció del Sprint 03:** S'ha creat la interfície `AuthRepository` a `domain/repository/` i renombrat la implementació a `AuthRepositoryImpl`, corregint el feedback rebut sobre la falta d'abstracció de domini.

**Branching strategy millorada:** S'han creat 5 feature branches amb commits més granulars per millorar la traçabilitat al repositori.

Branches:
- `feature/t1-retrofit-config` — Retrofit, DTOs, API service, domain models, repository
- `feature/t2-hotel-search-booking` — Pantalles de cerca i reserva, HotelViewModel
- `feature/t3-image-gallery` — Galeria d'imatges, ImageEntity, GalleryViewModel
- `feature/t4-reservations` — Llista de reserves, badge a Home, correcció AuthRepository
- `feature/cleanup-strings-docs` — Neteja de strings, design.md, tests

---

## 4. Retrospectiva

### Qué funcionó bien
- Retrofit s'ha integrat sense problemes amb l'arquitectura Hilt existent
- La galeria d'imatges amb storage intern funciona correctament amb persistència
- La correcció d'AuthRepository amb interfície millora l'arquitectura MVVM

### Qué no funcionó
- L'API retorna null en alguns camps de la resposta de reserva, va requerir fer DTOs nullable
- El preu de la reserva es guardava per nit en lloc de total, va caldre corregir-ho

### Qué mejoraremos
- Planificar millor els commits des del principi del sprint
- Fer reviews del codi abans de fer merge a main

---

## 5. Autoevaluación (0-10)

**Nota: 8**

Totes les tasques del sprint s'han completat segons els requisits. Retrofit connecta correctament amb l'API d'hotels, les reserves es guarden localment com a trips, la galeria d'imatges funciona amb persistència, i s'ha corregit l'arquitectura d'AuthRepository del Sprint 03. S'ha millorat la traçabilitat amb més branches i commits.