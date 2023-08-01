package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

// starting up a spring boot mock web env breaks with graalvm
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BreakAotCompilationTest {

	@Test
	fun startsUp() {
	}
}
