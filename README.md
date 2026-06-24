📌 Spam Detection System using Java + Python ML API
🧠 Project Overview

This project is a full-stack Spam Detection System that classifies messages as Spam or Ham (Not Spam) using Machine Learning.

It integrates:

Java (Spring Boot) → Backend API & main application
Python (Flask) → Machine Learning model serving API
Machine Learning (Naive Bayes + TF-IDF) → Email/SMS classification

The system takes user input from Java backend, sends it to Python ML API, and returns prediction results.

🚀 Features
🔍 Detects spam vs ham messages
🤖 Machine Learning model trained using Naive Bayes
⚡ Fast REST API using Flask
🔗 Java–Python integration via HTTP requests
📊 TF-IDF text vectorization
💾 Model persistence using Pickle
🧪 Tested with real-time API requests
🏗️ System Architecture
User Input
   ↓
Java Spring Boot Backend
   ↓ (HTTP Request)
Flask Python API
   ↓
ML Model (TF-IDF + Naive Bayes)
   ↓
Prediction (Spam / Ham)
   ↓
Response returned to Java
🧰 Tech Stack
Backend (Java)
Spring Boot
REST APIs
Maven
Machine Learning (Python)
Scikit-learn
Pandas
NumPy
Naive Bayes Classifier
TF-IDF Vectorizer
API
Flask
JSON communication
📂 Project Structure
spam-detection-java/
│
├── python-ml-api/
│   ├── ml_api.py              # Flask API
│   ├── ml_model.py            # Training script
│   ├── emails.csv             # Dataset
│   └── detector/
│       ├── model.pkl
│       ├── vectorizer.pkl
│
├── src/                       # Java Spring Boot code
│   ├── controller/
│   ├── service/
│   ├── model/
│   └── repository/
│
├── pom.xml
└── README.md
⚙️ How It Works
1. Model Training
Dataset (emails.csv) is loaded
Text is converted using TF-IDF
Model trained using Multinomial Naive Bayes
Saved as .pkl files
2. Flask API
Loads trained model
Exposes endpoint /predict
Accepts JSON input:
{
  "text": "Congratulations! You won a lottery"
}
3. Prediction Output
{
  "text": "Congratulations! You won a lottery",
  "prediction": "spam",
  "confidence": 0.91
}
4. Java Integration
Java backend sends HTTP request to Flask API
Receives prediction response
Displays result to user
▶️ How to Run the Project
Step 1: Run Python ML API
cd python-ml-api
python ml_api.py

API runs at:

http://localhost:5000
Step 2: Train Model (if needed)
python ml_model.py
Step 3: Run Java Backend
mvn spring-boot:run
📡 API Endpoint
Predict Spam
POST /predict
Request Body:
{
  "text": "Free iPhone offer, click now!"
}
Response:
{
  "text": "Free iPhone offer, click now!",
  "prediction": "spam"
}
📊 Model Details
Algorithm: Multinomial Naive Bayes
Feature Extraction: TF-IDF Vectorizer
Dataset: SMS/Email spam dataset
Output: Binary classification (Spam / Ham)
🎯 Future Improvements
Improve accuracy using deep learning models
Add frontend UI dashboard
Deploy on cloud (AWS / Render / Azure)
Add authentication system
Real-time email integration
👨‍💻 Author

Your Name
Final Year IT Student
Project: Spam Detection System using ML + Java

⭐ Summary

This project demonstrates:

Machine Learning integration with backend systems
Real-world API communication
Full-stack system design
Practical AI deployment using Flask + Java
