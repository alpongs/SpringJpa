package study.springjpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import study.springjpa.model.Member;
import study.springjpa.model.dto.MemberDto;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이름으로 검색하여 가져올때 인터페이스 선언.
     * @param name 검색하고자 하는 이름.
     * @return 찾는 경우 해당 Member Object.
     */
    List<Member> findByName(String name);

    /**
     * 이름으로 단건 조회.
     * @param name      이름.
     * @return          단일 Member.
     */
    Member findOneByName(String name);

    /**
     * Optional 사용하여 결과 처리.
     * @param name      이름.
     * @return          Optional Member.
     */
    Optional<Member> findOptionalByName(String name);

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

    /**
     * NamedQuery 이용한 Member 이름으로 검색하기.
     * @param name  검색하고자 하는 이름.
     * @return      검색된 Member 리스트.
     */
    @Query(name = "Member.findByName")
    List<Member> findByUsername(@Param("name") String name);


    /**
     * Query, Annotation 이용한 JPQL 쿼리 작성.
     * @param name  검색하는 이름.
     * @param age   검색하는 나이.
     * @return      검색된 Member 리스트.
     */
    @Query("select m from Member m where m.name = :name and m.age = :age")
    List<Member> findByUser(@Param("name")String name, @Param("age")int age);

    /**
     * 단일 필드만 조회가 가능하다.
     * @return UserName List.
     */
    @Query("select m.name from Member m")
    List<String> findUsernameList();

    /**
     * DTO 깩체를 통한 JPQL.
     * @return MemberDto 객체 리스트.
     */
    @Query("select new study.springjpa.model.dto.MemberDto(m.id, m.name, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDtoList();

    /**
     * Collection 타입으로 in 절 지원.
     * Collection 파라미터 바인딩.
     * @param names List 이름 리스트.
     * @return List Member Entity.
     */
    @Query("select m from Member m where m.name in :names")
    List<Member> findByNames(@Param("names")List<String> names);

    /**
     * Count Query 사용.
     * @param age      이름
     * @param pageable  페이지.
     * @return Member List.
     */
    Page<Member> findPageByAge(int age, Pageable pageable);

    /**
     * Slice 적용하여 Page 정보 찾아오기 limit +1 이된다.
     * @param age           나이.
     * @param pageable      페이지 정보.
     * @return              검색된 리스트.
     */
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    /**
     * 나이 조건을 이용하여 페이지 찾아오기 위한 인터페이스.
     * @param age           나이.
     * @param pageable      페이지 정보.
     * @return              검색된 리스트.
     */
    List<Member> findListByAge(int age, Pageable pageable);

    /**
     * Sort 조건을 이용하여 정렬하는 인터페이스.
     * @param age           나이.
     * @param sort          정렬.
     * @return              검색된 리스트.
     */
    List<Member> findByAge(int age, Sort sort);

    /**
     * Count Query 분리 후 Query 동작.
     * 왜? 분류했을까?? 고민을 해봐야 한다.
     * @param pageable      페이지 정보.
     * @return              검색된 리스트.
     */
    @Query(value = "select m from Member m where m.age = :age"
        , countQuery = "select count(m.name) from Member m where m.age = :age")
    Page<Member> findMemberAllCountBy(@Param("age") int age, Pageable pageable);

}
