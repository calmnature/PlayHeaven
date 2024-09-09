<div align='center'>
  <img src="https://capsule-render.vercel.app/api?type=waving&fontColor=48648a&height=150&section=header&text=Play%20Heaven&fontSize=40&desc=Game%20E-Commerce&descAlignY=70&descAlign=50"/>
  <h3>디지털 게임 유통 플랫폼</h3>
  <h4>☑ Micro Servcie Architecture</h4>
  <h4>☑ 선착순 구매 할인</h4>
</div>

<span id="table"></span>
## 🗂️ 목차
1. [프로젝트 소개](#1)
2. [개발 환경](#2)
3. [기술 스택](#3)
4. [API 명세서](#4)
5. [프로젝트 아키텍쳐](#5)
6. [프로젝트 주요 기능](#6)
7. [트러블 슈팅](#7)
8. [기술적 의사 결정](#8)
9. [보완 필요](#9)
<br><br><br><br>

<span id="1"></span>
## 🏭1. 프로젝트 소개
Play Heaven은 <b>Spring 기반의 E-Commerce 디지털 게임 유통 플랫폼</b>을 주제로 한 프로젝트입니다.<br>
누구나 판매자가 되어 게임을 등록할 수 있으며, 판매자가 원하는 수량만큼 할인할 재고를 등록하게 될 경우 게임을 구매하는 사용자들은 할인을 받을 수 있습니다.

<br><br>
[목차](#table)
<br><br><br><br>

<span id="2"></span>
## ⚙2. 개발 환경
  - **JDK Version : JDK 17**
  - **Database : MySQL**
  - **Build Tool : IntelliJ IDEA**

<br><br>
[목차](#table)
<br><br><br><br>

<span id="3"></span>
## ⚙3. 기술 스택
  - ![SpringBoot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
  - ![Database](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
  - ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white)
  - ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

<br><br>
[목차](#table)
<br><br><br><br>

<span id="4"></span>
## 📙4. API 명세서
<a href="https://documenter.getpostman.com/view/3667234/2sAXjRXVfu">API 명세서</a>는 링크를 통해 확인할 수 있습니다.

<br><br>
[목차](#table)
<br><br><br><br>

<span id="5"></span>
## 📽️5. 프로젝트 아키텍쳐
### ERD

### Micro Service Architecture

<br><br>
[목차](#table)
<br><br><br><br>

<span id="6"></span>
## 🔍6. 프로젝트 주요 기능
1. 사용자 (Member Service)
  - 회원 가입
  - 로그인(JWT 토큰 발급)
  - 회원 정보 수정
  - 회원 탈퇴 (Soft Delete)
2. 게임 (Game Service)
  - 게임 등록
  - 게임 재고 추가
  - 게임 조회
3. 주문 (Order Service)
  - 주문
    - 주문 요청
    - 주문 조회(주문 내역)
    - 주문 환불
  - 위시리스트
    - 위시리스트 등록
    - 위시리스트 조회
    - 위시리스트 삭제
4. 결제 (Payment Service)
  - 결제

<br><br>
[목차](#table)
<br><br><br><br>

<span id="7"></span>
## 💣7. 트러블 슈팅
<details>
  <summary><b>JPA Pagination 에러 발생</b></summary>
  - <b>원인</b> : PageRequest.of()에서 Sort.by() 사용 시 정렬 컬럼 이름을 "orders_id"로 지정하게 되면 <b>JPA가 언더바(_)를 외래키를 의미하는 컬럼으로 간주</b>하여 "orders" 컬럼으로 정렬을 하려는 문제 발생<br>
  - <b>해결</b> : <b>"ordersId"로 수정</b> 시 정상 작동<br>
  - 상세 트러블 슈팅 링크 : https://bestdevelop-lab.tistory.com/163
</details>

<details>
  <summary><b>Eureka Client UnsatisfiedDependencyException 에러</b></summary>
  - <b>원인</b> : Eureka Client는 <b>Eureka Server와 통신하기 위해 HTTP 요청</b>을 전송<br>
  - <b>해결</b> : <b>Spring Web Dependency 추가</b><br>
  - <a href="https://bestdevelop-lab.tistory.com/172">상세 트러블 슈팅 링크</a>
</details>

<details>
  <summary><b>[HMAC-SHA] WeakKeyException</b></summary>
  - <b>원인</b> : HMAC-SHA 알고리즘은 요구되는 <b>최소 길이가 256bit</b><br>
  - <b>해결</b> : <b>Secret Key의 길이를 늘려줌</b><br>
  - <a href="https://bestdevelop-lab.tistory.com/181">상세 트러블 슈팅 링크</a>
</details>

<br><br>
[목차](#table)
<br><br><br><br>

<span id="8"></span>
## 💡8. 기술적 의사 결정
<details>
  <summary><b>Database vs Redis</b></summary>
  - 이메일 인증 코드는 '사용자 이메일', '인증 코드' 두 개를 저장하고 일정 시간이 지난 뒤 자동 삭제<br>
  - 이를 <b>Database에 저장하게 될 경우</b> Database는 <b>디스크 기반 저장소를 사용</b>하기 때문에 I/O 성능이 낮고, <b>디스크 쓰기 작업이 빈번히 발생</b>할 수 있음<br>
  - 또한, 인증 코드를 일정 시간 후에 삭제하려면 <b>별도의 스케줄링 작업</b>이 필요<br>
  - 하지만 <b>Redis를 사용할 경우</b> In-Memory Database이기 때문에 <b>속도가 빠를 뿐만 아니라, TTL 설정을 통해 별도의 삭제 작업 없이 자동으로 만료 처리</b> 가능<br><br>
  - <b>결론</b> : 속도적으로 성능이 빠를 뿐만 아니라, 자동으로 만료처리가 되고 추후 JWT Token에서 Refresh Token을 사용하기 위해 Redis 선택
</details>

<details>
  <summary><b>Rest Template vs Feign Clinet vs Web Client</b></summary>
  > <b>RestTemplate</b><br>
      → 기존 Spring 프로젝트에서 많이 사용되던 HTTP 통신 라이브러리이나, Spring 5부터는 공식적으로 <b>Deprecated</b> 되어 더 이상 업데이트가 이루어지지 않음<br>
      → 코드가 간결하지만 확장성 면에서 한계가 존재<br><br>
  > <b>WebClient</b><br>
      → Spring 5에서 도입된 HTTP 통신을 위한 리액티브 프로그래밍 지원 클라이언트<br>
      → <b>비동기 방식과 리액티브 패러다임을 지원</b>하지만, 현재 서비스에서 비동기 통신의 필요성이 크지 않아 <b>오버스펙이라 판단</b>되어 제외<br><br>
  > <b>Feign Client</b><br>
      → HTTP 통신을 보다 간결하고 선언적으로 처리할 수 있는 Spring Cloud 라이브러리<br>
      → RestTemplate에 비해 <b>코드의 가독성과 유지보수성이 훨씬 뛰어남</b><br>
      → 또한, Feign Client는 로드 밸런싱, HTTP 요청 로깅, 리트라이 메커니즘 등 다양한 기능을 쉽게 사용할 수 있어 RestTemplate보다 유연한 확장이 가능<br><br>
  - <b>결론</b> : 비동기 통신의 필요성이 낮고, 간결하고 유지보수하기 쉬운 HTTP 통신 방법이 필요하므로 <b>Feign Client 선택</b><br>
  - <a href="https://bestdevelop-lab.tistory.com/174">Rest Template / Feign Clinet / Web Client 비교</a><br>
  - <a href="https://bestdevelop-lab.tistory.com/175">Feign Client 선택 이유 및 사용 예제</a><br>
</details>

<br><br>
[목차](#table)
<br><br><br><br>

<span id="9"></span>
## ➕9. 프로젝트 보완 사항
- Redis 캐싱
  - DB I/O를 이용한 JMeter 성능 측정
  - Redis 캐싱 적용
  - Redis 캐싱을 이용한 JMeter 성능 측정
- 분산 락
- 주문 → 결제 시 동기 → 비동기 통신
