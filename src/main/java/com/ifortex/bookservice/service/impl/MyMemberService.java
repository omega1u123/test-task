package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.repository.MemberRepository;
import com.ifortex.bookservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class MyMemberService implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findMember() {
        return memberRepository.findMemberWithOldestRomanceBookAndMostRecentRegistration();
    }

    @Override
    public List<Member> findMembers() {
        return memberRepository.findAllWithNoBorrowedBooks();
    }
}
