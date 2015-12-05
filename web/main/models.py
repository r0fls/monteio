from django.db import models
from django.contrib.auth.models import User
import datetime
from dateutil import relativedelta

class UserProfile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    calls = models.IntegerField()
    #months = relativedelta(datetime.datetime.now(),user.date_joined())

# Create your models here.
