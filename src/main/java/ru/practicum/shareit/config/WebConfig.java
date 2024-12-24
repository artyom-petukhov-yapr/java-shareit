package ru.practicum.shareit.config;

import lombok.SneakyThrows;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;
import java.text.SimpleDateFormat;

@Configuration
@EnableWebMvc
public class WebConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper result = new ModelMapper();
        result.createTypeMap(String.class, Date.class).setConverter(dateToStringConverter);
        result.createTypeMap(Date.class, String.class).setConverter(stringToDateConverter);
        return result;
    }

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    Converter<String, Date> dateToStringConverter = new AbstractConverter<>() {
        @SneakyThrows
        @Override
        protected Date convert(String source) {
            if (source == null || source.isBlank()) {
                return null;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return format.parse(source);
        }
    };

    Converter<Date, String> stringToDateConverter = new AbstractConverter<>() {
        @SneakyThrows
        @Override
        protected String convert(Date source) {
            if (source == null) {
                return null;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return format.format(source);
        }
    };
}