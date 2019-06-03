from rest_framework import serializers
from eatitapp.models import Version
from eatitapp.models import Food
from eatitapp.models import Ingredient
from eatitapp.models import User

class VersionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Version
        fields = ('version',)

class FoodSerializer(serializers.ModelSerializer):
    class Meta:
        model = Food
        fields = ('id', 'name', 'photo', 'price', 'prop_hot',
                  'prop_sweet', 'prop_sour', 'prop_cal',
                  'prop_soup', 'prop_main', 'prop_temp',)

class IngredientSerializer(serializers.ModelSerializer):
    class Meta:
        model = Ingredient
        fields = ('id', 'fid', 'name',)

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'nickname', 'name', 'profile_image', 'evaluate',)
