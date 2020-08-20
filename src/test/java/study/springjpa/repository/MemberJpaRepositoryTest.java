package study.springjpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import study.springjpa.model.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository repository;
    @Autowired
    private MemberRepository memberRepository;

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
    void testMember() {
        Member memberB = new Member("MemberB");
        Member saveMember = memberRepository.save(memberB);

        Optional<Member> byId = memberRepository.findById(saveMember.getId());
        if (byId.isPresent()) {
            Member member = byId.get();
            assertThat(saveMember.getId()).isEqualTo(member.getId());
            assertThat(saveMember.getName()).isEqualTo(member.getName());
            assertThat(saveMember).isEqualTo(member);
        }
    }
}