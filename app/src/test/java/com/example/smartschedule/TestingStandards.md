# Testing Standards & Guidelines - SmartSchedule Project

## 📋 General Principles

### 1. Test Structure - AAA Pattern
Every test should follow this structure:
```kotlin
@Test
fun `when [condition], should [expected_result]`() {
    // Arrange - Setup test data
    val input = TestDataFactory.createSomething()
    
    // Act - Execute the action
    val result = objectUnderTest.method(input)
    
    // Assert - Verify the result
    assertThat(result).isTrue()
}
```

### 2. Test Naming Convention
- **Format:** `when [condition], should [expected_result]`
- **Language:** English in backticks for test names
- **Examples:**
    - `when user is ADMIN, should be able to create managers`
    - `when shift starts after it ends, should return validation error`

### 3. Assertions - Use Truth Library
**✅ Recommended:**
```kotlin
assertThat(result).isTrue()
assertThat(list).hasSize(5)
assertThat(user.name).contains("Amit")
assertThat(error).isNotNull()
```

**❌ Avoid:**
```kotlin
assertEquals(true, result)
assertEquals(5, list.size)
assertTrue(result)
```

## 🏗️ Project Structure

```
📁 app/src/test/java/com/example/smartschedule/
├── 📁 domain/
│   ├── 📁 models/          # Model tests (User, Employee, etc.)
│   ├── 📁 validation/      # Validation tests (ShiftValidation, etc.)
│   └── 📁 usecase/         # Use Case tests
├── 📁 data/
│   ├── 📁 mappers/         # Mapper tests
│   └── 📁 repository/      # Repository tests (with Mocking)
├── 📁 presentation/
│   └── 📁 viewmodel/       # ViewModel tests (with Mocking)
└── 📁 testutils/
    ├── TestDataFactory.kt  # Test data factory
    └── TestExtensions.kt   # Useful extensions
```

## 🎯 Types of Tests

### 1. Happy Path Tests
Tests for the main flow when everything works correctly.
```kotlin
@Test
fun `when valid shift is created, should return success`()
```

### 2. Edge Cases
Extreme or unusual scenarios.
```kotlin
@Test
fun `when shift duration is 0 minutes, should return validation error`()
```

### 3. Error Handling
Tests for error scenarios.
```kotlin
@Test
fun `when invalid input provided, should throw IllegalArgumentException`()
```

### 4. Business Logic
Tests for business rules.
```kotlin
@Test
fun `when employee exceeds max shifts per week, should not be assignable`()
```

## 🏭 Test Data Factory Usage

### Creating Objects
```kotlin
// Simple test - use defaults
val user = TestDataFactory.createUser()

// Custom test - with parameters
val admin = TestDataFactory.createUser(
    name = "Admin User",
    type = UserType.ADMIN
)

// Use convenience methods
val morningShift = TestDataFactory.createMorningShift()
```

### Factory Principles
- **Default Values:** Sensible defaults for all parameters
- **Named Parameters:** Use named parameters for clarity
- **Convenience Methods:** Pre-configured objects for common scenarios
- **Realistic Data:** Use realistic test data (not "test123")

## 🔧 Dependencies & Tools

### Required Dependencies (already added)
```kotlin
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0") 
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
testImplementation("com.google.truth:truth:1.1.4")
testImplementation("androidx.arch.core:core-testing:2.2.0")
```

### Common Assertions
- `assertThat(value).isTrue()` / `isFalse()`
- `assertThat(value).isEqualTo(expected)`
- `assertThat(value).isNull()` / `isNotNull()`
- `assertThat(list).hasSize(expected)`
- `assertThat(list).contains(item)`
- `assertThat(string).contains("substring")`

## 📊 Coverage Targets

| Layer | Coverage Target | Priority |
|-------|----------------|----------|
| Domain Layer | 95% | High |
| Use Cases | 85% | High |
| ViewModels | 70% | Medium |
| Repositories | 60% | Medium |

## ❌ Things to Avoid

### 1. Dependent Tests
```kotlin
// ❌ Bad - tests depend on each other
class BadTest {
    companion object {
        var counter = 0
    }
    
    @Test
    fun first_test() {
        counter++
    }
    
    @Test  
    fun second_test() {
        assertThat(counter).isEqualTo(1) // Depends on previous test!
    }
}
```

### 2. Tests That Are Too Long
```kotlin
// ❌ Bad - test checks too many things
@Test
fun `giant_test_that_checks_everything`() {
    // 50 lines of testing...
}

// ✅ Good - small, focused tests
@Test
fun `when user is created, should have valid id`()

@Test
fun `when user is created, should have valid email`()
```

### 3. Magic Numbers/Strings
```kotlin
// ❌ Bad
val shift = createShift(startHour = 8, duration = 480) // What is 480?

// ✅ Good
val STANDARD_SHIFT_DURATION_MINUTES = 480
val shift = createShift(startHour = 8, duration = STANDARD_SHIFT_DURATION_MINUTES)
```

## 🚀 Running Tests

### In Android Studio
- Right-click on file/folder → "Run Tests"
- **Keyboard shortcut:** Ctrl+Shift+F10 (Windows) / Cmd+Shift+R (Mac)

### Command Line
```bash
./gradlew test
./gradlew testDebugUnitTest  # Unit tests only
```

## 📈 Current Progress

### Completed Tests
- ✅ **ShiftValidation:** 7 tests
- ✅ **UserType:** 11 tests
- ✅ **UserStatus:** 12 tests
- ✅ **TestDataFactory:** Ready for use

### Next Steps
- Use Cases testing (LoginUseCase, AddEmployeeUseCase)
- Repository testing with Mocking
- ViewModel testing with StateFlow

---

**Last Updated:** January 28, 2025  
**Version:** 1.0