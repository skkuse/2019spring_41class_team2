from rest_framework import serializers
from eatitapp.models import Version

class VersionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Version
        fields = ('version',)