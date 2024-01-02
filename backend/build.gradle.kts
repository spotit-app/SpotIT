plugins {
	java
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.3"
	jacoco
}

group = "com.spotit"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven(url = "https://jitpack.io")
}

dependencies {
	implementation("com.okta.spring:okta-spring-boot-starter:3.0.5")
	implementation("com.github.imagekit-developer:imagekit-java:2.0.0")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")

	runtimeOnly("org.postgresql:postgresql")
	compileOnly("org.projectlombok:lombok")
	
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	testImplementation("com.redis:testcontainers-redis:2.0.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<JavaCompile> {
	val compilerArgs = options.compilerArgs
	compilerArgs.add("-Amapstruct.defaultComponentModel=spring")
}

tasks.test {
	useJUnitPlatform()

	finalizedBy(tasks.jacocoTestReport) 
}

tasks.jacocoTestReport {
	dependsOn(tasks.test) 
	
	reports {
		csv.required = true
	}

	classDirectories.setFrom(files(classDirectories.files.map {
		fileTree(it) {
			setIncludes(listOf(
				"**/*ServiceImpl.class",
				"**/*Controller.class",
				"**/utils/*.class",
				"**/config/*.class"
			))
		}
	}))	
}
