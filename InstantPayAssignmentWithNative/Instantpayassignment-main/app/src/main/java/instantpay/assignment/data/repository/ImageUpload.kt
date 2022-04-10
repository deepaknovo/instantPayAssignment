package instantpay.assignment.data.repository


import android.app.Application
import com.google.gson.Gson
import instantpay.assignment.data.api.BackEndApi
import instantpay.assignment.data.api.WebServiceClient
import instantpay.assignment.data.model.request.GetToken
import instantpay.assignment.data.model.response.ImageUploadDto
import instantpay.assignment.data.model.response.TokenResponseDto

import okhttp3.MultipartBody

class ImageUpload {
    private var api: BackEndApi? = null

    constructor(context: Application) {
        api = WebServiceClient.getApiService(context)
    }

    companion
    object {
        private var ref: ImageUpload? = null;
        fun getInstance(context: Application): ImageUpload {
            if (ref != null) {
                return ref as ImageUpload
            }
            ref = ImageUpload(context)
            return ref as ImageUpload;
        }
    }

    suspend fun getTokenApi(body: GetToken): TokenResponseDto? {

        val res = api?.getTokenData(body)
        if (res?.isSuccessful == true) {
            return res.body();
        } else {
            return Gson().fromJson(res?.errorBody()?.charStream(), TokenResponseDto::class.java)
        }
    }


    suspend fun uploadImage(body: MultipartBody.Part): ImageUploadDto? {

        val res = api?.uploadImage(body)
        if (res?.isSuccessful == true) {
            return res.body();
        } else {
            return Gson().fromJson(res?.errorBody()?.charStream(), ImageUploadDto::class.java)
        }
    }


}