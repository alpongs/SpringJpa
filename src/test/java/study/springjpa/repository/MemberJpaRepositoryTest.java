package study.springjpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;

import study.springjpa.model.Member;
import study.springjpa.model.Team;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
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
}