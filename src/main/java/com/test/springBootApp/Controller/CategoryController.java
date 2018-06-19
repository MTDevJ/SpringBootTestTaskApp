package com.test.springBootApp.Controller;

import com.test.springBootApp.Entity.Category;
import com.test.springBootApp.Service.CategoryActionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CategoryController {

    private CategoryActionServiceImpl service;

    @Autowired
    public void setService(CategoryActionServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/categories")
    public Model getAll(Model model,Pageable pageable, @AuthenticationPrincipal User user) {
        return service.getAll(model, pageable ,user);
    }

    @GetMapping("/addCategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    String addCategory(Model model){
        return service.add(model);
    }

    @GetMapping("/deleteCategory/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    String deleteCategory(@PathVariable Integer id){
        return service.delete(id);
    }

    @GetMapping("/editCategory/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    String editCategory(@PathVariable Integer id ,Model model){
        return service.edit(id, model);
    }

    @PostMapping("/saveCategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveCategory(@ModelAttribute @Valid Category category,
                               RedirectAttributes requestAttribute) {
        return service.saveCategory(category, requestAttribute);
    }

    @GetMapping("/sortCategory/{sortColumn}")
    public String sortCategory(@PathVariable String sortColumn) {
        return service.sort(sortColumn);
    }

    @GetMapping("/setPageSizeCategories")
    public String setPageSizeFromModel(@RequestParam Integer pageSize) {
        return service.setPageSizeFromModel(pageSize);
    }

}
