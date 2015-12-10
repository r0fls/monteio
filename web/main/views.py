from django.shortcuts import render, redirect
from forms import UserForm
from django.contrib.auth import login, authenticate
from django.http import HttpResponseRedirect, HttpResponse
from monte import futurePrice
from django.contrib.auth.decorators import login_required
from django.http import JsonResponse
import json
from models import UserProfile

from models import User

# Iterations for Monte carlo simulation
ITERATIONS = 100


def adduser(request):
    if request.method == "POST":
        form = UserForm(request.POST)
        if form.is_valid():
            new_user = User.objects.create_user(**form.cleaned_data)
            up = UserProfile.objects.create(user=new_user)
            new_user = authenticate(username=request.POST['username'],
                                    password=request.POST['password'])
            login(request, new_user)
            return redirect('../accounts/profile')
    else:
        form = UserForm()

    return render(request, 'registration/registration_form.html', {'form': form})

@login_required
def price(request):
    if not request.user.is_superuser:
        profile = UserProfile.objects.get(user=request.user)
        calls = profile.calls
        if profile.calls > 0:
            profile.calls -= 1
            profile.save()
        else:
            return redirect('purchase.html')
    days = int(request.GET.get('days', ''))
    strike = float(request.GET.get('strike', ''))
    ticker = request.GET.get('ticker','').upper()
    putcall = request.GET.get('type','')
    f = futurePrice.futurePrice(request.GET.get('ticker', ''))
    #import pdb;pdb.set_trace()
    x = 0
    for i in range(ITERATIONS):
        x = x + futurePrice.price(f, days, strike,putcall[0])
    value = x / ITERATIONS
    response_data = {'ticker':str(ticker),
                     'days':str(days),
                     'strike':str(strike),
                     'type':putcall,
                     'price':round(value,3),
                     'calls':calls}
    return JsonResponse(response_data)
