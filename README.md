![ic_medguard_logo1](https://github.com/user-attachments/assets/b9451089-1f57-49d4-abe9-b179a24d67eb)

# MedGuard AI

**MedGuard AI** is a Kotlin Multiplatform application designed to improve medication safety by identifying potentially dangerous drug interactions using a **reliable rule-based engine**, enhanced with **optional AI explanations**.

The project focuses on **safety-first design**, accessibility, and graceful degradation when AI services are unavailable — making it suitable for real world healthcare scenarios and educational use.



##Project Goals

- Prevent dangerous medication combinations
- Provide clear, human-readable risk explanations
- Remain functional without external AI services
- Demonstrate clean Kotlin Multiplatform architecture
- Prioritize accessibility and usability for all users


## Key Features

- 🧠 **Rule-based medication risk engine** (offline & deterministic)
- 🤖 **Optional AI explanations** with graceful fallback
- 📊 **Visual risk graph** (Low / Moderate / High)
- ♿ **Accessibility Mode** (large text across the UI)
- 📷 **Medicine scan flow** (camera-ready, demo simulated)
- 🛡️ Safe handling of network, quota, and API failures
- 🧱 Clean separation of UI, domain logic, and risk evaluation
## App Architecture
<img width="2816" height="1536" alt="Arch" src="https://github.com/user-attachments/assets/f3b02b8c-2c5a-415a-94be-670b97358d2b" />
composeApp/
├── commonMain/ Shared UI, domain logic, risk engine
├── androidMain/Android-specific implementations
├── iosMain/iOS-specific implementations
iosApp/iOS application entry point (SwiftUI)




## Kotlin Multiplatform Architecture

This is a **Kotlin Multiplatform (KMP)** project targeting **Android and iOS**.



