from django.shortcuts import render
from forms import UserForm
from django.contrib.auth import login
from django.http import HttpResponseRedirect
from monte import futurePrice
from django.contrib.auth.decorators import login_required

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

@login_required()
def price(request):
    if request.method == 'POST':
        days = int(request.POST.get('days',''))
        strike = float(request.POST.get('strike',''))
        f=futurePrice.futurePrice(request.POST.get('ticker',''))
        x = 0
        for i in range(ITERATIONS):
            x=x+futurePrice.price(f,days,strike)
        value = x/ITERATIONS
        return render(request, 'price.html',{'{ticker}{days}{price}': value})
    else:
        return render(request, 'price.html')
