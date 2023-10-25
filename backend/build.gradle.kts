plugins {
	java
	id("org.springframework.boot") version "3.1.5"
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
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	compileOnly("org.projectlombok:lombok")
	
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	
	annotationProcessor("org.projectlombok:lombok")
	
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mongodb")
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
			setExcludes(listOf(
				"**/BackendApplication.class"
			))
		}
	}))	
}
