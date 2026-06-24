import pandas as pd
import pickle
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
import os

import os

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
file_path = os.path.join(BASE_DIR, 'emails.csv')

dataset = pd.read_csv(file_path)

vectorizer = TfidfVectorizer()
X = vectorizer.fit_transform(dataset['text'])
y = dataset['spam']

model = MultinomialNB()
model.fit(X, y)


save_dir = os.path.join(BASE_DIR, "detector")

pickle.dump(model, open(os.path.join(save_dir, "model.pkl"), "wb"))
pickle.dump(vectorizer, open(os.path.join(save_dir, "vectorizer.pkl"), "wb"))

print("Model trained & saved inside detector folder!")