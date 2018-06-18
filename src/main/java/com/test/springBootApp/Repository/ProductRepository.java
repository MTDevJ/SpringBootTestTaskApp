package com.test.springBootApp.Repository;

import com.test.springBootApp.Entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProductRepository extends PagingAndSortingRepository<Product,Integer> {

   @Query("SELECT p FROM Product p INNER JOIN p.productCategory c ORDER BY c.categoryName ASC")
    Page<Product> sortByCategoryASC(Pageable pageable);

    @Query("SELECT p FROM Product p INNER JOIN p.productCategory c ORDER BY c.categoryName DESC")
    Page<Product> sortByCategoryDESC(Pageable pageable);

    @Query("SELECT p FROM Product p INNER JOIN p.productCategory c WHERE c.categoryId = ?1")
    Page<Product> findAllByCategoryIdOrderBySomeASC(Integer id, Pageable pageable);

    @Query("SELECT p FROM Product p INNER JOIN p.productCategory c WHERE c.categoryId = ?1")
    Page<Product> findAllByCategoryIdOrderBySomeDESC(Integer id, Pageable pageable);

    Product findByproductName(String productName);
}
