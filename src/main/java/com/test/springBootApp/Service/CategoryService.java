package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Category;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface CategoryService {

    String saveCategory(Category category, RedirectAttributes requestAttribute);
}
