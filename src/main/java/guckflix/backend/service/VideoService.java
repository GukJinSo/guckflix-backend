package guckflix.backend.service;

import guckflix.backend.dto.response.VideoDto;
import guckflix.backend.entity.Video;
import guckflix.backend.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public List<VideoDto> findById(Long movieId, String locale){
        List<VideoDto> dtos = new ArrayList<>();
        List<Video> videos = videoRepository.findByMovieId(movieId, locale);
        for (Video video : videos) {
            dtos.add(VideoEntityToDto(video));
        }
        return dtos;
    }

    private VideoDto VideoEntityToDto(Video entity){
        VideoDto videoDto = new VideoDto();
        videoDto.setMovieId(entity.getMovie().getId());
        videoDto.setName(entity.getName());
        videoDto.setKey(entity.getKey());
        videoDto.setSite(entity.getSite());
        videoDto.setOfficial(entity.getOfficial());
        videoDto.setIso639(entity.getIso639());
        videoDto.setIso3166(entity.getIso3166());
        videoDto.setType(entity.getType());
        videoDto.setPublishedAt(entity.getPublishedAt());
        return videoDto;
    }
}
