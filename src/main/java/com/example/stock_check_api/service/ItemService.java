package com.example.stock_check_api.service;

import com.example.stock_check_api.dto.ItemDto;
import com.example.stock_check_api.dto.ItemForm;
import com.example.stock_check_api.entity.Category;
import com.example.stock_check_api.entity.Item;
import com.example.stock_check_api.entity.User;
import com.example.stock_check_api.exception.ForbiddenException;
import com.example.stock_check_api.exception.ItemDuplicationException;
import com.example.stock_check_api.exception.ItemNotFoundException;
import com.example.stock_check_api.localization.Translator;
import com.example.stock_check_api.repository.ItemRepository;
import com.example.stock_check_api.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ItemService {

    private ItemRepository itemRepository;
    private CategoryService categoryService;
    private AuthService authService;
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(ItemService.class);



    @Autowired
    public ItemService(ItemRepository itemRepository, AuthService authService, ModelMapper modelMapper, CategoryService categoryService) {
        this.itemRepository = itemRepository;
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
    }


    /**
     * 全件取得
     *
     * @return ユーザーに紐づくItem全レコードを作成順に並び替えItmDtoにマップしたリスト
     **/
    public List<ItemDto> findByUserOrderByIdDesc(User user){
        List<Item> items = itemRepository.findByUserOrderByIdDesc(user);
        return items.stream().
                map(item -> modelMapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }

    /**
     * 新規投稿時に名前の重複がないかチェック
     *
     * @param itemName 重複をチェックするItem/nameフィールドのバリュー
     * @return <code>true</code> 重複がある場合;
     * <code>false</code> それ以外の場合
     **/
    private boolean isDuplicated(String itemName, User user) {
        return !itemRepository.findByItemNameAndUser(itemName, user).isEmpty();
    }

    /**
     * 更新時に更新中の商品以外に名前の重複がないかチェック
     *
     * @param itemName 重複をチェックするItem/nameフィールドのバリュー
     * @return <code>true</code> 重複がある場合;
     * <code>false</code> それ以外の場合
     **/
    private boolean isDuplicated(String itemName, int itemId, User user) {
        return !itemRepository.findByItemNameAndUserAndIdNot(itemName, user, itemId).isEmpty();
    }



    /**
     * 投稿データ1件取得
     *
     * @param itemId 検索対象のItemレコードのid
     * @return 検索対象のItemレコード一件
     **/
    public Item findById(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(Translator.toLocale("item.error.notfound") + itemId));
    }

    /**
     * 投稿データ1件取得し、Dtoにマップして返す
     *
     * @param itemId 検索対象のItemレコードのid
     * @return 検索対象のItemDtoレコード一件
     **/
    public ItemDto findByIdAndReturnDto(int itemId){
        Item item = findById(itemId);
        return modelMapper.map(item, ItemDto.class);
    }

    /**
     * 新規投稿時
     *
     * @param receivedItemData クライアントから受信したデータが入ったItemDtoインスタンス
     * @return レスポンスで表示するため、コントローラーに渡すid
     **/
    public ItemDto postAndReturnDto(ItemForm receivedItemData, User user) {
        // 名前が重複していれば例外を投げる
        if (isDuplicated(receivedItemData.getItemName(), user)) {
            throw new ItemDuplicationException(Translator.toLocale("item.error.name.duplication") + receivedItemData.getItemName());
        }

        //もしカテゴリALLでリクエストが来たら、エラー
        if(receivedItemData.getCategoryId() == 0){
            throw new RuntimeException("Category with id 0 not allowed");

        }

        Category category = categoryService.findById(receivedItemData.getCategoryId());

        // Itemインスタンスを作成し、受け取ったItemDtoオブジェクトから必要な値をセット
        Item newItem = new Item(receivedItemData.getItemName(),
                receivedItemData.getExpireDate(),
                receivedItemData.getPurchasedOn(),
                user,
                category);
        itemRepository.save(newItem);

        return modelMapper.map(newItem, ItemDto.class);

    }

    /**
     * アイテム更新時
     *
     * @param itemId           更新対象のItemレコードのid
     * @param receivedItemData クライアントから受信したデータが入ったItemDtoインスタンス
     **/
    public void update(int itemId, ItemForm receivedItemData, User user) {
        Item toBeUpdatedItem = findById(itemId);
        // 現在の商品以外で名前が重複していれば例外を投げる
        if (isDuplicated(receivedItemData.getItemName(), itemId, user)) {
            throw new ItemDuplicationException(Translator.toLocale("item.error.name.duplication") + receivedItemData.getItemName());
        }

        Category category = categoryService.findById(receivedItemData.getCategoryId());

        // Itemインスタンスを作成し、受け取ったItemDtoオブジェクトから必要な値とidをセット
        toBeUpdatedItem.setExpireDate(receivedItemData.getExpireDate());
        toBeUpdatedItem.setPurchasedOn(receivedItemData.getPurchasedOn());
        toBeUpdatedItem.setItemName(receivedItemData.getItemName());
        toBeUpdatedItem.setCategory(category);
        itemRepository.save(toBeUpdatedItem);
    }

    /**
     * アイテム削除時
     *
     * @param itemId 削除対象のItemレコードのid
     **/
    public void deleteById(int itemId) {

        // アイテムに紐づいた画像があれば削徐
        // deleteImgIfAny(itemToBeDeleted);

        itemRepository.deleteById(itemId);
    }

    /**
     * アイテム検索
     *
     * @param name 検索するキーワード
     * @return 曖昧検索で引っかかったレコードのリスト
     **/
    public List<ItemDto> search(String name, User user) {
        List<Item> items = itemRepository.findByItemNameContainingAndUserOrderByIdDesc(name, user);
        return items.stream().
                map(item -> modelMapper.map(item, ItemDto.class))
                .collect(Collectors.toList());

    }


    public void isAuthorized(int itemId, UserPrincipal userPrincipal){
        if(findById(itemId).getUser() != authService.findById(userPrincipal.getId())){
            throw new ForbiddenException("アクセスが禁止されています - " + itemId);
        }

    }




//    /**
//     * 画像投稿
//     *
//     * @param itemId 投稿画像を紐づけるItemレコードのid
//     * @param file   クライアントから受け取った画像ファイル
//     **/
//    public void addImg(int itemId, MultipartFile file) {
//
//        // アイテムが存在するかチェック
//        Item itemToBeUpdated = findById(itemId);
//
//        // アイテムに紐づいた画像が既にあれば削除
//        deleteImgIfAny(itemToBeUpdated);
//
//        // 初回アップロード時に画像ディレクトリを作成する
//        File tempFile = new File("src/main/resources/static/images");
//        if (!tempFile.exists() && !tempFile.mkdirs()) {
//            throw new RuntimeException("ディレクトリの作成に失敗しました");
//        }
//
//        // Optionalを使いNullチェック
//        Optional<String> originalFileNameOp = Optional.ofNullable(file.getOriginalFilename());
//        String originalFileName = originalFileNameOp.orElseThrow(() -> new MultipartException("ファイルに異常があります"));
//        int point = originalFileName.lastIndexOf(".");
//
//        // 新しい画像ファイル名を設定
//        String fileName = "image_" + itemId + originalFileName.substring(point);
//
//        // 保存先のパスを設定
//        String imagePath = tempFile.getPath() + "/" + fileName;
//
//        // ファイル内容を書き込み
//        try (FileOutputStream fos = new FileOutputStream(imagePath)) {
//
//            // 画像ファイルをByteに変換＋書き込み
//            fos.write(file.getBytes());
//
//            // img_urlをセット
//            itemToBeUpdated.setImage(imagePath);
//
//            // 更新済みのオブジェクトをsaveする
//            itemRepository.save(itemToBeUpdated);
//        } catch (IOException e) {
//            throw new ImageFailedToBeUploaded("画像のアップロードが適切に行われませんでした", e);
//        }
//    }

//    /**
//     * 画像取得
//     *
//     * @param itemId 画像を取得したいアイテムレコードのid
//     * @return 対象画像をバイト列に変換したもの
//     **/
//    public byte[] getImageBytes(int itemId) {
//        Item theItem = findById(itemId);
//
//        byte[] bytes;
//        Resource resource = resourceLoader.getResource("File:" + theItem.getImage());
//        try (InputStream image = resource.getInputStream(); ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
//            // byteへ変換
//            int c;
//            while ((c = image.read()) != -1) {
//                bout.write(c);
//            }
//            bytes = bout.toByteArray();
//
//        } catch (IOException e) {
//            throw new ImageNotFoundException("このアイテムの画像登録はございません- id: " + itemId, e);
//        }
//        return bytes;
//    }


    //    /**
//     * （商品idが存在することを確かめた後）紐づく画像があれば削除する
//     *
//     * @param theItem 紐づく画像が存在するかチェックするItemレコード
//     **/
//    private void deleteImgIfAny(Item theItem) {
//        String imageUrl = theItem.getImage();
//        if (imageUrl != null) {
//            File file = new File(imageUrl);
//            if (file.exists() && !file.delete()) {
//                throw new RuntimeException("登録済みの画像の削除に失敗しました");
//            }
//        }
//    }


}

