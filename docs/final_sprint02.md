# Sprint 02 – Execution & Review

## 1. Resultados obtenidos

El Sprint 02 se ha completado satisfactoriamente. Se han implementado las operaciones CRUD inMemory para trips y actividades siguiendo el patrón MVVM, validación de datos con DatePickers, persistencia de preferencias con SharedPreferences, multi-idioma (en/ca/es), dark/light mode y 65 unit tests. Se ha aplicado la branching strategy con 6 feature branches que faltó en el Sprint 01.

---

## 2. Tareas completadas

| ID   | Completada | Comentarios |
|------|------------|-------------|
| T1.1 | ✅ | CRUD trips inMemory con MVVM |
| T1.2 | ✅ | CRUD activities inMemory con MVVM |
| T1.3 | ✅ | DatePickers obligatorios, validación de fechas |
| T1.4 | ✅ | SharedPreferences: username, dob, darkMode, language |
| T1.5 | ✅ | Multi-idioma funcional (en, ca, es) |
| T2.1 | ✅ | Flujo: Menu → Travel → Itinerary (CRUD) |
| T2.2 | ✅ | UI flow para trips y actividades |
| T2.3 | ✅ | Actualización dinámica con StateFlow |
| T3.1 | ✅ | Validación con mensajes de error visibles en la UI |
| T3.2 | ✅ | 65 unit tests (dominio, DataSource, Repository, ViewModel) |
| T3.3 | ✅ | Logs en Logcat para CRUD y errores |
| T3.4 | ✅ | Documentación actualizada en /docs |
| T3.5 | ✅ | Logs con niveles DEBUG, INFO, ERROR |

---

## 3. Desviaciones

**Ninguna desviación significativa.** Todas las tareas planificadas en el plan_sprint02.md se han completado. Se ha aplicado la branching strategy con 6 feature branches mergeadas a main de forma incremental, corrigiendo la desviación del Sprint 01.

---

## 4. Retrospectiva

### Qué funcionó bien
- La branching strategy se aplicó correctamente con commits por funcionalidad
- La arquitectura MVVM se respeta en todas las capas
- Los 65 unit tests cubren todas las funcionalidades principales
- Las 3 traducciones están completas y consistentes en formato

### Qué no funcionó
- El sistema de colores requirió refactorización al implementar light mode
- El cambio de idioma necesita `recreate()`, que reinicia la Activity

### Qué mejoraremos en el próximo sprint
- Mejor planificación del tiempo para evitar refactorizaciones tardías
- Empezar los tests antes en el proceso de desarrollo
- Investigar las tecnologías nuevas (Room, AppCompat) antes de implementar

---

## 5. Autoevaluación (0-10)

**Nota: 8**

Todas las tareas del sprint se han completado según los requisitos. La arquitectura MVVM se respeta, las validaciones funcionan con feedback visible, los tests cubren las funcionalidades principales y se ha aplicado la branching strategy.