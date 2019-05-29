from django.db import models

# Create your models here.
'''
class eatit(models.Model):
    Title = models.CharField('TITLE', max_length=50)
    Content = models.TextField('CONTENT')

    def __str__(self):
        return self.Title
'''

class Version(models.Model):
    version = models.CharField(max_length=10)
    
    def __str__(self):
        return self.version

class Food(models.Model):
    name = models.CharField(max_length=100)
    photo = models.ImageField(blank=True)
    price = models.IntegerField(default=0)
    prop_hot = models.IntegerField(default=0)
    prop_sweet = models.IntegerField(default=0)
    prop_sour = models.IntegerField(default=0)
    prop_cal = models.IntegerField(default=0)
    prop_soup = models.IntegerField(default=0)
    prop_main = models.IntegerField(default=0)
    prop_temp = models.IntegerField(default=0)

'''
class User(models.Model):
    naver_id = models.CharField(max_length=255)
    

    def __str__(self):
        return self.naver_id
'''