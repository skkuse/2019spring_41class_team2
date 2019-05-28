from django.db import models

# Create your models here.
class eatit(models.Model):
    Title = models.CharField('TITLE', max_length=50)
    Content = models.TextField('CONTENT')

    def __str__(self):
        return self.Title
