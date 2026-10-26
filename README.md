# camping-rental-api

Spring Boot 2.6.3 / Maven / MySQL. 바이트코드는 Java 11, 실행은 JDK 21 기준으로 검증됨.

## 요구사항
- JDK 21 (빌드에 `javac` 필요 → `sudo apt install openjdk-21-jdk-headless`)
- Maven 설치 불필요 (`./mvnw` 사용)
- 배포 시: 원격 서버에 Docker + Docker Compose

> JDK 21 호환을 위해 `pom.xml`에 `lombok.version=1.18.34`를 고정했고, `java.version`은 11로 유지한다.
> (Spring 5.3이 Java 21 바이트코드를 못 읽을 수 있음)

## 설정 파일 (profile)
`src/main/resources/` — 전부 `.gitignore` 대상이라 git에 없음. 새 환경에서는 직접 넣어야 한다.

| 파일 | 용도 |
|---|---|
| `application.properties` | 공통 설정, 기본 profile `local` 지정 |
| `application-local.properties` | 로컬 DB(`localhost:3306/campal_dev1`), 쿠키 `localhost` / `secure=false` |
| `application-prod.properties` | RDS, 쿠키 `campal.co.kr` / `secure=true` |

같은 키는 profile 파일이 공통 설정을 덮어쓴다. profile은 빌드가 아니라 **실행 시** 선택한다.

## 로컬 실행
```bash
./mvnw spring-boot:run          # local profile, http://localhost:8081
```
로컬 MySQL(`campal_dev1`)이 떠 있어야 한다.

## 빌드 / 직접 실행
```bash
./mvnw -DskipTests clean package
java -jar target/server-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
# 또는 SPRING_PROFILES_ACTIVE=prod java -jar ...
```

## 운영 배포 (원격 Docker + Caddy HTTPS)
구성: `외부 :443 → Caddy → (Docker 내부망) app:8081`

- `Dockerfile` — `eclipse-temurin:21-jre`, `SPRING_PROFILES_ACTIVE=prod`
- `docker-compose.yml` — `app` + `caddy`. 앱 8081은 호스트에 노출 안 함 (호스트 8081은 개발용으로 비어 있음)
- `Caddyfile` — `api.campal.co.kr` → `app:8081`, Let's Encrypt 인증서 자동 발급/갱신

**사전 조건**
1. `api.campal.co.kr` A 레코드 → 원격 서버 IP (`dig +short api.campal.co.kr`로 확인)
2. 보안그룹/방화벽 443 인바운드 `0.0.0.0/0` (Let's Encrypt 검증 + 브라우저 직접 호출)
3. RDS 보안그룹에서 원격 서버 IP의 3306 허용
4. 빌드하는 곳에 `application-prod.properties` 존재 (jar에 포함됨)

**배포**
```bash
./mvnw -DskipTests clean package

export DOCKER_HOST=ssh://사용자@서버주소
docker compose up -d --build
docker compose logs -f caddy     # "certificate obtained successfully" 확인
unset DOCKER_HOST
```

**확인**
```bash
curl https://api.campal.co.kr/api/v1/ping     # {"message":"pong", ...}
```

## 내리기 / 정리
```bash
docker compose down              # 컨테이너, 네트워크
docker compose down --rmi all    # + 이미지(앱, caddy)
docker compose down --rmi all -v # + caddy_data 볼륨 (인증서 삭제, 재발급 필요)
```

## 주의
- 이미지 안에 `application-prod.properties`(비밀번호/키)가 들어간다 → 공개 레지스트리에 push 금지.
- `caddy_data` 볼륨을 지우지 말 것. 재발급이 반복되면 Let's Encrypt 발급 제한에 걸린다.
- 인증서 만료일 확인:
  `echo | openssl s_client -connect api.campal.co.kr:443 -servername api.campal.co.kr 2>/dev/null | openssl x509 -noout -dates`
- http→https 리다이렉트가 필요하면 `docker-compose.yml`의 caddy `ports`에 `"80:80"` 추가.
