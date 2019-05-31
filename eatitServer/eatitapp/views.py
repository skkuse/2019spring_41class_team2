from django.shortcuts import render
from rest_framework import viewsets
from eatitapp.serializers import VersionSerializer
from eatitapp.serializers import FoodSerializer
from eatitapp.serializers import IngredientSerializer
from eatitapp.models import Version
from eatitapp.models import Food
from eatitapp.models import Ingredient


# Create your views here.
class VersionViewSet(viewsets.ModelViewSet):
    queryset = Version.objects.all()
    serializer_class = VersionSerializer

class FoodViewSet(viewsets.ModelViewSet):
    queryset = Food.objects.all()
    serializer_class = FoodSerializer

class IngredientViewSet(viewsets.ModelViewSet):
    queryset = Ingredient.objects.all()
    serializer_class = IngredientSerializer

