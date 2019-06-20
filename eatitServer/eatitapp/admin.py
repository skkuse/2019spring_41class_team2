from django.contrib import admin
from eatitapp.models import *

# Register your models here.
# admin.py

from django.contrib import admin


class UserAdmin(admin.ModelAdmin):
    list_display = ('id', 'nickname', 'name', 'profile_image', 'evaluate',)
    fields = ('nickname', 'name', 'profile_image', 'evaluate',)

class FoodAdmin(admin.ModelAdmin):
    list_display = ('id', 'name', 'photo', 'price', 'prop_hot',
                  'prop_sweet', 'prop_sour', 'prop_cal',
                  'prop_soup', 'prop_main', 'prop_temp',)
    fields = ('name', 'photo', 'price', 'prop_hot',
                  'prop_sweet', 'prop_sour', 'prop_cal',
                  'prop_soup', 'prop_main', 'prop_temp',)


class IngredientAdmin(admin.ModelAdmin):
    list_display = ('id', 'fid', 'name',)
    fields = ('fid', 'name',)

admin.site.register(User, UserAdmin)
admin.site.register(Food, FoodAdmin)
admin.site.register(Ingredient, IngredientAdmin)
