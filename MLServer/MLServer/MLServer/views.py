from MLServer.MLServer.tryangle_models import User
from rest_framework import viewsets, permissions, authentication, status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.parsers import JSONParser, MultiPartParser
from MLServer.MLServer.serializers import UserSerializer
from process.segmentation import MaskRCNN

import os
import sys
import cv2
import numpy as np
import skimage.io

from process.pose import CVPoseEstimator, PoseGuider, CvClassifier, HumanPose

cv_estimator = CVPoseEstimator()
pose_classifier = CvClassifier()

# cv_estimator = CVPoseEstimator()
# pose_classifier = CvClassifier()
mask_rcnn = MaskRCNN()


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]


def segment(image):
    ### 밝기 보정 소스
    gamma = 0.4
    lookUpTable = np.empty((1, 256), np.uint8)
    for i in range(256):
        lookUpTable[0, i] = np.clip(pow(i / 255.0, gamma) * 255.0, 0, 255)
    res = cv2.LUT(image, lookUpTable)

    ### CLAHE
    lab = cv2.cvtColor(image, cv2.COLOR_BGR2LAB)
    lab_planes = cv2.split(lab)
    clahe = cv2.createCLAHE(2.0, (8, 8))
    lab_planes[0] = clahe.apply(lab_planes[0])
    lab = cv2.merge(lab_planes)
    clahe_image = cv2.cvtColor(lab, cv2.COLOR_LAB2BGR)

    # 현재까지는 clahe 가 가장 보기 좋음
    # 적어도 effective line 을 찾기에는 유용함
    image = clahe_image

    # Run detection
    results = mask_rcnn.detect(image)
    r = results[0]
    return r


class ImageSegmentationView(APIView):
    # authentication_classes = [authentication.TokenAuthentication]
    # permission_classes = [permissions.IsAuthenticated]

    def post(self, request, format=None):
        file = request.FILES['file']
        image = cv2.imdecode(np.frombuffer(file.file.read(), np.uint8), cv2.IMREAD_UNCHANGED)
        # image = cv2.resize(image, None, fx=0.2, fy=0.2, interpolation=cv2.INTER_AREA)

        r = segment(image)
        return Response(r, status=status.HTTP_200_OK, content_type='application/json')
