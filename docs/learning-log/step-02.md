# 2단계 — 등록

- 관련 강의: 6강
- 상태: 완료
- 시작일: 2026-07-16
- 완료일: 2026-07-16

## 요구사항 요약

`등록` 명령을 입력하면 명언 내용과 작가를 순서대로 입력받는다.

```
명령) 등록
명언 : 현재를 사랑하라.
작가 : 작자미상
명령) 종료
```

## 질문 로그

### 질문1
**Q.** IntelliJ에서 실행하면 한글 출력이 `◊◊◊◊`처럼 깨지는 이유는?

**A.** JDK 18+부터 `file.encoding`은 기본 UTF-8이지만, `System.out`(콘솔 출력)은 별도로 Windows 네이티브 코드페이지(한글 Windows는 MS949)를 따름. `println`이 MS949 바이트를 출력하는데 IntelliJ Gradle 콘솔은 UTF-8로 디코딩을 시도해 깨짐.

**트러블슈팅**
- 증상: 콘솔에 `◊◊◊◊`, `◊◊◊)` 등 마름모 문자로 한글이 깨져 표시됨
- 원인: `System.out`의 인코딩(native/MS949)과 콘솔이 기대하는 인코딩(UTF-8) 불일치
- 해결: Run Configuration → VM options에 `-Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8` 추가. 추후 12단계(표준입출력 리다이렉팅) 테스트에서도 재현될 수 있어 `build.gradle.kts`의 `tasks.test`에 동일 옵션을 미리 추가해둠.

---
