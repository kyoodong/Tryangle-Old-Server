from django.db import models


class User(models.Model):
    class Meta:
        app_label = 'tryangle'
    id = models.CharField(db_column='id', max_length=300, primary_key=True)


class Image(models.Model):
    class Meta:
        app_label = 'tryangle'

    id = models.AutoField(db_column='id', primary_key=True)
    url = models.CharField(db_column='url', max_length=300)
    author = models.ForeignKey(User, on_delete=models.CASCADE, db_column='author')
    composition_problem_count = models.IntegerField(db_column="composition_problem_count")
    score = models.IntegerField(db_column="score")


class Object(models.Model):
    class Meta:
        app_label = 'tryangle'

    id = models.AutoField(db_column='id', primary_key=True)
    name = models.CharField(db_column='name', max_length=100)


class Pose(models.Model):
    class Meta:
        app_label = 'tryangle'

    id = models.AutoField(db_column='id', primary_key=True)
    name = models.CharField(db_column='name', max_length=45)


class Color(models.Model):
    class Meta:
        app_label = 'tryangle'

    id = models.AutoField(db_column='id', primary_key=True)
    red = models.IntegerField(db_column='red')
    green = models.IntegerField(db_column='green')
    blue = models.IntegerField(db_column='blue')


class UserImageReferLog(models.Model):
    class Meta:
        app_label = 'tryangle'

    id = models.AutoField(db_column='id', primary_key=True)
    user_id = models.ForeignKey(User, on_delete=models.CASCADE, db_column='user_id')
    image_id = models.ForeignKey(Image, on_delete=models.CASCADE, db_column='image_id')
    count = models.IntegerField(db_column='count')


class ImageObject(models.Model):
    class Meta:
        app_label = 'tryangle'

    id = models.AutoField(db_column='id', primary_key=True)
    image_id = models.ForeignKey(Image, on_delete=models.CASCADE, db_column='image_id')
    object_id = models.ForeignKey(Object, on_delete=models.CASCADE, db_column='object_id')
    center_x = models.FloatField(db_column='center_x')
    center_y = models.FloatField(db_column='center_y')
    area = models.FloatField(db_column='area')


class ImageEffectiveLine(models.Model):
    class Meta:
        app_label = 'tryangle'

    id = models.AutoField(db_column='id', primary_key=True)
    image_id = models.ForeignKey(Image, on_delete=models.CASCADE, db_column='image_id')
    start_x = models.FloatField(db_column='start_x')
    start_y = models.FloatField(db_column='start_y')
    end_x = models.FloatField(db_column='end_x')
    end_y = models.FloatField(db_column='end_y')


class ImageDominantColor(models.Model):
    class Meta:
        app_label = 'tryangle'

    id = models.AutoField(db_column='id', primary_key=True)
    image_id = models.ForeignKey(Image, on_delete=models.CASCADE, db_column='image_id')
    color_id = models.ForeignKey(Color, on_delete=models.CASCADE, db_column='color_id')


class ObjectPersonPose(models.Model):
    class Meta:
        app_label = 'tryangle'

    id = models.AutoField(db_column='id', primary_key=True)
    object_id = models.ForeignKey(Object, on_delete=models.CASCADE, db_column='object_id')
    pose_id = models.ForeignKey(Pose, on_delete=models.CASCADE, db_column='pose_id')
