# Letterly

**Letterly** is a modern Android word puzzle game inspired by popular word-guessing games. The project focuses on clean architecture, maintainable code structure, and scalable feature development.

Players must guess a hidden word within a limited number of attempts. After each guess, the game provides visual feedback for every letter, indicating whether the letter is correct, present in the word, or absent.

---

## Game Modes

**Classic Mode**
5-letter words with 6 attempts. The standard difficulty for a balanced gameplay experience.

**Advanced Mode**
6-letter words with 7 attempts. A more challenging mode requiring deeper vocabulary.

**Expert Mode**
7-letter words with 8 attempts. Designed for experienced players who want a more complex challenge.

---

## Features

- Word guessing puzzle gameplay across three difficulty modes
- Visual feedback per letter — correct, present, or absent
- Interactive keyboard with dynamic coloring
- Dictionary validation and duplicate guess detection
- AI-powered hints via Groq API — contextual clues without revealing the answer
- Local word database using Room
- Unit testing for domain logic
- CI pipeline using GitHub Actions

---

## Architecture

The project follows **Clean Architecture + MVVM** with three distinct layers:

```
app/
├── data/           # Room database, Retrofit (Groq API), repository implementations
├── domain/         # Models, repository interfaces, use cases
└── presentation/   # Activity, Fragments, ViewModels, UI state
```

### Layer Responsibilities

| Layer | Responsibility |
|---|---|
| **Domain** | Pure Kotlin. Defines game models (`GameBoard`, `LetterTile`, `LetterState`, `GuessResult`), repository interfaces, and all use cases. No Android dependencies. |
| **Data** | Implements `WordRepository` and `HintRepository`. Owns the Room database, `GroqApiService` for AI hint fetching, and DTOs with mapping logic. |
| **Presentation** | `GameViewModel` drives all game logic via `GameState` and `HintState`. `GameFragment` observes state and dispatches `GameEvent`. Single activity shell. |
| **DI** | Hilt modules (`DatabaseModule`, `NetworkModule`, `RepositoryModule`) wire all dependencies. |

### Key Patterns

- **Use Case Layer** — Each piece of game logic is a focused use case: `EvaluateGuessUseCase`, `CheckGameStatusUseCase`, `GetHintUseCase`, `CheckWordExistsUseCase`, and more.
- **MVVM + StateFlow** — `GameViewModel` exposes `GameState` and `HintState` as `StateFlow`. The fragment reacts to state changes via `repeatOnLifecycle`.
- **Dependency Injection** — Hilt handles all wiring. No singletons or service locators.
- **Single Activity** — `MainActivity` hosts `StartFragment` and `GameFragment` via Navigation Component.

---

## AI Hints

Hints are powered by the **Groq API** using a chat completion endpoint. When a player requests a hint, `GetHintUseCase` sends the target word and previously shown hints to Groq, which returns a contextual clue without revealing the answer directly. Each hint builds on the previous ones to progressively guide the player.

The Groq API key is injected securely via `local.properties`.

---

## Tech Stack

| Technology | Usage |
|---|---|
| Kotlin | Primary language |
| Android Views (XML) | UI layout |
| Coroutines + Flow | Async operations and reactive state |
| Hilt | Dependency injection |
| Room | Local word database |
| Retrofit + Gson | Groq API networking |
| Navigation Component | Fragment navigation |
| View Binding | Type-safe view access |

---

## Configuration

1. Open `local.properties`
2. Add your [Groq API](https://console.groq.com) key:
   ```
   GROQ_API_KEY=your_api_key_here
   ```
3. Build and run.

`local.properties` is excluded from version control via `.gitignore`.
