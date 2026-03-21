package com.sgt_hackathon.build_your_own_git;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.sgt_hackathon.build_your_own_git.utils.HashUtil.calculateSHA1;

@SpringBootApplication
public class BuildYourOwnGitApplication {

	public static void main(String[] args) {

		SpringApplication.run(BuildYourOwnGitApplication.class, args);


	}

}
