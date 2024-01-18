# <img src="https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/b319291f-ec97-419b-949f-c6a7e3834c32" width="50" height="50"/> 맛.ZIP (위치기반 부산 맛집 공유 커뮤니티 앱)
![image](https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/f65ff2dc-eddb-4f19-89d2-9a2e2cfdcca4)
- 일정 : 2023.11.17.~ 2023.12.01 (업데이트중)
- 배포 URL : (출시 진행중)
- Test ID : ooo@naver.com
- Test PW : 1234
<br>

## 프로젝트 소개
- 넘쳐나는 광고성 맛집 정보에 지친 사람들을 위한 신뢰 기반 부산 맛집 공유 SNS입니다.
- 개인의 프로필 페이지에 나의 맛집에 대한 정보를 작성할 수 있습니다.
- 내 근처 맛집 정보를 지도에 띄워 제공합니다.
- 검색을 통해 다른 유저들의 맛집 정보와 식당상세 정보를 찾아볼 수 있습니다.
- 다양한 유저들을 팔로우하며 마음에 드는 게시글에 좋아요를 누르거나 댓글을 작성 할 수 있습니다.
<br>

## 팀원 구성
| 손주원(팀장) | 김경태 | 서동옥 | 손유영 | 최수연 |
| :---: | :---: | :---: | :---: | :---: |
| <img src="https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/637dc64e-6e5f-4fa1-b2a7-68a0f9174de0" width="90" height="80"/>| <img src="https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/684414ca-d820-4321-9860-dd8ed2dea7ec" width="80" height="80"/> | <img src="https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/a2e19a00-19cc-419b-8a26-e1b85be54b4b" width="90" height="90"/> | <img src="https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/547f8182-a3a9-4605-8202-1f2fa82f66a7" width="80" height="80"/> | <img src="https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/074fd9af-a75c-461c-b510-4c09f8491bd5" width="90" height="80"/> |
| [@jurwon](https://github.com/jurwon) | [@RomanticToad](https://github.com/RomanticToad) | [@eastok](https://github.com/eastok) | [@sonyuyoung](https://github.com/sonyuyoung) | [@Sophie](https://github.com/ssasdfccaf) |
<br>

## 1. 개발 환경
- Frontend : Android
- Backend : SpringBoot, MySQL, Firebase
- 버전관리 : GitHub
- Tool : Android Studio, Intelij, MySQL Workbench
<br>

## 2. 프로젝트 설계
![image](https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/f12b2e6b-0419-452a-97b0-3f4032240fc2)
![image](https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/69ea8669-d816-435b-8b2e-4bba5eaf5b80)
<br><br>

## 3. 화면 구성
![image](https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/e5002030-483c-4a08-abcc-d55a5e1f0a30)<br>
<br>

## 4. 페이지별 기능
### \[초기화면\]
- 서비스 접속시 보이는 화면으로 splash 화면이 잠시 나온 뒤 다음 페이지가 나타납니다.
  - 자동로그인 되어 있지 않은 경우 : 로그인/회원가입 선택 페이지
  - 자동로그인 되어 있는 경우 : 홈 화면
  <br>
  
  | 초기화면 |
  | :---: |
  | ![초기화면](https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/57f3d1c2-085c-41c1-8d22-a7fb97ba3529) |

  <br>

### \[회원가입\]
- 회원가입에 필요한 정보를 입력하고 버튼을 누르면 유효성검사가 진행되고 통과하지 못한 경우 경고 문구가 표시됩니다.
  <br>
  
  | 회원가입 |
  | :---: |
  |  |

  <br>

### \[로그인\]
  - 회원가입에 필요한 정보를 입력하고 버튼을 누르면 유효성검사가 진행되고 통과하지 못한 경우 경고 문구가 표시됩니다.
  <br>
  
  | 로그인(자동로그인x) |
  | :---: |
  |  |

  <br>

  | 로그인(자동로그인ㅇ) |
  | :---: |
  | ![초기화면](https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/57f3d1c2-085c-41c1-8d22-a7fb97ba3529) |

  <br>
  
### \[프로필\]
<br>
| 내 프로필(+수정) |
  | :---: |
  | ![초기화면](https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/57f3d1c2-085c-41c1-8d22-a7fb97ba3529) |

<br>

### \[홈\]
<br>
| 게시글 목록(팔로우한 유저 없을 때) |
  | :---: |
  | ![초기화면](https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/57f3d1c2-085c-41c1-8d22-a7fb97ba3529) |

<br>

| 게시글 목록(팔로우한 유저 있을 때) |
  | :---: |
  | ![초기화면](https://github.com/jurwon/Find_My_Matzip_Android/assets/35756071/57f3d1c2-085c-41c1-8d22-a7fb97ba3529) |

<br>



- 프로필
- 프로필 수정
- 타 유저의 프로필(팔로우,언팔로우)
- 드로워 메뉴(회원탈퇴,로그아웃,검색,채팅)
- 홈(게시글 목록, 팔로우한 유저의 게시글 목록)
- 게시글 상세(댓글,좋아요싫어요)
- 식당(식당 상세,리뷰보기)
- 게시글 작성
- 내 주변 식당 찾기(반경 설정, 반경내 식당 리스트)
- 식당등록(수정)
- 채팅
<br><br>

## 5. 역할 분담
<br>

## 6. 코드 리뷰(개인 작성 부분)

<br>

## 7. 트러블 슈팅(개인 작성 부분)
<br>

## 8. 개선 목표



