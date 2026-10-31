# 🏙️ SmartCity360

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue?logo=eclipse)
![MySQL](https://img.shields.io/badge/MySQL-8-blue?logo=mysql&logoColor=white)
![Payara](https://img.shields.io/badge/Payara-7-005CA9)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-Academic-lightgrey)

> A Jakarta EE 10 smart city management platform demonstrating **5 Gang of Four design patterns** through real-world municipal infrastructure modules — energy metering, zone management, citizen complaints, streetlight networks, and policy automation.

Built for the **JIAT Object-Oriented Design Patterns II (OODP II)** course.

---

## ✨ Design Patterns Implemented

| # | Module | Pattern | What It Does |
|:-:|--------|---------|-------------|
| 1 | **Energy** | 🎀 **Decorator** | Wraps raw meter streams with stackable security layers (AES/GCM encryption, HMAC-SHA256 authentication, GZIP compression) |
| 2 | **Zone** | 🏗️ **Composite** | Models city → district → street → building hierarchy with recursive energy aggregation and cascading power control |
| 3 | **Complaint** | 🔗 **Chain of Responsibility** | Routes citizen complaints through escalating authority tiers (Local Staff → Supervisor → City Manager) with full audit traces |
| 4 | **Streetlight** | 🪶 **Flyweight** | Shares immutable hardware specs across thousands of streetlight instances to minimize memory usage |
| 5 | **Policy** | 🔤 **Interpreter** | Parses and evaluates boolean policy rules at runtime via a recursive-descent AST (e.g., `temperature > 35 AND humidity < 80`) |

---

## 🛠️ Tech Stack

- **Language:** Java 21
- **Platform:** Jakarta EE 10 (JAX-RS, JPA, CDI, EJB)
- **Server:** Payara 7
- **Database:** MySQL 8 (15 InnoDB tables)
- **Build:** Apache Maven 3.9+
- **Testing:** JUnit 5
- **Frontend:** Vanilla JS + CSS3 dark-themed dashboard

---

## 📁 Project Structure

```
smartcity360/
├── pom.xml
├── sql/
│   ├── schema.sql                  # MySQL schema (15 tables)
│   └── seed-data.sql               # Demo data
└── src/
    ├── main/
    │   ├── java/lk/jiat/smartcity360/
    │   │   ├── config/             # JAX-RS app, seeder, exception mapper
    │   │   ├── security/           # Token auth, PBKDF2 hashing, filters
    │   │   ├── persistence/        # JPA entities (15), DAOs (14), exceptions
    │   │   ├── energy/             # 🎀 Decorator — streams & layers
    │   │   ├── zone/               # 🏗️ Composite — zones & buildings
    │   │   ├── complaint/          # 🔗 Chain of Responsibility — handlers
    │   │   ├── streetlight/        # 🪶 Flyweight — model registry
    │   │   ├── policy/             # 🔤 Interpreter — AST parser & engine
    │   │   ├── sensor/             # IoT sensor telemetry
    │   │   ├── waste/              # Smart waste management
    │   │   └── resident/           # Resident & staff directory
    │   ├── resources/META-INF/
    │   │   └── persistence.xml
    │   └── webapp/
    │       ├── login.html
    │       ├── index.html          # SPA dashboard
    │       ├── css/style.css
    │       ├── js/                 # Modular vanilla JS controllers
    │       └── WEB-INF/web.xml
    └── test/java/lk/jiat/smartcity360/
        ├── energy/                 # Decorator round-trip tests
        ├── zone/                   # Composite aggregation tests
        ├── complaint/              # Chain escalation tests
        ├── streetlight/            # Flyweight identity tests
        └── policy/                 # Interpreter grammar tests
```

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version |
|------|---------|
| JDK | 21+ |
| Maven | 3.9+ |
| MySQL | 8.x |
| Payara Server | 7 |
| MySQL Connector/J | 8.x |

### 1. Database Setup

```sql
CREATE DATABASE IF NOT EXISTS smartcity360 CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'smartcity360'@'localhost' IDENTIFIED BY 'smartcity360123';
GRANT ALL PRIVILEGES ON smartcity360.* TO 'smartcity360'@'localhost';
FLUSH PRIVILEGES;

USE smartcity360;
SOURCE sql/schema.sql;
SOURCE sql/seed-data.sql;
```

### 2. Payara JDBC Configuration

Copy the MySQL driver into Payara:
```bash
copy mysql-connector-j-8.x.x.jar <payara-dir>/glassfish/domains/domain1/lib/
```

Create the connection pool and JNDI resource:
```bash
asadmin start-domain

asadmin create-jdbc-connection-pool \
  --datasourceclassname com.mysql.cj.jdbc.MysqlDataSource \
  --restype javax.sql.DataSource \
  --property serverName=localhost:portNumber=3306:databaseName=smartcity360:user=smartcity360:password=smartcity360123:useSSL=false:allowPublicKeyRetrieval=true:serverTimezone=UTC \
  SmartCity360Pool

asadmin create-jdbc-resource --connectionpoolid SmartCity360Pool jdbc/smartcity360

asadmin ping-connection-pool SmartCity360Pool
```

### 3. Build & Deploy

```bash
mvn clean package
asadmin deploy --force target/smartcity360.war
```

### 4. Open the App

Navigate to **http://localhost:8080/smartcity360/login.html**

---

## 🔐 Default Accounts

| Username | Password | Role | Access |
|----------|----------|------|--------|
| `admin` | `Admin@123` | ADMIN | Full access — create meters, manage chains, bulk operations, edit policies |
| `staff` | `Staff@123` | STAFF | Operational — monitor telemetry, resolve complaints, update waste bins |
| `resident` | `Resident@123` | RESIDENT | Citizen — read-only dashboard + file complaints |

---

## 🧪 Running Tests

All design pattern implementations are tested as plain Java classes with zero container dependencies:

```bash
mvn test
```

| Test Class | Pattern Tested |
|------------|---------------|
| `EnergyStreamTest` | Decorator round-trip, tampering, compression |
| `ZoneTest` | Composite aggregation, subtree power control |
| `ComplaintChainTest` | Chain of Responsibility escalation routing |
| `StreetlightRegistryTest` | Flyweight instance identity & memory |
| `PolicyParserTest` | Interpreter grammar precedence & AST |

---

## 🔧 Troubleshooting

| Issue | Cause | Fix |
|-------|-------|-----|
| `UnsupportedClassVersionError` | JDK < 21 | Set `JAVA_HOME` to JDK 21, restart Payara |
| `Lookup of jdbc/smartcity360 fails` | Missing JDBC pool/resource | Re-run `asadmin` pool creation commands |
| `ClassNotFoundException: MysqlDataSource` | Driver JAR missing | Copy `mysql-connector-j` into `domain1/lib/` |
| `Table not found` | Schema not loaded | Run `SOURCE sql/schema.sql` in MySQL |
| `401 Unauthorized` after redeploy | Sessions cleared (in-memory) | Log in again |
| Port 8080 in use | Another service running | Stop conflicting process or change Payara port |

---

## 📄 License

This project is developed for academic purposes as part of the JIAT OODP II curriculum.
