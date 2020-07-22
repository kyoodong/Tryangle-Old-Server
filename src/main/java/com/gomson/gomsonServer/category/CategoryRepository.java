package com.gomson.gomsonServer.category;

import com.gomson.gomsonServer.domain.Category;
import com.gomson.gomsonServer.domain.LabelImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager manager;

    public String save(Category category) {
        manager.persist(category);
        return category.getName();
    }

    public List<Category> findAll() {
        return manager.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }
}
