# Sprint 03 – Execution & Review

## 1. Resultados obtenidos

El Sprint 03 se ha completado satisfactoriamente. Se ha reemplazado el almacenamiento inMemory por Room Database con persistencia SQLite, se ha implementado Firebase Authentication (login, registro, recuperación de contraseña), se ha persistido la información de usuario en la BD local vinculando los trips al usuario autenticado, y se han creado 24 tests instrumentados para los DAOs. Se ha usado Hilt como librería de inyección de dependencias.

---

## 2. Tareas completadas

| ID   | Completada | Comentarios |
|------|------------|-------------|
| T1.1 | ✅ | Room Database creada con TypeConverters |
| T1.2 | ✅ | Entities: TripEntity, ActivityEntity, UserEntity, AccessLogEntity |
| T1.3 | ✅ | DAOs: TripDao, ActivityDao, UserDao, AccessLogDao |
| T1.4 | ✅ | CRUD amb DAO per a trips i activitats |
| T1.5 | ✅ | ViewModels actualitzats per usar Room amb Hilt |
| T1.6 | ✅ | UI s'actualitza amb StateFlow + Flow de Room |
| T2.1 | ✅ | App connectada amb Firebase |
| T2.2 | ✅ | Pantalla de login dissenyada |
| T2.3 | ✅ | Login amb Firebase (email & password) |
| T2.4 | ✅ | Logout implementat (menú Home i Profile) |
| T2.5 | ✅ | Logs de login/logout amb Logcat |
| T3.1 | ✅ | Pantalla de registre dissenyada |
| T3.2 | ✅ | Registre amb Firebase i verificació d'email |
| T3.3 | ✅ | Recuperació de contrasenya implementada |
| T4.1 | ✅ | Taula users amb login, username, birthdate, address, country, phone, acceptEmails |
| T4.2 | ✅ | Trips vinculats a l'usuari autenticat |
| T4.3 | ✅ | design.md actualitzat amb esquema de BD |
| T4.4 | ✅ | Log d'accessos (login/logout) persistit a la BD |
| T5.1 | ✅ | 24 tests instrumentats per a DAOs |
| T5.2 | ✅ | Validació de noms duplicats i dates |
| T5.3 | ✅ | Logs amb Logcat per a operacions de BD |
| T5.4 | ✅ | Documentació actualitzada amb esquema de BD |

---

## 3. Desviaciones

**Ninguna desviación significativa.** Todas las tareas planificadas en el plan_sprint03.md se han completado. Se ha aplicado la branching strategy con 4 feature branches mergeadas a main:

- `feature/t1-room-database` — Room, Hilt, Entities, DAOs
- `feature/t2-t3-firebase-auth` — Firebase Auth, login, register, forgot password
- `feature/t4-user-persistence` — Edit profile, user data unificado, pantallas actualizadas
- `feature/t5-dao-tests` — Tests instrumentats per a DAOs

---

## 4. Retrospectiva

### Qué funcionó bien
- La migración de inMemory a Room fue limpia gracias a la arquitectura MVVM del Sprint 02
- Hilt simplifica la inyección de dependencias en ViewModels y Repositories
- Firebase Authentication se integró sin problemas con la navegación existente
- Los tests instrumentados cubren las 4 tablas de la BD

### Qué no funcionó
- Unificar los datos de usuario entre SharedPreferences, Room y Firebase requirió refactorización
- El Flow de Room no detectaba cambios en tablas relacionadas (activities), requirió un refresh trigger manual

### Qué mejoraremos en el próximo sprint
- Mejor planificación de la estructura de datos antes de implementar
- Diseñar los flujos de datos entre capas antes de codificar

---

## 5. Autoevaluación (0-10)

**Nota: 8**

Todas las tareas del sprint se han completado según los requisitos. Room Database reemplaza correctamente el almacenamiento inMemory, Firebase Authentication funciona con login, registro y recuperación de contraseña, los datos de usuario se persisten localmente, y los tests instrumentados cubren los DAOs. Se ha usado Hilt como DI tal como se requería.