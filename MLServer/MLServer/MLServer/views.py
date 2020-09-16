from MLServer.MLServer.tryangle_models import User
from rest_framework import viewsets, permissions, authentication, status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.parsers import JSONParser, MultiPartParser
from MLServer.MLServer.serializers import UserSerializer

import cv2
import numpy as np
import process.image_api as api
from process.guider import Guider


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]


class ImageSegmentationView(APIView):
    # authentication_classes = [authentication.TokenAuthentication]
    # permission_classes = [permissions.IsAuthenticated]

    def post(self, request, format=None):
        file = request.FILES['file']
        image = cv2.imdecode(np.frombuffer(file.file.read(), np.uint8), cv2.IMREAD_UNCHANGED)
        # image = cv2.resize(image, None, fx=0.2, fy=0.2, interpolation=cv2.INTER_AREA)

        r = api.segment(image)
        return Response(r, status=status.HTTP_200_OK, content_type='application/json')


class ImageGuideView(APIView):
    def post(self, request, format=None):
        file = request.FILES['file']
        image = cv2.imdecode(np.frombuffer(file.file.read(), np.uint8), cv2.IMREAD_UNCHANGED)
        guider = Guider(image)
        body = {"guide": guider.guide_list}
        print(body)
        return Response(str(body), status=status.HTTP_200_OK, content_type='application/json')

