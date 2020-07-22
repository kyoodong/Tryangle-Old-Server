package com.gomson.gomsonServer.label;

import com.gomson.gomsonServer.category.CategoryRepository;
import com.gomson.gomsonServer.domain.Category;
import com.gomson.gomsonServer.domain.LabelImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LabelImageService {

    private final LabelImageRepository labelImageRepository;
    private final CategoryRepository categoryRepository;

    // Autowired 안 해줘도 알아서 넣어주긴 함
    // Class에 @AllArgsConstructor 혹은 @RequiredArgsConstructor 을 달아 주고, 생성자를 지우는 방식 권장
    @Autowired
    public LabelImageService(LabelImageRepository labelImageRepository, CategoryRepository categoryRepository) {
        this.labelImageRepository = labelImageRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * 이미지 추가
     * @param images
     * @param categoryName
     * @return
     */
    @Transactional
    public boolean add(MultipartFile[] images, String categoryName) {
        // 카테고리 저장
        Category category = new Category(categoryName);
        categoryRepository.save(category);

        // 카테고리에 사진 추가
        List<LabelImage> labelImageList = new ArrayList<>();
        int index = 0;
        for (MultipartFile image : images) {
            try {
                File directory = ResourceUtils.getFile("classpath:static/training_image");
                File file = new File(directory, image.getOriginalFilename());
                if (file.exists())
                    continue;

                image.transferTo(file);
                LabelImage labelImage = new LabelImage(image.getOriginalFilename(), category, 0, index % 3, null);
                labelImageList.add(labelImage);
                index++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return labelImageRepository.save(labelImageList);
    }

    /**
     * 이미지 조회
     * @param scoredBy 채점자
     * @return
     */
    public List<LabelImage> findList(String category, Integer scoredBy, int offset, int limit) {
        return labelImageRepository.findList(category, scoredBy, offset, limit);
    }

    @Transactional
    public Boolean scoreImage(String imageId, int score) {
        return labelImageRepository.scoreImage(imageId, score);
    }

    public List<LabelImage> loadUnlabeledImageList(String category, Integer scoredBy, int limit) {
        return labelImageRepository.loadUnlabeledImageList(category, scoredBy, limit);
    }

    public List<LabelImage> loadLastScoredImageList(String category, Integer scoredBy,
                                                    LocalDateTime anchorDatetime, int limit) {
        return labelImageRepository.loadLastScoredImageList(category, scoredBy, anchorDatetime, limit);
    }
}
