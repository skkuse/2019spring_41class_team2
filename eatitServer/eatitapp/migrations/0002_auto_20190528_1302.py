# Generated by Django 2.2.1 on 2019-05-28 04:02

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('eatitapp', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Version',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('version', models.CharField(max_length=10)),
            ],
        ),
        migrations.DeleteModel(
            name='eatit',
        ),
    ]
