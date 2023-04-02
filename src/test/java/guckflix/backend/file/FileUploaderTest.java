package guckflix.backend.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import guckflix.backend.dto.MovieDto;
import guckflix.backend.security.authen.PrincipalDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileUploaderTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testFormDataWithImage() throws Exception {

        MovieDto.Post postForm = new MovieDto.Post();
        postForm.setTitle("Test Movie");
        postForm.setOverview("This is a test movie");
        postForm.setGenres(Arrays.asList(Map.entry(1L, "Action"), Map.entry(2L, "Action")));
        postForm.setReleaseDate(LocalDate.now());
        String dtoJson = objectMapper.writeValueAsString(postForm);

        // Multipart 데이터 생성
        MockMultipartFile originFile = new MockMultipartFile("originFile", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test data".getBytes());
        MockMultipartFile w500File = new MockMultipartFile("w500File", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test data".getBytes());
        MockMultipartFile json = new MockMultipartFile("form", "form", "application/json", dtoJson.getBytes(StandardCharsets.UTF_8));

        // MockMvc로 요청 전송
        mockMvc.perform(MockMvcRequestBuilders.multipart("/movies")
                        .file(originFile)
                        .file(w500File)
                        .file(json)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }
}