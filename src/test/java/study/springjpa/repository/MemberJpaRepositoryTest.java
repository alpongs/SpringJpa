package study.springjpa.repository;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;

import study.springjpa.model.Member;
import study.springjpa.model.Team;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository repository;

    @Autowired
    private EntityManager em;

    @Test
    void testCreateMember() {

        Member memberA = new Member("MemberA");
        Member saveMember = repository.save(memberA);
        Member findMember = repository.find(saveMember.getId());

        assertThat(saveMember.getId()).isEqualTo(findMember.getId());
        assertThat(saveMember.getName()).isEqualTo(findMember.getName());
        assertThat(saveMember).isEqualTo(findMember);
    }

    @Test
    void memberCount() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
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

        List<Member> memberList = repository.findAll();
        assertThat(memberList.size()).isEqualTo(4);
    }

    @Test
    void deleteMember() {
        Member member1 = new Member("Member1", 10);
        Member member2 = new Member("Member2", 20);
        repository.save(member1);
        repository.save(member2);

        assertThat(repository.count()).isEqualTo(2);
        repository.delete(member1);
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void baseCRUD() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
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

        // 단건 조회 검증.
        Member findMember1 = repository.findById(member1.getId()).orElse(null);
        assertThat(findMember1).isEqualTo(member1);
        Member findMember2 = repository.findById(member2.getId()).orElse(null);
        assertThat(findMember2).isEqualTo(member2);
        Member findMember3 = repository.findById(member3.getId()).orElse(null);
        assertThat(findMember3).isEqualTo(member3);
        Member findMember4 = repository.findById(member4.getId()).orElse(null);
        assertThat(findMember4).isEqualTo(member4);

        // 리스트 조회 검증
        List<Member> findAll = repository.findAll();
        assertThat(findAll.stream().count()).isEqualTo(4);

        long count = repository.count();
        assertThat(count).isEqualTo(4);

        // delete 검증
        repository.delete(member1);
        repository.delete(member2);
        repository.delete(member3);
        repository.delete(member4);

        long reCount = repository.count();
        assertThat(reCount).isEqualTo(0L);
    }

    @Test
    void findByNameAndAgeGraterThan() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
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

        List<Member> findList = repository.findByNameAndAgeGreaterThan("Member2", 10);
        assertThat(findList.size()).isEqualTo(1L);

        for (Member member : findList) {
            assertThat(member.getName()).isEqualTo("Member2");
            assertThat(member.getAge()).isEqualTo(20);
        }
    }

    @Test
    void findByPage() {
        // given
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        em.persist(teamA);
        em.persist(teamB);

        Random random = new Random();
        for (int i = 0; i <= 500; i++) {
            Member member = new Member("Member_" + i, 10, teamA);
            em.persist(member);
        }

        // when
        // 페이지 계산 공식 ...
        // totalPage = totalCount / size ...
        // 마지막 페이지...
        // 최초 페이지...
        List<Member> byPage = repository.findByPage(10, 100,  10);
        long count = repository.totalCount(10);

        // then
        assertThat(byPage.size()).isEqualTo(10);
        assertThat(count)
            .isEqualTo(501);


        // print
        for (Member member : byPage) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void bulkConditionAgePlusTest() {
        // given
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
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

        // when
        em.flush();
        em.clear();
        int updateCount = repository.bulkAgePlus(10);

        // then
        assertThat(updateCount).isEqualTo(4);

    }

    @Test
    void findMemberLazyTest() {
        // given
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
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

        List<Member> findAll = repository.findAll();

        // then
        for (Member member : findAll) {

            // 지연 로딩으로 인한 N + 1 문제 발생.
//            System.out.println("member = " + member.getTeam().getName());

            // Hibername 기능으로 확인 ( 지연 로딩 여부를 확인. )
            boolean initialized = Hibernate.isInitialized(member.getTeam());
            System.out.println("initialized = " + initialized);

            // JPA 표준 방법으로 확인.
            PersistenceUnitUtil util = em.getEntityManagerFactory().getPersistenceUnitUtil();
            boolean loaded = util.isLoaded(member.getTeam());
            System.out.println("loaded = " + loaded);
        }

        assertThat(findAll.size()).isEqualTo(4);

    }
}