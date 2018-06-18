package com.test.springBootApp.Controller;

import com.test.springBootApp.Entity.Category;
import com.test.springBootApp.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CategoryController {

    private CategoryRepository repository;
    private String sortNameMethod = "ASC";
    private String sortColumn = "categoryId";
    private Page<Category> categories;
    private Integer pageSize = 10;

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    @Autowired
    public void setRepository(CategoryRepository repository) {
        this.repository = repository;
    }

    public void setSortNameMethod(String sortNameMethod) {
        this.sortNameMethod = sortNameMethod;
    }

    @GetMapping("/categories")
    public String getAll(Model model,Pageable pageable, @AuthenticationPrincipal User user) {
        categories = filterAndSort(pageable);
        initModel(model,user);
        return "/categories";
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
        repository.deleteById(id);
        return "redirect:/categories";
    }

    @GetMapping("/editCategory/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    String editCategory(@PathVariable Integer id ,Model model){
        model.addAttribute("category", repository.findById(id));
        model.addAttribute("model","EDIT");
        return "/actions/actionCategory";
    }

    @PostMapping("/saveCategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveCategory(@ModelAttribute @Valid Category category, Errors errors,
                               RedirectAttributes requestAttribute) {
        if (errors.hasErrors()) {
            return "redirect:/categories";
        }
        if (category.getCategoryId() == null) {
            if ( repository.findBycategoryName(category.getCategoryName()) != null ) {
                requestAttribute.addFlashAttribute("message","Категория '" + category.getCategoryName() + "' уже существует!");
                return "redirect:/addCategory";
            } else{
                repository.save(new Category(category.getCategoryName(), category.getCategoryDescription()));
            }
        } else {
            repository.save(category);
        }
        return "redirect:/categories";
    }

    @GetMapping("/sortCategory/{sortColumn}")
    public String sortCategory(@PathVariable String sortColumn) {
        this.setSortColumn(sortColumn);
        if (this.sortNameMethod.equals("ASC"))
            this.setSortNameMethod("DESC");
        else
            this.setSortNameMethod("ASC") ;
        return "redirect:/categories";
    }

    @GetMapping("/setPageSizeCategories")
    public String setPageSizeFromModel(@RequestParam Integer pageSize) {
        if (pageSize == null)
            this.setPageSize(this.pageSize);
        else
            this.setPageSize(pageSize);
        return "redirect:/categories";
    }

    private Page<Category> filterAndSort(Pageable pageable) {
        Pageable page;
        switch (sortNameMethod) {
            case "ASC":
                page = PageRequest.of(pageable.getPageNumber(), pageSize,
                        Sort.by(Sort.Direction.ASC, sortColumn));
                categories = repository.findAll(page);
                break;
            case "DESC":
                page = PageRequest.of(pageable.getPageNumber(), pageSize,
                        Sort.by(Sort.Direction.DESC, sortColumn));
                categories = repository.findAll(page);
                break;
            default:
                page = PageRequest.of(pageable.getPageNumber(), pageSize,
                        Sort.by(Sort.Direction.ASC, sortColumn));
                categories = repository.findAll(page);
                break;
        }
        return categories;
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
