package study.springjpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.springjpa.model.Member;
import study.springjpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    /**
     * Domain Class Converter before.
     *
     * @param id member ID.
     * @return Member Name.
     */
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        assert member != null;
        return member.getName();
    }

    /**
     * Domain Class Converter after.
     * @param member        member id.
     * @return              Member name.
     */
    @GetMapping("/member/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getName();
    }

    /**
     * 스프링 데이터 제공하는 페이징과 정렬 기능.
     * @param pageable      페이징 정보.
     * @return              페이징된 데이터.
     */
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }
}
