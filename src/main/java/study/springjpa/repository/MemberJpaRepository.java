package study.springjpa.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import study.springjpa.model.Member;

/**
 * JPA Repository 이용한 CRUD 기본 인터페이스 생성. For Example:
 * <pre>
 *
 *      MemberJpaRepository memberJpaRepository;
 *
 *      T function(..) {
 *           ...
 *           Member member = memberJpaRepository.find(...);
 *           ...
 *      }
 * </pre>
 *
 * @author 윤일.
 * @version 0.1
 */
@Repository
public class MemberJpaRepository {

    /**
     * EntityManager 영속성 관리 메니져.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Member 저장.
     *
     * @param member 저장하고자 하는 객체 정보.
     * @return member 객체(Increment 정보가 포함)
     */
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    /**
     * Member Id 값을 이용한 Member 정보조회.
     *
     * @param id ( member_id 회원번호 )
     * @return 검색에 존재하는 Member 정보.
     */
    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    /**
     * 회원 정보를 삭제한다.
     *
     * @param member 회원정보 객체를 삭제.
     */
    public void delete(Member member) {
        em.remove(member);
    }

    /**
     * 회원의 전체 정보 검색.
     *
     * @return 검색된 정보를 List 구성하여 반환.
     */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    /**
     * Java 8 기능을 이용한 Optional 정보를 이용하여 회원정보를 검색한다.
     * @param id member_id.
     * @return 안전한 방법으로 회원정보가 유무에 따라서 Optional로 반환한다.
     */
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    /**
     * 회원정보의 카운트를 가져올떄 사용한다. 이 방법 말구 다른 방법으로도 사용이 가능하다.
     *
     * @return 회원정보의 카운트.
     */
    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    /**
     * 회원정보 및 나이 정보를 이용한 검색 인터페이스.
     * @param name name
     * @param age age
     * @return 검색된 리스트 정보.
     */
    List<Member> findByNameAndAgeGreaterThan(String name, int age) {
        return em.createQuery("select m from Member m where m.name = :name and m.age > :age", Member.class)
            .setParameter("name", name)
            .setParameter("age", age)
            .getResultList();
    }
}
