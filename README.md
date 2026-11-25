# Pet Haven — Pure Java CLI Pet Shop Platform

A comprehensive, zero-framework, console-based pet shop and adoption platform written entirely in **Pure Java (JDK 21 LTS)**, strictly adhering to the **Chitkara University "Programming in Java" (Course Code: 25CSE0202)** course syllabus.

---

## 📌 Project Overview

- **Language**: Pure Java (JDK 21 LTS)
- **Frameworks**: None (Zero Spring, Zero Hibernate, Zero third-party dependencies)
- **Persistence**: Pure Java File I/O (`BufferedReader`, `BufferedWriter`, CSV streaming with atomic temporary file commit/rollback)
- **Execution**: Single-threaded CLI; adoptions and health scans run synchronously
- **Analytics**: Stream API (`filter`, `map`, `sorted`, `Collectors.groupingBy`, `Collectors.summarizingDouble`)
- **Architecture**: Domain-Driven, Modular Layered Architecture (Model, Repository, Service, CLI)

---

## 📅 9-Day Milestone & Contribution Roadmap

| Day | Milestone / Syllabus Topic | Status |
| :---: | :--- | :---: |
| **Day 1** | **Core OOP Abstractions & Custom Exception Hierarchy** (`Pet` base class, enums, `Adoptable`/`Vaccinable` interfaces, `PetShopException` tree) | **Completed** ✅ |
| **Day 2** | **Concrete Pet Subclasses & Validation** (`Dog`, `Cat`, `Bird`, `Customer`, `Order`, `InputValidator`) | **Completed** ✅ |
| **Day 3** | **Generics & Collections Engine** (`Repository<T, ID>`, `HashMap`, `TreeSet`, `HashSet`, `PriorityQueue`, custom `Comparator`s) | **Completed** ✅ |
| **Day 4** | **Pure Java File I/O Engine** (CSV persistence with `BufferedReader`, `BufferedWriter`, `try-with-resources`) | **Completed** ✅ |
| **Day 5** | **Transaction Engine & Rollback Simulation** (Two-phase commit, atomic file swap, rollback on error) | **Completed** ✅ |
| **Day 6** | **Synchronous service operations** (single-threaded inventory and adoption processing) | **Current** |
| **Day 7** | **Manual health scans** (health checks run when selected from the CLI) | **Current** |
| **Day 8** | **Functional Programming & Stream API Analytics** (`filter`, `map`, `Collectors.groupingBy`, `summarizingDouble`) | **Completed** ✅ |
| **Day 9** | **Interactive CLI Presentation & v1.0.0 Release** (`MenuController`, end-to-end testing, tag release) | **Completed** ✅ |

---

## 🏛️ Syllabus Coverage (25CSE0202)

| Syllabus Unit | Course Concept | Implementation in Project |
| :--- | :--- | :--- |
| **Unit 1: Core OOP Foundations** | Classes/objects, constructors, `this`/`static`, encapsulation, inheritance, polymorphism, abstraction/interfaces, wrapper conversions, exception hierarchy, custom exceptions, propagation. | `Pet` abstract class, `Dog`/`Cat`/`Bird` hierarchies, `Adoptable`/`Vaccinable` contracts, `PetShopException` custom exception tree, defensive input sanitization. |
| **Unit 2: Collections Framework** | `List`, `Set`, `Map`, `Queue`, `Comparable`, `Comparator`, Iterators. | `HashMap` for $O(1)$ ID lookups, `HashSet` for unique microchips, `TreeSet` for sorted catalog views, `PriorityQueue` for waitlists. |
| **Unit 3: Multithreading & Concurrency** | `Thread`, `Runnable`, `synchronized`, `wait()`/`notifyAll()`, `ExecutorService`, `ScheduledExecutorService`, daemon threads. | Not implemented; the application runs synchronously on the main thread. |
| **Unit 4 & 5: File I/O & Transactions** | Byte/Character streams, `BufferedReader`/`BufferedWriter`, `try-with-resources`, transaction atomicity (`commit`/`rollback`). | Pure CSV database engine with atomic temporary file swapping and in-memory rollback. |
| **Unit 5: Lambdas, Streams & Generics** | Generics, Functional Interfaces, Stream pipelines, `Collectors`. | Generic `Repository<T, ID>`, Stream analytics (`groupingBy`, `partitioningBy`, `summarizingDouble`). |
| **Unit 6: Guided Java Project** | End-to-end integration, menu-driven CLI, test coverage, code documentation. | Interactive `MenuController` and automated `--test` runner suite. |

---

## 🚀 Quickstart (Compile & Run)

### Prerequisites
- JDK 21 or higher installed (`java -version`, `javac -version`)

### Compilation
```powershell
# From the project root (PET_SHOP)
javac -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
```

### Run in Interactive CLI Mode
```powershell
java -cp bin com.petshop.Main
```

### Run Automated End-to-End Verification Mode
```powershell
java -cp bin com.petshop.Main --test
```

---

## 📂 Package Architecture

```
PET_SHOP/
├── .gitignore
├── README.md
├── data/
│   ├── pets.csv                               // Active CSV database
│   └── orders.csv                             // Transactional orders log
└── src/
    └── com/petshop/
        ├── Main.java                          // Application launcher & verification runner
        ├── cli/
        │   └── MenuController.java            // Interactive console menus & input routing
        ├── exception/
        │   ├── DuplicatePetException.java      // Conflict error (duplicate ID/microchip)
        │   ├── InsufficientStockException.java // State validation error
        │   ├── InvalidPetDataException.java    // Input validation error
        │   ├── PetNotFoundException.java      // Entity missing error
        │   ├── PetShopException.java          // Base checked domain exception
        │   └── TransactionFailedException.java // Two-phase commit failure error
        ├── model/
        │   ├── Adoptable.java                 // Adoption interface contract
        │   ├── Bird.java                      // Concrete Avian pet
        │   ├── Cat.java                       // Concrete Feline pet
        │   ├── Customer.java                  // Customer domain entity
        │   ├── Dog.java                       // Concrete Canine pet
        │   ├── Order.java                     // Adoption order transaction record
        │   ├── Pet.java                       // Abstract base class (Comparable, Adoptable, Vaccinable)
        │   ├── PetStatus.java                 // Lifecycle state enum
        │   ├── PetType.java                   // Species classification enum
        │   ├── Vaccinable.java                // Healthcare & vaccine interface
        │   └── comparator/
        │       ├── PetAgeComparator.java      // Comparator for age sorting
        │       └── PetPriceComparator.java    // Comparator for price sorting
        ├── repository/
        │   ├── FilePetRepository.java         // CSV reader/writer via BufferedReader/BufferedWriter
        │   ├── InMemoryPetRepository.java     // Multi-collection indexed repository
        │   └── Repository.java                // Generic Repository<T, ID> interface
        ├── service/
        │   ├── AnalyticsService.java          // Stream API analytics & Collectors
        │   ├── PetService.java                // Domain coordinator and adoption processing
        │   └── TransactionCoordinator.java    // Two-phase commit & atomic file swap
        └── util/
            └── InputValidator.java            // Crash-proof Scanner console input parser
```

---

## 👤 Author
- **Ankesh Singh** ([@ankesh2008](https://github.com/ankesh2008))
- Chitkara University Institute of Engineering and Technology
- Course: BE CSE (Artificial Intelligence and Machine Learning)
