package com.test.springBootApp.Service;


public interface ActionService {

    void sort(String sortColumn);

    void setPageSizeFromModel(Integer pageSize);
}
