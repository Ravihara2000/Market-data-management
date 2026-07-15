# 📈 Market Data Management System

A Spring Boot REST API system that ingests, aggregates, and serves real-time market data from multiple external sources, with derived field calculations, caching, async audit logging, and Swagger documentation.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Caching | Spring Cache (`@Cacheable`) |
| Async | Virtual Threads (Java 21) |
| API Docs | Swagger / SpringDoc OpenAPI |
| Build Tool | Maven |
| Security | Spring Security + JWT |

---

## 📐 Architecture Overview

```
┌─────────────────────────────────────────────┐
│              External Sources               │
│         (BLOOMBERG, ICE, REUTERS...)        │
└────────────────────┬────────────────────────┘
                     │ REST API
┌────────────────────▼────────────────────────┐
│              API Component                  │
│     (Source-specific request mapping)       │
└────────────────────┬────────────────────────┘
                     │
┌────────────────────▼────────────────────────┐
│              Core Component                 │
│  - Aggregation Engine                       │
│  - Derived Field Calculator                 │
│  - Cache Manager                            │
│  - Audit Logger (Async / Virtual Thread)    │
└────────────────────┬────────────────────────┘
                     │
┌────────────────────▼────────────────────────┐
│                 MySQL DB                    │
│    market_data | consolidated | audit       │
└─────────────────────────────────────────────┘
```

---

## 📦 Project Structure

```
src/
├── main/java/com/market/
│   ├── api/                  # Source-specific API layer (Bloomberg adapter etc.)
│   ├── core/                 # Core business logic (aggregation, calculation)
│   ├── controller/           # REST controllers
│   ├── service/              # Service layer
│   ├── repository/           # JPA repositories
│   ├── entity/               # DB entities (MarketData, ConsolidatedMarketData, Audit)
│   ├── dto/                  # Request / Response DTOs
│   ├── config/               # Cache config, Async config, Swagger config
│   └── util/                 # Utility classes
└── resources/
    └── application.properties
```

---

## ⚙️ Business Logic

### 1. Market Data Ingestion
- Receives market data from external sources via a REST API endpoint
- Each message contains fields: `symbol`, `last_traded_price`, `bid_price`, `mid_price`, `ask_price`, `market_timestamp`, `source`, and optional fields: `last_coupon_date`, `interest_rate`, `volatility`
- The system is designed with a **separate API component** and a **core component** to support multiple sources with different message formats in the future
- Source-specific adapters normalize incoming data into a common internal model before processing

### 2. Market Data Aggregation (Consolidation)
- A symbol can receive market data from **multiple sources** (e.g. BLOOMBERG, ICE)
- For each symbol, all source records are aggregated into a **single consolidated record**
- Consolidation takes the **latest non-null value** for each field across all sources
- The consolidated record is tagged with `source: "CONSOLIDATED"`
- When a source record is **deleted**, the consolidated record is **recalculated** from the remaining sources
- A new market data message from a source **completely overwrites** the previous record for that source

### 3. Derived Field Calculations
- **Accrued Interest** — calculated as:
  ```
  accrued_interest = (last_traded_price × interest_rate) / days_between(last_coupon_date, today)
  ```
  - Requires: `last_traded_price`, `interest_rate`, `last_coupon_date`
  - Day count must be greater than zero

- **Theoretical Price** — calculated only for symbols that have a `depends_on_symbol`:
  ```
  theoretical_price = depends_on_symbol's last_traded_price × symbol's volatility
  ```
  - If the `depends_on_symbol`'s LTP changes, the theoretical price is **automatically recalculated**
  - Requires: `volatility` and the parent symbol's `last_traded_price`

- If **any required field is missing**, the derived field defaults to **zero**

### 4. Caching (`@Cacheable`)
- Consolidated market data responses are cached using Spring's `@Cacheable` annotation to reduce redundant DB reads and calculations
- Cache is **evicted** (`@CacheEvict`) whenever market data is added, updated, or deleted for a symbol, ensuring the cache stays consistent
- Separate cache entries per symbol for granular invalidation

### 5. Audit Logging
- Every create, update, and delete operation is recorded in a dedicated **`audit` table**
- Audit records capture: `action`, `data` (JSON payload), `timestamp`, and `user`
- Audit inserts are performed **asynchronously** using Java 21 **Virtual Threads**, so they never block the main request thread
- Virtual threads are configured via a custom `AsyncConfig` using `Executors.newVirtualThreadPerTaskExecutor()`

### 6. Input Validation
- All API inputs are validated using Bean Validation (`@NotNull`, `@NotBlank`, `@Positive`, etc.)
- Informative error responses are returned for invalid inputs with field-level messages
- Symbol and source are validated to exist before querying or deleting

---

## 🔌 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/market-data` | Add or update a market data message |
| `GET` | `/api/market-data/{symbol}/source/{source}` | Get latest market data for a symbol from a specific source |
| `GET` | `/api/market-data/{symbol}/consolidated` | Get consolidated market data with derived fields for a symbol |
| `POST` | `/api/market-data/consolidated/bulk` | Get consolidated market data for a list of symbols |
| `DELETE` | `/api/market-data/{symbol}/source/{source}` | Delete market data for a symbol and source |

> Full API documentation available via Swagger UI at:
> **`http://localhost:8080/swagger-ui/index.html`**

---

## 🗄️ Database Tables

### `market_data`
Stores raw market data per symbol per source.

### `consolidated_market_data`
Stores the aggregated view per symbol across all sources.

### `audit`
Stores an audit trail of all operations.

| Column | Type | Description |
|---|---|---|
| `id` | BIGINT | Primary key |
| `action` | VARCHAR | CREATE / UPDATE / DELETE |
| `data` | TEXT | JSON snapshot of the payload |
| `timestamp` | DATETIME | When the action occurred |
| `user` | VARCHAR | Who triggered the action |

---

## ⚡ Async Virtual Thread Configuration

```java
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "virtualThreadExecutor")
    public Executor virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
```

Audit service uses `@Async("virtualThreadExecutor")` to offload DB writes to virtual threads without blocking the request.

---

## 🧠 Caching Configuration

```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("consolidatedMarketData");
    }
}
```

- `@Cacheable("consolidatedMarketData")` on consolidated query methods
- `@CacheEvict(value = "consolidatedMarketData", key = "#symbol")` on write/delete methods

---

## 🛠️ Getting Started

### Prerequisites
- Java 21+
- MySQL 8+
- Maven 3.8+

### Setup

```bash
# Clone the repository
git clone https://github.com/your-org/market-data-management-system.git
cd market-data-management-system

# Configure DB in application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/market_db
spring.datasource.username=root
spring.datasource.password=yourpassword

# Build and run
mvn clean install
mvn spring-boot:run
```

### Access Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Sample Market Data Request

```json
POST /api/market-data

{
  "symbol": "AAPL_OPTION",
  "last_traded_price": "16.345",
  "bid_price": "16.321",
  "mid_price": "14.54",
  "ask_price": "17.21",
  "market_timestamp": 1691167567124,
  "depends_on_symbol": "AAPL",
  "source": "BLOOMBERG"
}
```

### Consolidated Response

```json
{
  "symbol": "AAPL_OPTION",
  "bid_price": "115",
  "mid_price": "110",
  "ask_price": "120",
  "accrued_interest": "3.45",
  "theoretical_price": "18.90",
  "market_timestamp": 1691167567124,
  "source": "CONSOLIDATED"
}
```

---

