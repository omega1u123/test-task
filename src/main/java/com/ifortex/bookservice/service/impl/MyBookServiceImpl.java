package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repository.BookRepository;
import com.ifortex.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class MyBookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Map<String, Long> getBooks() {
        var books = bookRepository.findAll();
        Map<String, Long> genreCount = new HashMap<>();
        for (Book b : books){
            for(String g : b.getGenres()){
                if(!genreCount.containsKey(g)){
                    genreCount.put(g, 1L);
                }else{
                    genreCount.put(g, genreCount.get(g) + 1);
                }
            }
        }
        return genreCount.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        return bookRepository.findAllByCriteria(searchCriteria);
    }
}
