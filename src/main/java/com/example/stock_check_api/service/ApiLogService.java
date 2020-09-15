package com.example.stock_check_api.service;

import com.example.stock_check_api.dto.ApiLogDto;
import com.example.stock_check_api.dto.ApiLogSearchResultsForm;
import com.example.stock_check_api.entity.ApiLog;
import com.example.stock_check_api.repository.ApiLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ApiLogService {
    private final ApiLogRepository apiLogRepository;

    private final String ALL = "^/api/items/*$";

    private final String SINGLE = "^/api/items/\\d+/*$";

    private final String UPLOAD = "^/api/items/upload/\\d+/*$";

    private final String IMAGE = "^/api/items/images/\\d+/*$";

    private final String SEARCH = "^/api/items/search/*$";

    // [2019-07-11 11:10:15] 0:0:0:0:0:0:0:1 "GET /api/items" 401 5(ms)の形式にマッチするパターン
    // 正常なAPIのリクエスト以外にもマッチ
    private final Pattern ALL_URL_PATTERN = Pattern.compile("^\\[.+] [\\.:\\d]+ \"([A-Z]+) (.+)\" (\\d{3}) (\\d+)\\(ms\\)$");

    // 全てのItem APIのURLパターンをまとめたパターン。このどれにも一致しないrequestUrlはunknownとしてまとめられる
    private final Pattern VALID_URL_PATTERNS = Pattern.compile(ALL + "|" + SINGLE + "|" + UPLOAD + "|" + IMAGE + "|" + SEARCH);

    @Value("${log-files.path}")
    private String logFilesPath;


    public ApiLogService(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    /**
     * 前日付のログを集計する
     *
     * @return DBに保存された件数を返す（ApiLogBatchでのログ出力で使用）
     **/
    public int aggregateApiDataFromTheDayBefore() throws IOException {
        Map<String, ApiLogDto> apiLogDtoMap = new HashMap<>();
        Stream<String> lines = Files.lines(getPathForLogFile());
        lines.forEach(line -> insertIntoMap(line, apiLogDtoMap));
        List<ApiLog> apiLogList = convertMapToList(apiLogDtoMap);
        apiLogRepository.saveAll(apiLogList);
        return apiLogList.size();
    }

    /**
     * 集計対象のログファイルをPathとして返す
     *
     * @return 集計対象のログファイル
     **/
    private Path getPathForLogFile() {
        String localDate = LocalDate.now().minusDays(2).toString();
        return Paths.get(logFilesPath + localDate + ".log");
    }

    /**
     * 集計対象のログファイルが存在するか確認する
     *
     * @return true 存在する　false 存在しない
     */
    public boolean isLogFileExists() {
        return Files.exists(getPathForLogFile());
    }

    /**
     * ログファイルから読み取った行から必要な情報をまとめ、ApiLogDtoMapに詰める
     *
     * @param line         ログファイルから読み取った一行
     * @param apiLogDtoMap 空のapiLogDtoMap
     **/
    private void insertIntoMap(String line, Map<String, ApiLogDto> apiLogDtoMap) {
        Matcher matcher = ALL_URL_PATTERN.matcher(line);
        String requestUrl;

        // ファイルから読み取った一行分のlineがALL_URL_PATTERNに一致しなければ抜ける
        if (!matcher.find()) {
            return;
        }

        // requestUrlが、存在しないURL(VALID_URL_PATTERNSに一致しない)の場合、unknownに書き換え
        // それ以外の場合、数字があれば{id}に置き換え
        if (!VALID_URL_PATTERNS.matcher(matcher.group(2)).find()) {
            requestUrl = "unknown";
        } else {
            requestUrl = matcher.group(2).replaceAll("\\d+", "{id}");
        }

        // ファイルから読み取った一行分のlineからDBに保存する情報を抜き出す
        String requestMethod = matcher.group(1);
        int statusCode = Integer.parseInt(matcher.group(3));
        int processingTime = Integer.parseInt(matcher.group(4));

        // 同じ結果のログをまとめるためキーを設定
        String key = requestMethod + " " + requestUrl + " " + statusCode;

        // キーに一致すれば、アクセス回数と平均処理時間を上書きする
        if (apiLogDtoMap.containsKey(key)) {
            ApiLogDto apiLogDto = apiLogDtoMap.get(key);
            int newAccessCount = apiLogDto.getAccessCount() + 1;
            int totalProcessingTime = apiLogDto.getAvgProcessingTime() * apiLogDto.getAccessCount() + processingTime;
            int newAvgProcessingTime = totalProcessingTime / newAccessCount;

            apiLogDto.setAccessCount(newAccessCount);
            apiLogDto.setAvgProcessingTime(newAvgProcessingTime);

            // 未出のキーであればマップに新たに枠を追加
        } else {
            apiLogDtoMap.put(key, new ApiLogDto(requestUrl, requestMethod, statusCode, 1, processingTime));

        }
    }

    /**
     * APiLogDtoMapからApiLogのリストに変換
     *
     * @param apiLogDtoMap APiLogDtoをバリューに持ったマップ
     * @return ApiLogのリスト
     **/
    private List<ApiLog> convertMapToList(Map<String, ApiLogDto> apiLogDtoMap) {
        return apiLogDtoMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .map(d -> new ApiLog(d.getRequestUrl(), d.getRequestMethod(), d.getStatusCode(), d.getAccessCount(), d.getAvgProcessingTime()))
                .collect(Collectors.toList());
    }

    // 以下のメソッドはApiLogControllerで使用

    /**
     * 特定の期間に集計されたログ情報を集計日昇順で取得
     *
     * @param startDate 取得したい集計期間の始まり
     * @param endDate   取得したい集計期間の終わり
     * @return ログ情報のリスト
     **/
    public List<ApiLogSearchResultsForm> searchAndAggregateByDate(LocalDate startDate, LocalDate endDate) {
        return apiLogRepository.searchAndAggregateByDate(startDate, endDate);
    }

}

