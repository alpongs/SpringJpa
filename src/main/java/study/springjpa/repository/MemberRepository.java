package study.springjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.springjpa.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
