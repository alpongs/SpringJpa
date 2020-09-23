package study.springjpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import study.springjpa.model.Member;
import study.springjpa.model.Team;
import study.springjpa.model.dto.MemberDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

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
            assertNotNull(optionalMember);
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

    @Test
    void findByUsernameList() {
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
        List<String> usernameList = memberRepository.findUsernameList();

        // then
        assertThat(usernameList.size()).isEqualTo(4);

        for (String s : usernameList) {
            System.out.println("usernameList = " + s);
        }
    }

    @Test
    void findMemberDto() {

        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("Member1", 10, teamA);
        Member member2 = new Member("Member2", 20, teamA);
        Member member3 = new Member("Member3", 30, teamB);
        Member member4 = new Member("Member4", 40, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // when
        List<MemberDto> memberDtoList = memberRepository.findMemberDtoList();

        // then
        assertThat(memberDtoList.size()).isEqualTo(4);

        for (MemberDto member : memberDtoList) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void findByNames() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("Member1", 10, teamA);
        Member member2 = new Member("Member2", 20, teamA);
        Member member3 = new Member("Member3", 30, teamB);
        Member member4 = new Member("Member4", 40, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // when
        List<String> asList = Arrays.asList("Member1", "Member2");
        List<Member> members = memberRepository.findByNames(asList);

        // then
        assertThat(members.size()).isEqualTo(asList.size());
    }

    @Test
    void findOneByName() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("Member1", 10, teamA);
        Member member2 = new Member("Member2", 20, teamA);
        Member member3 = new Member("Member3", 30, teamB);
        Member member4 = new Member("Member4", 40, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // when
        Member findMember = memberRepository.findOneByName("Member1");

        // 결과가 없을때, javax.persistence.NoResultException 발생되어 예외처리를 해야 했다.
        // SpringJPA 에서는 단건 조회할때 예외가 발생하면 예외를 무시하고 null 반환해준다.
        Member notFound = memberRepository.findOneByName("Member10");

        // then
        assertThat(findMember.getAge()).isEqualTo(10);
        assertThat(findMember.getName()).isEqualTo("Member1");
        assertNull(notFound);
    }
}
