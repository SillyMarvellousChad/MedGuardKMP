![logo1](https://github.com/user-attachments/assets/113bccc8-de62-498e-bcb0-3aa8777e3a23)

# MedGuard AI

Medication safety is often overlooked, yet dangerous drug interactions are one of the most common preventable healthcare risks. **MedGuard AI** is a cross-platform mobile application designed to help users identify **potentially harmful medication combinations** in a clear, accessible, and reliable way.

Inspired by real-world medication safety challenges, MedGuard AI combines a **deterministic rule-based risk engine** with **optional AI-powered explanations** to ensure the app remains functional even when AI services are unavailable. The focus is on **patient safety, accessibility, and trust**, rather than blind dependence on AI.

Built using **Kotlin Multiplatform and Compose Multiplatform**, MedGuard AI runs seamlessly on **Android and iOS**, demonstrating how healthcare-focused tools can be built responsibly across platforms without sacrificing reliability.



## Key Features

**Medication Interaction Analysis**
  Detects potentially dangerous combinations such as:

  * Anticoagulants + NSAIDs
  * Opioids + Benzodiazepines / Alcohol
  * Polypharmacy risks
  * Known high-risk drug pairings

* **Rule-Based Risk Engine (Always Works)**
  A deterministic safety engine classifies interactions into:

  * 🟢 Low Risk
  * 🟡 Moderate Risk
  * 🔴 High Risk

* 🤖 **Optional AI Insights**
  AI explanations enhance understanding when available — but the app **never breaks** if AI fails.

* 📊 **Visual Risk Graphs**
  Simple green / yellow / red bar graphs to help users instantly understand risk severity.

* 📷 **Text-Based Medication Scanner**
  Allows users to scan medicine labels or prescriptions and auto-populate medication names.

* ♿ **Accessibility Mode**
  Larger text, high-contrast UI, and simplified layouts for elderly users and patients with visual impairments.

* ⚠️ **Medical Disclaimer & Safe Design**
  Clear disclaimers ensure users understand that MedGuard AI is an informational tool, not a replacement for professional medical advice.

---

## 🎥 Demo Video and Screenshots

👉 **[https://drive.google.com/file/d/1PDhA0t0rWj2uandyjBn5EI9AS4mYo8vD/view?usp=drive_link]**

<img width="200" alt="image" src="https://github.com/user-attachments/assets/12f18219-529e-430b-bdbc-e7be92c64687" />
<img width="200" alt="image" src="https://github.com/user-attachments/assets/2cd5d27d-e35f-4e55-aa40-71b672a0cbb8" />
<img width="200" alt="image" src="https://github.com/user-attachments/assets/c6ea885f-e58c-43a2-8b67-c4ea6c558bf7" />
<img width="200" alt="image" src="https://github.com/user-attachments/assets/34f2ccea-95ff-46e1-87f8-71dcdd771e68" />
<img width="200" alt="image" src="https://github.com/user-attachments/assets/47ee3639-2559-4953-8355-5e861a184ddb" />


---
## 🎥 Essay Link 
https://docs.google.com/document/d/1bjCUUsWPaM45VbkSBar5IPswqf98tc4ziuWEf5uercI/edit?usp=sharing
---
## Project Structure

This is a **Kotlin Multiplatform (KMP)** project targeting **Android and iOS**.

```
MedGuardKMP/
├── composeApp/
│   ├── commonMain/      # Shared UI, logic, and domain code
│   ├── androidMain/     # Android-specific implementations
│   └── iosMain/         # iOS-specific implementations
│
├── iosApp/              # iOS application entry point
└── README.md
```

* `commonMain` contains all shared Compose UI, risk engine logic, and models.
* Platform-specific code is isolated cleanly to avoid duplication.

---

## How to Run the Project

### Prerequisites

* **JDK 17 or higher**
* **Android Studio (latest stable)**
* **Xcode (for iOS builds)**
* **Kotlin Multiplatform plugin**
* **Compose Multiplatform plugin**

Follow the official setup guide:
👉 [https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-create-first-app.html](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-create-first-app.html)

---

### ▶️ Build & Run — Android

**macOS / Linux**

```bash
./gradlew :composeApp:assembleDebug
```

**Windows**

```bash
.\gradlew.bat :composeApp:assembleDebug
```

Or simply use the **Run** button in Android Studio.

---

### ▶️ Build & Run — iOS

1. Open the `iosApp` folder in **Xcode**
2. Select a simulator or device
3. Click **Run**

---

## 🔑 AI Configuration (Optional)

MedGuard AI **does NOT require AI to function**.

If you want AI explanations:

1. Create a Gemini API key
   👉 [https://aistudio.google.com/](https://aistudio.google.com/)

2. Add a `local.properties` file:

```
apiKey=your-gemini-api-key
model=gemini-2.5-flash
```

If the API fails or is missing, the app automatically falls back to **rule-based safety analysis**.

---

## Architecture

MedGuard AI follows a **clean, layered architecture**:

* **UI Layer (Compose Multiplatform)**
  Screens, accessibility controls, graphs, and scanner UI.

* **Domain Layer**
  Risk engine, risk rules, and deterministic safety evaluation.

* **Optional AI Layer**
  Non-blocking AI explanations with graceful fallback.

This architecture ensures **stability, testability, and safety**, which is essential for healthcare-oriented software.
<img width="2816" height="1536" alt="Ar" src="https://github.com/user-attachments/assets/0610c321-f191-4133-aeeb-631c7711027f" />


---

## 🧪 Why This Project Matters

* Prioritizes **user safety over novelty**
* Demonstrates **responsible AI usage**
* Works even when AI services fail
* Designed for **real patients**, not just demos
* Fully cross-platform with **shared UI & logic**

---

## ⚠️ Medical Disclaimer

MedGuard AI provides **informational medication safety insights only**.
It is **not a diagnostic tool** and does **not replace professional medical advice**.

Always consult a qualified healthcare professional before starting, stopping, or combining medications.

---

## Built Using

* **Kotlin Multiplatform**
* **Compose Multiplatform**
* **Gemini API (optional)**
* **Kotlin Coroutines**
* **Material 3**
* **Modern Android & iOS toolchains**

---

## 📄 License

This project is licensed under the **Apache License 2.0**.








