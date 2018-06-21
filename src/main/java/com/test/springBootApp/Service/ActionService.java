package com.test.springBootApp.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface ActionService {

    Model getAll(Model model, Pageable pageable, User user);

    String add(Model model);

    String edit(Integer id, Model model);

    String sort(String sortColumn);

    String setPageSizeFromModel(Integer pageSize);
}
