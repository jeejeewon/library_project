# Library Web Project
**Java Servlet & JSP 기반의 도서관 통합 관리 시스템**

본 프로젝트는 도서 대출 관리와 사용자 커뮤니티 기능을 결합한 웹 애플리케이션입니다. 사용자 중심의 UI/UX 설계를 바탕으로 효율적인 정보 전달과 직관적인 인터페이스 구현에 초점을 맞추어 진행했습니다.

---

## 1. 프로젝트 개요

**프로젝트명**: 도서관 통합 관리 및 사용자 플랫폼 구축  
**개발 기간**: 2025.04.23 ~ 2025.05.23 (1개월)  
**참여 인원**: 4명  
**핵심 목표**: MVC 아키텍처 기반의 동적 콘텐츠 렌더링 및 사용자 편의성을 고려한 인터페이스 설계  

---

## 2. 기술 스택

### UI/UX & Frontend
- HTML5 / CSS3 / JavaScript / jQuery

### Backend & Server
- Java (JDK) / JSP / Servlet
- Apache Tomcat

### Database & Tools
- MySQL
- Eclipse / SQL Developer / Git

---

## 3. 데이터베이스 설계 (ERD)

![Database ERD](https://github.com/user-attachments/assets/cfa53297-7c4b-41e4-bca6-485271a72890)

---

## 4. 담당 업무 및 UI 구현 상세

### 레이아웃 모듈화 및 시각적 일관성 유지
- **Composite View 패턴 적용**: `head.jsp`, `center.jsp`, `tail.jsp`로 화면 구조를 분리해서 코드의 가독성을 높이고 유지보수가 용이한 모듈형 레이아웃을 구현했습니다.
- **Global Style 정의**: 프로젝트 전반의 폰트, 컬러 시스템, 여백 등을 규격화해서 사용자에게 일관성을 제공하고자 노력했습니다.

### 사용자 인증 및 회원 관리 시스템
- **Flow 설계**: 회원가입부터 로그인, 아이디/비밀번호 찾기까지 이어지는 사용자 흐름을 설계했습니다.
- **개인화 인터페이스**: 마이페이지를 통해 사용자 본인의 정보를 한눈에 파악하고 관리할 수 있는 대시보드 형태의 UI를 구현했습니다.
- **API**: Kakao Login API를 연동했습니다.

### 관리자용 통합 관리 페이지
- **회원 목록 관리**: 전체 회원 정보를 모니터링 할 수 있는 전용 페이지를 구현했습니다.
- **데이터 시각화**:  회원 데이터를 Table 형태로 깔끔하게 구성해서 관리자가 필요한 정보를 쉽게 찾을 수 있도록 했습니다.

---

## 5. 주요 구현 화면

| 공통 레이아웃 설계 | 회원 인증 시스템 인터페이스 |
| :--- | :--- |
| ![Layout](https://github.com/user-attachments/assets/3280cbca-ed57-43a0-9493-7165a12be465) | ![Auth](https://github.com/user-attachments/assets/93bc5a82-c334-4c4f-870d-3f0eeb1f24e9) |

| 마이페이지 대시보드 | 관리자 회원 관리 시스템 |
| :--- | :--- |
| ![Mypage](https://github.com/user-attachments/assets/20bd4aad-0f94-4d7a-abfe-a46c15815450) | ![Admin](https://github.com/user-attachments/assets/f9943bdb-aebc-4de5-8f2b-1a5231372fee) |

---

## 6. 프로젝트 소개

[프로젝트 소개서(PDF) 다운로드](https://raw.githubusercontent.com/jeejeewon/library_project/main/도서관프로젝트.pdf)
