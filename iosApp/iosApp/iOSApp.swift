import SwiftUI

@main
struct iOSApp: App {
    init() {
       KoinKt.doInitKoin()
       SetUpKoin.setUpNavigateCore()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

public enum SetUpKoin{

    public static func setUpNavigateCore() {
        KoinKt.doInitKoiniOS(appConfig: config)
    }

}