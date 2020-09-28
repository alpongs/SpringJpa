package study.springjpa.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import study.springjpa.model.Member;

/**
 * 사용자 구현 클래스.
 * * 규칙 : 리포지토리 인터페이스 이름 + Impl
 * * 스프링 데이터 JPA 인식해서 스프링 빈으로 등록.
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
            .getResultList();
    }
}
