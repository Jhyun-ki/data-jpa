package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    //@Query(name = "Member.findByUsername")
    // 1. 없어도 Member.메서드이름으로 찾는다.
    // 2. namedQuery없으면 메서드이름으로 쿼리날림.
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalMemberByUsername(String username);

    //count 쿼리를 따로 분리하지 않으면
    //페이징 시에 totalCount를 가져오는부분에서 left join이 필요하지 않는데도
    //left join을 하고 totalCount를 가져오게되어 조인하는 부분에서 성능저하가 일어날 수 있다.
    //sorting 복잡해지면 @Query 안에다가 넣어버리자
//    @Query(value = "select m from Member m left join m.team t",
//            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    //slice는 추가 count 쿼리 없이 다음 페이지만 확인 가능(내부적으로 limit + 1 조회)
    Slice<Member> findSliceByAge(int age, Pageable pageable);
}
