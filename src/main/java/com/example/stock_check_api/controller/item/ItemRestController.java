package com.example.stock_check_api.controller.item;

import com.example.stock_check_api.dto.ItemDto;
import com.example.stock_check_api.dto.ItemForm;
import com.example.stock_check_api.entity.Item;
import com.example.stock_check_api.entity.User;
import com.example.stock_check_api.localization.Translator;
import com.example.stock_check_api.response.ItemResponse;
import com.example.stock_check_api.security.UserPrincipal;
import com.example.stock_check_api.service.AmazonClient;
import com.example.stock_check_api.service.AuthService;
import com.example.stock_check_api.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.1.7:3000", "http://192.168.1.4"}, methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS})
@RequestMapping("/api/items")
@RestController
public class ItemRestController {
    private final ItemService itemService;
    private final AuthService authService;
    private final AmazonClient amazonClient;
    private ModelMapper modelMapper;

    @Autowired
    public ItemRestController(ItemService itemService, AmazonClient amazonClient, AuthService authService, ModelMapper modelMapper) {
        this.itemService = itemService;
        this.amazonClient = amazonClient;
        this.authService = authService;
        this.modelMapper = modelMapper;
    }


    /**
     * アイテム全件取得
     **/
    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = authService.findById(userPrincipal.getId());
        List<ItemDto> items = itemService.findByUserOrderByIdDesc(user);
        return new ResponseEntity<>(items, HttpStatus.OK);

    }

    /**
     * アイテム新規投稿
     **/
    @PostMapping
    public ResponseEntity<ItemDto> postItem(@RequestBody @Validated ItemForm itemForm, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = authService.findById(userPrincipal.getId());
        ItemDto itemDto = itemService.postAndReturnDto(itemForm, user);
        return ResponseEntity.ok(itemDto);
    }

    /**
     * アイテム1件取得
     **/
    @PostAuthorize("returnObject.body.getUserId() == #userPrincipal.getId()")
    @GetMapping("/{itemId:\\d+}")
    public ResponseEntity<ItemDto> getItem(@PathVariable int itemId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ItemDto itemDto = itemService.findByIdAndReturnDto(itemId);
        return new ResponseEntity<>(itemDto, HttpStatus.OK);
    }


    /**
     * アイテム更新
     **/
    @PreAuthorize("@permissionEvaluator.isAuthorized(#itemId, #userPrincipal)")
    @PutMapping("/{itemId:\\d+}")
    public ResponseEntity<ItemResponse> updateItem(@RequestBody @Validated ItemForm receivedItemData, @PathVariable int itemId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = authService.findById(userPrincipal.getId());
        itemService.update(itemId, receivedItemData, user);
        ItemResponse response = new ItemResponse(itemId, Translator.toLocale("item.update.success"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * アイテム削除
     **/
    @PreAuthorize("@permissionEvaluator.isAuthorized(#itemId, #userPrincipal)")
    @DeleteMapping("/{itemId:\\d+}")
    public ResponseEntity<ItemResponse> deleteItem(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int itemId) {
        Item itemToBeDeleted = itemService.findById(itemId);
        amazonClient.deleteFileFromS3Bucket(itemToBeDeleted);
        itemService.deleteById(itemId);
        ItemResponse response = new ItemResponse(itemId, Translator.toLocale("item.delete.success"));
        return ResponseEntity.ok(response);
    }


    /**
     * 画像のアップロード
     **/
    @PreAuthorize("@permissionEvaluator.isAuthorized(#itemId, #userPrincipal)")
    @PostMapping(value = "/upload/{itemId:\\d+}")
    public ResponseEntity<ItemDto> uploadImg(@PathVariable int itemId, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        amazonClient.uploadFile(itemId, file);
        ItemDto item = itemService.findByIdAndReturnDto(itemId);
        return ResponseEntity.ok(item);
    }

    /**
     * アイテム名前検索
     **/
    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchForItem(@RequestParam("name") String name, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = authService.findById(userPrincipal.getId());
        List<ItemDto> searchResults = itemService.search(name, user);
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

}


// S3とクラウドフロントから取得しているので使っていない
//    /**
//     * 画像取得
//     **/
//    @ResponseBody
//    @GetMapping("/images/{itemId:\\d+}")
//    public HttpEntity<byte[]> getImg(@PathVariable int itemId) {
//        byte[] b = itemService.getImageBytes(itemId);
//
//        // 画像をレスポンスデータとして返却
//        HttpHeaders headers = new HttpHeaders();
//
//        try (InputStream inputStream = new ByteArrayInputStream(b)) {
//            String contentType = URLConnection.guessContentTypeFromStream(inputStream);
//            headers.setContentType(MediaType.valueOf(contentType));
//        } catch (IOException e) {
//            throw new ImageNotFoundException("対象の画像が存在しません", e);
//        }
//        return new HttpEntity<>(b, headers);
//    }