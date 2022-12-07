package guckflix.backend.config;

import guckflix.backend.dto.request.PagingRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Spring Data Jpa의 Pageable 사용하는 것 대신 커스텀 페이징 객체 Resolver
 */
public class PagingRequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == PagingRequest.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String paramPage = webRequest.getParameter("page");
        String paramLimit = webRequest.getParameter("limit");

        /**
         * page : 사용자가 실제 db의 몇 페이지를 요청했는가? (0부터 시작)
         * requestPage : 뷰 단에서 자신이 몇 페이지를 요청했는지 확인할 변수 (0 또는 1부터 시작)
         * 그러나 0페이지 요청 시 1페이지, 1페이지 요청하더라도 1페이지라고 응답되어야 함
         * limit : 페이지 사이즈
         * offset : (페이지 사이즈 X page) 번째 데이터부터 출력
         */
        int page = (paramPage == null || paramPage.equals("")) ? 0 : Integer.parseInt(paramPage); // defaultVaule 0
        int limit = (paramLimit == null || paramLimit.equals("")) ? 20 : Integer.parseInt(paramLimit); // defaultVaule 20
        int requestPage = (page == 0) ? 1 : page;
        int offset = page - 1 >= 0 ? (page - 1 ) * limit : 0;

        return new PagingRequest(requestPage, offset, limit);
    }
}
