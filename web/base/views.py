from django.shortcuts import render,redirect

def home(request):
    if request.user.is_authenticated():
        return render(request,'price.html')
    else:
        return redirect('/accounts/login/')

def register(request):
    return render(request,'registration/registration_form.html')
