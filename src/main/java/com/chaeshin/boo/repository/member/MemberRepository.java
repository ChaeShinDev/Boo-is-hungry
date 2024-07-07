package com.chaeshin.boo.repository.member;


import com.chaeshin.boo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, BaseMemberCrudRepository {
}
