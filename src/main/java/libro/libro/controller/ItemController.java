package libro.libro.controller;

import libro.libro.domain.Item;
import libro.libro.dto.ItemDto;
import libro.libro.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    //----------------페이징 위치 함수 시작----------------//
    //== nowPage 함수 ==//
    public int getNowPage(Page<Item> itemList) {
        return itemList.getPageable().getPageNumber() + 1;
    }

    //== startPage 함수 ==//
    public int getStartPage(int nowPage) {
        return Math.max(nowPage - 4, 1);
    }

    //== endPage 함수 ==//
    public int getEndPage(int nowPage, Page<Item> itemList) {
        return Math.min(nowPage + 5, itemList.getTotalPages());
    }
    //----------------페이징 위치 함수 종료----------------//


    //== 상품 홈 페이지 ==//
    @GetMapping("/user/itemHome")
    public String itemHome(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<Item> itemList = itemService.getItemList(pageable);
        int nowPage = getNowPage(itemList);
        int startPage = getStartPage(nowPage);
        int endPage = getEndPage(nowPage, itemList);

        model.addAttribute("itemList", itemList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/item/itemHome";
    }

    //== 상품 검색 ==//
    @GetMapping("/user/itemHome/search")
    public String itemSearch(
            @PageableDefault(page = 0, size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam("keyword") String keyword,
            Model model
    ) {
        Page<Item> searchList = itemService.getSearchList(keyword, pageable);
        int nowPage = getNowPage(searchList);
        int startPage = getStartPage(nowPage);
        int endPage = getEndPage(nowPage, searchList);

        model.addAttribute("searchList", searchList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/item/itemSearch";
    }

    //== 카테고리 게시판 ==//
    @GetMapping("/user/item/category/{category}")
    public String categoryHome(
            @PathVariable("category") String category,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "good", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            Model model
    ) {
        Page<Item> categoryList = itemService.getCategoryList(category, pageable);
        int nowPage = getNowPage(categoryList);
        int startPage = getStartPage(nowPage);
        int endPage = getEndPage(nowPage, categoryList);

        model.addAttribute("category", category);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/item/categoryList";
    }

    //== 상품등록 페이지 ==//
    @GetMapping("/user/item/post")
    public String postPage() {
        return "/item/itemPost";
    }

    //== 상품 등록 ==//
    @PostMapping("/user/item/post")
    public String post(
            @RequestParam MultipartFile uploadFile,
            Principal principal,
            ItemDto itemDto
    ) throws IllegalStateException, IOException {

        String writer = principal.getName();

        if (!uploadFile.isEmpty()) {
            itemService.saveItem(uploadFile, writer, itemDto);
        } else {
            log.info("Posting Fail, Because there is no file!!");
            return "/item/wrongPage";
        }

        log.info("Posting Success!!");
        return "redirect:/user/itemHome";
    }

    //== 상품 상세조회 ==//
    @GetMapping("/user/item/{title}")
    public String detail(
            @PathVariable("title") String title,
            Model model,
            Principal principal
    ) {
        String member = principal.getName();
        Item item = itemService.getItem(title);

        model.addAttribute("itemTitle", title);
        model.addAttribute("item", item);
        model.addAttribute("member", member);
        return "/item/itemDetail";
    }

    //== 상품 상세조회 이미지 ==//
    @GetMapping("/user/item/image/{saveFileName}")
    @ResponseBody
    public Resource showImage(
            @PathVariable("saveFileName") String saveFileName
    ) throws MalformedURLException {
        return new UrlResource("file:C:\\Temp\\upload\\" + saveFileName);
    }

    //== 상품 좋아요 ==//
    @PostMapping("/user/item/good/{title}")
    public String updateGood(@PathVariable("title") String title) {
        String url = "/user/item/" + title;
        itemService.updateGood(title);
        log.info("Good update!!");
        return "redirect:" + url;
    }

    //== 상품 수정 페이지 ==//
    @GetMapping("/user/item/edit/{title}")
    public String editPage(
            @PathVariable("title") String title,
            Model model
    ) {
        Item item = itemService.getItem(title);
        model.addAttribute("item", item);
        return "/item/itemEdit";
    }

    //== 상품 수정 ==//
    @PostMapping("/user/item/edit/{title}")
    public String editItem(
            @PathVariable("title") String title,
            @RequestParam MultipartFile uploadFile,
            @RequestParam(value = "saveFileName", required = false) String saveFileName,
            ItemDto itemDto
    ) throws IllegalStateException, IOException {
        String url = "/user/item/" + title;
        if (!uploadFile.isEmpty()) {  //파일을 바꿔서 수정
            itemService.updateItemWithFile(uploadFile, itemDto);
        } else {  //기존 파일 유지하며 수정
            itemService.updateItemWithSaveFileName(saveFileName, itemDto);
        }
        log.info("Edit Success!!");
        return "redirect:" + url;
    }
}
