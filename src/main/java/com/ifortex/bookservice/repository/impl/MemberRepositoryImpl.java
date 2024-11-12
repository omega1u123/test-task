package com.ifortex.bookservice.repository.impl;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Member findMemberWithOldestRomanceBookAndMostRecentRegistration() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);
        Root<Member> member = query.from(Member.class);

        Join<Member, Book> borrowedBooks = member.join("borrowedBooks");

        Predicate romanceGenre = cb.like(cb.lower(borrowedBooks.get("genres").as(String.class)), "%romance%");
        Order oldestBook = cb.asc(borrowedBooks.get("publicationDate"));

        Order mostRecentMember = cb.desc(member.get("membershipDate"));

        query.select(member)
                .where(romanceGenre)
                .orderBy(oldestBook, mostRecentMember);

        List<Member> result = entityManager.createQuery(query).setMaxResults(1).getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<Member> findAllWithNoBorrowedBooks() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);
        Root<Member> member = query.from(Member.class);

        LocalDateTime startOf2023 = Year.of(2023).atDay(1).atStartOfDay();
        LocalDateTime endOf2023 = Year.of(2023).atMonth(12).atEndOfMonth().atTime(23, 59, 59);
        Predicate registeredIn2023 = cb.between(member.get("membershipDate"), startOf2023, endOf2023);

        Predicate noBorrowedBooks = cb.isEmpty(member.get("borrowedBooks"));

        query.select(member)
                .where(cb.and(registeredIn2023, noBorrowedBooks));

        return entityManager.createQuery(query).getResultList();
    }
}
