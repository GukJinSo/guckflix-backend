package guckflix.backend.dto.paging;

import lombok.Getter;
import lombok.Setter;


/**
 * 페이징 요청 객체. ArgumentResolver에서 사용하여 컨트롤러 코드의 중복을 줄임
 * SpringDataJpa.Pageable와 유사한 구현
 */

@Getter
@Setter
public class PagingRequest {

    private int requestPage;

    private int offset;

    private int limit;

    public PagingRequest(int requestPage, int offset, int limit) {
        this.requestPage = requestPage;
        this.offset = offset;
        this.limit = limit;
    }

}
