# Pet Haven — Pure Java CLI Pet Shop Platform

A comprehensive, zero-framework, console-based pet shop and adoption platform written entirely in **Pure Java (JDK 21 LTS)**, strictly adhering to the **Chitkara University "Programming in Java" (Course Code: 25CSE0202)** course syllabus.

---

## 📌 Project Overview

- **Language**: Pure Java (JDK 21 LTS)
- **Frameworks**: None (Zero Spring, Zero Hibernate, Zero external dependencies)
- **Persistence**: Pure Java File I/O (`BufferedReader`, `BufferedWriter`, CSV streaming with atomic temporary file commit/rollback)
- **Architecture**: Domain-Driven, Layered Architecture (Model, Repository, Service, Concurrency, CLI)

---

## 📅 9-Day Daily Contribution Roadmap (16 Nov – 24 Nov 2026)

| Day | Date | Milestone / Syllabus Topic | Status |
| :---: | :---: | :--- | :---: |
| **Day 1** | **16 Nov 2026** | **Core OOP Abstractions & Custom Exception Hierarchy** (`Pet` base class, enums, `Adoptable`/`Vaccinable` interfaces, `PetShopException` tree) | **Completed** ✅ |
| **Day 2** | 17 Nov 2026 | **Concrete Pet Subclasses & Validation** (`Dog`, `Cat`, `Bird`, `Customer`, `Order`, `InputValidator`) | ⏳ Upcoming |
| **Day 3** | 18 Nov 2026 | **Generics & Collections Engine** (`Repository<T, ID>`, `HashMap`, `TreeSet`, `HashSet`, `PriorityQueue`, custom `Comparator`s) | ⏳ Upcoming |
| **Day 4** | 19 Nov 2026 | **Pure Java File I/O Engine** (CSV persistence with `BufferedReader`, `BufferedWriter`, `try-with-resources`) | ⏳ Upcoming |
| **Day 5** | 20 Nov 2026 | **Transaction Engine & Rollback Simulation** (Two-phase commit, atomic file swap, rollback on error) | ⏳ Upcoming |
| **Day 6** | 21 Nov 2026 | **Multithreading Part 1: Thread Safety** (`synchronized` inventory locks, bounded `wait()` / `notifyAll()` order queue) | ⏳ Upcoming |
| **Day 7** | 22 Nov 2026 | **Multithreading Part 2: Background Services** (`ScheduledExecutorService` health alerts, worker thread pool) | ⏳ Upcoming |
| **Day 8** | 23 Nov 2026 | **Functional Programming & Stream API Analytics** (`filter`, `map`, `Collectors.groupingBy`, `summarizingDouble`) | ⏳ Upcoming |
| **Day 9** | 24 Nov 2026 | **Interactive CLI Presentation & v1.0.0 Release** (`MenuController`, end-to-end testing, tag release) | ⏳ Upcoming |

---

## 🏛️ Syllabus Coverage (25CSE0202)

| Syllabus Unit | Course Concept | Implementation in Project |
| :--- | :--- | :--- |
| **Unit 1: Core OOP Foundations** | Classes/objects, constructors, `this`/`static`, encapsulation, inheritance, polymorphism, abstraction/interfaces, wrapper conversions, exception hierarchy, custom exceptions, propagation. | `Pet` abstract class, `Dog`/`Cat`/`Bird` hierarchies, `Adoptable`/`Vaccinable` contracts, `PetShopException` custom exception tree, defensive input sanitization. |
| **Unit 2: Collections Framework** | `List`, `Set`, `Map`, `Queue`, `Comparable`, `Comparator`, Iterators. | `HashMap` for $O(1)$ ID lookups, `HashSet` for unique microchips, `TreeSet` for sorted catalog views, `PriorityQueue` for waitlists. |
| **Unit 3: Multithreading & Concurrency** | `Thread`, `Runnable`, `synchronized`, `wait()`/`notifyAll()`, `ExecutorService`, `ScheduledExecutorService`. | Thread-safe inventory locks, bounded Producer-Consumer adoption queue, scheduled background health/feeding alerts. |
| **Unit 4 & 5: File I/O & Transactions** | Byte/Character streams, `BufferedReader`/`BufferedWriter`, `try-with-resources`, transaction atomicity (`commit`/`rollback`). | Pure CSV database engine with atomic temporary file swapping and in-memory rollback. |
| **Unit 5: Lambdas, Streams & Generics** | Generics, Functional Interfaces, Stream pipelines, `Collectors`. | Generic `Repository<T, ID>`, Stream analytics (`groupingBy`, `partitioningBy`, `summarizingDouble`). |

---

## 🚀 Quickstart (Compile & Run)

### Prerequisites
- JDK 21 or higher installed (`java -version`, `javac -version`)

### Compilation & Execution
```bash
# Clone the repository
git clone https://github.com/ankesh2008/PET_SHOP.git
cd PET_SHOP

# Compile all source files into bin directory
javac -d bin src/com/petshop/model/*.java src/com/petshop/exception/*.java src/com/petshop/Main.java

# Run Day 1 Verification Suite
java -cp bin com.petshop.Main
```

---

## 📂 Day 1 File Layout

```
PET_SHOP/
├── .gitignore
├── README.md
└── src/
    └── com/petshop/
        ├── Main.java                          // Verification runner
        ├── model/
        │   ├── Pet.java                       // Abstract base class (implements Comparable, Adoptable, Vaccinable)
        │   ├── PetType.java                   // Enum for species classification
        │   ├── PetStatus.java                 // Enum for lifecycle states
        │   ├── Adoptable.java                 // Interface for adoption operations
        │   └── Vaccinable.java                // Interface for healthcare & immunization tracking
        └── exception/
            ├── PetShopException.java          // Base checked exception with error codes
            ├── PetNotFoundException.java      // 404-style entity not found exception
            ├── DuplicatePetException.java      // Conflict exception (duplicate ID/microchip)
            ├── InsufficientStockException.java // State validation exception
            └── InvalidPetDataException.java    // Defensive input validation exception
```

---

## 👤 Author
- **Ankesh Singh** ([@ankesh2008](https://github.com/ankesh2008))
- Chitkara University Institute of Engineering and Technology
