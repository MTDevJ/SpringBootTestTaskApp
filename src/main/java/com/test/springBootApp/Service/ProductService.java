package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Category;
import com.test.springBootApp.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ProductService {

    Page<Product> getAll(Pageable pageable);

    Map<Boolean, String> saveProduct(Product product, MultipartFile image) throws IOException;

    void searchByCategory(Integer categoryId, Pageable pageable);

    void sortProductByCategory();

    Boolean delete(Integer id);

    Iterable<Category> findAllCategories();

    Page<Product> filterAndSort(Pageable pageable);
}
