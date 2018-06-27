package com.test.springBootApp.Controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorMessage","error-404\n Запрашиваемый ресурс не обнаружен!");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorMessage", "error-500\n Внутренняя ошибка сервера");
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorMessage", "error-403\n В доступе отказано!");
            }
        }
        return "/errorPage";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
