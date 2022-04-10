package instantpay.assignment.data.model.request

import com.google.gson.annotations.SerializedName


data class GetToken(
        @SerializedName("ciphertext") val ciphertext: String?) {
}