package study.springjpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;

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
        Member findMember1 = repository.findById(member1.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        Member findMember2 = repository.findById(member2.getId()).get();
        assertThat(findMember2).isEqualTo(member2);
        Member findMember3 = repository.findById(member3.getId()).get();
        assertThat(findMember3).isEqualTo(member3);
        Member findMember4 = repository.findById(member4.getId()).get();
        assertThat(findMember4).isEqualTo(member4);

        // 리스트 조회 검증
        List<Member> findAll = repository.findAll();
        assertThat(findAll.stream().count()).isEqualTo(4L);

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
    public void findByNameAndAgeGraterThan() {
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
}