from django.shortcuts import render
from forms import UserForm
from django.contrib.auth import login
from django.http import HttpResponseRedirect, HttpResponse
from monte import futurePrice
from django.contrib.auth.decorators import login_required
from django.http import JsonResponse
import json
from models import UserProfile

# Iterations for Monte carlo simulation
ITERATIONS = 100


def adduser(request):
    if request.method == "POST":
        form = UserForm(request.POST)
        if form.is_valid():
            new_user = User.objects.create_user(**form.cleaned_data)
            login(new_user)
            return HttpResponseRedirect('main.html')
    else:
        form = UserForm()

    return render(request, 'adduser.html', {'form': form})

@login_required
def price(request):
    if not request.user.is_superuser:
        profile = UserProfile.objects.get(user=request.user)
        profile.calls -= 1
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
                     'price':round(value,3)}
    return JsonResponse(response_data)
