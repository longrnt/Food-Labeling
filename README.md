# 🍽️ Food Labeling System

A scalable application for managing and labeling food items using a PostgreSQL database, Spring Boot REST APIs, and a simple UI. Designed to efficiently support millions of food records with flexible label filtering and query capabilities.

---

## 📌 Goals

* ✅ Store and manage millions of food items
* ✅ Assign and manage labels with full many-to-many support
* ✅ Query foods by one or more labels (AND logic)
* ✅ Provide simple UI and REST APIs

---

## 📦 Tech Stack

| Layer         | Technology                 |
| ------------- | -------------------------- |
| Backend       | Java 21, Spring Boot 3.5.0 |
| Database      | PostgreSQL                 |
| ORM           | Spring Data JPA            |
| Frontend (UI) | HTML, JS (React)           |
| Testing       | JUnit, Mockito             |

---

## 🗂️ Project Structure

```
Food-Labeling/
├── src/
│   ├── main/
│   │   ├── java/com/food/labeling/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   ├── payload/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   └── resources/
│   │       └── application.yml
├── db/
│   ├── schema.sql
│   └── food_insert_100_records.sql
├── front-end/
│   ├── src/
│   │   ├── components
│   │   │   ├── FoodListPage.jsx
│   │   │   ├── LabelManagerPage.jsx
│   │   ├── css
│   │   ├── App.jsx
│   │   ├── main.jsx
│   ├── index.html
├── README.md
└── pom.xml
```

---

## 🧼 Database Schema

### Entity Summary

#### `Food`

* `food_id`: Primary Key
* `food_name`: Unique and required
* `labels`: Many-to-many with `Label`
* `created_at`, `updated_at`: Auto-managed timestamps

#### `Label`

* `label_id`: Primary Key
* `label_name`: Unique and required
* `foods`: Back reference to `Food`
* `created_at`, `updated_at`: Auto-managed timestamps

### ERD

```
Food (
  food_id PK,
  food_name TEXT UNIQUE NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
)

Label (
  label_id PK,
  label_name TEXT UNIQUE NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
)

food_label (
  food_id FK → Food,
  label_id FK → Label,
  PRIMARY KEY (food_id, label_id)
)
```

---

## 🔌 REST API Documentation

---

### 📗 Food API

#### 🔹 `GET /foods/getByLabels`

Filterable food querying by one or more labels.
Returns foods matching **all** the provided labels (AND filter).

**Query Parameters**:

| Name         | Type    | Required | Default    | Description                    |
| ------------ | ------- | -------- | ---------- | ------------------------------ |
| `labels`     | List    | No       | N/A        | Filter by one or more labels   |
| `pageNumber` | Integer | No       | `0`        | Page number                    |
| `pageSize`   | Integer | No       | `10`       | Items per page                 |
| `sortBy`     | String  | No       | `foodName` | Field to sort by               |
| `sortOrder`  | String  | No       | `asc`      | Sorting order: `asc` or `desc` |

**Example**:

```
GET /foods/getByLabels?labels=vegan,gluten-free&pageNumber=0&pageSize=5&sortBy=foodName&sortOrder=desc
```

**Response**: `FoodResponse`

```json
{
  "content": [
    {
      "foodId": 1,
      "foodName": "Avocado",
      "labels": ["vegan", "low-carb"]
    }
  ],
  "pageNumber": 0,
  "pageSize": 5,
  "totalElements": 100,
  "totalPages": 10,
  "lastPage": false
}
```

---

#### 🔹 `POST /foods/{foodId}/labels/{labelId}`

Assign a label to a food.

**Responses**:

* `200 OK`: Successfully assigned
* `404 Not Found`: Food or label not found

---

#### 🔹 `DELETE /foods/{foodId}/labels/{labelId}`

Remove a label from a food.

**Responses**:

* `200 OK`: Label removed
* `404 Not Found`: Food or label not found


### 📘 Label API

#### 🔹 `GET /labels/getAll/counts`

Query all labels with food count respectively. 
Returns a list of all labels and the number of foods tagged with each.

**Response**:

```json
[
  { "labelId": 1, "labelName": "vegan", "foodCount": 132 },
  { "labelId": 2, "labelName": "gluten-free", "foodCount": 87 }
]
```

---

#### 🔹 `POST /labels/add`

Create a new label.

**Request Body**:

```json
{
  "labelName": "low-carb"
}
```

**Responses**:

* `201 Created`: Label created
* `400 Bad Request`: Label name is empty or already exists

---

#### 🔹 `PUT /labels/update/{labelId}`

Update an existing label's name.

**Path Param**:

* `labelId`: ID of the label to update

**Request Body**:

```json
{
  "labelName": "high-fiber"
}
```

**Responses**:

* `200 OK`: Label updated
* `404 Not Found`: Label not found
* `400 Bad Request`: Null or empty label name

---

#### 🔹 `DELETE /labels/delete/{labelId}`

Delete a label by ID.

**Response**:

* `200 OK`: Label deleted
* `404 Not Found`: Label not found

---

#### 🔹 `GET /labels/getAll`

Fetch all labels (without food counts).

**Response**:

```json
[
  { "labelId": 1, "labelName": "vegan" },
  { "labelId": 2, "labelName": "gluten-free" }
]
```
---


## 💻 User Interface

A basic web interface provides to:
- Food Management
  * Filter food items by labels (API: filterable food querying by one or more labels)
  * View label usage stats (API: query all labels with food count respectively)
  * Assign/unassign labels to food items
- Label Management
  * Create, update, and delete labels


---

## 🚀 Getting Started

### Step 1: Clone the Repository

```bash
git clone https://github.com/longrnt/Food-Labeling.git
```

### Step 2: Configure the Database

```sql
CREATE DATABASE food_labeling;
```

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/food_labeling
    username: youruser
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

### Step 3: Run the App
- Download and install Java 21 [[Link](https://www.oracle.com/java/technologies/downloads/)]
- Download and install Maven [[Link](https://maven.apache.org/download.cgi)]
- Download and install Node.js [[Link](https://nodejs.org/en/download)]
- Run Back-end

```bash
cd Food-Labeling
mvn spring-boot:run
```

- Back-end starts up at port 8080 by default. You can change it by updating the `server.port` property in `src/main/resources/application.yml`
- Spring Data JPA will automatically create tables (Food, Label, Food_Label) in your database after you run Back-end in the first time.

- Run Front-end

```bash
cd Food-Labeling/front-end
npm install
```

- Wait until dependencies installation completes, run the following command to start up the Front-end:

```bash
npm run dev
```

- Front-end starts up at port 3000 by default.
- Note that Front-end calls REST API in Back-end via `localhost:8080` by default. You'll need to update it accordingly if you changed the default host:port in the Back-end. Please go to `front-end/src/components/` and update `const API_BASE = 'http://localhost:8080';` accordingly on both FoodListPage.jsx and LabelManagerPage.jsx

### Testing Data
- For `Food` table, you can run the query in `/Food-Labeling/db/food_insert_100_records.sql` to insert 100 records in `Food` table. 
- For `Label` table, you can manually create some labels on UI (e.g. vegan, high-protein, gluten-free, high-carb). 
- Enjoy!

### Future enhancement
- Add searching feature for food on Food Management page. This will help user quickly get the food they want.
- Add pagination and sorting for Label Management table.