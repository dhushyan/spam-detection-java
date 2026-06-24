# 🛡️ Spam Detection Classifier — Java Spring Boot Version

Converted from: **Python Django** → **Java Spring Boot**

---

## 📁 Project Structure

```
spam-detection-java/
├── pom.xml                                         ← Maven (replaces requirements.txt)
├── python-ml-api/
│   └── ml_api.py                                   ← Flask wrapper for your model.pkl
└── src/main/
    ├── java/com/spamdetection/
    │   ├── SpamDetectionApplication.java            ← Main (replaces manage.py)
    │   ├── config/
    │   │   └── SecurityConfig.java                  ← Auth + CSRF (replaces Django auth)
    │   ├── controller/
    │   │   └── SpamController.java                  ← Views + URLs combined
    │   ├── dto/
    │   │   └── Dtos.java                            ← Forms (replaces forms.py)
    │   ├── model/
    │   │   ├── Email.java                           ← DB model (replaces models.py)
    │   │   └── User.java                            ← User model
    │   ├── repository/
    │   │   ├── EmailRepository.java                 ← ORM queries
    │   │   └── UserRepository.java
    │   └── service/
    │       ├── EmailService.java                    ← Business logic
    │       ├── MlService.java                       ← Calls Python Flask API
    │       └── UserService.java                     ← Auth logic
    └── resources/
        ├── application.properties                   ← Settings (replaces settings.py)
        ├── static/css/style.css
        └── templates/
            ├── home.html                            ← home.html (Thymeleaf)
            ├── dashboard.html                       ← dashboard.html
            ├── login.html
            └── register.html
```

---

## ⚙️ Django → Java Mapping

| Django | Java Spring Boot |
|--------|-----------------|
| `settings.py` | `application.properties` |
| `models.py` | `model/Email.java`, `model/User.java` |
| `views.py` | `controller/SpamController.java` |
| `urls.py` | `@GetMapping`, `@PostMapping` annotations |
| `forms.py` | `dto/Dtos.java` |
| `ml_model.py` | Python Flask API (`ml_api.py`) called via REST |
| `{% csrf_token %}` | Automatic via `th:action="@{/url}"` in Thymeleaf |
| `migrations/` | `spring.jpa.hibernate.ddl-auto=update` |
| `db.sqlite3` | MySQL `spam_detection_db` |
| `@login_required` | `SecurityConfig.java` (global rules) |
| Django admin | Can add `/admin` controller later |

---

## 🚀 Setup & Run

### Step 1 — MySQL Setup
```sql
CREATE DATABASE spam_detection_db;
```
Update `application.properties`:
```
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Step 2 — Start Python ML API
```bash
# Copy ml_api.py into your existing Django project root
# (where model.pkl, vectorizer.pkl, threshold.pkl are)

pip install flask
python ml_api.py
# Runs on http://localhost:5000
```

### Step 3 — Start Java Spring Boot App
```bash
# In spam-detection-java/ folder:
mvn spring-boot:run
# Runs on http://localhost:8080
```

### Step 4 — Open Browser
```
http://localhost:8080
```

---

## 🔐 CSRF — No More 403 Errors!

In Django you had to manually add `{% csrf_token %}` in every form.

In Spring Boot + Thymeleaf, just use `th:action="@{/url}"` and the CSRF token is
**automatically injected** — you never get CSRF 403 errors.

```html
<!-- Django (manual) -->
<form method="POST">
    {% csrf_token %}
    ...
</form>

<!-- Spring Boot Thymeleaf (automatic) -->
<form th:action="@{/check}" method="post">
    <!-- No manual token needed! -->
    ...
</form>
```

---

## 🧪 Test the ML API directly
```bash
curl -X POST http://localhost:5000/predict \
  -H "Content-Type: application/json" \
  -d '{"text": "Congratulations! You won a free iPhone! Click here now!"}'
```
Expected response:
```json
{
  "prediction": "spam",
  "confidence": 0.97,
  "message": "Spam detected! Be careful."
}
```
