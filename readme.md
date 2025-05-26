# ✨ Eurekagram
> 유레카그램은 유레카 부트캠프 참여자들만을 위한 비공개 커뮤니티 피드형 SNS 입니다.  
> 개발 기간 : 2025.05.08 ~ 25.05.16

<br>

## 📑 목차
- [팀원 소개](#-팀원-소개)
- [기획 의도](#-기획-의도)
- [기술 스택](#-기술-스택)
- [기능 상세](#-기능-상세)
- [트러블슈팅](#-트러블슈팅)
- [한계점](#-한계와-개선-방향)

<br>

## 👤 팀원 소개
| 이름 | 사진                                                       | GitHub                                        | 역할 | 파트               |
|--------|------------------------------------------------------------|-----------------------------------------------|------|--------------------|
| 서보인 | <img src="https://avatars.githubusercontent.com/u/0000001" width="80"/> | [@sbi1024](https://github.com/sbi1024)        | 팀장 | BackEnd/FrontEnd   |
| 문태신 | <img src="https://avatars.githubusercontent.com/u/0000002" width="80"/> | [@taeaeaexin](https://github.com/taeaeaexin)  | 팀원 | BackEnd/FrontEnd   |
| 박소연 | <img src="https://avatars.githubusercontent.com/u/0000003" width="80"/> | [@so-yeon1](https://github.com/so-yeon1)      | 팀원 | BackEnd/FrontEnd   |

<br>

# 🎯 기획 의도
유레카그램은 유레카 부트캠프 참여자들만을 위한 비공개 커뮤니티 피드형 SNS 입니다.
사진과 짧은 글을 통해 서로의 일상을 공유하고, 유레카 부트캠프라는 공통된 소속감을 기반으로 자연스럽게
소통할 수 있도록 돕습니다.

<br>

## 🧱 기술 스택

<b>Back-End</b>
<p align="left">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=java,spring,gradle,mysql&theme=light" />
  </a>
</p>

<b>Front-End</b>
<p align="left">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=html,css,js&theme=light" />
  </a>
</p>

<b>Collaboration-Tool</b>
<p align="left">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=git,github,discord&theme=light" />
  </a>
</p>

<br>

## 🔧 기능 상세
### ⚡️ 일반 사용자
1. 로그인 기능
2. 회원 가입 기능
3. 피드 등록 기능
4. 피드 전체 조회 기능
5. 나의 피드 조회 기능
6. 피드 댓글 기능 (대댓글 없음)
7. 본인 피드 수정 기능
8. 본인 정보 수정

### ⚡ 관리자
1. 로그인 기능 (애초에 DB상에 관리자 계정 정보를 넣을 예정)
2. 피드 전체 조회 기능
3. 피드 특정 키워드로 조회 기능 (닉네임)
4. 피드 삭제 기능 (수정기능은 필요하지 않을듯 싶음)
5. 사용자 정보 전체 조회 기능
6. 사용자 정보 수정 기능 (true false)
7. 사용자 정보 삭제 기능


## ✨ 필요한 페이지
### ⚡ 일반 사용자
1. 로그인 페이지
2. 회원 가입 페이지
3. 메인 페이지 (피드 전체 조회 되는 페이지) > 검색 기능이 여기에 있어야 함
4. 피드 상세 조회 페이지 (댓글 작성 기능이 포함 됨, 내가 작성한 댓글을 수정할 수 있어야 함)
5. 피드 등록 페이지
6. 마이 페이지 (내가 등록한 피드가 보이는 페이지) > 이것도 그냥 한 줄로 쭉 보여줄수 있도록 배치
7. 기존에 등록한 피드를 수정하는 페이지
8. 개인 정보 수정 페이지

### ⚡ 관리자
1. 로그인 페이지
2. 메인 페이지 (피드 전체 조회 되는 페이지) > 검색 기능이 여기에 있어야 함
3. 피드 상세 조회 페이지 (피드삭제, 댓글 삭제)
4. 사용자 관리 페이지 - 승인 / 미승인 (검색 기능 필요)
5. 사용자 관리 페이지 - 정보 변경 (검색 기능 필요)

## 🔍 트러블슈팅

## ⏳ 한계와 개선 방향
