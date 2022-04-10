package instantpay.assignment.utils

import android.content.Context

val ASSIGNMENT_APP = "ASSIGNMENT"
val TOKEN = "TOKEN"

// Pref Key declaration start
class Pref {


    companion object {


        fun setString(context: Context, key: String, value: String) {
            var sharedPref = context.getSharedPreferences(ASSIGNMENT_APP, Context.MODE_PRIVATE);
            with(sharedPref.edit()) {
                putString(key, value);
                commit()
            }
        }


        fun getString(context: Context, key: String): String? {
            var sharedPref = context.getSharedPreferences(ASSIGNMENT_APP, Context.MODE_PRIVATE);
            return sharedPref.getString(key, null)
        }


    }
}