package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Category;
import com.test.springBootApp.Exception.NotFoundException;
import com.test.springBootApp.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class CategoryActionServiceImpl implements ActionService, CategoryService {

    private CategoryRepository repository;
    private String sortNameMethod = "ASC";
    private String sortColumn = "categoryId";
    private Page<Category> categories;
    private Integer pageSize = 10;

    @Autowired
    public void setRepository(CategoryRepository repository) {
        this.repository = repository;
    }

    public void setSortNameMethod(String sortNameMethod) {
        this.sortNameMethod = sortNameMethod;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Model getAll(Model model, Pageable pageable, User user) {
        categories = filterAndSort(pageable);
        initModel(model,user);
        return model;
    }

    public String add(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("model","ADD");
        return "/actions/actionCategory";
    }

    public String edit(Integer id, Model model) {
        model.addAttribute("category", repository.findById(id));
        model.addAttribute("model","EDIT");
        return "/actions/actionCategory";
    }

    public String delete(Integer id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new NotFoundException();
        }
        return "redirect:/categories";
    }

    public String saveCategory(Category category, RedirectAttributes requestAttribute) {
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

    public String sort(String sortColumn) {
        this.setSortColumn(sortColumn);
        if (this.sortNameMethod.equals("ASC"))
            this.setSortNameMethod("DESC");
        else
            this.setSortNameMethod("ASC") ;
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

    public String setPageSizeFromModel(Integer pageSize) {
        if (pageSize == null)
            this.setPageSize(this.pageSize);
        else
            this.setPageSize(pageSize);
        return "redirect:/categories";
    }


}
