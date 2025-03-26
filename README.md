<p align='center'><img width="518" alt="Image" src="https://github.com/user-attachments/assets/739c96ac-ebcb-4235-aa44-d79a5315e090" />



# 1. OverView(프로젝트 개요)
- 프로젝트 이름: ComNCheck
- 프로젝트 설명: 한국외국어대학교 컴퓨터공학부 알리미
- 프로젝트 시작 계기 : 학창시절 과회장을 하면서 학부 행사 같은 경우 카톡방에 쌓이고, 학교 공지는 홈페이지에 수시로 들어가야 하는 불편함을 하나의 서비스로 해결하기 위해
- 프로젝트 사이트 : https://www.comncheck.com
- 프로젝트에서 하고 싶었던 부분 : Spring, FastAPI 백엔드 개발, 쿠버네티스 기반 서버 구축 및 운영
# 2. Team Members (팀원 및 팀 소개)
|                                               조성민                                                |                                               노성원                                                |                                               이예림                                                |
|:------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------:|
|                                         Lead, BE, Server                                         |                                        FE, UI/UX Designer                                        |                                        FE, UI/UX Designer                                        |
| <p align='center'><img src="https://avatars.githubusercontent.com/u/101984130?v=4" height=100/>  | <p align='center'><img src="https://avatars.githubusercontent.com/u/129041262?v=4" height=100/>  | <p align='center'><img src="https://avatars.githubusercontent.com/u/129266668?v=4" height=100/>  |
|                           [@sungmin306](https://github.com/sungmin306)                           |                           [@sungwonnoh](https://github.com/sungwonnoh)                           |                             [@YerimLee](https://github.com/yerimi00)                             |
# 3. Project Preview
<p align='center'><img src="https://github.com/user-attachments/assets/07c2a735-6f02-4421-9c76-c0f3740af64f" />

- 약 134명의 사용자가 현재 접속하여 사용중(2025.03.24 기준)

# 4. Architecture
<p align='center'><img src='https://github.com/user-attachments/assets/8fe431d7-f6df-4780-a497-55da01240110'/></p>

1. 단일 노드에서 `K3s` 기반으로 구축했다.(기존 GCP에서 미니PC 설치 후 서버 이동)
2. 클라이언트(Client)는 `Nginx` 서버로 접속한다. → 80 또는 **`443`**
  - Nginx 내부적으로 Certbot을 이용하여 SSL 인증을 진행
3. `Nginx`는 “/” 경로로 온 요청은 Next.js 컨테이너로 요청을 보낸다.
4. `Nginx`는 “/api” 경로로 온 요청은 `Spring` 컨테이너로 요청을 보낸다.
  - 직접적으로 외부 통신 하지 않는 파드 같은 경우 Cluster IP로 설정하여 외부 서버에서 접근하는것을 막았다.
  - `FastAPI` 및 `Next.js` 같은 경우 Replicaset 기반으로 5개의 파드가 동작해 비스의 확장성과 안정성이 보장한다.
5. 모든 요청과 응답은 `Nginx`를 통해 전달한다.
6. DB 서버는 백업기능을 위해 서버 로컬에서 관리한다.

# 5. Service Detail
|                                                         일정 관리                                                         |                                                         알람 기능                                                         |
|:---------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------:|
| <img width="492" alt="Image" src="https://github.com/user-attachments/assets/20bb9454-bdbd-47aa-a32d-6fb397a94da1" /> | <img width="495" alt="Image" src="https://github.com/user-attachments/assets/024f8a41-c2f2-4f0e-9e14-aadb0aaa78a6" /> |
|                                                         **익명 질문**                                                         |                                                       **추가 기능**                                                       |
| <img width="490" alt="Image" src="https://github.com/user-attachments/assets/4669557e-f505-40cf-9084-88ce13ce25ce" /> | <img width="490" alt="Image" src="https://github.com/user-attachments/assets/97f16b11-08ec-400f-93dd-c61c2c84b7d1" /> |

# 4. Tech stack

### Backend

<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/FastAPI-009688?style=flat-square&logo=Spring&logoColor=white"/> 
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/> 
<img src="https://img.shields.io/badge/Kubernetes-326CE5?style=flat-square&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> 
<img src="https://img.shields.io/badge/Ubuntu-E95420?style=flat-square&logo=Ubuntu&logoColor=white"/>


# 5. 앞으로의 목표

백엔드

- [ ]  테스트코드 작성
- [ ]  레디스 이용 → 성능 최적화
- [ ]  코드 리펙토링(클린코드 만들기)
- [ ]  로직개선

클라우드

- [ ]  control-plane, worker 노드 분리(고가용성)
- [ ]  HPA 설정
- [ ]  모니터링 툴 세팅
- [ ]  CI/CD 환경구성
