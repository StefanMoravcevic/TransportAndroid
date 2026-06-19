plugins {
	alias(libs.plugins.android.application)
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "com.programdoo.transport"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.programdoo.transport"
		minSdk = 33
		targetSdk = 35
		versionCode = 4
		versionName = "1.0.3"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	buildFeatures {
		viewBinding = true
		dataBinding = true
	}
}

dependencies {
	implementation(libs.appcompat)
	implementation(libs.material)
	implementation(libs.constraintlayout)
	implementation(libs.lifecycle.livedata.ktx)
	implementation(libs.lifecycle.viewmodel.ktx)
	implementation(libs.navigation.fragment)
	implementation(libs.navigation.ui)
	implementation(libs.jwt.decode)
	implementation ("com.google.android.gms:play-services-location:21.0.1")
	implementation ("com.github.bumptech.glide:glide:4.16.0")
	implementation("org.jsoup:jsoup:1.17.2")
	annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

	// api
	implementation(libs.okhttp)
	implementation(libs.gson)
	implementation(libs.rxjava3)
	implementation(libs.rxjava3.rxandroid)
	implementation(libs.retrofit)
	implementation(libs.retrofit.converter.gson)
	implementation(libs.retrofit.adapter.rxjava3)
	implementation(libs.reactive.streams)


	// dependency injection
	implementation(libs.hilt)
	implementation(libs.annotation)
	implementation(libs.activity)
	implementation(libs.play.services.location)
	annotationProcessor(libs.hilt.compiler)
	// For instrumentation tests
	//androidTestImplementation("com.google.dagger:hilt-android-testing:2.57.1")
	//androidTestAnnotationProcessor("com.google.dagger:hilt-compiler:2.57.1")
	// For local unit tests
	//testImplementation("com.google.dagger:hilt-android-testing:2.57.1")
	//testAnnotationProcessor("com.google.dagger:hilt-compiler:2.57.1")

	// autogen getters and setters
	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
	//testCompileOnly("org.projectlombok:lombok:1.18.42")
	//testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

	// autodispose
	implementation(libs.autodispose)
	implementation(libs.autodispose.android)
	implementation(libs.autodispose.lifecycle)

	// kizitenwose calendar
//	implementation(libs.calendar);

	// ui
	implementation(libs.weekview)
	implementation(libs.weekview.jsr)
	implementation(libs.swipe.refresh.layout)

	testImplementation(libs.junit)
	androidTestImplementation(libs.ext.junit)
	androidTestImplementation(libs.espresso.core)
}