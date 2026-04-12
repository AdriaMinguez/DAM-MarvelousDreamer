# Contributing to Marvelous Dreamer

## Branching Strategy

All development is done in **feature branches** created from `main`. Each branch corresponds to a task or group of related tasks from the sprint backlog.

### Branch naming

```
feature/<task-id>-<short-description>
```

Examples:
- `feature/t1.1-trip-crud`
- `feature/t1.3-validation-datepickers`
- `feature/t1.4-t1.5-preferences-i18n`
- `feature/t2-itinerary-flow`
- `feature/t3-tests-logs`
- `feature/dark-light-theme`

### Workflow

1. Create the branch from `main`:
   ```bash
   git checkout main
   git pull
   git checkout -b feature/<branch-name>
   ```

2. Add the relevant files, commit and push:
   ```bash
   git add <files>
   git commit -m "Task TX.X: short description"
   git push origin feature/<branch-name>
   ```

3. Merge back to `main`:
   ```bash
   git checkout main
   git merge feature/<branch-name>
   git push
   ```

4. Repeat for each task block.

---

## Commit Messages

Format:
```
Task TX.X: short description of the change
```

Examples:
- `Task T1.1/T1.2: CRUD trips and activities MVVM`
- `Task T1.3: DatePickers and date validation`
- `Task T1.4/T1.5: SharedPreferences and multi-language`
- `Task T2: itinerary flow and dynamic navigation`
- `Task T3: unit tests and build config`

For non-task commits:
- `docs: final_sprint02, video evidence`
- `chore: add project gradle files`

---

## Releases

Each sprint produces a release tagged as `vX.0.0`:

- Sprint 01 → `v1.0.0`
- Sprint 02 → `v2.0.0`
- Sprint 03 → `v3.0.0`

Releases are created through GitHub with a description summarizing the implemented tasks.
