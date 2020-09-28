package study.springjpa.repository.custom;

import java.util.List;

import study.springjpa.model.Member;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();

}
