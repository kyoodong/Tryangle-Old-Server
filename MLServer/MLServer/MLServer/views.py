from django.shortcuts import render
from MLServer.MLServer.models import User
from rest_framework import viewsets, permissions, authentication
from rest_framework.views import APIView
from rest_framework.response import Response
from MLServer.MLServer.serializers import UserSerializer
import os
import sys

ROOT_DIR = "gomson"
sys.path.append(os.path.join(ROOT_DIR, "codes/coco/"))  # To find local version
sys.path.append(ROOT_DIR)  # To find local version

from mrcnn import utils, visualize
import mrcnn.model as modellib
from process.pose import CVPoseEstimator, PoseGuider, CvClassifier, HumanPose

import coco


MODEL_DIR = os.path.join(ROOT_DIR, "logs")
COCO_MODEL_PATH = os.path.join(ROOT_DIR, "mask_rcnn_coco.h5")
cv_estimator = CVPoseEstimator()
pose_classifier = CvClassifier()


class InferenceConfig(coco.CocoConfig):
    # Set batch size to 1 since we'll be running inference on
    # one image at a time. Batch size = GPU_COUNT * IMAGES_PER_GPU
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1
    NUM_CLASSES = 1 + 81  # COCO has 80 classes + sky


config = InferenceConfig()
model = modellib.MaskRCNN(mode="inference", model_dir=MODEL_DIR, config=config)
model.load_weights(COCO_MODEL_PATH, by_name=True)

cv_estimator = CVPoseEstimator()
pose_classifier = CvClassifier()


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
        user = [user for user in User.objects.all()]
        # user = UserSerializer(User.objects.all())
        return Response(user)
