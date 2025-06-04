package org.courselab.app

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual fun screenDetails(): ScreenDetails {
    TODO("Not yet implemented")
}