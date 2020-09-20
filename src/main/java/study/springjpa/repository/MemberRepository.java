package study.springjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import study.springjpa.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);

    List<Member> findByNameAndAgeGreaterThan(String name, int age);
}
