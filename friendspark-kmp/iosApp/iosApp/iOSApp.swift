import SwiftUI
// import FirebaseCore

// Firebase setup:
// class AppDelegate: NSObject, UIApplicationDelegate {
//     func application(_ application: UIApplication,
//                      didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
//         FirebaseApp.configure()
//
//         return true
//     }
// }

@main
struct iOSApp: App {

    // Firebase setup:
    // @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}