# Repository Guidelines

## Project Structure & Module Organization
- `healthy/` contains the Android client. Main code lives in `healthy/app/src/main/java/com/example/myapplication`, with feature UI under `ui/`, networking and repositories under `data/`, and shared widgets under `widget/`. Android resources live in `healthy/app/src/main/res`.
- `api/` contains the Spring Boot backend. Keep HTTP entry points in `controller/`, business logic in `service/`, persistence in `repository/`, and domain types in `entity/`, `dto/`, and `enums/`.
- `api/video/` stores local media used by the API. `docker/docker-compose.yaml` defines the MariaDB instance used for local development.

## Build, Test, and Development Commands
- `cd docker && docker compose up -d`: start MariaDB on port `3319`.
- `cd api && ./mvnw spring-boot:run`: run the backend on `http://localhost:8080`.
- `cd api && ./mvnw test`: run backend JUnit 5 tests.
- `cd healthy && ./gradlew assembleDebug`: build the Android debug APK.
- `cd healthy && ./gradlew test`: run local JVM unit tests.
- `cd healthy && ./gradlew connectedAndroidTest lint`: run device tests and Android lint.

## Coding Style & Naming Conventions
- Use 4-space indentation in Kotlin, Java, XML, and Gradle Kotlin DSL files.
- Follow existing package-by-feature organization; add new Android screens under the nearest `ui/<feature>/` package instead of creating broad utility buckets.
- Use `PascalCase` for classes, `lowerCamelCase` for methods and fields, and `snake_case` for Android resource files such as `activity_login.xml` or `item_course_list.xml`.
- No dedicated formatter config is checked in, so use Android Studio or IntelliJ default formatting and avoid unrelated reformatting.

## Testing Guidelines
- Backend tests live in `api/src/test/java` and use Spring Boot test support, JUnit 5, and Mockito.
- Android unit tests live in `healthy/app/src/test`; instrumentation tests live in `healthy/app/src/androidTest`.
- Name tests after the subject under test with a `*Test` suffix. Add or update tests for each service, controller, repository, or view-model change.

## Commit & Pull Request Guidelines
- Recent history uses short, task-focused subjects, often in Chinese, sometimes prefixed with an issue or MR marker such as `!31 修正登录页面`.
- Keep commits scoped to one logical change and write the subject as a brief summary.
- PRs should list affected modules (`api`, `healthy`, `docker`), manual test steps, linked issues, and screenshots for UI changes.

## Configuration & Security Tips
- `api/src/main/resources/application.yml` contains local datasource defaults, profile settings, emulator URLs (`10.0.2.2`), and an API key. Move secrets to environment-specific overrides and do not commit personal credentials.
