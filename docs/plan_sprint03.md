# Sprint 03 – Planning Document

## 1. Sprint Goal
1. Implement SQLite persistence using Room Database for trips and itinerary items.
2. Replace in-memory storage with Room (DAO, Entities, Database).
3. Implement Firebase Authentication (login, register, recover password).
4. Persist user information in local DB and link trips to authenticated users.
5. Implement Hilt as dependency injection library.
6. Test database functionality and document the schema.
7. Create release v3.0.0 in GitHub.

---

## 2. Sprint Backlog

| ID   | Tarea                                                          | Responsable | Estimació (h) | Prioritat |
|------|----------------------------------------------------------------|-------------|----------------|-----------|
| T1.1 | Crear la classe Room Database                                  | Adrià       | 2              | Alta      |
| T1.2 | Definir Entities per a Trip i ItineraryItem                    | Adrià       | 3              | Alta      |
| T1.3 | Crear Data Access Objects (DAOs) per a operacions CRUD         | Adrià       | 3              | Alta      |
| T1.4 | Implementar operacions CRUD amb DAO per a trips i activitats   | Adrià       | 3              | Alta      |
| T1.5 | Modificar ViewModels per a usar Room en lloc d'inMemory        | Adrià       | 3              | Alta      |
| T1.6 | Assegurar actualització de la UI amb canvis a la BD            | Adrià       | 2              | Alta      |
| T2.1 | Connectar l'app amb Firebase                                   | Adrià       | 2              | Alta      |
| T2.2 | Dissenyar pantalla de login                                    | Adrià       | 2              | Alta      |
| T2.3 | Implementar login amb Firebase (auth & password)               | Adrià       | 3              | Alta      |
| T2.4 | Implementar acció de logout                                    | Adrià       | 1              | Alta      |
| T2.5 | Logs de login/logout amb Logcat                                | Adrià       | 1              | Media     |
| T3.1 | Dissenyar pantalla de registre                                 | Adrià       | 2              | Alta      |
| T3.2 | Implementar registre amb Firebase i verificació d'email        | Adrià       | 3              | Alta      |
| T3.3 | Implementar recuperació de contrasenya                         | Adrià       | 2              | Alta      |
| T4.1 | Persistir informació d'usuari a la BD local (taula user)       | Adrià       | 3              | Alta      |
| T4.2 | Modificar taula trips per a múltiples usuaris                  | Adrià       | 2              | Alta      |
| T4.3 | Actualitzar documentació amb esquema de BD a design.md         | Adrià       | 1              | Media     |
| T4.4 | Persistir accessos (log de login/logout amb userid i datetime) | Adrià       | 2              | Alta      |
| T5.1 | Unit tests per a DAOs i interaccions amb BD                    | Adrià       | 3              | Alta      |
| T5.2 | Validació de dades (noms duplicats, dates vàlides)             | Adrià       | 2              | Alta      |
| T5.3 | Logs amb Logcat per a operacions de BD                         | Adrià       | 1              | Media     |
| T5.4 | Actualitzar documentació amb esquema i ús de la BD             | Adrià       | 1              | Media     |

---

## 3. Definition of Done (DoD)

- [ ] Room Database funcional amb Entities, DAOs i TypeConverters
- [ ] CRUD de trips i activitats persistent (reemplaçant inMemory)
- [ ] Hilt configurat com a DI per a Repository i Database
- [ ] Firebase Authentication funcional (login, registre, recuperar contrasenya)
- [ ] Informació d'usuari i trips associats a l'usuari autenticat
- [ ] Log d'accessos (login/logout) persistit a la BD
- [ ] Tests unitaris per a DAOs i interaccions amb BD
- [ ] Logs visibles a Logcat per a operacions de BD i autenticació
- [ ] Documentació actualitzada a /docs amb esquema de BD
- [ ] Release v3.0.0 publicada a GitHub amb vídeo a /docs/evidence/v3.0.0

---

## 4. Riesgos identificados

- Poca experiència amb Room Database i migracions
- Configuració de Firebase pot ser complexa (google-services.json, regles)
- Gestió de l'estat d'autenticació entre pantalles
- Integració de Hilt amb l'arquitectura existent
- Temps limitat per implementar tots els requisits de persistència

---

⚠ Este documento no puede modificarse después del 30% del sprint.  
Fecha límite modificación: 12/04/2026