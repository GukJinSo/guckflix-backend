package guckflix.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.dto.paging.Paging;
import guckflix.backend.dto.paging.PagingRequest;
import guckflix.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RedisController {

//    private final RedisTemplate<String, Object> redisTemplate;
    private final MovieService movieService;
    private final ObjectMapper objectMapper;


//    @GetMapping("/redis/save")
//    public String save(PagingRequest pagingRequest) throws JsonProcessingException {
//
//        Paging<MovieDto.Response> popular = movieService.findPopular(pagingRequest);
//
//        String key = "popular:"+((Integer) pagingRequest.getRequestPage()).toString();
//
//        String valueString = objectMapper.writeValueAsString(popular);
//
//        redisTemplate.opsForValue().set(key, valueString);
//
//        return "OK";
//    }
//
//    @GetMapping("/redis/get")
//    public Object get(PagingRequest pagingRequest) throws JsonProcessingException {
//
//        String key = "popular:"+((Integer) pagingRequest.getRequestPage()).toString();
//
//        String jsonString = (String) redisTemplate.opsForValue().get(key);
//
//
//        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Paging.class, MovieDto.Response.class);
//
//        Paging<MovieDto.Response> paging = objectMapper.readValue(jsonString, javaType);
//
//        return paging;
//    }
//
//    @DeleteMapping("/redis/evict")
//    public String delete(PagingRequest pagingRequest){
////        redisTemplate.opsForValue().
//
//    return null;
//    }


}
