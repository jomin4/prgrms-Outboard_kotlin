# 환경 세팅 컨벤션

## 스펙

| 항목 | 값 | 이유 |
|---|---|---|
| Kotlin | 2.1.10 | 교재([slog.gg/p/14130](https://www.slog.gg/p/14130)) 기준 버전 고정 |
| Gradle | 8.10 (Wrapper) | 교재 기준. 전역 설치 대신 wrapper로 프로젝트에 내장 |
| JDK | 21 (Temurin) | 아래 트러블슈팅 참고 — Gradle 8.10의 상한 버전 |
| 패키지 루트 | `com.ll` | 교재 커리큘럼 기준 |

## 트러블슈팅 히스토리

### 문제: 시스템 JDK(25)로는 Gradle 8.10 빌드가 실패함

**증상**
```
FAILURE: Build failed with an exception.
* What went wrong:
25.0.3
```

**원인**
Gradle 8.10은 Java 23까지만 지원. 시스템에 설치된 JDK가 25라서 Gradle 데몬이 뜨자마자 버전 체크에서 실패.

**해결**
시스템 JDK는 그대로 두고, **프로젝트 전용 JDK 21**을 `~/.jdks/`에 별도로 받아서 Gradle에게만 그 경로를 알려줌.

- `gradle.properties`에 `org.gradle.java.home=<JDK21 경로>` 지정 → 터미널(`gradlew`)에서 정상 동작
- `.idea/gradle.xml`에 `<option name="gradleJvm" value="temurin-21" />` 추가 → IntelliJ 동기화도 정상 동작

**왜 두 곳에 다 설정해야 하나**
`gradle.properties`는 CLI(`gradlew`)가 읽는 설정이고, IntelliJ는 자체 프로젝트 JDK(`misc.xml`의 `project-jdk-name`)를 우선 사용하려 하기 때문에 별도로 `.idea/gradle.xml`에 Gradle JVM을 지정해줘야 함. 하나만 설정하면 터미널과 IDE 중 한쪽만 빌드에 성공하는 상황이 생김.

### 문제: Windows 콘솔에서 한글 출력이 `◊◊◊◊`로 깨짐

**증상**
IntelliJ Run 창에서 `println("명언앱")`이 마름모 문자로 깨져 나옴.

**원인**
JDK 18+부터 `file.encoding`은 기본 UTF-8이지만, `System.out`은 별도로 Windows 네이티브 코드페이지(한글 Windows는 MS949)를 따름. 콘솔이 UTF-8로 디코딩을 시도하면서 불일치 발생.

**해결**
- Run Configuration에 VM 옵션 `-Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8` 추가
- **이 설정을 `.idea/runConfigurations/MainKt.xml`로 영구 저장하고 git에 커밋**해서, 다음에 프로젝트를 새로 열거나 클론해도 매번 수동으로 재설정할 필요가 없게 함. IntelliJ의 기본 임시(temporary) 실행 설정은 워크스페이스 로컬 상태라 공유되지 않으므로, 반드시 "공유 가능한 실행 구성"(Save configuration)으로 전환해야 재현성이 생김
- `build.gradle.kts`의 `tasks.test`에도 동일 VM 인자를 추가해 12단계(TDD) 테스트 실행 시에도 동일 문제가 재현되지 않도록 선반영

**컨벤션화**: Windows에서 콘솔 출력이 있는 Kotlin 프로젝트를 새로 세팅할 때는 이 인코딩 옵션을 실행 구성 생성과 동시에 기본으로 넣어둔다. 문제가 생긴 뒤에 고치는 게 아니라 세팅 단계에 포함시키는 것이 하네스 엔지니어링 원칙에 맞다.

## 재현 방법

새 환경(또는 다음 프로젝트)에서는 [`scripts/setup-env.ps1`](../../scripts/setup-env.ps1)을 실행하면 위 과정이 전부 자동화됩니다.
