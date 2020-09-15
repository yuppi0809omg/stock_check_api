package com.example.stock_check_api.batch;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.stock_check_api.service.ApiLogService;

@Component
public class ApiLogBatch {

    private final ApiLogService apiLogService;

    private final Logger logger = LoggerFactory.getLogger(ApiLogBatch.class);

    public ApiLogBatch(ApiLogService apiLogService) {
        this.apiLogService = apiLogService;
    }

    @Scheduled(cron = "${cron}", zone = "Asia/Tokyo")
    public void performDataAggregation() {

        logger.info("アクセス集計処理を開始します");

        // 対象のファイルが存在しなければ抜ける
        if (!apiLogService.isLogFileExists()) {
            logger.info("集計対象のログファイルがありませんでした");
            return;
        }

        try {
            // 集計処理。結果に合わせてログ出力
            int count = apiLogService.aggregateApiDataFromTheDayBefore();
            if (count == 0) {
                logger.info("アクセス集計処理が完了しましたー登録データはありませんでした");
            } else {
                logger.info("アクセス集計処理が完了しましたー計" + count + "件のデータを登録しました");
            }

        } catch (IOException e) {
            logger.error("集計中にエラーが発生しましたーアクセス集計処理に失敗しました", e);
        }
    }
}
