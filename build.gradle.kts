import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.4.0"
}

group = "ro"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {

	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
//	implementation("org.jetbrains.kotlin:kotlin-allopen")
//	implementation("org.jetbrains.kotlin:kotlin-noarg")

	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
	testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.1")
	testImplementation("org.assertj:assertj-core:3.20.1")
	testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.test {
	useJUnitPlatform()
}
