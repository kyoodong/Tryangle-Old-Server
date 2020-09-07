from rest_framework import serializers
from MLServer.MLServer.tryangle_models import User, Image


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ['id']


# class ImageSerializer(serializers.HyperlinkedModelSerializer):
#     class Meta:
#         model = Image
#         fields =
