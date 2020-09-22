package study.springjpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import study.springjpa.model.Member;

import static org.assertj.core.api.Assertions.assertThat;

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
            Assertions.assertNotNull(optionalMember);
        }

    }

    @Test
    void baseCRUD() {

        // given
        Member member1 = new Member("Member1", 10);
        Member member2 = new Member("Member2", 20);
        Member member3 = new Member("Member3", 30);
        Member member4 = new Member("Member4", 40);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // when
        // 단건 조회....
        Member findMember1 = memberRepository.findById(member1.getId()).orElse(null);
        Member findMember2 = memberRepository.findById(member2.getId()).orElse(null);
        Member findMember3 = memberRepository.findById(member3.getId()).orElse(null);
        Member findMember4 = memberRepository.findById(member4.getId()).orElse(null);

        // 리스트 조회 검증.
        List<Member> memberList = memberRepository.findAll();

        // then
        // 단건 조회 결과...
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        assertThat(findMember3).isEqualTo(member3);
        assertThat(findMember4).isEqualTo(member4);

        // 리스트 조회 결과..
        assertThat(memberList.size()).isEqualTo(4);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        memberRepository.delete(member3);
        memberRepository.delete(member4);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(0L);
    }

    @Test
    void testCount() {
        // given
        Member member1 = new Member("Member1", 10);
        Member member2 = new Member("Member2", 20);
        Member member3 = new Member("Member3", 30);
        Member member4 = new Member("Member4", 40);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // when
        List<Member> top3ByAge = memberRepository.findTop3ByAgeGreaterThan(13);

        // then
        assertThat(top3ByAge.size()).isEqualTo(3);
    }

    @Test
    void testFindTop() {
        Member member1 = new Member("Member1", 10);
        Member member2 = new Member("Member2", 20);
        Member member3 = new Member("Member3", 30);
        Member member4 = new Member("Member4", 40);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        //when
        List<Member> topByAge = memberRepository.findTopByAge(10);

        for (Member member : topByAge) {
            System.out.println("member = " + member);
        }

        // then
        assertThat(topByAge.size()).isEqualTo(3);
    }


    @Test
    void namedQueryFindByUser() {

        // given
        Member member1 = new Member("Member1", 10);
        Member member2 = new Member("Member2", 20);
        Member member3 = new Member("Member3", 30);
        Member member4 = new Member("Member4", 40);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // when
        List<Member> members = memberRepository.findByUsername("Member1");

        // then
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    void findByUser() {
        // given
        Member member1 = new Member("Member1", 10);
        Member member2 = new Member("Member2", 20);
        Member member3 = new Member("Member3", 30);
        Member member4 = new Member("Member4", 40);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // when
        List<Member> findMember1 = memberRepository.findByUser("Member1", 10);
        List<Member> notFoundUser = memberRepository.findByUser("member1", 9);

        // then
        assertThat(findMember1.size()).isEqualTo(1);
        assertThat(notFoundUser.size()).isZero();
    }
}
