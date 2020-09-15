package com.example.stock_check_api.model_mapper;

import com.example.stock_check_api.dto.ItemDto;
import com.example.stock_check_api.entity.Item;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

// @Configurationをつけると、Mapperにexplicit mappingを設定できる。
// こレがナイトトコントローラーで使用した際にexplicit mappingが効かない
@Configuration
public class ItemModelMapper {

    @Autowired
    private ModelMapper modelMapper;

    // Bean登録が終わった後に呼ばれる
    @PostConstruct
    public void MapItemToDto(){
            LocalDate today = LocalDate.now();

            Converter<LocalDate, Boolean> checkIsExpired =
                    ctx -> ctx.getSource().isBefore(today);

            Converter<LocalDate, Boolean> checkWillBeExpired =
                    ctx -> !ctx.getSource().isBefore(today) && ctx.getSource().isBefore((today.plusDays(7)));

        Converter<LocalDate, Long> calcRemaningDays =
                ctx -> DAYS.between(today, ctx.getSource());

            modelMapper.typeMap(Item.class, ItemDto.class).addMappings(mapper->
                    mapper.using(checkIsExpired)
                            .map(Item::getExpireDate, ItemDto::setIsExpired))
                    .addMappings(mapper->
                            mapper.using(checkWillBeExpired)
                                    .map(Item::getExpireDate, ItemDto::setWillBeExpired))
                    .addMappings(mapper->
                            mapper.using(calcRemaningDays)
                                .map(Item::getExpireDate, ItemDto::setRemainingDays));


    }
}
