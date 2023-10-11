package com.cthacademy.android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentUploadVideo.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentUploadVideo : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var iUri : Uri
    private lateinit var vUri : Uri

    private lateinit var number : String
    private lateinit var id : String
    private lateinit var videoUploader : String
    private lateinit var videoUri : String
    private lateinit var videoStorageUri : String
    private lateinit var image : String
    private lateinit var title : String
    private lateinit var description : String
    private lateinit var phone : String
    private var point : Int = 0
    private var isLocked : Boolean = false
    private var isVideoChange : Boolean = false
    private var isNew : Boolean = false
    private var hasImage : Boolean = false
    private var hasVideo : Boolean = false

    private lateinit var backBtn : RelativeLayout
    private lateinit var vUploader : TextView
    private lateinit var vTitle : TextView
    private lateinit var vTitleTxt : EditText
    private lateinit var vDescription : TextView
    private lateinit var vDescriptionTxt : EditText
    private lateinit var vImage : ImageView
    private lateinit var vImageBtn : CardView
    private lateinit var vImageBtnImg : ImageView
    private lateinit var vVideoBtn : CardView
    private lateinit var vVideoBtnImg : ImageView
    private lateinit var vLockImg : ImageView
    private lateinit var vLockBtn : CardView
    private lateinit var vLockBtnImg : ImageView
    private lateinit var vPoint : TextView
    private lateinit var vPointBtn : CardView
    private lateinit var vPointTxt : EditText

    private lateinit var vFinish : Button
    private lateinit var storageRef: StorageReference

    private val PICTURE_UPLOAD_REQUEST_CODE = 100
    private val VIDEO_UPLOAD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICTURE_UPLOAD_REQUEST_CODE) {
                val uri: Uri = data?.data!!
                iUri = uri
                hasImage = true


            } else if (requestCode == VIDEO_UPLOAD_REQUEST_CODE) {
                val uri: Uri = data?.data!!
                vUri = uri
                hasVideo = true
                isVideoChange = true

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_upload_video, container, false)
        storageRef = FirebaseStorage.getInstance().reference
        getUserData()
        getViewID(view)
        vPoint.hint = point.toString()
        vUploader.text = videoUploader
        vLockImg.visibility = View.GONE
        vPoint.visibility = View.GONE
        if (title.isNotEmpty() && description.isNotEmpty()&& videoStorageUri.isNotEmpty()&& id.isNotEmpty()&& image.isNotEmpty()&& title.isNotEmpty()){
            vTitle.text = title
            vDescription.text = description
            vPoint.text = point.toString()


        }else{
            isNew = true
        }
        if (isLocked){
            vLockImg.visibility = View.VISIBLE
            vPoint.visibility = View.VISIBLE
        }else{
            vLockImg.visibility = View.GONE
            vPoint.visibility = View.GONE
        }

        backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        setupListeners(requireContext(),isNew)
        return view
    }
    private fun getUserData(){
        arguments.let { bundle ->
            phone = bundle?.getString("phone").toString()
            number = bundle?.getString("number").toString()
            videoUploader = bundle?.getString("videoUploader").toString()
            videoStorageUri = bundle?.getString("videoUri").toString()
            id = bundle?.getString("id").toString()
            image = bundle?.getString("imageUri").toString()
            title = bundle?.getString("title").toString()
            description = bundle?.getString("description").toString()
            point = bundle?.getLong("point",0).toString().toInt()
            isLocked = bundle?.getBoolean("isLocked", false) == true
        }
    }

    private fun getViewID(view:View){
        backBtn = view.findViewById(R.id.Upload_Video_BackBtn)
        vUploader = view.findViewById(R.id.Upload_video_uploader_ex)
        vTitle = view.findViewById(R.id.Upload_video_title_ex)
        vTitleTxt = view.findViewById(R.id.Upload_video_title)
        vDescription = view.findViewById(R.id.Upload_video_description_ex)
        vDescriptionTxt = view.findViewById(R.id.Upload_video_description)
        vImage = view.findViewById(R.id.Upload_video_thumbnail_ex)
        vImageBtn = view.findViewById(R.id.Upload_video_uploadImageBtn)
        vImageBtnImg = view.findViewById(R.id.Upload_video_uploadImageBtn_img)
        vVideoBtn = view.findViewById(R.id.Upload_video_uploadVideoBtn)
        vVideoBtnImg = view.findViewById(R.id.Upload_video_uploadVideoBtn_img)
        vLockBtn = view.findViewById(R.id.Upload_video_LockBtn)
        vLockBtnImg = view.findViewById(R.id.Upload_video_LockBtn_img)
        vLockImg = view.findViewById(R.id.Upload_video_lock_ex)
        vPoint = view.findViewById(R.id.Upload_video_Point_ex)
        vPointBtn = view.findViewById(R.id.Upload_video_Point)
        vPointTxt = view.findViewById(R.id.Upload_video_Point_txt)
        vFinish = view.findViewById(R.id.Upload_video_Finish)

    }

    private fun setupListeners(context: Context,isNew: Boolean) {

        vLockBtn.setOnClickListener {
            isLocked = !isLocked
            if (isLocked){
                vLockImg.visibility = View.VISIBLE
                vPoint.visibility = View.VISIBLE
                updateImage(context,image,true,vImage)
            }else{
                vLockImg.visibility = View.GONE
                vPoint.visibility = View.GONE
                updateImage(context,image,false,vImage)
            }

        }

        vImageBtn.setOnClickListener {
            uploadImage()
        }
        vVideoBtn.setOnClickListener {
            launchVideoPicker()
        }

        vFinish.setOnClickListener{
            if (!isNew){
                if (!isVideoChange){
                    updateToDB(id)
                }else{
                    uploadVideoToFirebaseStorage(id,vUri)
                }
            }else{
                if (hasImage && hasVideo && vTitle.text.isNotEmpty() && vDescription.text.isNotEmpty()){
                    if (isLocked && vPoint.text.isEmpty()){
                        return@setOnClickListener
                    }
                    createNewVideoAndUpload(requireContext(),vUri)
                    parentFragmentManager.popBackStack()
                }else{
                    return@setOnClickListener
                }

            }
            parentFragmentManager.popBackStack()
        }

        vTitleTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is being changed
                val maxLength = 19
                if ((s?.length ?: 0) > maxLength) {
                    vTitle.text = "${s?.subSequence(0, maxLength - 2)}..."
                } else {
                    if (s?.isEmpty() == true) {
                        vTitle.text = title
                    }else{
                        vTitle.text = s
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed
            }
        })

        vDescriptionTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is being changed
                val maxLength = 56
                if ((s?.length ?: 0) > maxLength) {
                    vDescription.text = "${s?.subSequence(0, maxLength - 2)}..."
                } else {
                    if (s?.isEmpty() == true) {
                        vDescription.text = description
                    }else{
                        vDescription.text = s
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed
            }
        })

        vPointTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is being changed
                if (s?.isEmpty() == true) {
                    vPoint.text = point.toString()
                }else{
                    vPoint.text = s
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed

            }
        })

    }

    private fun updateImage(context: Context,str: String , lock: Boolean, img: ImageView) {
        return if (!lock){
            img.setImageBitmap(convertBase64ToImage(context,str))
        }else{
            img.setImageDrawable(null)
            img.setBackgroundColor(Color.parseColor("#D6D0D0"))
        }
    }

    private fun convertImageToBase64(context: Context, imageUri: Uri): String {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)

        // Compress the image
        val compressedBitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        compressedBitmap?.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)

        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun convertBase64ToImage(context: Context, base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)

        return BitmapFactory.decodeStream(inputStream)
    }

    private fun uploadImage(){
        ImagePicker.with(this)
            .cropSquare()
            .compress(1024)
            .maxResultSize(512, 512)
            .start(PICTURE_UPLOAD_REQUEST_CODE)
    }

    private fun launchVideoPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        startActivityForResult(intent, VIDEO_UPLOAD_REQUEST_CODE)
    }

    private fun updateToDB(videoUri: String){

        val db = FirebaseFirestore.getInstance()
        val videosRef = db.collection("MissingNumbersLesson").document("Videos")
        val lessonRef = videosRef.collection("Number_$number").document(videoUri)
        image = convertImageToBase64(requireContext(), iUri)
        if (isLocked) {

            val data = hashMapOf<String, Any>(
                "image" to image,
                "title" to vTitle.text.toString(),
                "description" to vDescription.text.toString(),
                "isLocked" to isLocked,
                "point" to point
            )
            lessonRef.update(data)
        } else {
            val data = hashMapOf<String, Any>(
                "image" to image,
                "title" to vTitle.text.toString(),
                "description" to vDescription.text.toString(),
                "isLocked" to isLocked,
                "point" to 0
            )
            lessonRef.update(data)
        }
    }

    private fun uploadVideoToFirebaseStorage(id: String,videoUri: Uri) {
        val videoRef = FirebaseStorage.getInstance().reference
            .child(videoStorageUri)

        val db = FirebaseFirestore.getInstance()
        val videosRef = db.collection("MissingNumbersLesson").document("Videos")
        val lessonRef = videosRef.collection("Number_$number")
        videoRef.delete()
        lessonRef.get().addOnSuccessListener {
            val uploadRef = lessonRef.document(id)
//            image = convertImageToBase64(requireContext(), iUri)
            uploadRef.get().addOnSuccessListener {
                if (isLocked) {
                    val data = hashMapOf<String, Any>(
                        "image" to image,
                        "title" to vTitle.text.toString(),
                        "uploaderPhone" to vTitle.text.toString(),
                        "description" to vDescription.text.toString(),
                        "isLocked" to isLocked,
                        "point" to point
                    )
                    uploadRef.update(data)
                    videoRef.putFile(videoUri).addOnProgressListener {taskSnapshot ->
                        val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

                    }
                } else {
                    val data = hashMapOf<String, Any>(
                        "image" to image,
                        "title" to vTitle.text.toString(),
                        "description" to vDescription.text.toString(),
                        "isLocked" to isLocked,
                        "point" to 0
                    )
                    uploadRef.update(data)
                    videoRef.putFile(videoUri).addOnProgressListener {taskSnapshot ->
                        val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

                    }
                }
            }
        }
    }

    private fun createNewVideoAndUpload(context: Context, videoUri: Uri) {
        val videoRef = FirebaseStorage.getInstance().reference
            .child("MissingNumbersLessonVideos")
            .child("Number_$number")

        val db = FirebaseFirestore.getInstance()
        val videosRef = db.collection("MissingNumbersLesson").document("Videos")
        val lessonRef = videosRef.collection("Number_$number")

        videoRef.listAll()
            .addOnSuccessListener { listResult ->
                val videoCount = listResult.items.count()

                lessonRef.get().addOnSuccessListener { querySnapshot ->

                    val documentCount = querySnapshot.size()
                    val uploadRef = lessonRef.document("Video_${documentCount + 1}")
                    image = convertImageToBase64(context, iUri)
                    uploadRef.get().addOnSuccessListener {
                        if (isLocked) {
                            val data = hashMapOf(
                                "uri" to "MissingNumbersLessonVideos/Number_$number/${videoCount + 1}.mp4",
                                "image" to image,
                                "title" to vTitle.text.toString(),
                                "uploaderPhone" to phone,
                                "description" to vDescription.text.toString(),
                                "isLocked" to isLocked,
                                "point" to point
                            )
                            uploadRef.set(data)
                            videoRef.child("${videoCount + 1}.mp4").putFile(videoUri).addOnProgressListener {taskSnapshot ->
                                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

                            }
                        } else {
                            val data = hashMapOf(
                                "uri" to "MissingNumbersLessonVideos/Number_$number/${videoCount + 1}.mp4",
                                "image" to image,
                                "title" to vTitle.text.toString(),
                                "uploaderPhone" to phone,
                                "description" to vDescription.text.toString(),
                                "isLocked" to isLocked,
                                "point" to 0
                            )
                            uploadRef.set(data)
                            videoRef.child("${videoCount + 1}.mp4").putFile(videoUri).addOnProgressListener {taskSnapshot ->
                                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

                            }
                        }
                    }
                }
            }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentUploadVideo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentUploadVideo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}