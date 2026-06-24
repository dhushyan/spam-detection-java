from django.shortcuts import render, redirect
from .models import Message
from .forms import MessageForm

import pickle
import os

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

model      = pickle.load(open(os.path.join(BASE_DIR, 'detector/model.pkl'),      'rb'))
vectorizer = pickle.load(open(os.path.join(BASE_DIR, 'detector/vectorizer.pkl'), 'rb'))
threshold  = pickle.load(open(os.path.join(BASE_DIR, 'detector/threshold.pkl'),  'rb'))

print(f"✅ Loaded model with spam threshold: {threshold}")


def predict_message(message):
    vec          = vectorizer.transform([message])
    spam_prob    = model.predict_proba(vec)[0][1]      # probability of spam
    result       = "Spam" if spam_prob >= threshold else "Ham"
    probability  = round(spam_prob * 100, 2)
    return result, probability


def Home(request):
    result      = None
    probability = None

    if request.method == 'POST':
        form = MessageForm(request.POST)
        if form.is_valid():
            message             = form.cleaned_data['text']
            result, probability = predict_message(message)
            Message.objects.create(text=message, result=result)
    else:
        form = MessageForm()

    messages = Message.objects.all().order_by('-created_at')

    return render(request, 'home.html', {
        'form':        form,
        'result':      result,
        'probability': probability,
        'messages':    messages,
    })


def dashboard(request):
    total        = Message.objects.count()
    spam_count   = Message.objects.filter(result='Spam').count()
    ham_count    = Message.objects.filter(result='Ham').count()
    spam_percentage = 0

    if total > 0:
        spam_percentage = round((spam_count / total) * 100, 2)

    messages = Message.objects.all().order_by('-created_at')

    return render(request, 'dashboard.html', {
        'total':           total,
        'spam_count':      spam_count,
        'ham_count':       ham_count,
        'spam_percentage': spam_percentage,
        'messages':        messages,
    })


def delete_message(request, id):
    if request.method == "POST":
        Message.objects.filter(id=id).delete()
    return redirect('dashboard')


def clear_history(request):
    if request.method == "POST":
        Message.objects.all().delete()
    return redirect('dashboard')