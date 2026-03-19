package com.eric.sportradar_coding_challenge_java;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SportradarCodingChallengeJavaApplicationTests {

	@Test
	void contextLoads() {
	}

}
