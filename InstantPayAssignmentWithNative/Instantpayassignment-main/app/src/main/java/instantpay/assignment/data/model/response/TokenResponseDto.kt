package instantpay.assignment.data.model.response


import com.google.gson.annotations.SerializedName


class TokenResponseDto {
    @SerializedName("resp")
    val resp: Int? = null

    @SerializedName("msg")
    val msg: String? = null

    @SerializedName("token")
    val token: String? = null

    @SerializedName("orignal_plaintext")
    val orignal_plaintext: String? = null

}