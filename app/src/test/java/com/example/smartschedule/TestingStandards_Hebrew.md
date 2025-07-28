# מדריך בדיקות וסטנדרטים - פרויקט SmartSchedule

## 📋 עקרונות כלליים

### 1. מבנה בדיקה - דפוס AAA
כל בדיקה צריכה לעקוב אחר המבנה הזה:
```kotlin
@Test
fun `when [תנאי], should [תוצאה צפויה]`() {
    // Arrange - הכנת נתוני בדיקה
    val input = TestDataFactory.createSomething()
    
    // Act - ביצוע הפעולה הנבדקת
    val result = objectUnderTest.method(input)
    
    // Assert - בדיקת התוצאה
    assertThat(result).isTrue()
}
```

### 2. מוסכמות שמות בדיקות
- **פורמט:** `when [תנאי], should [תוצאה צפויה]`
- **שפה:** אנגלית בתוך גרשיים אחוריים עבור שמות בדיקות
- **דוגמאות:**
    - `when user is ADMIN, should be able to create managers`
    - `when shift starts after it ends, should return validation error`

### 3. Assertions - שימוש בספריית Truth
**✅ מומלץ:**
```kotlin
assertThat(result).isTrue()
assertThat(list).hasSize(5)
assertThat(user.name).contains("עמית")
assertThat(error).isNotNull()
```

**❌ להימנע:**
```kotlin
assertEquals(true, result)
assertEquals(5, list.size)
assertTrue(result)
```

## 🏗️ מבנה הפרויקט

```
📁 app/src/test/java/com/example/smartschedule/
├── 📁 domain/
│   ├── 📁 models/          # בדיקות מודלים (User, Employee וכו')
│   ├── 📁 validation/      # בדיקות validation (ShiftValidation וכו')
│   └── 📁 usecase/         # בדיקות Use Cases
├── 📁 data/
│   ├── 📁 mappers/         # בדיקות Mappers
│   └── 📁 repository/      # בדיקות Repository (עם Mocking)
├── 📁 presentation/
│   └── 📁 viewmodel/       # בדיקות ViewModel (עם Mocking)
└── 📁 testutils/
    ├── TestDataFactory.kt  # Factory לנתוני בדיקה
    └── TestExtensions.kt   # Extensions שימושיים
```

## 🎯 סוגי בדיקות

### 1. בדיקות המסלול הראשי (Happy Path)
בדיקות של הזרימה הרגילה כשהכל עובד כמו שצריך.
```kotlin
@Test
fun `when valid shift is created, should return success`()
```

### 2. מקרי קצה (Edge Cases)
תרחישים קיצוניים או לא שגרתיים.
```kotlin
@Test
fun `when shift duration is 0 minutes, should return validation error`()
```

### 3. טיפול בשגיאות
בדיקות של תרחישי שגיאה.
```kotlin
@Test
fun `when invalid input provided, should throw IllegalArgumentException`()
```

### 4. לוגיקה עסקית
בדיקות של חוקים עסקיים.
```kotlin
@Test
fun `when employee exceeds max shifts per week, should not be assignable`()
```

## 🏭 שימוש ב-Test Data Factory

### יצירת אובייקטים
```kotlin
// בדיקה פשוטה - ברירות מחדל
val user = TestDataFactory.createUser()

// בדיקה מותאמת - עם פרמטרים
val admin = TestDataFactory.createUser(
    name = "משתמש מנהל",
    type = UserType.ADMIN
)

// שימוש בשיטות נוחות
val morningShift = TestDataFactory.createMorningShift()
```

### עקרונות Factory
- **ערכי ברירת מחדל:** ערכים הגיוניים לכל הפרמטרים
- **פרמטרים עם שמות:** שימוש בפרמטרים עם שמות לבהירות
- **שיטות נוחות:** אובייקטים מוגדרים מראש לתרחישים נפוצים
- **נתונים ריאליסטיים:** שימוש בנתוני בדיקה ריאליסטיים (לא "test123")

## 🔧 תלויות וכלים

### תלויות נדרשות (כבר הוספו)
```kotlin
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0") 
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
testImplementation("com.google.truth:truth:1.1.4")
testImplementation("androidx.arch.core:core-testing:2.2.0")
```

### Assertions נפוצים
- `assertThat(value).isTrue()` / `isFalse()`
- `assertThat(value).isEqualTo(expected)`
- `assertThat(value).isNull()` / `isNotNull()`
- `assertThat(list).hasSize(expected)`
- `assertThat(list).contains(item)`
- `assertThat(string).contains("substring")`

## 📊 יעדי כיסוי

| שכבה | יעד כיסוי | עדיפות |
|------|-----------|---------|
| שכבת Domain | 95% | גבוהה |
| Use Cases | 85% | גבוהה |
| ViewModels | 70% | בינונית |
| Repositories | 60% | בינונית |

## ❌ דברים להימנע מהם

### 1. בדיקות תלויות זו בזו
```kotlin
// ❌ רע - בדיקות תלויות זו בזו
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
        assertThat(counter).isEqualTo(1) // תלוי בבדיקה הקודמת!
    }
}
```

### 2. בדיקות ארוכות מדי
```kotlin
// ❌ רע - בדיקה שבודקת יותר מדי דברים
@Test
fun `giant_test_that_checks_everything`() {
    // 50 שורות של בדיקות...
}

// ✅ טוב - בדיקות קטנות וממוקדות
@Test
fun `when user is created, should have valid id`()

@Test
fun `when user is created, should have valid email`()
```

### 3. מספרים/מחרוזות קסומים
```kotlin
// ❌ רע
val shift = createShift(startHour = 8, duration = 480) // מה זה 480?

// ✅ טוב
val STANDARD_SHIFT_DURATION_MINUTES = 480
val shift = createShift(startHour = 8, duration = STANDARD_SHIFT_DURATION_MINUTES)
```

## 🚀 הרצת בדיקות

### ב-Android Studio
- לחץ ימני על קובץ/תיקייה → "Run Tests"
- **קיצור מקלדת:** Ctrl+Shift+F10 (Windows) / Cmd+Shift+R (Mac)

### שורת פקודה
```bash
./gradlew test
./gradlew testDebugUnitTest  # בדיקות יחידה בלבד
```

## 📈 התקדמות נוכחית

### בדיקות שהושלמו
- ✅ **ShiftValidation:** 7 בדיקות
- ✅ **UserType:** 11 בדיקות
- ✅ **UserStatus:** 12 בדיקות
- ✅ **TestDataFactory:** מוכן לשימוש

### השלבים הבאים
- בדיקות Use Cases (LoginUseCase, AddEmployeeUseCase)
- בדיקות Repository עם Mocking
- בדיקות ViewModel עם StateFlow

## 💡 טיפים חשובים

### 1. כתיבת בדיקה טובה
- **ממוקדת:** בדיקה אחת לפונקציונליות אחת
- **עצמאית:** לא תלויה בבדיקות אחרות
- **חוזרת:** תמיד תיתן אותה תוצאה
- **מהירה:** פחות משנייה

### 2. מתי לכתוב בדיקות
- **לפני כתיבת קוד חדש** (TDD - Test Driven Development)
- **אחרי כתיבת קוד חדש** (Traditional approach)
- **כשמוצאים באג** - כתוב בדיקה שתופס אותו

### 3. מה לבדוק
- **Happy Path:** התרחיש הרגיל והצפוי
- **Edge Cases:** מקרי קצה ותרחישים קיצוניים
- **Error Handling:** מה קורה כשמשהו משתבש
- **Business Rules:** חוקים עסקיים חשובים

---

**עודכן לאחרונה:** 28 בינואר 2025  
**גרסה:** 1.0