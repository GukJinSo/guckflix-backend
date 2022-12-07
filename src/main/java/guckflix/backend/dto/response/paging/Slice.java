package guckflix.backend.dto.response.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Slice<T> {

    private Boolean hasNext;

    @JsonProperty("page")
    private long requestPage;

    @JsonProperty("results")
    private List<T> list;

    private int size;

    public Slice(Boolean hasNext, long requestPage, List<T> list, int size) {
        this.hasNext = hasNext;
        this.requestPage = requestPage;
        this.list = list;
        this.size = size;
    }

    /**
     * 서비스 단에서 Slice<Entity> -> Slice<Dto>로 안의 제네릭 내용물만 갈아끼우는 경우 사용
     */
    public <C> Slice<C> convert(List<C> list){
        return new Slice<>(
                getHasNext(), getRequestPage(), list, getSize()
        );
    }

}
