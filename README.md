# Phone_dialer
 
# Phone Dialer App

## Overview
The **Phone Dialer App** is a fully functional dialer application built using **Kotlin** and **Jetpack Compose** in **Android Studio**. It utilizes **Broadcast Receiver** and **Services** to manage calls effectively and can be set as the **default phone app** on Android devices. The app supports a **minimum SDK of 30**.

## Features

### 📞 Incoming Call Management
- Displays incoming calls.
- Allows users to **accept or reject** calls.

### 📲 Outgoing Call Management
- Enables users to **initiate and make outgoing calls**.

### 📜 Call Logs
- Maintains a **detailed log** of all **incoming, outgoing, and missed calls**.

### 📇 Contacts Management
- Displays a list of contacts.
- Allows users to **manage** their contacts.

## Tech Stack
- **Kotlin**: Primary programming language.
- **Jetpack Compose**: Modern UI toolkit for building native UIs.
- **Broadcast Receiver**: Listens for incoming calls and call states.
- **Services**: Handles background call processing.

## Setup Instructions
1. **Clone the Repository**
   ```sh
   git clone https://github.com/MONIBUR2002/Phone_dialer.git
   cd phone-dialer
   ```
2. **Open in Android Studio**
   - Open Android Studio and select **Open an Existing Project**.
   - Navigate to the cloned folder and open the project.

3. **Run the App**
   - Connect an Android device or use an emulator.
   - Set the app as the **default phone app** when prompted.
   - Build and run the app.

## Permissions Required
The app requires the following permissions:
```xml
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ANSWER_PHONE_CALLS"/>
    <uses-permission android:name="android.permission.MANAGE_OWN_CALLS" />
    <uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
    <uses-permission android:name="android.permission.WRITE_CALL_LOG" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.WRITE_CONTACTS" />
    <uses-feature android:name="android.hardware.telephony" />
```

## Contributing
If you'd like to contribute:
- Fork the repository.
- Create a feature branch (`git checkout -b feature-name`).
- Commit your changes (`git commit -m 'Add new feature'`).
- Push to the branch (`git push origin feature-name`).
- Open a pull request.



## Contact
If you have any questions or issues, please feel free to contact **moniburhaque2002@gmail.com**.

