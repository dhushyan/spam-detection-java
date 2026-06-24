
from flask import Flask, request, jsonify
import pickle
import os

app = Flask(__name__)


BASE_DIR = os.path.dirname(os.path.abspath(__file__))


MODEL_PATH      = os.path.join(BASE_DIR, "detector", "model.pkl")
VECTORIZER_PATH = os.path.join(BASE_DIR, "detector", "vectorizer.pkl")
THRESHOLD_PATH  = os.path.join(BASE_DIR, "detector", "threshold.pkl")

print("Loading ML model...")
with open(MODEL_PATH, "rb") as f:
    model = pickle.load(f)

with open(VECTORIZER_PATH, "rb") as f:
    vectorizer = pickle.load(f)

with open(THRESHOLD_PATH, "rb") as f:
    threshold = pickle.load(f)

print("✅ Model loaded successfully!")


@app.route("/health", methods=["GET"])
def health():
    """Health check endpoint — Java calls this to verify ML API is running"""
    return jsonify({"status": "ok", "model": "spam-classifier"})


@app.route("/predict", methods=["POST"])
def predict():
    """
    Main prediction endpoint
    Java sends:  {"text": "email content here"}
    Returns:     {"prediction": "spam", "confidence": 0.94, "message": "Spam detected"}
    """
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "Missing 'text' field"}), 400

    email_text = data["text"]

    if not email_text.strip():
        return jsonify({"error": "Email content cannot be empty"}), 400

   
    features = vectorizer.transform([email_text])


    proba = model.predict_proba(features)[0]
    spam_probability = float(proba[1]) 


    is_spam = spam_probability >= float(threshold)

    prediction = "spam" if is_spam else "ham"
    message    = "Spam detected! Be careful." if is_spam else "Looks safe (ham)."

    return jsonify({
        "prediction": prediction,
        "confidence": round(spam_probability, 4),
        "message":    message
    })


if __name__ == "__main__":
    
    print("🚀 ML API running on http://localhost:5000")
    app.run(host="0.0.0.0", port=5000, debug=True)
