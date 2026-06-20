pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "planning-backend"

include("auth-service")
include("task-service")
include("gateway")

project(":auth-service").projectDir = file("authService")
project(":task-service").projectDir = file("taskService")
project(":gateway").projectDir = file("gatewayservice")
