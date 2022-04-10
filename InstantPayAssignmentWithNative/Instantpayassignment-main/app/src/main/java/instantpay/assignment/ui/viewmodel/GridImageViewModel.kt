package instantpay.assignment.ui.viewmodel

import android.app.Application
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import instantpay.assignment.data.model.request.GetToken
import instantpay.assignment.data.model.response.ImageUploadDto
import instantpay.assignment.data.model.response.TokenResponseDto
import instantpay.assignment.data.repository.ImageUpload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class GridImageViewModel(context: Application) : AndroidViewModel(context) {

    val repo = ImageUpload.getInstance(context);

    var imageUploadResponse = MutableLiveData<ImageUploadDto>()
    var tokenResponse = MutableLiveData<TokenResponseDto>()
    fun generateRandom(): Byte {
        return (0..10).random().toByte()
    }

    fun encryptData(textToEncrypt: String): String {
        val sharedvector =
                byteArrayOf(generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom(), generateRandom())

        var key = "ab821eb4b7d352cd65e84c5a7f38dbb0966262c651cf7064a0d821d8b2a20a5a";
        val message: String = "This is some sample plaintext data to encrypt";
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(
                Cipher.ENCRYPT_MODE,
                SecretKeySpec(key.toByteArray(), "AES"),
                IvParameterSpec(sharedvector)
        )
        val encrypted = c.doFinal(message.toByteArray(charset("UTF-8")))
        val encryptedText: String = Base64.encodeToString(sharedvector + encrypted, Base64.DEFAULT)
        return encryptedText;
    }

    fun getTokenData(body: GetToken) {
        viewModelScope.launch {
            with(Dispatchers.IO) {

                try {
                    tokenResponse.value = repo.getTokenApi(body);
                    Log.d("responseToken", GsonBuilder().setPrettyPrinting().create().toJson(body))
                } catch (e: Exception) {
                    Toast.makeText(getApplication(), "Catch: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    fun upload(body: MultipartBody.Part) {

        viewModelScope.launch {
            with(Dispatchers.IO) {

                try {
                    imageUploadResponse.value = repo.uploadImage(body);
                    Log.d("responseToken", GsonBuilder().setPrettyPrinting().create().toJson(body))

                } catch (e: Exception) {
                    Toast.makeText(getApplication(), "Catch: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }


        }

    }


}

