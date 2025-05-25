import SwiftUI

@main
struct iOSApp: App {
    init() {
    KoinKt.doDoInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}