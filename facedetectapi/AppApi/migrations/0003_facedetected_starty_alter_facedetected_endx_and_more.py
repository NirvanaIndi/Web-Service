# Generated by Django 4.0.3 on 2022-06-05 08:02

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('AppApi', '0002_facedetected'),
    ]

    operations = [
        migrations.AddField(
            model_name='facedetected',
            name='StartY',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='facedetected',
            name='EndX',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='facedetected',
            name='EndY',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='facedetected',
            name='StartX',
            field=models.IntegerField(default=0),
        ),
    ]
