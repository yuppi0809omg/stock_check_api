package com.example.stock_check_api.controller.api_log;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.example.stock_check_api.dto.ApiLogForm;
import com.example.stock_check_api.dto.ApiLogSearchResultsForm;
import com.example.stock_check_api.service.ApiLogService;

@Controller
public class ApiLogController {

    private final ApiLogService apiLogService;

    public ApiLogController(ApiLogService apiLogService) {
        this.apiLogService = apiLogService;
    }

    /**
     * ログ全件表示
     **/
    @GetMapping("/logs")
    public ModelAndView searchAll(ModelAndView modelAndView) {
        modelAndView.setViewName("logs");
        modelAndView.addObject("apiLogForm", new ApiLogForm());
        return modelAndView;

    }


    /**
     * 　ログ検索結果表示
     **/
    @GetMapping("logs/search")
    public ModelAndView searchByDate(@ModelAttribute("apiLogForm") @Validated ApiLogForm apiLogForm, BindingResult result, ModelAndView modelAndView) {
        modelAndView.setViewName("logs");

        if (result.hasErrors()) {
            return modelAndView;
        }

        List<ApiLogSearchResultsForm> searchResults = apiLogService.searchAndAggregateByDate(apiLogForm.getStartDate(), apiLogForm.getEndDate());
        if (searchResults.size() == 0) {
            modelAndView.addObject("noResultMessage", "対象のログはございません");
        } else {
            modelAndView.addObject("searchResults", searchResults);
            modelAndView.addObject("startDate", apiLogForm.getStartDate());
            modelAndView.addObject("endDate", apiLogForm.getEndDate());
        }
        return modelAndView;
    }
}

