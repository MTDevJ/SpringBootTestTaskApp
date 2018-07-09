package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Category;
import com.test.springBootApp.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoryActionServiceImpl implements ActionService, CategoryService {

    private CategoryRepository repository;

    private Page<Category> categories;
    private String sortColumn = "categoryId";
    private String sortNameMethod = "ASC";
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

    public Page<Category> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void delete(Integer id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }
    }

    public Category findOne(Integer id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        }
        return null;
    }

    public Boolean saveCategory(Category category) {
        if (category.getCategoryId() == null) {
            if ( repository.findBycategoryName(category.getCategoryName().trim()) != null ) {
                return false;
            } else{
                repository.save(new Category(category.getCategoryName(), category.getCategoryDescription()));
            }
        } else {
            repository.save(category);
        }
        return true;
    }

    public Page<Category> filterAndSort(Pageable pageable) {
        Pageable page;
        switch (sortNameMethod) {
            case "ASC":
                page = PageRequest.of(pageable.getPageNumber(), pageSize,
                        Sort.by(Sort.Direction.ASC, sortColumn));
                break;
            case "DESC":
                page = PageRequest.of(pageable.getPageNumber(), pageSize,
                        Sort.by(Sort.Direction.DESC, sortColumn));
                break;
            default:
                page = PageRequest.of(pageable.getPageNumber(), pageSize,
                        Sort.by(Sort.Direction.ASC, sortColumn));
                break;
        }
        categories = repository.findAll(page);
        return categories;
    }

    public void sort(String sortColumn) {
        this.setSortColumn(sortColumn);
        if (this.sortNameMethod.equals("ASC"))
            this.setSortNameMethod("DESC");
        else
            this.setSortNameMethod("ASC") ;
    }

    public void setPageSizeFromModel(Integer pageSize) {
        if (pageSize == null)
            this.setPageSize(this.pageSize);
        else
            this.setPageSize(pageSize);
    }
}
