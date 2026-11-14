# Detective Mystery Game (Java OOP)

Explore a mansion, collect clues, interrogate suspects and find who killed Lord Blackwood.

## Versions
- **Swing GUI (recommended):** `java GameWindow` - animated scenes for 7 rooms, 6 character portraits, clue panel, typewriter text
- **Terminal:** `java Main` - colored text version

## OOP Concepts Used
- Abstraction: `Suspect` (abstract class)
- Inheritance: `Witness`, `Culprit` extend `Suspect`
- Interface: `Interrogatable`
- Polymorphism: `interrogate()` behaves differently per suspect
- Encapsulation: private fields with getters
- Collections: `ArrayList`, `HashMap`
- Composition: `Room` has `Clue`s and a `Suspect`

## How to Run
```
javac *.java
java GameWindow
```
Requires Java 14 or newer.
