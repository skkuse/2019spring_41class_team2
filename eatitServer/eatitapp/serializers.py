from rest_framework import serializers
from eatitapp.models import Version
from eatitapp.models import Food

class VersionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Version
        fields = ('version',)

class FoodSerializer(serializers.ModelSerializer):
    class Meta:
        model = Food
        fields = ('name', 'photo', 'price', 'prop_hot',
                  'prop_sweet', 'prop_sour', 'prop_cal',
                  'prop_soup', 'prop_main', 'prop_temp',)
