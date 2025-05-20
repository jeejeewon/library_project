// package Vo;
// 이 자바 파일(KakaoTokenResponseVo.java)이 당신의 프로젝트 내에서 'Vo' 라는 이름의 폴더(패키지) 안에 속해있음을 알려줍니다.
// 관련된 데이터 서식지(VO, DTO) 파일들을 함께 모아두는 역할을 합니다.
package Vo;

// import ... : 다른 사람들이 미리 만들어 놓은 유용한 도구 상자(라이브러리)에서 필요한 도구(클래스)들을 가져와서 사용하겠다고 선언하는 부분입니다.
//              여기서는 Jackson 라이브러리에서 제공하는 특별한 표시(@어노테이션) 도구들을 가져옵니다.
//              이 도구들은 카카오가 보내주는 JSON 형식의 데이터를 이 Java 서식지에 맞게 자동으로 채워 넣을 때 사용됩니다.
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // JSON 데이터에는 있지만 이 서식지에는 없는 항목은 무시하라는 지시 도구
import com.fasterxml.jackson.annotation.JsonProperty; // JSON 데이터 항목 이름과 Java 변수 이름을 서로 연결해주는 도구

/**
 * 카카오 토큰 발급 API(/oauth/token)로부터 받은 응답(JSON 데이터)의 내용을
 * Java 코드 안에서 쉽게 다룰 수 있도록, 그 구조에 맞춰 정의한 '데이터 서식지'(DTO 또는 VO 클래스)입니다.
 *
 * 예를 들어, 카카오가 {"access_token": "값1", "token_type": "bearer", ...} 와 같은 JSON 데이터를 보내주면,
 * 이 서식지를 이용해서 Java 코드 안에서는 response.getAccessToken() 처럼 편리하게 "값1"을 꺼내 쓸 수 있게 됩니다.
 *
 * @JsonIgnoreProperties(ignoreUnknown = true): 카카오 응답 JSON에 혹시 우리가 이 서식지에 정의하지 않은
 * 추가 정보가 포함되어 있더라도, 오류를 발생시키지 않고 그냥 무시하라는 특별 지시입니다.
 * (나중에 카카오 API 명세가 바뀌어도 유연하게 대처 가능)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
// public class KakaoTokenResponseVo { ... }: 'KakaoTokenResponseVo' 라는 이름의 새로운 데이터 서식지(클래스)를 정의합니다.
// public: 이 서식지는 프로젝트 내 다른 파일에서도 자유롭게 사용할 수 있다는 의미입니다.
public class KakaoTokenResponseVo {

    /**
     * 사용자 '액세스 토큰(Access Token)' 값입니다. (가장 중요한 정보!)
     * 이 토큰은 마치 카카오 서비스들을 이용할 수 있는 '정식 출입증'과 같습니다.
     * 이 출입증을 제시해야만 카카오 사용자 정보 조회 등 다른 API들을 호출할 수 있습니다.
     *
     * @JsonProperty("access_token"): 카카오가 보내주는 JSON 데이터에서 "access_token" 이라는 이름표가 붙은 값을
     * 찾아서 아래 선언된 'accessToken' Java 변수에 자동으로 넣어달라는 지시입니다.
     */
    @JsonProperty("access_token")
    // private: 이 변수는 이 서식지(클래스) 내부에서만 직접 사용하고, 외부에서는 아래 getter/setter 메소드를 통해 접근합니다. (정보 보호)
    // String: 글자들(문자열)을 저장하는 Java 데이터 타입입니다. (액세스 토큰은 긴 문자열입니다)
    // accessToken: 이 변수의 이름입니다. (JSON 이름과 비슷하게 짓지만, Java 작명 관례(camelCase)를 따릅니다)
    private String accessToken;

    /**
     * 발급된 토큰의 종류를 나타냅니다. 카카오 로그인의 경우 항상 "bearer" 라는 고정된 값을 가집니다.
     * "Bearer" 타입은 토큰 자체에 접근 권한이 포함되어 있다는 의미로 사용됩니다.
     * JSON 데이터에서는 "token_type" 이라는 이름으로 옵니다.
     */
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * 사용자 '리프레시 토큰(Refresh Token)' 값입니다.
     * 위에서 받은 '액세스 토큰(정식 출입증)'은 보안을 위해 유효 시간이 짧습니다 (보통 몇 시간).
     * 이 유효 시간이 다 지나면 액세스 토큰은 더 이상 사용할 수 없게 됩니다.
     * 이때, 이 '리프레시 토큰(갱신용 토큰)'을 사용하면 카카오에 다시 로그인할 필요 없이
     * 새로운 액세스 토큰을 발급받을 수 있습니다. 리프레시 토큰은 액세스 토큰보다 유효 시간이 훨씬 깁니다 (보통 몇 주 또는 몇 달).
     * JSON 데이터에서는 "refresh_token" 이라는 이름으로 옵니다.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * '액세스 토큰(정식 출입증)'이 앞으로 몇 초 동안 더 유효한지를 나타내는 숫자입니다.
     * 예를 들어 값이 21599 라면, 약 6시간 동안 유효하다는 의미입니다.
     * JSON 데이터에서는 "expires_in" 이라는 이름으로 옵니다.
     */
    @JsonProperty("expires_in")
    // int: 정수(소수점 없는 숫자)를 저장하는 Java 데이터 타입입니다.
    private int expiresIn;

    /**
     * '리프레시 토큰(갱신용 토큰)'이 앞으로 몇 초 동안 더 유효한지를 나타내는 숫자입니다.
     * 예를 들어 값이 5183999 라면, 약 60일 동안 유효하다는 의미입니다.
     * JSON 데이터에서는 "refresh_token_expires_in" 이라는 이름으로 옵니다.
     */
    @JsonProperty("refresh_token_expires_in")
    private int refreshTokenExpiresIn;

    /**
     * 이 토큰(액세스 토큰)으로 접근할 수 있는 사용자의 정보 범위를 나타내는 문자열입니다.
     * 사용자가 카카오 로그인 시 동의한 정보 항목들에 해당합니다.
     * 여러 개의 범위(scope)가 있을 경우, 각 범위 이름이 공백으로 구분되어 표시됩니다. (예: "profile account_email")
     * JSON 데이터에서는 "scope" 라는 이름으로 옵니다. (선택적으로 오는 항목일 수 있음)
     */
    @JsonProperty("scope")
    private String scope;

    // --- Getters and Setters ---
    // 위에서 private 으로 선언한 변수들은 '정보 은닉'이라는 개념에 따라 외부에서 직접 건드릴 수 없습니다.
    // 대신, 외부에서 이 변수들의 값을 안전하게 가져가거나(get) 설정(set)할 수 있도록
    // 정해진 규칙(JavaBeans 규약)에 따라 만드는 공개된(public) 메소드(기능)들입니다.
    // 보통 자동으로 생성하는 기능을 IDE(이클립스 등)에서 제공합니다.

    // accessToken 변수의 값을 외부에서 읽어갈 수 있게 하는 메소드
    public String getAccessToken() {
        return accessToken; // 내부 변수 accessToken의 값을 반환(return)합니다.
    }

    // 외부에서 accessToken 변수의 값을 설정(변경)할 수 있게 하는 메소드
    public void setAccessToken(String accessToken) { // 외부에서 설정할 값을 파라미터(accessToken)로 받습니다.
        // this.accessToken 은 이 클래스 내부에 선언된 멤버 변수 accessToken 을 가리키고,
        // = 뒤의 accessToken 은 파라미터로 전달받은 값을 의미합니다.
        // 즉, 외부에서 받은 값으로 내부 변수 값을 덮어쓰는(설정하는) 역할을 합니다.
        this.accessToken = accessToken;
    }

    // tokenType 변수의 값을 외부에서 읽어갈 수 있게 하는 메소드
    public String getTokenType() {
        return tokenType;
    }

    // 외부에서 tokenType 변수의 값을 설정할 수 있게 하는 메소드
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    // refreshToken 변수의 값을 외부에서 읽어갈 수 있게 하는 메소드
    public String getRefreshToken() {
        return refreshToken;
    }

    // 외부에서 refreshToken 변수의 값을 설정할 수 있게 하는 메소드
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // expiresIn 변수의 값을 외부에서 읽어갈 수 있게 하는 메소드
    public int getExpiresIn() {
        return expiresIn;
    }

    // 외부에서 expiresIn 변수의 값을 설정할 수 있게 하는 메소드
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    // refreshTokenExpiresIn 변수의 값을 외부에서 읽어갈 수 있게 하는 메소드
    public int getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    // 외부에서 refreshTokenExpiresIn 변수의 값을 설정할 수 있게 하는 메소드
    public void setRefreshTokenExpiresIn(int refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    // scope 변수의 값을 외부에서 읽어갈 수 있게 하는 메소드
    public String getScope() {
        return scope;
    }

    // 외부에서 scope 변수의 값을 설정할 수 있게 하는 메소드
    public void setScope(String scope) {
        this.scope = scope;
    }

} // KakaoTokenResponseVo 클래스 정의 끝
