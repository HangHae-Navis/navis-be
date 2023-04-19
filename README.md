# [나비스] 원활한 팀 업무와 일정관리 및 팀 내 커뮤니케이션을 도와주는 서비스

### 🖥️ [NAVIS 바로가기](http://navis.kro.kr/)

![Navis.png](https://file.notion.so/f/s/16012a9c-5c5d-49ba-b816-e089454bbfdb/%EC%88%98%EC%A0%95%EB%90%A8_Thumbnail-1.png?id=420679cc-adfc-400c-bcbb-f5a5a896ca54&table=block&spaceId=4da59022-f2bc-4297-affd-e72b4fbb3e50&expirationTimestamp=1681972292171&signature=UnTgbPhMRXsbjsvZcN9gjx2G9q1Qw1dw10FynAeYgSo&downloadName=%EC%88%98%EC%A0%95%EB%90%A8_Thumbnail-1.png)

⚓[프론트엔드 깃허브](https://github.com/HangHae-Navis/navis-fe)

⚓[백엔드 깃허브](https://github.com/HangHae-Navis/navis-be)

---

## 🛠️ 서비스 아키텍처

![아키텍처.drawio (2).png](https://file.notion.so/f/s/0bcb45b9-f788-42dc-96be-88471df89ec0/%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98.drawio_(2).png?id=88bc210d-0646-4722-8a21-200366c3a6fc&table=block&spaceId=4da59022-f2bc-4297-affd-e72b4fbb3e50&expirationTimestamp=1681972384258&signature=nkeTv91eIapt-vVuaJa80v3vq4zPX_BQ-sOKnSt-Y3c&downloadName=%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98.drawio+%282%29.png)

---

## 📋 API

[https://sparta-kdh.kro.kr/swagger-ui/index.html](https://sparta-kdh.kro.kr/swagger-ui/index.html)

---

## 📔 ERD

![navisERD.png](https://file.notion.so/f/s/49426f43-2317-4dac-b7ae-c3cb4d60b67c/navisERD.png?id=1e73632b-cc1a-44f2-85d9-9300fa8191d7&table=block&spaceId=4da59022-f2bc-4297-affd-e72b4fbb3e50&expirationTimestamp=1681972415457&signature=5fBAHoDz64xoCzjcZNNVYzJwOXjVy4DYgThyWaLTbCQ&downloadName=navisERD.png)

---

## 🔧 **기술적 의사결정**

| 사용 기술 | 기술 설명 |
| --- | --- |
| github actions/ngninx | 무중단배포 CI/CD를 구성하기 위해 사용
| SMTP | 사용자들의 중복 회원 가입 더 효과적으로 막기 위해 이메일 인증을 도입하였다. |
| OAuth2 | 사용자들에게 더 쉽고 간편한 로그인/회원가입을 구현 하기 위해 사용하였다. |
| redis | 회원가입시 이메일 인증, APILimiter의 기능에 db가 필요했고 둘다 휘발적인 내용이기 때문에 빠르고 간단한 redis를 사용하였다.  |
| WebSocket&Stomp | 실시간 채팅을 구현하기 위해 WebSocket이 사용되었고 간단하게 메시지 송/수신이 가능한 Stomp를 이용하였다. |
| SSE | 사용자들에게 실시간 알림을 보내주기 위해 SSE가 도입되었다. |
| Swagger | 백엔드가 구현한 API들을 프론트 분들이 간단하게 테스트 해볼 수 있고 직관적으로 값이 뭐가 들어가고 리스폰 되는지 알 수 있기 때문에 사용하였다.   |

---

# ⚓주요기능

# **그룹 생성 및 가입**

- 쉽고 빠르게 그룹을 생성, 가입할 수 있습니다
- 자동으로 생성되는 초대 코드를 공유하여 원하는 사용자만 접근할 수 있게 합니다.
---
# 마크다운 게시글 작성

- 마크다운을 활용하여 글을 자신이 원하는대로 자유롭고 심도있게 작성할 수 있습니다.
- 거의 모든 종류의 게시글에 적용되어 있어 능숙한 만큼 완성도 높은 게시글을 만들 수 있습니다.  
---

# 과제, 제출과 피드백

- 어드민, 서포터는 해당 그룹의 모든 참가자의 과제 제출 현황을 파악할 수 있습니다.
- 어드민, 서포터는 제출자의 과제물을 확인한 뒤, 쉽게 합격 / 반려 여부를 정하고 피드백을 남길 수 있습니다.
- 24시간 남은 과제는 그룹 페이지의 상단에 노출되어 빠른 접근이 가능합니다.
- 과제가 올라오면 해당 그룹에 가입된 유저에게 알림이 뜨도록 되어 있어서 빠르게 확인할 수 있습니다.
---

# 간편하고 빠른 투표

- 그룹 내 모든 사용자가 이용할 수 있는 투표입니다.
- 모든 투표는 익명 투표이며, 언제든 투표 현황을 빠르게 확인할 수 있습니다.
---

# 설문조사와 응답 확인

- 어드민과 서포터는 자유롭게 설문 게시글을 작성할 수 있습니다.
- 전체통계에서는 각 항목에 대한 응답과 선택형, 체크박스의 통계를 확인 할 수 있습니다.
- 개별응답 보기에서는 선택한 유저의 응답만 볼 수 있습니다.
---

# 채팅
- 상대방의 이메일(계정명)을 안다면, 1대1 채팅이 가능합니다.
---

# 알림
- 가입되어 있는 그룹에 공지사항과 과제 게시글이 등록되면 유저에게 알림이 떠 빠르게 확인할 수 있습니다.
---

## 👥 팀원소개

| 역할 | 이름 | 분담 |
| --- | --- | --- |
| FE 👑 | 김재우 | 메인, 그룹, 그룹 게시글 페이지(게시글, 과제, 설문, 투표), 어드민&프로필 페이지 제작 / 메인 페이지 그룹 개설 및 가입 모달 로직 구현 / 어드민&프로필 페이지 CRUD / 그룹 게시글 페이지 과제, 투표, 설문 CURD, 그룹 게시글 페이지 댓글 기초 로직 구현 / 카카오톡 소셜 로그인 구현, 출시 후 버그 관리 |
| FE | 김태현 | 로그인 / 회원가입, 프로젝트의 전체적인 스타일링, 프론트 CI/CD, 프로젝트 설계, 마크다운 렌더링 | 에디터 기능, 게시글 작성 기능, 헤더 푸터 등 글로벌 레이아웃 작업, 채팅/알림 실시간 기능, 추후 QA 사항 수정 / 출시 후 유저 피드백 관리 |
| BE 👑 | 김동현 | 회원가입, 로그인, 채팅, 알림, 투표 CRD, 공지 CRUD, 백엔드 서버, 백엔드 CI/CD구축 |
| BE | 김동민 | 그룹 관련 CRUD / 그룹 관리기능 전반 / 24시간 이내 마감 과제 표시 / 최근 열람한 글 목록 |
| BE | 손채이 | 일반 게시글 CRUD / 과제 게시글 CRUD / 과제 제출, 과제 피드백 기능 / 설문 게시글 |
| Design | 함보라 | 디자인 담당 |

### 📱**팀원 개인 정보 (연락처)**

| 이름 | 깃허브 | 블로그 |
| --- | --- | --- |
| 김재우 | https://github.com/Nidurolak | https://karoludin.tistory.com/ |
| 김태현 |  https://github.com/tjdrkr2580 |  |
| 김동현 |  https://github.com/kil6176 | https://velog.io/@dkssudtp1 |
| 김동민 |  https://github.com/mydmk7874 | https://velog.io/@mydmk |
| 손채이 |  https://github.com/chaeyi0318 |  |
---
