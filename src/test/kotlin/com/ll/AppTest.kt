package com.ll

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class AppTest {

    @BeforeEach
    fun setUp() {
        File("db/wiseSaying").deleteRecursively()
        File("data.json").delete()
    }

    private fun run(input: String): String {
        val originalIn = System.`in`
        val originalOut = System.out

        System.setIn(ByteArrayInputStream(input.toByteArray()))
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        App().run()

        System.setIn(originalIn)
        System.setOut(originalOut)

        return outputStream.toString()
    }

    @Test
    fun `등록 테스트`() {
        val output = run(
            """
            등록
            현재를 사랑하라.
            작자미상
            종료
            """.trimIndent()
        )

        assertThat(output).contains("1번 명언이 등록되었습니다.")
    }

    @Test
    fun `목록 테스트`() {
        val output = run(
            """
            등록
            현재를 사랑하라.
            작자미상
            등록
            과거에 집착하지 마라.
            작자미상
            목록
            종료
            """.trimIndent()
        )

        assertThat(output).contains("2 / 작자미상 / 과거에 집착하지 마라.")
        assertThat(output).contains("1 / 작자미상 / 현재를 사랑하라.")
    }

    @Test
    fun `삭제 테스트`() {
        val output = run(
            """
            등록
            현재를 사랑하라.
            작자미상
            삭제?id=1
            삭제?id=1
            종료
            """.trimIndent()
        )

        assertThat(output).contains("1번 명언이 삭제되었습니다.")
        assertThat(output).contains("1번 명언은 존재하지 않습니다.")
    }

    @Test
    fun `수정 테스트`() {
        val output = run(
            """
            등록
            과거에 집착하지 마라.
            작자미상
            수정?id=1
            현재와 자신을 사랑하라.
            홍길동
            종료
            """.trimIndent()
        )

        assertThat(output).contains("명언(기존) : 과거에 집착하지 마라.")
        assertThat(output).contains("작가(기존) : 작자미상")
    }

    @Test
    fun `빌드 테스트`() {
        run(
            """
            등록
            현재를 사랑하라.
            작자미상
            빌드
            종료
            """.trimIndent()
        )

        val dataJson = File("data.json").readText()
        assertThat(dataJson).contains("\"id\": 1")
        assertThat(dataJson).contains("현재를 사랑하라.")
    }
}