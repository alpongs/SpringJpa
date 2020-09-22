package study.springjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import study.springjpa.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이름으로 검색하여 가져올때 인터페이스 선언.
     * @param name 검색하고자 하는 이름.
     * @return 찾는 경우 해당 Member Object.
     */
    List<Member> findByName(String name);

    /**
     * 이름 및 나이로 해당 정보를 검색하고자 할때 사용된다.
     * @param name 검색하고 하고자 하는 이름.
     * @param age 조건에 해당하는 나이.
     * @return 검색되는 정보를 List 객체로 담아서 받아온다.
     */
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

    /**
     * 나이 조건에 해당하는 Member List 가져올 때 사용된다.
     * Top, First -> limit 해당 뒤에 숫자가 오면은 해당 limit x 가 된다.
     * @param age 나이
     * @return Member 리스트.
     */
    List<Member> findTopByAge(int age);

    /**
     * 특정 나이 조건 보다 큰 Member 검색 시, Top 3명(Limit 조건으로 가져온다.)
     * @param age 나이 조건.
     * @return 나이에 해당하는 Member 리스트.
     */
    List<Member> findTop3ByAgeGreaterThan(int age);

    /**
     * 검색하고자 하는 나이가 있는지 여부 검사.
     * @param age 나이 조건.
     * @return 해당 조건이 있으면 true, 없으면 false.
     */
    boolean existsByAge(int age);

    @Query(name = "Member.findByName")
    List<Member> findByUsername(@Param("name") String name);
}
