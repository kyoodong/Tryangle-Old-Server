package com.gomson.gomsonServer.label;

import com.gomson.gomsonServer.domain.LabelImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public List<LabelImage> findList(Integer scoredBy) {
        return manager.createNativeQuery("SELECT l FROM LabelImage l WHERE l.scoredBy = :scoredBy ORDER BY l.id ASC OFFSET 0 LIMIT 10", LabelImage.class)
                .setParameter("scoredBy", scoredBy).getResultList();
    }
}
