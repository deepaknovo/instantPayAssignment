package instantpay.assignment.data.api

import android.content.Context

import android.util.Log
import instantpay.assignment.utils.Pref
import instantpay.assignment.utils.TOKEN

import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(context: Context) : Interceptor {
    private val context = context
    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("Accept", "application/json; charset=utf-8")
        requestBuilder.addHeader("Content-type", "application/json; charset=utf-8")
        if (chain.request().header("No-Authentication") == null) {
            var yourtokenvalue = Pref.getString(context, TOKEN);
            requestBuilder.addHeader("Authorization", "Bearer " + yourtokenvalue)

        }

        val response = chain.proceed(requestBuilder.build())
        Log.d("code", response.code().toString())
        if (response.code() == 401) {
            Log.d("sender", "Logout From App")

            return response
        }
        return response

    }
}

