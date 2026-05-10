# Sprint 04 – Planning Document

## 1. Sprint Goal
1. Integrate Retrofit to connect with the hotel reservation REST API.
2. Implement hotel search, booking and reservation management screens.
3. Save reservation info locally in the database as new trips.
4. Create an image gallery feature per trip with local storage.
5. List and cancel reservations with associated hotel/room images.
6. Create release v4.0.0 in GitHub.

---

## 2. Sprint Backlog

| ID   | Tarea                                                           | Responsable | Estimació (h) | Prioritat |
|------|-----------------------------------------------------------------|-------------|----------------|-----------|
| T1.1 | Afegir dependència Retrofit i configurar client HTTP            | Adrià       | 2              | Alta      |
| T1.2 | Crear models de dades i interfícies API (MVVM)                  | Adrià       | 3              | Alta      |
| T1.3 | Crear capa repository per abstraure l'ús de l'API               | Adrià       | 2              | Alta      |
| T1.4 | Tests unitaris amb mock de la connexió remota                   | Adrià       | 3              | Alta      |
| T2.1 | Pantalla de cerca d'hotels (London, Paris, Barcelona)           | Adrià       | 3              | Alta      |
| T2.2 | Mostrar dades d'hotels i habitacions de l'API                   | Adrià       | 3              | Alta      |
| T2.3 | Reservar habitació i guardar info localment com a trip           | Adrià       | 4              | Alta      |
| T2.4 | Mostrar imatges d'hotels i habitacions a la reserva             | Adrià       | 2              | Alta      |
| T3.1 | Permetre adjuntar múltiples imatges a un trip                   | Adrià       | 3              | Alta      |
| T3.2 | Guardar imatges localment (BD o storage)                        | Adrià       | 3              | Alta      |
| T3.3 | Mostrar galeria d'imatges al detall del trip                    | Adrià       | 2              | Alta      |
| T4.1 | Pantalla per llistar totes les reserves locals                  | Adrià       | 2              | Alta      |
| T4.2 | Eliminar reserves localment i via API                           | Adrià       | 2              | Alta      |
| T4.3 | Mostrar imatges d'hotel i habitació a la llista de reserves     | Adrià       | 2              | Media     |
| T4.4 | Actualitzar "My Trips" per indicar si té reserva d'hotel        | Adrià       | 2              | Media     |

---

## 3. Definition of Done (DoD)

- [ ] Retrofit configurat amb client HTTP i Hilt DI
- [ ] Cerca d'hotels funcional per London, Paris i Barcelona amb DatePickers
- [ ] Reserva d'habitació guardada localment com a trip
- [ ] Imatges d'hotels i habitacions mostrades a les pantalles de cerca i reserva
- [ ] Galeria d'imatges per trip amb emmagatzematge local
- [ ] Llista de reserves amb opció d'eliminar
- [ ] "My Trips" indica si un trip té reserva d'hotel
- [ ] Tests unitaris amb mock de l'API
- [ ] Arquitectura MVVM amb Hilt i Room mantinguda
- [ ] Release v4.0.0 publicada a GitHub amb vídeo a /docs/evidence/v4.0.0

---

## 4. Riesgos identificados

- L'API del professor pot no estar disponible o canviar
- Gestió d'imatges locals pot ser complexa (permisos, emmagatzematge)
- Integrar Retrofit amb l'arquitectura Hilt + Room existent
- Temps limitat per implementar galeria i reserves completes

---

⚠ Este documento no puede modificarse después del 30% del sprint.  
Fecha límite modificación: 10/05/2026