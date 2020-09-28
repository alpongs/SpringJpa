package study.springjpa.repository;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import study.springjpa.model.Member;
import study.springjpa.model.Team;
import study.springjpa.model.dto.MemberDto;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EntityManager em;

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

    @Test
    void findPageByNameTest() {
        // given
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        for (int i = 0; i <= 500; i++) {
            Member member = new Member("Member_" + i, 10, teamA);
            memberRepository.save(member);
        }

        // when
        // 검색 조건 나이 10살
        // 정렬 조건 이름으로 내림 차순
        // 페이징 조건 첫 번째 페이지, 페이지당 보여줄 데이터는 3건

        PageRequest of = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name"));
        Page<Member> pageByAge = memberRepository.findPageByAge(10, of);

        // then
        List<Member> content = pageByAge.getContent();                  // 조회된 컨텐츠 데이터.
        assertThat(content.size()).isEqualTo(10);                       // 페이지당 보여야 될 개수.
        assertTrue(pageByAge.isFirst());
        //assertThat(pageByAge.getTotalElements()).isEqualTo(501);      // 조회된 전체 카운트.
        //assertThat(pageByAge.getTotalPages()).isEqualTo(50);          // 페이지 번호
        //assertThat(pageByAge.getNumber()).isEqualTo(0);               // 0번 페이지.
        System.out.println(pageByAge.getTotalElements());               // 검색된 전체 카운트.
        System.out.println(pageByAge.getTotalPages());                  // 전체 페이지 개수.
        System.out.println(pageByAge.getNumber());                      // 조회한 페이지.
        System.out.println(pageByAge.getNumberOfElements());            // 조회된 페이지의 개수.
        System.out.println(pageByAge.getSize());                        //
        System.out.println(pageByAge.getSort().toString());
        System.out.println(pageByAge.getPageable().toString());
    }

    @Test
    void findSliceByAgeTest() {
        // given
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        for (int i = 0; i <= 500; i++) {
            Member member = new Member("Member_" + i, 10, teamA);
            memberRepository.save(member);
        }

        // when
        PageRequest of = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name"));
        Slice<Member> sliceByAge = memberRepository.findSliceByAge(10, of);

        // then
        List<Member> content = sliceByAge.getContent();
        assertThat(content.size()).isEqualTo(10);

        System.out.println("slice Number : " + sliceByAge.getNumber());
        assertThat(sliceByAge.getSize()).isEqualTo(10);
        System.out.println("NumberOfElements : " + sliceByAge.getNumberOfElements());
        System.out.println("hasContents : "  + sliceByAge.hasContent());
        assertFalse(sliceByAge.isLast());
    }

    @Test
    void findDistCountByTest() {
        // given
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        for (int i = 0; i <= 500; i++) {
            Member member = new Member("Member_" + i, 10, teamA);
            memberRepository.save(member);
        }

        // when
        PageRequest of = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name"));
        Page<Member> memberAllCountBy = memberRepository.findMemberAllCountBy(10, of);

        // then
        assertThat(memberAllCountBy.getSize()).isEqualTo(10);
    }

    @Test
    void MemberToMemberDtoConvert() {
        // given
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        for (int i = 0; i <= 500; i++) {
            Member member = new Member("Member_" + i, 10, teamA);
            memberRepository.save(member);
        }

        // when
        PageRequest of = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name"));
        Page<Member> pageByAge = memberRepository.findPageByAge(10, of);

        Page<MemberDto> map = pageByAge.map(m -> new MemberDto(m.getId(), m.getName(), m.getTeam().getName()));

        // then
        for (MemberDto memberDto : map) {
            System.out.println("memberDto = " + memberDto);
        }
        assertThat(map.getSize()).isEqualTo(10);

    }

    @Test
    void bulkUpdateAgeTest() {
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
        int result = memberRepository.bulkUpdateAge(12);

        // then
        assertThat(result).isEqualTo(3);
    }

    @Test
    void findFetchJoinTest() {
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
        List<Member> memberFetchJoin = memberRepository.findMemberFetchJoin();



        // then
        assertThat(memberFetchJoin.size()).isEqualTo(4);

        for (Member member : memberFetchJoin) {
//            System.out.println("member = " + member);

            // Hibername 기능으로 확인 ( 지연 로딩 여부를 확인. )
            boolean initialized = Hibernate.isInitialized(member.getTeam());
            System.out.println("initialized = " + initialized);
        }
    }

    @Test
    void fetchJoinSpringJPA() {
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
        List<Member> findAll = memberRepository.findAll();

        // then
        assertThat(findAll.size()).isEqualTo(4);
    }

    @Test
    void findMemberEntityGraphTest() {
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
        List<Member> memberEntityGraph = memberRepository.findMemberEntityGraph();


        // then
        assertThat(memberEntityGraph.size()).isEqualTo(4);
    }

    @Test
    void findEntityGraphByNameTest() {
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
        List<Member> findMembers = memberRepository.findEntityGraphByName("Member1");

        // then
        assertThat(findMembers.size()).isEqualTo(1);

    }

    @Test
    void queryHint() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("Member1", 10, teamA);
        Member member2 = new Member("Member2", 20, teamA);
        Member member3 = new Member("Member3", 30, teamB);
        Member member4 = new Member("Member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByName("Member1");
        Member dirtyCheckingByName = memberRepository.findDirtyCheckingByName("Member2");

        // then
        assertThat(findMember.getName()).isEqualTo("Member1");
        assertThat(dirtyCheckingByName.getName()).isEqualTo("Member2");

        findMember.changeName("Member22");
        dirtyCheckingByName.changeName("Member22");

        em.flush();
        em.clear();
        Member checkFindMember = memberRepository.findReadOnlyByName("Member1");
        Member checkFindDirtyCheckMember = memberRepository.findDirtyCheckingByName("Member2");

        System.out.println("checkFindMember = " + checkFindMember);
        System.out.println("checkFindDirtyCheckMember = " + checkFindDirtyCheckMember);

    }

    @Test
    void findCustomByName() {
        // give
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("Member1", 10, teamA);
        Member member2 = new Member("Member2", 20, teamA);
        Member member3 = new Member("Member3", 30, teamB);
        Member member4 = new Member("Member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();


        // when
        List<Member> memberCustom = memberRepository.findMemberCustom();

        // then
        assertThat(memberCustom.size()).isEqualTo(4);

    }


    @Test
    void auditingTest() throws InterruptedException {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("Member1", 10, teamA);
        Member member2 = new Member("Member2", 20, teamA);
        Member member3 = new Member("Member3", 30, teamB);
        Member member4 = new Member("Member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();


        // when
        Member dirtyCheckingByName = memberRepository.findDirtyCheckingByName("Member1");

        sleep(1000);

        // then
        assertThat(dirtyCheckingByName.getName()).isEqualTo("Member1");
        dirtyCheckingByName.changeName("Korea1");

    }

}
