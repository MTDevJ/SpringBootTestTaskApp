package com.test.springBootApp.Controller;

import com.test.springBootApp.Entity.Product;
import com.test.springBootApp.Service.ProductActionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ProductController  {

    private ProductActionServiceImpl service;

    @Autowired
    public void setService(ProductActionServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/goods")
    public Model getAll(Model model,Pageable pageable, @AuthenticationPrincipal User user) {
       return service.getAll(model, pageable, user);
    }

    @GetMapping("/addProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addProduct(Model model) {
        return service.add(model);
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable Integer id, Model model) {
        return service.edit(id, model);
    }

    @GetMapping("/deleteProduct/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteProduct(@PathVariable Integer id){
        return service.delete(id);
    }

    @PostMapping("/saveProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveProduct(@ModelAttribute @Valid Product product,
                              @RequestParam("productImage") MultipartFile productImage,
                              RedirectAttributes redirectAttributes) {
        return service.saveProduct(product, productImage, redirectAttributes);
    }

    @GetMapping("/searchByCategory")
    public String searchByCategory(@RequestParam Integer categoryId,
                                   Pageable pageable,
                                   Model model,
                                   @AuthenticationPrincipal User user){
        return service.searchByCategory(categoryId, pageable, model, user);
    }

    @GetMapping("/sortProduct/{sortColumn}")
    public String sortProduct(@PathVariable String sortColumn) {
        return service.sort(sortColumn);
    }

    @GetMapping("/sortProductByCategory")
    public String sortProductByCategory() {
        return service.sortProductByCategory();
    }

    @GetMapping("/setPageSizeGoods")
    public String setPageSizeFromModel(@RequestParam Integer pageSize) {
        return service.setPageSizeFromModel(pageSize);
    }

}
