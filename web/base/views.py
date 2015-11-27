from django.shortcuts import render

def home(request):
    return render(request,'index.html')

def register(request):
    return render(request,'registration/registration_form.html')
