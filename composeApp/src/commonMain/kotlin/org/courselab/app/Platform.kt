package org.courselab.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

