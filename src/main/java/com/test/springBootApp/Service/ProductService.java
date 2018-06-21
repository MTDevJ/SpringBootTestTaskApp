package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

public interface ProductService {

    String saveProduct(Product product, MultipartFile productImage, RedirectAttributes redirectAttributes) throws IOException;

    String searchByCategory(Integer categoryId, Pageable pageable, Model model, User user);

    String sortProductByCategory();

    String delete(Integer id, RedirectAttributes redirectAttributes);

}
