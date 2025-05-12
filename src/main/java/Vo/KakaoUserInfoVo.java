package Vo; // 파일이 위치한 패키지와 일치해야 합니다.

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 카카오 사용자 정보 API(/v2/user/me) 응답 데이터를 담는 DTO(Data Transfer Object) 클래스입니다. 카카오
 * API 응답 JSON 구조에 맞춰 필드를 정의하고, Jackson 라이브러리의 어노테이션을 사용하여 매핑합니다.
 * 
 * @JsonIgnoreProperties(ignoreUnknown = true): JSON 응답에 정의되지 않은 필드가 있어도 오류 없이
 *                                     무시합니다.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoVo { // 클래스명 public으로 변경

	/**
	 * 카카오 사용자 고유 ID (Long 타입) JSON 필드명 "id"와 매핑됩니다.
	 */
	@JsonProperty("id")
	private Long id;

	/**
	 * 사용자가 앱과 연결된 시각 (UTC, ISO 8601 형식 문자열) JSON 필드명 "connected_at"와 매핑됩니다.
	 */
	@JsonProperty("connected_at")
	private String connectedAt;

	/**
	 * 카카오 계정 정보를 담는 객체 JSON 필드명 "kakao_account"와 매핑됩니다. 내부 클래스 KakaoAccount 타입으로
	 * 정의됩니다.
	 */
	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	// --- Getters and Setters ---
	// 각 필드의 값을 가져오거나 설정하는 표준 getter/setter 메소드들입니다.

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConnectedAt() {
		return connectedAt;
	}

	public void setConnectedAt(String connectedAt) {
		this.connectedAt = connectedAt;
	}

	public KakaoAccount getKakaoAccount() {
		return kakaoAccount;
	}

	public void setKakaoAccount(KakaoAccount kakaoAccount) {
		this.kakaoAccount = kakaoAccount;
	}

	/**
	 * 카카오 계정(kakao_account) 내의 상세 정보를 담는 정적 내부 클래스입니다.
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoAccount {

		/**
		 * 카카오계정 대표 이메일 (사용자가 동의한 경우) JSON 필드명 "email"과 매핑됩니다.
		 */
		@JsonProperty("email")
		private String email;

		/**
		 * 카카오 프로필 정보를 담는 객체 JSON 필드명 "profile"과 매핑됩니다. 내부 클래스 Profile 타입으로 정의됩니다.
		 */
		@JsonProperty("profile")
		private Profile profile;

		// email_needs_agreement, is_email_valid, is_email_verified 등
		// 카카오 API 응답에 포함될 수 있는 다른 필드들도 필요에 따라 여기에 추가할 수 있습니다.

		// --- Getters and Setters ---
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Profile getProfile() {
			return profile;
		}

		public void setProfile(Profile profile) {
			this.profile = profile;
		}
	}

	/**
	 * 카카오 프로필(profile) 내의 상세 정보를 담는 정적 내부 클래스입니다.
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Profile {

		/**
		 * 사용자 닉네임 JSON 필드명 "nickname"과 매핑됩니다.
		 */
		@JsonProperty("nickname")
		private String nickname;

		/**
		 * 프로필 이미지 URL (640x640 크기) JSON 필드명 "profile_image_url"과 매핑됩니다.
		 */
		@JsonProperty("profile_image_url")
		private String profileImageUrl;

		/**
		 * 프로필 썸네일 이미지 URL (110x110 크기) JSON 필드명 "thumbnail_image_url"과 매핑됩니다.
		 */
		@JsonProperty("thumbnail_image_url")
		private String thumbnailImageUrl;

		// is_default_image 등 다른 프로필 관련 필드도 필요시 추가 가능

		// --- Getters and Setters ---
		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getProfileImageUrl() {
			return profileImageUrl;
		}

		public void setProfileImageUrl(String profileImageUrl) {
			this.profileImageUrl = profileImageUrl;
		}

		public String getThumbnailImageUrl() {
			return thumbnailImageUrl;
		}

		public void setThumbnailImageUrl(String thumbnailImageUrl) {
			this.thumbnailImageUrl = thumbnailImageUrl;
		}
	}
} // KakaoUserInfoVo 클래스 끝
