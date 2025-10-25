# Java 21

## 1. Overview
- Java 21 was released on **19 September 2023** as the latest Long-Term Support (LTS) version of Java. :contentReference[oaicite:1]{index=1}
- Being an LTS release means it will receive extended support (in Oracle’s case at least 8 years) so it’s a good target for production systems. :contentReference[oaicite:2]{index=2}
- It continues Java’s six-month release cadence for non‐LTS versions, but the LTS versions represent stable baselines. :contentReference[oaicite:3]{index=3}

## 2. Why upgrade to Java 21?
Upgrading brings:
- New language features and cleaner syntax → better readability, less boilerplate.
- Enhanced APIs and libraries → more tools at your disposal.
- Performance and concurrency improvements → better scalability and responsiveness.
- Long-term support stability → fewer disruptions in production.

## 3. Key Features & Enhancements
Here are some of the standout features introduced or finalized in Java 21:

### 3.1 Language and Syntax Improvements
- **Record Patterns (JEP 440)**: Records now support pattern matching for destructuring, enabling more expressive queries over data. :contentReference[oaicite:4]{index=4}
- **Pattern Matching for switch (JEP 441)**: Switch expressions/statements can now match patterns (not just values) in a more type-safe, concise way. :contentReference[oaicite:5]{index=5}
- **String Templates (Preview, JEP 430)**: A preview feature allowing embedded expressions inside string templates for better readability. :contentReference[oaicite:6]{index=6}
- **Unnamed Patterns & Variables**: To allow ignoring certain variables or to simplify deeply nested pattern matching. (Preview) :contentReference[oaicite:7]{index=7}

### 3.2 Concurrency & Threading
- **Virtual Threads (Project Loom)**: Lightweight threads to enable easier concurrency and high throughput in multi-threaded applications. :contentReference[oaicite:8]{index=8}
- **Structured Concurrency (Preview)**: An API to treat groups of related tasks as a single unit of work, simplifying lifecycle management of threads/tasks. :contentReference[oaicite:9]{index=9}
- **Scoped Values (Preview, JEP 446)**: A safer alternative to thread-locals, enabling immutable data sharing across threads in certain contexts. :contentReference[oaicite:10]{index=10}

### 3.3 Collections & APIs
- **Sequenced Collections (JEP 431)**: New interface types for collections with a defined encounter order (e.g., first, last, preceding, succeeding). :contentReference[oaicite:11]{index=11}
- **Key Encapsulation Mechanism API (JEP 452)**: For enhanced cryptographic support, enabling key‐encapsulation mechanisms (KEM) for asymmetric encryption workflows. :contentReference[oaicite:12]{index=12}
- **Foreign Function & Memory API (Third Preview, JEP 442)**: Improves interoperability with native code and memory outside the JVM, safer than JNI. :contentReference[oaicite:13]{index=13}
- **Vector API (Incubator)**: For high-performance vector computations that map efficiently to hardware instructions. :contentReference[oaicite:14]{index=14}

### 3.4 Performance & Platform
- **Generational ZGC (Garbage Collector)**: ZGC now supports generational collection to improve performance and reduce latency in large heaps. :contentReference[oaicite:15]{index=15}
- **Deprecation of Windows 32-bit x86 Port**: The 32-bit Windows build is marked for removal in future releases, aligning with platform trends. :contentReference[oaicite:16]{index=16}

## 4. Migration Considerations
When migrating to Java 21 from an older LTS (e.g., Java 17), keep in mind:
- Although it’s backward-compatible, always **test thoroughly** your existing code and dependencies. Some internal APIs may have changed or become unsupported. :contentReference[oaicite:17]{index=17}
- For **preview features** (String Templates, Structured Concurrency, etc.), you need to explicitly enable preview mode to use them (via compiler/runtime flags).
- If your project uses frameworks, libraries, or tools that haven’t yet been updated for Java 21, ensure compatibility before switching.
- Review improvements (e.g., virtual threads) and decide if they apply to your use case; you don’t have to adopt all new features immediately.
- Follow vendor guidance for long-term support (LTS) and plan for production adoption accordingly.

## 5. Practical Example Snippets

### 5.1 Pattern Matching for Switch

```java
sealed interface Shape permits Circle, Rectangle {}

record Circle(double radius) implements Shape {}
record Rectangle(double width, double height) implements Shape {}

static double area(Shape s) {
    return switch (s) {
        case Circle(double r) -> Math.PI * r * r;
        case Rectangle(double w, double h) -> w * h;
    };
}
```

### 5.2 Virtual Threads

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<String>> futures = executor.submit(() -> fetchData("url1"));
    // use executor for many lightweight tasks
}
```

### 5.3 Sequenced Collections

```java
SequencedSet<String> seqSet = new LinkedHashSet<>();  
seqSet.add("first");  
seqSet.add("second");
String first = seqSet.first();  
String last = seqSet.last();
```

## 6. Summary

Java 21 is a major LTS release that brings forward significant improvements across language design, concurrency model, collection APIs, performance, and system interoperability. It represents a strong foundation for modern Java applications and offers long-term stability for enterprise usage.
If you’re working on a Java codebase and considering upgrade, Java 21 is a compelling target.