from django.shortcuts import render
from rest_framework import viewsets
from eatitapp.serializers import VersionSerializer
from eatitapp.models import Version

# Create your views here.
class VersionViewSet(viewsets.ModelViewSet):
    queryset = Version.objects.all()
    serializer_class = VersionSerializer