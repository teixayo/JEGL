---

# JEGL (Java Efficient Game Loop)

<p align="center">
  <img src="https://img.shields.io/github/actions/workflow/status/teixayo/JEGL/test.yml" alt="GitHub Actions Workflow Status">
  <img src="https://img.shields.io/github/v/tag/teixayo/JEGL?label=version&color=blue" alt="Latest Version">
</p>

JEGL (Java Efficient Game Loop) is a lightweight Java library for creating smooth and efficient game loops. It helps manage game updates and timing accurately while using minimal CPU resources. It’s perfect for game development and real-time applications.

## Quick Start

Here’s how to get started with JEGL and use different loop types for various needs.

**Gradle:**

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.teixayo:JEGL:v1.0'
}
```

**Maven:**

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.teixayo</groupId>
        <artifactId>JEGL</artifactId>
        <version>v1.0</version>
    </dependency>
</dependencies>
```

### Examples

**Minecraft Server (20 TPS):**

```java
Loop loop = LoopBuilder.builder()
    .loopType(LoopType.BUSY_WAIT_LOCK)
    .updatePerSecond(20)
    .useThread()
    .loopApp(new MinecraftServer())
    .build();
loop.start();
```

**Game Application (120 FPS):**

```java
Loop loop = LoopBuilder.builder()
    .loopType(LoopType.LOCK)
    .updatePerSecond(120)
    .useThread()
    .loopApp(new Game())
    .build();
loop.start();
```

### Note

To fix issues with Windows sleep accuracy, make sure to call `DaemonThread.active()` before starting the loop.

## Loop Types

JEGL provides several types of loops to fit different needs:

- **`BusyWaitLockLoop`**: Combines busy-waiting with locking to provide very accurate timing for updates while managing CPU usage effectively. It’s best for situations where accuracy is crucial.
- **`LockLoop`**: Uses `LockSupport.parkNanos()` to efficiently sleep between updates. This type offers the best CPU usage while still maintaining good update accuracy.
- **`BusyWaitLoop`**: Continuously checks the update time, which uses a lot of CPU and can cause high CPU usage. It’s not recommended for most cases due to its inefficient CPU usage.
- **`BusyWaitYield`**: Similar to `BusyWaitLoop`, but uses `Thread.yield()` to hint to the system to reduce CPU usage. It’s a bit more efficient but still not ideal for CPU usage.

Avoid using `BusyWaitLoop` and `BusyWaitYield` due to their high CPU usage. Instead, consider `LockLoop` for the best CPU usage or `BusyWaitLockLoop` if timing accuracy is more critical.

## Loop Stats

JEGL includes a `LoopStats` class to give you real-time information about your game loop’s performance. You can access these stats through `loop.getLoopStats()`.

### Explanation

- **`currentMillisPerUpdate`**: Elapsed time in milliseconds for each update.
- **`currentUpdatePerSecond`**: Current updates per second.
- **`updates`**: Number of updates that have occurred.

---
