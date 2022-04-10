package instantpay.assignment.data.api

import android.content.Context

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WebServiceClient {
    private lateinit var interceptor: HttpLoggingInterceptor
    private lateinit var okHttpClient: OkHttpClient
    private var retrofit: Retrofit? = null
    private lateinit var apiService: BackEndApi
    public var base_url = "https://www.instantpay.in/ws/AndroidRecruitmentTest/"
    fun getApiService(context: Context): BackEndApi {
        if (!::apiService.isInitialized) {


            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(base_url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okhttpClient(context))
                        .build()
                apiService = retrofit?.create(BackEndApi::class.java)!!
            }
        }


        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()
    }


}
