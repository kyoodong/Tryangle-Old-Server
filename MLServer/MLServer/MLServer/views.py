import cv2
import numpy as np
from rest_framework import viewsets, permissions, status
from rest_framework.response import Response
from rest_framework.views import APIView

import process.image_api as api
from MLServer.MLServer.serializers import UserSerializer
from MLServer.MLServer.tryangle_models import User
from process.guider.guider import SimpleGuider, ComplexGuider
from process.component import LineComponent
import process.color as color_guide
from process.object import Human, Object
import matplotlib.pyplot as plt
from retrieval.feature.tf_extractor import extract_individual
from retrieval.image_retrieval import certain_retrieval
from background_classification.bg_cls import get_bg


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]


# 이미지 세그멘테이션
class ImageSegmentationView(APIView):
    # authentication_classes = [authentication.TokenAuthentication]
    # permission_classes = [permissions.IsAuthenticated]

    def post(self, request, format=None):
        file = request.FILES['file']
        image = cv2.imdecode(np.frombuffer(file.file.read(), np.uint8), cv2.IMREAD_UNCHANGED)
        # image = cv2.resize(image, None, fx=0.2, fy=0.2, interpolation=cv2.INTER_AREA)

        r = api.segment(image)
        body = {'result': list()}
        for index in range(r['rois'].shape[0]):
            body['result'].append({
                'rois': list(r['rois'][index]),
                'class_ids': r['class_ids'][index],
                'mask': [list(y) for y in r['masks'][index]]
            })
            # plt.imshow(r['masks'][:, :, index])
            # plt.show()
        return Response(str(body), status=status.HTTP_200_OK, content_type='application/json')


# App
class ImageGuideView(APIView):
    def post(self, request, format=None):
        file = request.FILES['file']
        image = cv2.imdecode(np.frombuffer(file.file.read(), np.uint8), cv2.IMREAD_UNCHANGED)
        dominant_colors = list(color_guide.find_dominant_colors(image))
        guider = SimpleGuider()
        guider.guide(image, False)

        component_list = list()
        mask = np.zeros((image.shape[0], image.shape[1]), np.uint8)
        count = 0
        for component in guider.component_list:
            if isinstance(component, LineComponent):
                component_list.append(component)
            else:
                component_dict = dict()
                data = dict()

                data['id'] = component.id
                data['class'] = component.object.clazz
                data['center_point_x'] = component.object.center_point[0]
                data['center_point_y'] = component.object.center_point[1]
                data['area'] = component.object.area
                mask = np.add(mask, (np.left_shift(component.object.mask.astype(np.uint8), count)))
                data['roi'] = list(component.object.roi)
                data['guide_list'] = component.guide_list
                component_dict['ObjectComponent'] = data
                component_list.append(component_dict)
                count += 1

        background = get_bg(image)
        body = {
            "component_list": component_list,
            "dominant_color_list": dominant_colors,
            "image_size": list(image.shape[:2]),
            "cluster": guider.cluster,
            "mask": [list(y) for y in mask],
            "background": background
        }
        return Response(str(body), status=status.HTTP_200_OK, content_type='application/json')


# 사진 DB 정리할 때 필요한 가이드 추출
class ImageComplexGuideView(APIView):
    def post(self, request, format=None):
        file = request.FILES['file']
        image = cv2.imdecode(np.frombuffer(file.file.read(), np.uint8), cv2.IMREAD_UNCHANGED)
        dominant_colors = list(color_guide.find_dominant_colors(image))
        guider = ComplexGuider()
        guider.guide(image, False)

        component_list = list()
        mask = np.zeros((image.shape[0], image.shape[1]), np.uint8)
        count = 0
        for component in guider.component_list:
            if isinstance(component, LineComponent):
                component_list.append(component)
            else:
                component_dict = dict()
                data = dict()

                if isinstance(component.object, Human):
                    data['pose'] = component.object.pose_class
                    pose_points = list()
                    for i in range(component.object.pose.shape[0]):
                        if component.object.pose[i][2] > 0.05:
                            pose_points.append(list(component.object.pose[i, :2]))
                        else:
                            pose_points.append(None)
                    data['pose_points'] = pose_points

                data['id'] = component.id
                data['class'] = component.object.clazz
                data['center_point_x'] = component.object.center_point[0]
                data['center_point_y'] = component.object.center_point[1]
                data['area'] = component.object.area
                mask = np.add(mask, (np.left_shift(component.object.mask.astype(np.uint8), count)))
                data['roi'] = list(component.object.roi)
                data['guide_list'] = component.guide_list
                component_dict['ObjectComponent'] = data
                component_list.append(component_dict)
                count += 1

        background = get_bg(image)
        body = {
            "component_list": component_list,
            "dominant_color_list": dominant_colors,
            "image_size": list(image.shape[:2]),
            "cluster": guider.cluster,
            "mask": [list(y) for y in mask],
            "background": background
        }
        return Response(str(body), status=status.HTTP_200_OK, content_type='application/json')


class ImageExtractFeatureView(APIView):
    def post(self, request, format=None):
        file = request.FILES['file']
        name = file.name
        image = cv2.imdecode(np.frombuffer(file.file.read(), np.uint8), cv2.IMREAD_UNCHANGED)
        result = extract_individual(image, name)
        return Response(result, status=status.HTTP_200_OK, content_type='application/json')


class SortForegroundImageView(APIView):
    def post(self, request, format=None):
        file = request.FILES['file']
        image_list = request.POST.getlist('imageList')
        image_list = list(map(lambda x: x.replace('"', ''), image_list))
        image = cv2.imdecode(np.frombuffer(file.file.read(), np.uint8), cv2.IMREAD_UNCHANGED)
        result = certain_retrieval(image, image_list, 'output')
        return Response(result, status=status.HTTP_200_OK, content_type='application/json')


class ImageBackgroundExtractFeatureView(APIView):
    def post(self, request, format=None):
        file = request.FILES['file']
        image = cv2.imdecode(np.frombuffer(file.file.read(), np.uint8), cv2.IMREAD_UNCHANGED)
        result = get_bg(image)
        return Response(result, status=status.HTTP_200_OK, content_type='application/json')
