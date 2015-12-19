from django.db import models
from django.contrib.auth.models import User
import datetime
from dateutil.relativedelta import relativedelta


class UserProfile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    calls = models.IntegerField(default=0)
    
    def __unicode__(self):
        return self.user.username


    def get_remaining(self):
        return 5 + relativedelta(
            datetime.datetime.today(),
            self.user.date_joined.replace(
                tzinfo=None)).months * 5 - self.calls
