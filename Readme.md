# Letterly

**Letterly** is a modern Android word puzzle game inspired by popular word-guessing games. The project focuses on clean architecture, maintainable code structure, and scalable feature development.

Players must guess a hidden word within a limited number of attempts. After each guess, the game provides visual feedback for every letter, indicating whether the letter is correct, present in the word, or absent.

The project emphasizes **clean game logic separation, testability, and structured UI rendering**.

---

## Game Modes

**Classic Mode**  
5‑letter words with 6 attempts. This is the standard difficulty and provides a balanced gameplay experience.

**Advanced Mode**  
6‑letter words with 7 attempts. A slightly more challenging mode that requires deeper reasoning and vocabulary.

**Expert Mode**  
7‑letter words with 8 attempts. Designed for experienced players who want a more complex word‑guessing challenge.

---

## Features

- Word guessing puzzle gameplay
- Multiple difficulty modes
- Visual feedback for each guessed letter
- Interactive keyboard with dynamic coloring
- Duplicate word detection
- Dictionary validation
- Game win / loss detection
- Clean and responsive UI
- Unit testing for domain logic
- CI pipeline using GitHub Actions

---

## Architecture

The project follows a **Clean Architecture + MVVM** approach to keep the codebase modular and easy to maintain.

The structure separates the application into three primary layers: