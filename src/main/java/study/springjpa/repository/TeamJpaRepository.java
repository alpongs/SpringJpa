package study.springjpa.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import study.springjpa.model.Team;

/**
 * JPA Repository 이용한 CRUD 기본 인터페이스 생성. For Example:
 * <pre>
 *
 *      TeamRepository teamRepository
 *
 *      T function(..) {
 *           ...
 *           Team team = teamRepository.find(...);
 *           ...
 *      }
 * </pre>
 *
 * @author 윤일.
 * @version 0.1
 */
@Repository
public class TeamJpaRepository {

    /**
     * EntityManager 영속성 관리 메니져.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Team 저장.
     *
     * @param team 저장하고자 하는 객체 정보.
     * @return team 객체(Increment 정보가 포함)
     */
    public Team save(Team team) {

        em.persist(team);
        return team;
    }

    /**
     * Team Id 값을 이용한 Team 정보조회.
     *
     * @param id ( team_id 회원번호 )
     * @return 검색에 존재하는 Team 정보.
     */
    public Team find(Long id) {
        return em.find(Team.class, id);
    }

    /**
     * 회원 정보를 삭제한다.
     *
     * @param team 회원정보 객체를 삭제.
     */
    public void delete(Team team) {
        em.remove(team);
    }

    /**
     * 회원의 전체 정보 검색.
     *
     * @return 검색된 정보를 List 구성하여 반환.
     */
    public List<Team> findAll() {
        return em.createQuery("select m from Team m", Team.class).getResultList();
    }

    /**
     * Java 8 기능을 이용한 Optional 정보를 이용하여 회원정보를 검색한다.
     * @param id team_id.
     * @return 안전한 방법으로 회원정보가 유무에 따라서 Optional 로 반환한다.
     */
    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    /**
     * 회원정보의 카운트를 가져올떄 사용한다. 이 방법 말구 다른 방법으로도 사용이 가능하다.
     *
     * @return 회원정보의 카운트.
     */
    public long count() {
        return em.createQuery("select count(m) from Team m", Long.class).getSingleResult();
    }
}
