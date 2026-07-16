# 10단계 — data.json 빌드

- 관련 강의: 34강
- 상태: 완료
- 시작일: 2026-07-16
- 완료일: 2026-07-16

## 요구사항 요약

`빌드` 명령을 입력하면 모든 명언을 모아서 `data.json` 파일을 생성한다.

```
명령) 빌드
data.json 파일의 내용이 갱신되었습니다.
```

```json
[
  {
    "id": 1,
    "content": "명언 1",
    "author": "작가 1"
  },
  {
    "id": 2,
    "content": "명언 2",
    "author": "작가 2"
  }
]
```

## 질문 로그

### 질문1
**Q.** 확장 함수(`fun WiseSaying.toJsonStr()`) 방식이 구체적으로 어떻게 동작하는가?

**A.** 클래스 자체는 전혀 안 바뀜. 컴파일러가 `fun WiseSaying.toJsonStr()`을 "숨겨진 첫 번째 매개변수로 수신 객체를 받는 일반 함수"로 변환하고, `wiseSaying.toJsonStr()` 호출도 `toJsonStr(wiseSaying)`으로 바꿔 실행. 함수 안에서 쓰는 `id`, `content` 등은 사실 `this.id`(this는 수신 객체)의 생략형. 자바로 치면 `WiseSayingUtil.toJsonStr(wiseSaying)` 같은 정적 유틸 메서드를 점(.) 문법으로 예쁘게 포장한 것. 지금까지 써온 `.reversed()`, `.first{}`, `.split()`, `.sortedBy{}`, `.joinToString{}` 전부 코틀린 표준 라이브러리가 미리 만들어둔 확장 함수이고, 이번에 처음으로 직접 만들어본 것.

---
