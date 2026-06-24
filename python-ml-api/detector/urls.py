from django.contrib import admin
from django.urls import path
from detector import views

urlpatterns = [
    path('admin/', admin.site.urls),


    path('', views.Home, name='home'),
    path('dashboard/', views.dashboard, name='dashboard'),


    path('delete/<int:id>/', views.delete_message, name='delete_message'),


    path('clear/', views.clear_history, name='clear_history'),
]