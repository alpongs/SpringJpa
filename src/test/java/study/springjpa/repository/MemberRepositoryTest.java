package study.springjpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import study.springjpa.model.Member;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void memberCreate() {
        Member member1 = new Member("Member1", 10);
        Member member2 = new Member("Member2", 20);
        Member member3 = new Member("Member3", 30);
        Member member4 = new Member("Member4", 40);
        Member member5 = new Member("Member5", 50);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        Optional<Member> optionalMember = memberRepository.findById(member1.getId());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            System.out.println("member = " + member);
        }
    }
}
