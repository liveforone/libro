package libro.libro.repository;

import libro.libro.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

    //== 제목으로 검색 + 페이징 ==//
    Page<Item> findByTitleContaining(String keyword, Pageable pageable);

    //== 제목으로 게시글 한개 찾기 ==//
    Item findByTitle(String title);

    //== 카테고리로 게시글 찾기 + 페이징 ==//
    Page<Item> findByCategory(String category, Pageable pageable);

    //== 좋아요 업데이트 ==//
    @Modifying
    @Query("update Item i set i.good = i.good + 1 where i.title = :title")
    void updateGood(@Param("title") String title);

    //== 수량 업데이트 - 주문 ==//
    @Modifying
    @Query("update Item i set i.remaining = i.remaining - 1 where i.title = :title")
    void minusRemaining(@Param("title") String title);

    //== 수량 업데이트 - 주문취소 ==//
    @Modifying
    @Query("update Item i set i.remaining = i.remaining + 1 where i.title = :title")
    void plusRemaining(@Param("title") String title);
}
