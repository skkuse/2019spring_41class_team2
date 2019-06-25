from django.db import models

# Create your models here.

class Version(models.Model):
    version = models.CharField(max_length=10)
    
    def __str__(self):
        return self.version

class Food(models.Model):
    id = models.IntegerField(primary_key=True)
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

class Ingredient(models.Model):
    id = models.IntegerField(primary_key=True)
    fid = models.ForeignKey(Food, on_delete=models.CASCADE)
    name = models.CharField(max_length=10)

class User(models.Model):
    id = models.IntegerField(primary_key=True)
    nickname = models.CharField(max_length=20)
    name = models.CharField(max_length=20)
    profile_image = models.CharField(max_length=200)
    evaluate = models.BooleanField(default=False)

class Cart(models.Model):
    uid = models.OneToOneField(User, on_delete=models.CASCADE, primary_key=True)
    size = models.IntegerField(default=0)
    items = models.TextField(null=True)

