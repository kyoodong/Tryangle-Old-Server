package com.gomson.gomsonServer.label;

import com.gomson.gomsonServer.domain.Category;
import com.gomson.gomsonServer.domain.LabelImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public class LabelImageRepository {

    @PersistenceContext
    private EntityManager manager;

    public boolean save(List<LabelImage> labelImageList) {
        try {
            for (LabelImage labelImage : labelImageList) {
                manager.persist(labelImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<LabelImage> findList(String category, Integer scoredBy, int offset, int limit) {
        return manager.createQuery("SELECT l FROM LabelImage l WHERE l.category = :category AND l.scoredBy = :scoredBy ORDER BY l.id ASC", LabelImage.class)
                .setParameter("scoredBy", scoredBy)
                .setParameter("category", new Category(category))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    public List<LabelImage> loadUnlabeledImageList(String category, Integer scoredBy, int limit) {
        return manager.createQuery("SELECT l FROM LabelImage l WHERE l.category = :category AND" +
                " l.scoredBy = :scoredBy AND l.score = 0", LabelImage.class)
                .setParameter("category", new Category(category))
                .setParameter("scoredBy", scoredBy)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<LabelImage> loadLastScoredImageList(String category, Integer scoredBy,
                                                    LocalDateTime anchorDatetime, int limit) {
        return manager.createQuery("SELECT l FROM LabelImage l WHERE l.category = :category AND " +
                "l.scoredBy = :scoredBy AND l.score <> 0 AND l.scoredAt < :anchorDatetime ORDER by l.scoredAt DESC",
                LabelImage.class)
                .setParameter("category", new Category(category))
                .setParameter("scoredBy", scoredBy)
                .setParameter("anchorDatetime", anchorDatetime)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<LabelImage> loadNextScoredImageList(String category, Integer scoredBy,
                                                    LocalDateTime anchorDatetime, int limit) {
        return manager.createQuery("SELECT l FROM LabelImage l WHERE l.category = :category AND " +
                        "l.scoredBy = :scoredBy AND l.score <> 0 AND l.scoredAt > :anchorDatetime ORDER by l.scoredAt ASC",
                LabelImage.class)
                .setParameter("category", new Category(category))
                .setParameter("scoredBy", scoredBy)
                .setParameter("anchorDatetime", anchorDatetime)
                .setMaxResults(limit)
                .getResultList();
    }

    public Boolean scoreImage(String imageId, int score) {
        try {
            manager.createQuery("UPDATE LabelImage l SET l.score = :score, l.scoredAt = :now WHERE l.id = :imageId")
                    .setParameter("score", score)
                    .setParameter("now", LocalDateTime.now())
                    .setParameter("imageId", imageId)
                    .executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
