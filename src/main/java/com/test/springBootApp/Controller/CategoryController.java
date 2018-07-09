package com.test.springBootApp.Controller;

import com.test.springBootApp.Entity.Category;
import com.test.springBootApp.Service.CategoryActionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private Page<Category> categories;
    private String sortColumn;

    @Autowired
    public void setService(CategoryActionServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/categories")
    public Model getAll(Model model,Pageable pageable, @AuthenticationPrincipal User user) {
        categories = service.filterAndSort(pageable);
        initModel(model,user);
        return model;
    }

    @GetMapping("/addCategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    String addCategory(Model model){
        model.addAttribute("category", new Category());
        model.addAttribute("model","ADD");
        return "/actions/actionCategory";
    }

    @GetMapping("/deleteCategory/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    String deleteCategory(@PathVariable Integer id){
        service.delete(id);
        return  "redirect:/categories";
    }

    @GetMapping("/editCategory/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    String editCategory(@PathVariable Integer id ,Model model){
        model.addAttribute("category", service.findOne(id));
        model.addAttribute("model","EDIT");
        return "/actions/actionCategory";
    }

    @PostMapping("/saveCategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveCategory(@ModelAttribute @Valid Category category,
                               RedirectAttributes redirectAttributes) {

        if (!service.saveCategory(category)) {
            redirectAttributes.addFlashAttribute("message","Категория '" + category.getCategoryName() + "' уже существует!");
            return "redirect:/addCategory";
        }
        return "redirect:/categories";
    }

    @GetMapping("/sortCategory/{sortColumn}")
    public String sortCategory(@PathVariable String sortColumn) {
        service.sort(sortColumn);
        return "redirect:/categories";
    }

    @GetMapping("/setPageSizeCategories")
    public String setPageSizeFromModel(@RequestParam Integer pageSize) {
        service.setPageSizeFromModel(pageSize);
        return "redirect:/categories";
    }

    private void initModel(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("pageNumber", categories.getNumber());
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("totalElements", categories.getTotalElements());
        model.addAttribute("size", categories.getSize());
        model.addAttribute("categoryList",categories.getContent());
        model.addAttribute("sortColumn",sortColumn);
        model.addAttribute("user", user);
    }
}
