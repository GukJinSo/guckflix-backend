package guckflix.backend.dto.response.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guckflix.backend.entity.Movie;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Paging<T> {

    @JsonProperty("page")
    private long requestPage;
    
    private List<T> list;

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("total_page")
    private int totalPage;

    private int size;

    public Paging(long requestPage, List<T> list, int totalCount, int totalPage, int size) {
        this.totalPage = totalPage;
        this.requestPage = requestPage;
        this.list = list;
        this.totalCount = totalCount;
        this.size = size;
    }

}
