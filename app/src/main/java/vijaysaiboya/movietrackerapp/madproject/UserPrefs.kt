package vijaysaiboya.movietrackerapp.madproject

import android.content.Context
import androidx.core.content.edit


object UserPrefs {

    // ==============================
    // PREF FILE NAME
    // ==============================
    private const val PREFS_NAME = "MOVIE_TRACKER_PREFS"

    // ==============================
    // KEYS
    // ==============================
    private const val KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN"
    private const val KEY_USER_NAME = "KEY_USER_NAME"
    private const val KEY_USER_EMAIL = "KEY_USER_EMAIL"
    private const val KEY_USER_DOB = "KEY_USER_DOB"
    private const val KEY_USER_COUNTRY = "KEY_USER_COUNTRY"

    // ==============================
    // LOGIN STATUS
    // ==============================
    fun setLoginStatus(context: Context, isLoggedIn: Boolean) {
        getPrefs(context)
            .edit {
                putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            }
    }

    fun isUserLoggedIn(context: Context): Boolean {
        return getPrefs(context)
            .getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // ==============================
    // USER NAME
    // ==============================
    fun saveUserName(context: Context, name: String) {
        getPrefs(context)
            .edit {
                putString(KEY_USER_NAME, name)
            }
    }

    fun getUserName(context: Context): String {
        return getPrefs(context)
            .getString(KEY_USER_NAME, "") ?: ""
    }

    // ==============================
    // USER EMAIL
    // ==============================
    fun saveUserEmail(context: Context, email: String) {
        getPrefs(context)
            .edit {
                putString(KEY_USER_EMAIL, email)
            }
    }

    fun getUserEmail(context: Context): String {
        return getPrefs(context)
            .getString(KEY_USER_EMAIL, "") ?: ""
    }

    // ==============================
    // USER DATE OF BIRTH
    // ==============================
    fun saveUserDob(context: Context, dob: String) {
        getPrefs(context)
            .edit {
                putString(KEY_USER_DOB, dob)
            }
    }

    fun getUserDob(context: Context): String {
        return getPrefs(context)
            .getString(KEY_USER_DOB, "") ?: ""
    }

    // ==============================
    // USER COUNTRY
    // ==============================
    fun saveUserCountry(context: Context, country: String) {
        getPrefs(context)
            .edit {
                putString(KEY_USER_COUNTRY, country)
            }
    }

    fun getUserCountry(context: Context): String {
        return getPrefs(context)
            .getString(KEY_USER_COUNTRY, "") ?: ""
    }

    // ==============================
    // CLEAR USER DATA (LOGOUT)
    // ==============================
    fun clearUserSession(context: Context) {
        getPrefs(context)
            .edit {
                clear()
            }
    }

    // ==============================
    // PRIVATE HELPER
    // ==============================
    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
