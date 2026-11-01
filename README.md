# 게시판

Spring Boot와 Thymeleaf로 만든 게시판 CRUD 프로젝트입니다.
화면은 서버에서 렌더링하고, 등록·수정·삭제는 jQuery Ajax로 JSON API를 호출합니다.

## 기술 스택

| 구분 | 사용 기술 |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4.1.1 (Web MVC, Data JPA, Validation) |
| View | Thymeleaf, jQuery 3.7.1 |
| Database | H2 (in-memory) |
| Build | Gradle 9.8.1 (Wrapper) |
| 기타 | Lombok, Spring Boot DevTools |

## 주요 기능

- **목록 조회**: 최신 글 순(id 내림차순) 정렬, 페이지당 10개, 페이지 버튼 10개 단위 페이징
- **상세 조회**: 제목, 작성자, 내용, 작성일, 수정일 표시
- **등록 / 수정**: 입력값 검증 후 실패 시 필드별 에러 메시지 표시
- **삭제**: 목록과 상세 화면에서 확인 후 삭제
- **작성일 / 수정일 자동 기록**: JPA Auditing(`@CreatedDate`, `@LastModifiedDate`)
- **예외 처리**: 존재하지 않는 게시글이나 범위를 벗어난 페이지 요청 시 오류 페이지로 이동

## 실행 방법

JDK 21이 필요합니다.

```bash
# macOS / Linux
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

실행 후 http://localhost:8080/board/list 에 접속합니다.

> H2 in-memory DB에 `ddl-auto: create` 설정을 사용하므로 애플리케이션을 재시작하면 데이터가 초기화됩니다.

### 테스트

```bash
./gradlew test
```

### H2 콘솔

| 항목 | 값 |
| --- | --- |
| URL | http://localhost:8080/h2-console |
| JDBC URL | `jdbc:h2:mem:board` |
| User Name | `sa` |
| Password | (빈 값) |

## URL 및 API

### 화면

| Method | URL | 설명 |
| --- | --- | --- |
| GET | `/board/list` | 게시글 목록 (`page`, `size` 쿼리 파라미터, `page`는 0부터 시작) |
| GET | `/board/write` | 게시글 등록 화면 |
| GET | `/board/{id}` | 게시글 상세 화면 |
| GET | `/board/{id}/edit` | 게시글 수정 화면 |

### API

| Method | URL | 설명 | 성공 응답 |
| --- | --- | --- | --- |
| POST | `/board` | 게시글 등록 | `200 OK`, 생성된 게시글 id |
| PUT | `/board/{id}` | 게시글 수정 | `200 OK` |
| DELETE | `/board/{id}` | 게시글 삭제 | `200 OK` |

등록과 수정의 요청 본문은 JSON 형식입니다.

```json
{
  "title": "제목",
  "writer": "작성자",
  "content": "내용"
}
```

| 필드 | 제약 조건 |
| --- | --- |
| `title` | 필수, 150자 이하 |
| `writer` | 필수, 100자 이하 |
| `content` | 필수, 10000자 이하 |

검증에 실패하면 `400 Bad Request`와 함께 필드별 에러 메시지를 반환합니다.

```json
{
  "title": "제목을 입력해주세요.",
  "content": "내용을 입력해주세요."
}
```

## 예외 처리

`GlobalExceptionHandler`에서 예외를 처리합니다.

| 예외 | 처리 방식 |
| --- | --- |
| `BoardException` | `ErrorCode`의 상태 코드와 메시지로 `error.html` 렌더링 |
| `MethodArgumentNotValidException` | `400` 상태 코드와 필드별 에러 메시지 JSON 반환 |

| ErrorCode | 상태 코드 | 메시지 |
| --- | --- | --- |
| `PAGE_NOT_FOUND` | 404 | 존재하지 않는 페이지입니다. |
| `BOARD_NOT_FOUND` | 404 | 존재하지 않는 게시글입니다. |

## 프로젝트 구조

```
src/main
├── java/com/example/board
│   ├── BoardApplication.java
│   ├── config
│   │   ├── GlobalExceptionHandler.java   # 전역 예외 처리
│   │   └── JpaConfig.java                # JPA Auditing 활성화
│   ├── controller
│   │   └── BoardController.java
│   ├── dto
│   │   ├── request/BoardRequest.java
│   │   └── response/BoardResponse.java
│   ├── entity
│   │   ├── BaseTime.java                 # 작성일, 수정일 공통 필드
│   │   └── Board.java
│   ├── enums
│   │   └── ErrorCode.java
│   ├── exception
│   │   └── BoardException.java
│   ├── repository
│   │   └── BoardRepository.java
│   └── service
│       └── BoardService.java
└── resources
    ├── application.yml
    ├── static/css/board.css
    └── templates
        ├── fragments/common.html         # 공통 head, header
        ├── index.html                    # 목록
        ├── detail.html                   # 상세
        ├── write.html                    # 등록
        ├── edit.html                     # 수정
        └── error.html                    # 오류
```
