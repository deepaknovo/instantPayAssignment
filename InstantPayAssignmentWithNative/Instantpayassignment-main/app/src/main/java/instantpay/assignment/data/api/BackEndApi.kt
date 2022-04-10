package instantpay.assignment.data.api


import instantpay.assignment.data.model.request.GetToken
import instantpay.assignment.data.model.response.ImageUploadDto
import instantpay.assignment.data.model.response.TokenResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface BackEndApi {
    @Headers("No-Authentication: true")
    @POST("getToken")
    suspend fun getTokenData(@Body req: GetToken): Response<TokenResponseDto>

    @Multipart
    @POST("uploadImage")
    suspend fun uploadImage(@Part file: MultipartBody.Part): Response<ImageUploadDto>


}

