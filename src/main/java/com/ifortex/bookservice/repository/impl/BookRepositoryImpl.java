package com.ifortex.bookservice.repository.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> findAll() {
        String query = "SELECT b FROM Book b";
        return entityManager.createQuery(query, Book.class).getResultList();
    }

    @Override
    public List<Book> findAllByCriteria(SearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> book = query.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getTitle() != null) {
            predicates.add(cb.like(cb.lower(book.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"));
        }

        if (criteria.getAuthor() != null) {
            predicates.add(cb.like(cb.lower(book.get("author")), "%" + criteria.getAuthor().toLowerCase() + "%"));
        }

        if (criteria.getDescription() != null) {
            predicates.add(cb.like(cb.lower(book.get("description")), "%" + criteria.getDescription().toLowerCase() + "%"));
        }

        if (criteria.getGenre() != null) {
            predicates.add(cb.like(cb.lower(book.get("genres").as(String.class)), "%" + criteria.getGenre().toLowerCase() + "%"));
        }

        if (criteria.getYear() != null) {
            LocalDateTime startOfYear = LocalDateTime.of(criteria.getYear(), 1, 1, 0, 0);
            LocalDateTime endOfYear = startOfYear.plusYears(1).minusSeconds(1);
            predicates.add(cb.between(book.get("publicationDate"), startOfYear, endOfYear));
        }

        query.select(book).where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
