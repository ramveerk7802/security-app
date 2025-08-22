# Android Security Application

This project is an Android security application designed to protect users from common privacy and security threats on their mobile devices. It actively monitors device permissions and hardware access to provide real-time alerts and insights into potential vulnerabilities.

---

## Features Implemented

Currently, the application includes the following core security features:

### 1. Mic/Camera/GPS Access Alerts

This feature enhances user awareness by providing immediate notifications whenever an application accesses sensitive hardware.

* **Real-time Detection**: The app actively monitors and detects when any installed application begins using the microphone, camera, or GPS/location services.
* **Instant Alerts**: Users receive a real-time notification the moment access is detected, allowing them to identify which app is responsible and take action if the access is unexpected.

### 2. Permission Abuse Detection

This module helps users understand the risks associated with the permissions granted to their installed applications.

* **App Scanner**: The application can perform a full scan of all installed apps to analyze their requested permissions.
* **Risk Highlighting**: It specifically identifies apps that have **dangerous permission combinations** (e.g., an app having access to the microphone, SMS, and location simultaneously).
* **User-Friendly Explanations**: For each high-risk app detected, the application provides a clear and simple explanation of the potential risks, helping the user make informed decisions about keeping or removing the app.

---

## How to Install and Test

### Installation

1.  Download the application's APK file from the link below:
    * **https://drive.google.com/file/d/1KwN7IlM12LoYp_ZQxZlEFvL3RtEIW3oB/view?usp=sharing**
2.  Open a file manager on your device, navigate to the downloaded APK file, and tap on it.
3.  You may need to enable **"Install from unknown sources"** in your device's settings to proceed.
4.  Follow the on-screen prompts to complete the installation.

### Testing the Features

#### To Test Access Alerts:

1.  Once the app is installed and set up, simply open another application that uses sensitive hardware.
2.  **Camera**: Open the default Camera app. You should receive an immediate notification that the camera is in use.
3.  **Microphone**: Use a voice recorder app or the voice search feature. You should get a notification for microphone access.
4.  **Location**: Open a maps application like Google Maps. A notification should appear indicating that location services are active.

#### To Test Permission Abuse Detection:

1.  Open the **Android Security Application**.
2.  Navigate to the **"Permission Scanner"** or **"App Scan"** section.
3.  Tap the **"Scan Now"** button to begin the analysis.
4.  The results will display a list of installed apps, highlighting any with risky permission combinations.
5.  Tap on a flagged app to view the detailed explanation of the associated risks.

---

## Limitations and Assumptions

* **Feature Scope**: This version includes 2 out of the 4 core requirements. The **AI/Heuristic Phishing Detection** and **Panic Mode** features are not yet implemented.
* **Android Version**: The application is built and tested for **Android API 29+ (Android 10 and newer)**. Functionality on older versions is not guaranteed.
* **Background Restrictions**: Device-specific battery optimizations (common on phones from manufacturers like Xiaomi, Huawei, or OnePlus) might occasionally delay or block background monitoring. For best results, the user may need to disable battery optimization for this app manually.
