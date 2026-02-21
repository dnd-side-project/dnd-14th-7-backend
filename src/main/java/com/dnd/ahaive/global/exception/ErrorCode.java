package com.dnd.ahaive.global.exception;

public enum ErrorCode {

    // 400 Bad Request
    INVALID_REQUEST(400, "INVALID_REQUEST", "요청이 올바르지 않습니다."),
    MISSING_REQUIRED_FIELD(400, "MISSING_REQUIRED_FIELD", "필수 입력값이 누락되었습니다."),
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "입력값이 유효하지 않습니다."),
    VALIDATION_FAILED(400, "VALIDATION_FAILED", "요청 데이터가 유효하지 않습니다."),
    DUPLICATED_USER(400, "DUPLICATED_USER", "이미 등록된 사용자입니다."),
    DUPLICATED_NICKNAME(400, "DUPLICATED_NICKNAME", "이미 등록된 닉네임입니다."),
    DUPLICATED_LIKE(400, "DUPLICATED_LIKE", "이미 좋아요를 누르셨습니다."),
    DUPLICATED_BOOKMARK(400, "DUPLICATED_BOOKMARK", "이미 북마크 되어있는 게시글입니다."),
    NOT_LIKE(400,"NOT_LIKE", "좋아요를 누르지 않았습니다."),
    NOT_BOOKMARK(400,"NOT_BOOKMARK", "북마크 되어있지 않은 게시글입니다."),

    // 400 Bad Request 검색 관련
    INVALID_SEARCH_KEYWORD_LENGTH(400, "INVALID_SEARCH_KEYWORD_LENGTH", "검색어는 2자 이상 입력해주세요."),
    INVALID_SEARCH_KEYWORD_MAXIMUM(400, "INVALID_SEARCH_KEYWORD_MAXIMUM", "검색어는 15자 이하로 입력해주세요."),
    INVALID_SEARCH_KEYWORD(400, "INVALID_SEARCH_KEYWORD", "검색어에 '대학', '대학교', '학교'는 단독으로 사용할 수 없습니다."),

    // 400 Bad Request 파일 관련 에러코드
    UNSUPPORTED_FILE_FORMAT(400, "UNSUPPORTED_FILE_FORMAT", "지원하지 않는 파일 형식입니다."),
    IMAGE_FILE_ONLY(400, "IMAGE_FILE_ONLY", "이미지 파일만 업로드 가능합니다."),
    INVALID_FILE_EXTENSION(400, "INVALID_FILE_EXTENSION", "허용되지 않는 파일 확장자입니다."),

    // 400 Bad Request 사용자 아이디 관련 에러코드
    USER_ID_EMPTY(400, "USER_ID_EMPTY", "아이디는 비어있을 수 없습니다."),
    USER_ID_DUPLICATE(400, "USER_ID_DUPLICATE", "이미 사용중인 아이디입니다."),
    USER_ID_LENGTH_INVALID(400, "USER_ID_LENGTH_INVALID", "아이디는 4자 이상 12자 이하로 입력해주세요."),
    USER_ID_UPPERCASE_NOT_ALLOWED(400, "USER_ID_UPPERCASE_NOT_ALLOWED", "영문 대문자는 사용할 수 없습니다."),
    USER_ID_SPACE_NOT_ALLOWED(400, "USER_ID_SPACE_NOT_ALLOWED", "공백은 포함될 수 없습니다."),
    USER_ID_SPECIAL_CHAR_NOT_ALLOWED(400, "USER_ID_SPECIAL_CHAR_NOT_ALLOWED", "특수문자는 포함될 수 없습니다."),
    USER_ID_EMOJI_NOT_ALLOWED(400, "USER_ID_EMOJI_NOT_ALLOWED", "이모티콘은 사용할 수 없습니다."),
    USER_ID_KOREAN_NOT_ALLOWED(400, "USER_ID_KOREAN_NOT_ALLOWED", "한글은 포함될 수 없습니다."),

    // 400 Bad Request 사용자 비밀번호 관련 에러코드
    PASSWORD_LENGTH_INVALID(400, "PASSWORD_LENGTH_INVALID", "패스워드는 6자 이상 18자 이하로 입력해주세요."),
    PASSWORD_NO_LOWERCASE(400, "PASSWORD_NO_LOWERCASE", "영문 소문자가 최소 1자 이상 포함되어야 합니다."),
    PASSWORD_NO_UPPERCASE(400, "PASSWORD_NO_UPPERCASE", "영문 대문자가 최소 1자 이상 포함되어야 합니다."),
    PASSWORD_NO_SPECIAL_CHAR(400, "PASSWORD_NO_SPECIAL_CHAR", "특수문자가 최소 1자 이상 포함되어야 합니다."),
    PASSWORD_SPACE_NOT_ALLOWED(400, "PASSWORD_SPACE_NOT_ALLOWED", "공백은 포함될 수 없습니다."),
    PASSWORD_EMOJI_NOT_ALLOWED(400, "PASSWORD_EMOJI_NOT_ALLOWED", "이모티콘은 사용할 수 없습니다."),
    PASSWORD_KOREAN_NOT_ALLOWED(400, "PASSWORD_KOREAN_NOT_ALLOWED", "한글은 포함될 수 없습니다."),

    // 400 Bad Request 이메일 관련 에러코드
    INVALID_EMAIL_FORMAT(400, "INVALID_EMAIL_FORMAT", "이메일 형식이 올바르지 않습니다."),

    // 400 약관 관련 에러코드
    REQUIRED_TERMS_NOT_AGREED(400, "REQUIRED_TERMS_NOT_AGREED", "필수 약관에 동의하지 않았습니다."),
    INVALID_EMAIL_CODE(400, "INVALID_EMAIL_CODE", "코드가 유효하지 않습니다. 다시 시도해주세요."),

    // 401 Unauthorized
    INVALID_CREDENTIALS(401, "INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않습니다."),
    UNAUTHORIZED(401, "UNAUTHORIZED", "인증이 필요합니다."),
    TOKEN_EXTRACTION_FAILED(401, "TOKEN_EXTRACTION_FAILED", "토큰 추출에 실패했습니다."),

    // 1000~1003 Custom Token Errors

    TOKEN_INVALID_TYPE(999, "TOKEN_INVALID_TYPE", "토큰 타입이 올바르지 않습니다."),
    TOKEN_INVALID(1000, "TOKEN_INVALID", "토큰이 유효하지 않습니다."),
    ACCESS_TOKEN_INVALID(1001, "ACCESS_TOKEN_INVALID", "엑세스 토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_INVALID(1002, "REFRESH_TOKEN_INVALID", "리프레시 토큰이 유효하지 않습니다."),

    // 401 social login
    GOOGLE_INVALID_TOKEN(401, "GOOGLE_INVALID_TOKEN", "유효하지 않은 ID 토큰입니다."),
    KAKAO_INVALID_TOKEN(401, "KAKAO_INVALID_TOKEN", "유효하지 않은 엑세스 토큰입니다."),
    NAVER_INVALID_TOKEN(401, "NAVER_INVALID_TOKEN", "유효하지 않은 엑세스 토큰입니다."),


    // 403 Forbidden
    SELF_ACCESS_ONLY(403, "SELF_ACCESS_ONLY", "본인의 정보만 조회할 수 있습니다."),
    FORBIDDEN(403, "FORBIDDEN", "접근 권한이 없습니다."),
    ACCESS_DENIED(403, "ACCESS_DENIED", "이 기능에 접근할 수 없습니다."),
    INSUFFICIENT_GENERATION_COUNT(403, "INSUFFICIENT_GENERATION_COUNT", "AI 생성 가능 횟수를 모두 사용했습니다."),


    // 404 Not Found
    NOT_FOUND(404, "NOT_FOUND", "요청한 자원이 존재하지 않습니다."),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "해당 사용자를 찾을 수 없습니다."),
    USER_WITHDRAWN(404, "USER_WITHDRAWN", "탈퇴한 회원입니다."),
    RESUME_NOT_FOUND(404, "RESUME_NOT_FOUND", "이력서를 찾을 수 없습니다."),
    POST_NOT_FOUND(404, "POST_NOT_FOUND", "해당 게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(404, "COMMENT_NOT_FOUND", "해당 댓글을 찾을 수 없습니다."),
    PROVIDER_NOT_FOUND(404, "PROVIDER_NOT_FOUND", "소셜 provider를 찾을 수 없습니다."),
    SEARCH_LOG_NOT_FOUND(404, "SEARCH_LOG_NOT_FOUND", "최근 검색어가 존재하지 않습니다."),
    SCHEDULE_NOT_FOUND(404, "SCHEDULE_NOT_FOUND", "해당 스케쥴을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(404, "CATEGORY_NOT_FOUND", "해당 카테고리를 찾을 수 없습니다."),
    COVER_LETTER_NOT_FOUND(404, "COVER_LETTER_NOT_FOUND", "자기소개서를 찾을 수 없습니다."),
    TOKEN_NOT_FOUND(404, "TOKEN_NOT_FOUND", "토큰이 존재하지 않습니다."),
    INSIGHT_NOT_FOUND(404, "INSIGHT_NOT_FOUND", "해당 인사이트를 찾을 수 없습니다."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED", "허용되지 않은 HTTP 메서드입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."),
    VECTOR_EMBEDDING_API_FAILED(500, "VECTOR_EMBEDDING_API_FAILED", "벡터 임베딩 API 호출에 실패했습니다."),
    UNKNOWN_ERROR(500, "UNKNOWN_ERROR", "예기치 못한 오류가 발생했습니다."),
    AI_RESPONSE_PARSE_ERROR(500, "AI_RESPONSE_PARSE_ERROR", "AI 응답 파싱에 실패했습니다.");


    private final Integer customStatusCode;
    private final String code;
    private final String message;

    ErrorCode(Integer customStatusCode, String code, String message) {
        this.customStatusCode = customStatusCode;
        this.code = code;
        this.message = message;
    }

    public Integer getActualStatusCode() {
        return customStatusCode;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
