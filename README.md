<div align="center">

# 🏍️ MotoCare

### An Android app for tracking motorcycle maintenance, expenses, and important service schedules.

<br />

<img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
<img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
<img src="https://img.shields.io/badge/IDE-Android%20Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white" />
<img src="https://img.shields.io/badge/Database-SQLite%20%2F%20Room-003B57?style=for-the-badge&logo=sqlite&logoColor=white" />
<img src="https://img.shields.io/badge/Auth-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />

<br />
<br />

<img src="https://img.shields.io/badge/Status-Academic%20Project-blue?style=flat-square" />
<img src="https://img.shields.io/badge/Offline-Supported-success?style=flat-square" />
<img src="https://img.shields.io/badge/API-hargaBensin-orange?style=flat-square" />
<img src="https://img.shields.io/badge/Made%20with-Kotlin-purple?style=flat-square" />
<img src="https://img.shields.io/badge/Build-Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" />

</div>

<br />

## 📌 About MotoCare

**MotoCare** is a simple Android application designed to help motorcycle users record and manage vehicle maintenance and expenses, such as service history, oil changes, fuel spending, and tax/STNK information.

This application was developed as a final project for the **Mobile Programming / Mobile Application Development** course.

Many motorcycle users often forget when they last serviced their vehicle, changed the oil, or paid vehicle tax. MotoCare helps users store all of this information in a more organized, structured, and accessible way.

MotoCare is designed as a personal-use application. Users can log in using Google, while the main vehicle data is stored locally on the user's device, allowing the app to remain usable even without an internet connection.

<br />

## ✨ Key Features

* 👋 **Onboarding screen** to introduce the main features of MotoCare
* 🔐 **Google Login** using Firebase Authentication
* 🏍️ **Initial motorcycle setup**
* 🛠️ **Optional service, oil, and tax setup**
* 📋 **Manage multiple motorcycles**
* ⭐ **Select an active motorcycle for the dashboard**
* 🧾 **Record motorcycle service history**
* 🛢️ **Record oil change schedules and history**
* ⛽ **Track fuel expenses**
* 🌐 **Fetch the latest fuel prices from the hargaBensin API**
* 📄 **Record tax/STNK information**
* 📊 **View monthly expense summaries**
* 📍 **Calculate estimated next service and oil change based on mileage and time interval**
* 🗂️ **View service, oil, tax, and fuel history using tabs**
* ✏️ **Edit and delete recorded data**
* ⚙️ **Manage profile, settings, backup/export, and app information**

<br />

## 🧰 Tech Stack

| Category             | Technology                               |
| -------------------- | ---------------------------------------- |
| Programming Language | Kotlin                                   |
| IDE                  | Android Studio                           |
| Build Tool           | Gradle                                   |
| User Interface       | XML Layout                               |
| Local Database       | SQLite / Room Database                   |
| List & Data Display  | RecyclerView                             |
| Navigation           | Intent & Activity                        |
| Authentication       | Firebase Authentication for Google Login |
| Icons                | Material Vector Drawable                 |
| External API         | hargaBensin API                          |

<br />

## 🗄️ Database Overview

MotoCare uses a local database to store motorcycle data and all vehicle-related expense records.

| Table             | Description                        |
| ----------------- | ---------------------------------- |
| `users`           | Stores logged-in user profile data |
| `motors`          | Stores motorcycle data             |
| `service_records` | Stores motorcycle service history  |
| `oil_records`     | Stores oil change records          |
| `fuel_records`    | Stores fuel expense records        |
| `tax_records`     | Stores tax/STNK information        |

<br />

## 🧱 Main Data Structure

### `motors`

| Field              | Description                |
| ------------------ | -------------------------- |
| `id`               | Motorcycle ID              |
| `name`             | Motorcycle name or type    |
| `plateNumber`      | Motorcycle plate number    |
| `currentKilometer` | Current motorcycle mileage |
| `isActive`         | Active motorcycle status   |

<br />

### `service_records`

| Field           | Description                       |
| --------------- | --------------------------------- |
| `id`            | Service record ID                 |
| `motorId`       | Related motorcycle ID             |
| `serviceDate`   | Service date                      |
| `serviceType`   | Type of service                   |
| `kilometer`     | Mileage at the time of service    |
| `intervalKm`    | Service interval based on mileage |
| `intervalMonth` | Service interval based on months  |
| `cost`          | Service cost                      |
| `note`          | Additional notes                  |

<br />

### `oil_records`

| Field           | Description                               |
| --------------- | ----------------------------------------- |
| `id`            | Oil change record ID                      |
| `motorId`       | Related motorcycle ID                     |
| `oilChangeDate` | Oil change date                           |
| `kilometer`     | Mileage at the time of oil change         |
| `nextKilometer` | Estimated mileage for the next oil change |
| `intervalKm`    | Oil change interval based on mileage      |
| `intervalMonth` | Oil change interval based on months       |
| `oilType`       | Type of oil                               |
| `cost`          | Oil change cost                           |

<br />

### `fuel_records`

| Field           | Description                        |
| --------------- | ---------------------------------- |
| `id`            | Fuel record ID                     |
| `motorId`       | Related motorcycle ID              |
| `fuelDate`      | Fuel refill date                   |
| `fuelType`      | Fuel type                          |
| `fuelBrand`     | Fuel brand                         |
| `octane`        | Fuel octane rating                 |
| `pricePerLiter` | Fuel price per liter               |
| `liter`         | Total fuel volume in liters        |
| `cost`          | Total fuel cost                    |
| `kilometer`     | Mileage at the time of fuel refill |

<br />

### `tax_records`

| Field      | Description           |
| ---------- | --------------------- |
| `id`       | Tax record ID         |
| `motorId`  | Related motorcycle ID |
| `dueDate`  | Tax due date          |
| `taxCost`  | Tax cost              |
| `status`   | Payment status        |
| `paidDate` | Tax payment date      |

<br />

## 🔄 Application Flow

1. The user opens the application.
2. The splash screen checks the user's login status.
3. If the user is not logged in, they are redirected to the onboarding screen.
4. The user logs in using a Google account.
5. The user enters their first motorcycle data.
6. The user can complete or skip the optional service, oil, and tax setup.
7. The dashboard displays the active motorcycle, monthly expenses, and upcoming maintenance schedules.
8. The user can change the active motorcycle from the motorcycle list page.
9. The `+` button is used to add service, oil, fuel, tax, or motorcycle records.
10. Vehicle history is displayed through service, oil, tax, and fuel tabs.
11. The application can fetch the latest fuel prices from an API to help users input fuel expenses.
12. Data is stored in a local database and can still be accessed offline.
13. The user can access profile, settings, backup/export, and about pages.

<br />

## 📍 Service and Oil Estimation Rules

MotoCare calculates the next service and oil change estimation using two main indicators:

* Motorcycle mileage
* Time interval in months

The application displays the estimate based on whichever condition is reached first.

| Maintenance Type |  Mileage | Time     |
| ---------------- | -------: | -------- |
| Service          | 3,000 km | 3 months |
| Oil Change       | 2,000 km | 2 months |

<br />

## 🌐 External API

MotoCare uses the following repository to fetch real-time fuel price data:

```text
https://github.com/alifmaulidanar/hargaBensin
```

Main endpoints used:

```text
https://api.alifmaulidanar.my.id/api-bbm/full
https://api.alifmaulidanar.my.id/api-bbm/{jenis}/{merek}/{oktan}
```

The API is only used to help retrieve fuel prices. If the API request fails, users can still enter the fuel price manually.

<br />

## 🚀 How to Run the Project

1. Clone this repository.

```bash
git clone https://github.com/adlafayyaz/Motocare.git
```

2. Open the project using **Android Studio**.

3. Wait until the **Gradle Sync** process is complete.

4. Make sure the Firebase configuration is available for the Google Login feature.

5. Run the application using an Android emulator or a real Android device.

<br />

## 👥 Team Members

| Name                              | Student ID | Role                                     |
| --------------------------------- | ---------- | ---------------------------------------- |
| Khaliz Kanigara Fathi Gunawan     | 2410512151 | Product Manager & Lead Backend Developer |
| Fathi Muhammad Luthfi Cardiana    | 2410512142 | UI/UX Designer & Frontend Developer      |
| M. Adla Fayyaz Fauzy              | 2410512154 | Frontend Developer - List & Navigation   |
| Rafid Abdan Syakur                | 2410512141 | Backend Developer                        |
| M. Syauqi Rabbani                 | 2410512166 | System Analyst                           |
| Ananta Jordan Surya Putra Ginting | 2410512164 | Quality Assurance & Technical Writer     |

<br />

## 📄 License

This project was developed for learning purposes as a final project for the **Mobile Programming / Mobile Application Development** course.

<br />

<div align="center">

**MotoCare — Keep your motorcycle maintenance organized, simple, and on track.**

</div>
