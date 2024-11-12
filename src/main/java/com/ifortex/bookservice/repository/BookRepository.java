package com.ifortex.bookservice.repository;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;

import java.util.List;

public interface BookRepository {

    List<Book> findAll();

    List<Book> findAllByCriteria(SearchCriteria criteria);

}
