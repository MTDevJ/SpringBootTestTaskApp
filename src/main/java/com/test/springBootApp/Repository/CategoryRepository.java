package com.test.springBootApp.Repository;

import com.test.springBootApp.Entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends PagingAndSortingRepository<Category,Integer> {

    Category findBycategoryName(String categoryName);
}
