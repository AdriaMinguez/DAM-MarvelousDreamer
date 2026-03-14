# Sprint 02 – Planning Document

## 1. Sprint Goal
1. Implement trip management logic – CRUD operations for trips and itinerary items.
2. Develop the travel itinerary/activities flow – Define how users can add, update, and delete travel activities.
3. Ensure user interactions are functional – Basic UI interactions without focusing on design.
4. Validate input and enforce constraints – Prevent incorrect or incomplete travel plans.
5. Document and test the logic – Ensure correctness before moving to the next sprint
6. Create release v2.0.0 in GitHub.

---

## 2. Sprint Backlog

| ID   | Tarea                                                      | Responsable | Estimació (h) | Prioritat |
|------|------------------------------------------------------------|-------------|----------------|-----------|
| T1.1 | CRUD inMemory per a trips (add, edit, delete)              | Adrià       | 4              | Alta      |
| T1.2 | CRUD inMemory per a activitats (add, update, delete)       | Adrià       | 4              | Alta      |
| T1.3 | Validació de dades (dates, camps obligatoris, DatePicker)  | Adrià       | 3              | Alta      |
| T1.4 | Persistència de preferències amb SharedPreferences         | Adrià       | 2              | Alta      |
| T1.5 | Implementar multi-idioma (en, ca, es)                      | Adrià       | 2              | Alta      |
| T2.1 | Estructurar flux d'itinerari (Menu → Trip → Itinerary)     | Adrià       | 2              | Alta      |
| T2.2 | UI flow per afegir i modificar trips i activitats          | Adrià       | 4              | Alta      |
| T2.3 | Actualització dinàmica de llistes (trips i itinerari)      | Adrià       | 2              | Alta      |
| T3.1 | Validació d'inputs en ViewModel i UI amb missatges d'error | Adrià       | 2              | Alta      |
| T3.2 | Unit tests per a CRUD de trips i activitats                | Adrià       | 3              | Alta      |
| T3.3 | Simulació d'interaccions i log d'errors                    | Adrià       | 1              | Media     |
| T3.4 | Actualitzar documentació amb resultats de tests            | Adrià       | 1              | Media     |
| T3.5 | Afegir logs (Logcat) amb bones pràctiques                  | Adrià       | 1              | Media     |

---

## 3. Definition of Done (DoD)

- [ ] CRUD de trips funcional en memòria seguint MVVM
- [ ] CRUD d'activitats funcional en memòria seguint MVVM
- [ ] DatePicker implementat en tots els camps de data
- [ ] Validació de dates (start < end, activitats dins del rang del trip)
- [ ] Missatges d'error visibles a la UI per a inputs incorrectes
- [ ] Preferències d'usuari persistides amb SharedPreferences
- [ ] Multi-idioma funcional (en, ca, es)
- [ ] Tests unitaris escrits i documentats
- [ ] Logs visibles a Logcat per a operacions CRUD i errors
- [ ] Documentació actualitzada a /docs
- [ ] Release v2.0.0 publicada a GitHub
- [ ] Vídeo de demostració gravat i pujat a /docs/evidence/v2.0.0

---

## 4. Riesgos identificados

- Poca experiència amb ViewModel i StateFlow
- Implementació de DatePicker en Compose pot ser complexa
- Gestió correcta de l'estat entre pantalles
- Temps limitat per implementar tests unitaris
- Integració de SharedPreferences amb el sistema de preferències existent

---

⚠ Este documento no puede modificarse después del 30% del sprint.  
Fecha límite modificación: 15/03/2026