package com.example.stock_check_api.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.stock_check_api.entity.Item;
import com.example.stock_check_api.exception.ImageFailedToBeUploaded;
import com.example.stock_check_api.localization.Translator;
import com.example.stock_check_api.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;



@Service
public class AmazonClient {

    @Autowired
    public AmazonClient(ItemRepository itemRepository,  ItemService itemService) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }


    private ItemService itemService;
    private ItemRepository itemRepository;
    private final Logger logger = LoggerFactory.getLogger(AmazonClient.class);
    private AmazonS3Client s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }


    /**
     * ファイルの形式をチェックし、不正ならExceptionを投げる
     *
     * @param file S3にアップロードするファイル
     * @param fileName アップロードする時につけるファイル名
     * @return アップロードされたファイルのURL
     *
     **/
    private String uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
        String keyName = s3client.getObject(bucketName, fileName).getKey();
        // アップロードが完了したら不要になったファイルを消す
        if(!file.delete()){
            logger.warn("ファイルの削除に失敗しました");
        }
        return "http://d3bs7pv4f2vus9.cloudfront.net/" + keyName;
    }

    /**
     * ファイルの形式をチェックし、不正ならExceptionを投げる
     *
     * @param multipartFile ファイル
     *
     **/
    private void validateFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new MultipartException(Translator.toLocale("image.upload.error.empty"));
        }
        Optional<String> contentTypeOp = Optional.ofNullable(multipartFile.getContentType());
        String contentType = contentTypeOp.orElseThrow(() -> new MultipartException(Translator.toLocale("image.upload.error.broken")));

        if (!contentType.matches("image/(jpeg|png|gif)")) {
            throw new MultipartException(Translator.toLocale(("image.upload.error.format") + contentType));
        }
    }

    /**
     * アイテムを取り、紐づく画像があればS3からファイルを削除する
     *
     * @param item 削除する画像が紐づくItem
     *
     **/
    public void deleteFileFromS3Bucket(Item item) {
        String fileUrl = item.getImage();

        if (fileUrl != null) {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            try {
                logger.info("画像を削除します-" + item.getId());
                DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucketName).withKeys(fileName);
                s3client.deleteObjects(delObjReq);
                logger.info("画像を削除しました-" + item.getId());
            } catch (Exception e) {
                throw new RuntimeException(Translator.toLocale(("image.delete.error.std")), e);
            }
        }else {
            logger.info("画像は存在しませんでした-" + item.getId());
        }
    }

    /**
     * ファイル名生成
     *
     * @param itemId 画像に紐付けるItemのid
     * @param multiPartFile アップロードするファイル
     * @return 成形されたファイル名
     *
     **/
    private String generateFileName(MultipartFile multiPartFile, int itemId) {
        // Optionalを使いNullチェック
        String originalFileName = Optional.ofNullable(multiPartFile.getOriginalFilename())
                .orElseThrow(() -> new MultipartException(Translator.toLocale("image.upload.error.broken")));

        return itemId + "-" + new Date().getTime() + "-" + originalFileName.replace(" ", "_")
                .replace("/", "_");
    }


    /**
     * 画像をアップロード
     *
     * @param itemId 検索対象のItemレコードのid
     * @param multipartFile アップロードするファイル
     *
     **/
    public void uploadFile(int itemId, MultipartFile multipartFile) {

        // ファイルが正しい形式か等をチェックし、不正ならExceptionを投げる
        validateFile(multipartFile);

        // アイテムが存在するかチェック
        Item itemToBeUpdated = itemService.findById(itemId);

        // アイテムに紐づいた画像が既にあれば削除
        deleteFileFromS3Bucket(itemToBeUpdated);

        try {
            // マルチパートファイルをファイルオブジェクトに変換
            File file = convertMultiPartToFile(multipartFile);
            //　ファイル名作成
            String fileName = generateFileName(multipartFile, itemId);
            // ファイルをBUCKETにアップロード
            String imageUrl = uploadFileTos3bucket(fileName, file);

            // URLをDBに保存
            itemToBeUpdated.setImage(imageUrl);
        } catch (Exception e) {
            throw new ImageFailedToBeUploaded(Translator.toLocale("image.upload.error.std"), e);
        }

    }


    /**
     * マルチパートファイルをファイルオブジェクトに変換
     *
     * @param multiPartFile ファイルオブジェクトに変換するファイル
     * @return 変換されたファイルオブジェクト
     **/
    private File convertMultiPartToFile(MultipartFile multiPartFile) {
        File file = new File(multiPartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {

            // 画像ファイルをByteに変換＋書き込み
            fos.write(multiPartFile.getBytes());
            return file;

        } catch (IOException e) {
            throw new ImageFailedToBeUploaded(Translator.toLocale("image.upload.error.std"), e);
        }

    }
}


