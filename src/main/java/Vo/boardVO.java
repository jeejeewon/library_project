package Vo;

import java.sql.Timestamp;  // 수정: java.security.Timestamp -> java.sql.Timestamp

//게시판테이블에서 조회해온 데이터를 저장할 VO
public class boardVO {

	//변수들 선언
	private int boardId;  //게시글 고유 번호
	private	int category;  //게시글 카테고리번호 (0:공지사항, 1:문의게시판, 2:내 서평)
	private String title;  //게시글 제목
	private String content;  //게시글 내용
	private String userId;  //유저 ID
	private int bookNo;  //도서번호
	private String file;  //첨부파일명
	private String bannerImg;  //배너파일명
	private Timestamp createdAt;  //게시글 작성일 (DATETIME)
	private int views;  //게시글 조회수
	private Boolean secret;  //게시글 공개 여부 (0/false :공개 , 1/true:비공개)
	private String reply;  //게시글 답변
	
	private String thumbnail; //책 이미지 경로 필드
	
	//기본생성자
	public boardVO() {}
	
	//모든 게시판정보 받아서 boardVO객체를 만드는 생성자
	public boardVO(int boardId, int category, String title, String content, String userId, int bookNo, String file, String bannerImg, Timestamp createdAt , int views, Boolean secret, String reply) {
		
		super();
		this.boardId = boardId;
		this.category = category;
		this.title = title;
		this.content = content;
		this.userId = userId;
		this.bookNo = bookNo;
		this.file = file;
		this.bannerImg = bannerImg;
		this.createdAt  = createdAt ;
		this.views = views;
		this.secret = secret;
		this.reply = reply;
		
	}
	
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
	
	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getBookNo() {
		return bookNo;
	}

	public void setBookNo(int bookNo) {
		this.bookNo = bookNo;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getBannerImg() {
		return bannerImg;
	}

	public void setBannerImg(String bannerImg) {
		this.bannerImg = bannerImg;
	}

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;

    }

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public Boolean getSecret() {
		return secret;
	}

	public void setSecret(Boolean secret) {
		this.secret = secret;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
}
