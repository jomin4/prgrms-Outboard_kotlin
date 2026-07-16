# Git 워크플로우 컨벤션

## 원칙

학습자는 **코드 작성에만 집중**하고, git 명령은 에이전트가 정해진 흐름만 반복 수행한다. "정해진 흐름만"은 프롬프트가 아니라 [`.claude/settings.json`](../../.claude/settings.json)의 permission allow/deny로 강제된다.

## 허용된 흐름

```
git add -A
git commit -m "N단계: <내용 요약>"
git push
```

- 커밋 메시지는 한글로 단계 내용을 요약.
- 트리거: 사용자가 "N단계 됐어", "다음", "커밋해줘"라고 말하면 자동 실행.

## 시스템으로 차단된 것

`.claude/settings.json`의 `deny`에 명시:
- `git push --force` / `-f`
- `git reset --hard`
- `git branch -D` (강제 삭제)
- `git checkout -- .` / `git restore .` (변경사항 폐기)
- `git clean -f`
- `rm -rf`

이 목록에 있는 명령은 에이전트가 시도해도 도구 실행 자체가 거부된다. deny는 allow보다 항상 우선한다.

## 원격 저장소

- `https://github.com/jomin4/prgrms-Outboard_kotlin` (Public)
- 원격 저장소 이름 규칙: `prgrms-{프로젝트 폴더명}`
