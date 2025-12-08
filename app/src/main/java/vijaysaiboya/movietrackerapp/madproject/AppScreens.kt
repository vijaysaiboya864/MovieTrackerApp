package vijaysaiboya.movietrackerapp.madproject

sealed class AppScreens(val route: String) {
    object Splash : AppScreens("splash_route")
    object Login : AppScreens("login_route")
    object Register : AppScreens("register_route")

    object Home : AppScreens("home_screen")
    object Profile : AppScreens("profile_screen")
    object ForgotPassword : AppScreens("forgot_screen")

}