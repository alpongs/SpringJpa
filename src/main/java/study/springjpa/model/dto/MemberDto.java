package study.springjpa.model.dto;


import lombok.Getter;
import lombok.ToString;
import study.springjpa.model.Member;

@Getter
@ToString(of = {"id", "name", "teamName"})
public class MemberDto {

    private final Long id;
    private final String name;
    private final String teamName;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.teamName = member.getTeam().getName();
    }

    public MemberDto(Long id, String name, String teamName) {
        this.id = id;
        this.name = name;
        this.teamName = teamName;
    }
}
