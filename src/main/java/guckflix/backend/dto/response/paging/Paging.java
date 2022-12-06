package guckflix.backend.dto.response.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    /**
     * 서비스 단에서 Paging<Entity> -> Paging<Dto>로 안의 제네릭 내용물만 갈아끼우는 경우 사용
     */
    public <C> Paging<C> convert(List<C> list){
        return new Paging<C>(
                getRequestPage(), list, getTotalCount(), getTotalPage(), getSize()
        );
    }

}
