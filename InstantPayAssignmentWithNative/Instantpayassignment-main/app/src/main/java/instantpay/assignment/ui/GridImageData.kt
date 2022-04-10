package instantpay.assignment.ui

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import instantpay.assignment.R
import instantpay.assignment.adapter.ImageAdapter
import instantpay.assignment.data.model.request.GetToken
import instantpay.assignment.databinding.FragmentGridImageDataBinding
import instantpay.assignment.ui.viewmodel.GridImageViewModel
import instantpay.assignment.utils.Pref
import instantpay.assignment.utils.TOKEN
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [GridImageData.newInstance] factory method to
 * create an instance of this fragment.
 */
class GridImageData : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var viewModel: GridImageViewModel;
    lateinit var binding: FragmentGridImageDataBinding;

    private val REQUEST_PICK_IMAGE = 2
    lateinit var currentPhotoPath: String
    var mDialog: ProgressDialog? = null
    var imageAdapter: ImageAdapter? = null
    var imageList = ArrayList<String>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GridImageViewModel::class.java);

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grid_image_data, container, false)
        binding.viewModel = viewModel;
        binding.lifecycleOwner = this;
        binding.uploadButton.setOnClickListener {
            openGallery();
        }
        viewModel.getTokenData(GetToken(viewModel.encryptData("")))
        viewModel.tokenResponse?.observe(viewLifecycleOwner, Observer { it ->

            if (it !== null) {
                if (it.msg == "Success") {
                    Pref.setString(requireContext(), TOKEN, it.token!!);

                } else {
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_LONG).show();
                }
            }

        })
        viewModel.imageUploadResponse?.observe(viewLifecycleOwner, Observer { it ->
            mDialog?.hide();
            if (it !== null) {
                if (it.msg == "Success") {
                    imageList.add(it.link!!)
                    imageAdapter?.notifyDataSetChanged();
                    Toast.makeText(requireContext(), "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_LONG).show();
                }
            }

        })


        imageAdapter = ImageAdapter(requireContext(), imageList)
        binding.gridview.setAdapter(imageAdapter);

        return binding.root;
    }


    @Throws(IOException::class)
    private fun createCapturedPhoto(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PHOTO_${timestamp}", ".png", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }


    fun openGallery() {

        Intent(Intent.ACTION_GET_CONTENT).also { intent ->

            intent.type = "image/*"
            intent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(intent, REQUEST_PICK_IMAGE)
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE) { //gallery

                val photoFile: File? = try {
                    createCapturedPhoto()
                } catch (ex: IOException) {
                    // If there is error while creating the File, it will be null
                    null
                }

                var out = FileOutputStream(photoFile)

                val uri = data?.getData()
                val input: InputStream? = requireContext().contentResolver.openInputStream(uri!!)
                var bitmap = BitmapFactory.decodeStream(input);

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);


                uploadToServer(Uri.parse(currentPhotoPath))
            }
        }
    }

    fun uploadToServer(uri: Uri?) {

        if (uri != null) {
            mDialog = ProgressDialog(requireContext())
            mDialog?.setMessage("Please wait...")
            mDialog?.setCancelable(false)
            mDialog?.show()
            val file = File(uri.path!!);
            val requestFile = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    file
            )


            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            viewModel.upload(body)
        }
    }
}