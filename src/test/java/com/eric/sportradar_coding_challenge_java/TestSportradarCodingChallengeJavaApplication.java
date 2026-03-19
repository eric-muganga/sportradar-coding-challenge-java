package com.eric.sportradar_coding_challenge_java;

import org.springframework.boot.SpringApplication;

public class TestSportradarCodingChallengeJavaApplication {

	public static void main(String[] args) {
		SpringApplication.from(SportradarCodingChallengeJavaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
