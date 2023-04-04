package guckflix.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BackendApplication {


	// TODO: 영화 삭제 -> 크레딧 삭제 -> 이미지 삭제 -> DONE
	// TODO: 배우 등록 -> 사진 업로드 -> DONE
	// TODO: 배우 삭제 -> 크레딧 삭제 -> DONE
	// TODO : ** 해당 영화들 찾아서 castingOrder 정렬 update 함 ->; -> 생각해보면 order가 계속 쌓여도 됨 -> DONE
	// TODO : 파일 업로드 됐는지 체크하는 테스트 추가 -> DONE
	// TODO: 배우 수정 -> 개인정보와 크레딧 수정 multipart....
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
