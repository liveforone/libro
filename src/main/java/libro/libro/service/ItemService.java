package libro.libro.service;

import libro.libro.domain.Item;
import libro.libro.dto.ItemDto;
import libro.libro.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    //== 전체 상품게시글 ==//
    @Transactional(readOnly = true)
    public Page<Item> getItemList(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    //== 상품 검색 ==//
    @Transactional(readOnly = true)
    public Page<Item> getSearchList(String keyword, Pageable pageable) {
        return itemRepository.findByTitleContaining(keyword, pageable);
    }

    //== 카테고리 게시판 ==//
    @Transactional(readOnly = true)
    public Page<Item> getCategoryList(String category, Pageable pageable) {
        return itemRepository.findByCategory(category, pageable);
    }

    //== 상품 저장 ==//
    @Transactional
    public void saveItem(MultipartFile uploadFile, String writer, ItemDto itemDto) throws IOException {
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid + "_" + uploadFile.getOriginalFilename();

        itemDto.setWriter(writer);
        itemDto.setSaveFileName(saveFileName);
        uploadFile.transferTo(new File(saveFileName));
        itemRepository.save(itemDto.toEntity());
    }

    //== 상품 상세조회 ==//
    @Transactional(readOnly = true)
    public Item getItem(String title) {
        return itemRepository.findByTitle(title);
    }

    //== 좋아요 업데이트 ==//
    @Transactional
    public void updateGood(String title) {
        itemRepository.updateGood(title);
    }

    //== 기존파일 변경하며 수정 ==//
    @Transactional
    public void updateItemWithFile(MultipartFile uploadFile, ItemDto itemDto) throws IOException{
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid + "_" + uploadFile.getOriginalFilename();
        itemDto.setSaveFileName(saveFileName);
        uploadFile.transferTo(new File(saveFileName));
        itemRepository.save(itemDto.toEntity());
    }

    //== 기존파일 유지하며 수정 ==//
    @Transactional
    public void updateItemWithSaveFileName(String saveFileName, ItemDto itemDto) {
        itemDto.setSaveFileName(saveFileName);
        itemRepository.save(itemDto.toEntity());
    }
}
