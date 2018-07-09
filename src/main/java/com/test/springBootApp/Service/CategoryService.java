package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<Category> getAll(Pageable pageable) ;

    Boolean saveCategory(Category category);

    void delete(Integer id);

    Category findOne(Integer id);
}
