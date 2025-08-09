package vuttr.VUTTR.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vuttr.VUTTR.entity.Tools;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ToolsRepository extends JpaRepository<Tools, Long> {
    Optional<Tools> findByIdAndUserId(Long id, Long userId);

    @Query("""
      select t
      from Tools t
      where t.user.id = :userId
      order by t.id
    """)
    List<Tools> findAllByUserId(@Param("userId") Long userId);

    @Query("""
      select distinct t
      from Tools t
      join t.tags tag
      where t.user.id = :userId
        and lower(tag) = lower(:tag)
      order by t.id
    """)
    List<Tools> findByUserIdAndTagIgnoreCase(@Param("userId") Long userId,
                                             @Param("tag") String tag);
}
