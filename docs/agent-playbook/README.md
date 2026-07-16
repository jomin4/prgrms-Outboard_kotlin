# 에이전트 하네스 플레이북

AI 코딩 에이전트(Claude Code)로 이 프로젝트를 진행하며 쌓은 **재사용 가능한 세팅 방식**을 정리한 문서입니다. "프롬프트로 매번 설명하기"가 아니라 **시스템 레벨로 강제하기**를 지향합니다.

## 왜 3층 구조인가

프롬프트/CLAUDE.md에 "이렇게 해줘"라고 적어두는 것은 에이전트가 매번 다시 읽고 "따르기로 판단"하는 것일 뿐, 지켜지리라는 보장이 없습니다. 이건 **프롬프트 엔지니어링**입니다.

**하네스 엔지니어링**은 에이전트가 아예 다르게 행동할 수 없도록 도구·설정·스크립트 레벨에서 제약을 겁니다.

```
[문서 레이어]     docs/               사람이 읽는 "왜" (설명, 트러블슈팅 기록)
[강제 레이어]     .claude/settings.json  에이전트를 막는 "룰" (권한 allow/deny)
[실행 레이어]     scripts/            재현을 자동화하는 "실행" (환경 세팅 스크립트)
```

## 이 프로젝트에서의 실제 적용

| 레이어 | 파일 | 내용 |
|---|---|---|
| 문서 | [`ENVIRONMENT.md`](./ENVIRONMENT.md) | JDK21+Gradle8.10 조합을 쓰는 이유, 겪은 트러블슈팅 |
| 문서 | [`GIT-WORKFLOW.md`](./GIT-WORKFLOW.md) | 챕터별 자동 커밋/푸시 컨벤션 |
| 강제 | [`.claude/settings.json`](../../.claude/settings.json) | git 명령어 allowlist/denylist (add/commit/push만 허용, force류 차단) |
| 실행 | [`setup-env.ps1`](../../scripts/setup-env.ps1) | JDK21 다운로드→gradle.properties 고정→빌드 검증까지 원클릭 |

## 다음 프로젝트에서 재사용하는 법

1. `scripts/setup-env.ps1`을 새 프로젝트에 복사 → JDK 버전 등 상수만 바꿔서 실행 → 환경 재현 완료
2. `.claude/settings.json`의 permissions 구조를 그대로 복사 → 그 프로젝트에 맞는 명령어로 allow/deny만 교체
3. `docs/agent-playbook/` 폴더 구조를 템플릿으로 복사 → 내용만 새로 채움

## 앞으로 추가하면 좋은 것들

프로젝트가 진행되며 필요해지는 하네스 항목은 [`HARNESS-BACKLOG.md`](./HARNESS-BACKLOG.md)에 정리해둡니다.
