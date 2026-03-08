# Sprint 01 – Execution & Review

## 1. Resultados obtenidos

El Sprint 01 se ha completado satisfactoriamente en cuanto a funcionalidad. Se han implementado todas las pantallas requeridas con datos mockeados, navegación completa entre screens, modelo de dominio, y la documentación del proyecto. La app arranca, navega y se ve tal como estaba planificado en los mockups.

---

## 2. Tareas completadas

| ID    | Completada     | Comentarios |
|-------|----------------|-------------|
| T1.1  | ✅              | Nombre original: Marvelous Dreamer |
| T1.2  | ✅              | Logo generado con IA |
| T1.3  | ✅              | minSdk 24, targetSdk 36 |
| T1.4  | ✅              | Kotlin 2.0.0 |
| T1.5  | ✅              | Proyecto inicializado en Android Studio |
| T2.1  | ✅              | Repositorio público en GitHub |
| T2.2  | ✅              | Git inicializado localmente |
| T2.3  | ✅              | LICENSE añadida |
| T2.4  | ⚠️             | CONTRIBUTING.md añadido, branching strategy básica |
| T2.5  | ✅              | README.md con descripción del proyecto |
| T2.6  | ✅              | Carpeta /docs creada |
| T2.7  | ✅              | design.md con arquitectura y diagrama de clases |
| T2.8  | ⚠️             | Trabajo realizado en main, sin feature branches |
| T2.9  | ✅              | Commits con mensajes descriptivos |
| T2.10 | ✅              | Release v1.0.0 publicada en GitHub |
| T3.1  | ✅              | 8 pantallas implementadas con datos mockeados |
| T3.2  | ✅              | Navegación completa con Jetpack Navigation Compose |
| T3.3  | ✅              | Diagrama de clases documentado en design.md |
| T3.4  | ✅              | Data model classes implementadas con @TODO |
| T4.1  | ✅              | Splash Screen con logo y animación |
| T4.2  | ✅              | About Page con info del desarrollador |
| T4.3  | ✅              | Terms & Conditions con Accept/Decline |
| T4.4  | ✅              | Preferences Screen (UI mock) |

---

## 3. Desviaciones

**Branching strategy no aplicada.** Durante el desarrollo se trabajó directamente en `main` en lugar de crear ramas por funcionalidad. La razón principal fue que, al estar aprendiendo Jetpack Compose y la estructura de Android, el código no era estable hasta fases avanzadas del sprint. Se optó por no subir código roto y hacer un único push final una vez todo compilaba y funcionaba correctamente. Aunque el resultado es funcional, el proceso no siguió las buenas prácticas de control de versiones.

---

## 4. Retrospectiva

### Qué funcionó bien
- Todas las pantallas requeridas están implementadas y son navegables
- El diseño visual es consistente gracias al sistema de colores y tema centralizado
- La documentación (design.md, color-palette.md, domain model) está completa y bien estructurada

### Qué no funcionó
- No se usaron feature branches durante el desarrollo
- Los commits se acumularon al final en lugar de hacerse de forma incremental
- El desconocimiento inicial de Jetpack Compose ralentizó el proceso de desarrollo

### Qué mejoraremos en el próximo sprint
- Crear una rama por funcionalidad y hacer commits frecuentes desde el inicio
- Mejor gestión del tiempo para no acumular trabajo al final del sprint
- Aplicar la branching strategy definida en CONTRIBUTING.md desde el primer día

---

## 5. Autoevaluación (0-10)

**Nota: 7**

Todas las tareas funcionales están completadas y la app cumple con los requisitos del sprint. La penalización viene por no haber seguido las buenas prácticas de control de versiones (branches, commits incrementales), que son parte importante de la evaluación. El resultado final es sólido pero el proceso de desarrollo mejorable.