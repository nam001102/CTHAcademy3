package com.cthacademy.android.navicationBar

import android.animation.ValueAnimator
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import cheekiat.slideview.OnChangeListener
import cheekiat.slideview.SlideView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.cthacademy.android.BitmapBlur.Companion.blurBitmap
import com.cthacademy.android.MainActivity
import com.cthacademy.android.R
import com.cthacademy.android.LoginActivity
import com.cthacademy.android.ThreadUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var bottomBar: AnimatedBottomBar? = null

    private lateinit var avatar: String
    private lateinit var picture: String
    private lateinit var phone: String
    private lateinit var name: String
    private lateinit var date: String
    private lateinit var passionArray : List<Int>
    private lateinit var missingNumbersArray : List<Int>
    private lateinit var personalArray : List<Int>
    private lateinit var statsNumberArray : List<Int>
    private lateinit var statsTitleArray : List<String>
    private lateinit var statsPointArray : List<Int>
    private lateinit var statsLockArray : List<Boolean>
    private lateinit var statsIdArray : List<String>
    private var isAdmin : Boolean = false
    private var isPictureUpdated : Boolean = false
    private var point : Int = 0

    private lateinit var myCardView : CardView
    private lateinit var profileContainerDate : CardView
    private lateinit var profileContainerPhone : CardView

    private lateinit var parent : LinearLayout
    private lateinit var setting : RelativeLayout

    private lateinit var imageBackGround : ImageView
    private lateinit var imageBlur : ImageView
    private lateinit var imageProfile : ImageView
    private lateinit var profileDateText : TextView
    private lateinit var profilePhoneText : TextView

    private lateinit var profileAvatar : ImageView
    private lateinit var profileName : TextView
    private lateinit var profileID : TextView
    private lateinit var profileInfo : TextView

    private lateinit var slide : SlideView
    private lateinit var slideText : TextView

    private lateinit var avatarChange : RelativeLayout
    private lateinit var avatarUpload : ImageView
    private lateinit var avatarCancel : ImageView
    private lateinit var pictureChange : RelativeLayout
    private lateinit var pictureUpload : ImageView
    private lateinit var pictureCancel : ImageView
    private lateinit var backButton : ImageView

    private val db = FirebaseFirestore.getInstance()
    private val AVATAR_UPLOAD_REQUEST_CODE = 100
    private val PICTURE_UPLOAD_REQUEST_CODE = 101



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AVATAR_UPLOAD_REQUEST_CODE) {
                val uri: Uri = data?.data!!

                // Update the avatar in Firestore or perform any necessary operations
                val userDocRef = db.collection("users").document(phone)
                userDocRef.update("avatar", convertImageToBase64(requireContext(), uri))

                // Set the selected avatar image to the ImageView
                profileAvatar.setImageURI(uri)
                reloadMainActivity()
            } else if (requestCode == PICTURE_UPLOAD_REQUEST_CODE) {
                val uri: Uri = data?.data!!

                // Update the picture in Firestore or perform any necessary operations
                val userDocRef = db.collection("users").document(phone)
                userDocRef.update("picture", convertImageToBase64(requireContext(), uri))

                // Set the selected picture image to the ImageView
                imageProfile.setImageURI(uri)
                blurImage(requireContext())
                reloadMainActivity()

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        getData()
        findView(view)
        updateView(requireContext())
        setListener()
        blurImage(requireContext())

        return view
    }
    fun setBottomBar(bottomBar: AnimatedBottomBar) {
        this.bottomBar = bottomBar
    }

    private fun findView(view: View){
        parent = view.findViewById(R.id.parent)
        setting = view.findViewById(R.id.Setting)
        myCardView = view.findViewById(R.id.Profile_CardView)
        profileContainerDate = view.findViewById(R.id.profile_container_date)
        profileContainerPhone = view.findViewById(R.id.profile_container_number)
        profileDateText = view.findViewById(R.id.profile_date_text)
        profilePhoneText = view.findViewById(R.id.profile_number_text)
        imageBackGround = view.findViewById(R.id.Profile_CardView_BackGround)
        imageBlur = view.findViewById(R.id.Profile_CardView_Blur)
        imageProfile = view.findViewById(R.id.profile_image)
        profileAvatar = view.findViewById(R.id.Profile_avatar)
        profileName = view.findViewById(R.id.Profile_Name)
        profileID = view.findViewById(R.id.Profile_ID)
        profileInfo = view.findViewById(R.id.Profile_Info)
        slide = view.findViewById(R.id.Slide)
        slideText = view.findViewById(R.id.Slide_text)
        avatarChange = view.findViewById(R.id.Avatar_change)
        avatarUpload = view.findViewById(R.id.Avatar_upload)
        avatarCancel = view.findViewById(R.id.Avatar_cancel)
        pictureChange = view.findViewById(R.id.Picture_change)
        pictureUpload = view.findViewById(R.id.Picture_upload)
        pictureCancel = view.findViewById(R.id.Picture_cancel)
        backButton = view.findViewById(R.id.backButton)
    }
    private fun setListener(){
        val slideOutToRightAnimation = R.anim.slide_out_to_right
        val slideOutToLeftAnimation = R.anim.slide_out_to_left
        val slideInFromRightAnimation = R.anim.slide_in_from_right
        val slideInFromLeftAnimation = R.anim.slide_in_from_left
        val animationFadeIn = R.anim.fade_in
        profileContainerDate.setOnClickListener {
            changeCardViewWidthWithAnimation(profileContainerDate,280,500)
            playAnimationOnTextView(profileDateText, animationFadeIn)
        }
        profileContainerPhone.setOnClickListener {
            changeCardViewWidthWithAnimation(profileContainerPhone,310,500)
            playAnimationOnTextView(profilePhoneText, animationFadeIn)
        }

        setting.setOnClickListener {
            avatarChange.visibility = View.VISIBLE
            pictureChange.visibility = View.VISIBLE
        }
        avatarCancel.setOnClickListener {
            avatarChange.visibility = View.GONE
        }
        pictureChange.setOnClickListener {
            pictureChange.visibility = View.GONE
        }
        avatarUpload.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .maxResultSize(512, 512)
                .start(AVATAR_UPLOAD_REQUEST_CODE)
        }
        pictureUpload.setOnClickListener {
            ImagePicker.with(this)
                .crop(9f,16f)
                .compress(1024)
                .maxResultSize(800, 1080)
                .start(PICTURE_UPLOAD_REQUEST_CODE)
        }

        slide.setOnChangeListener(object : OnChangeListener {
            override fun onProgressChanged(progress: Int) {
                val opacity = 1 - (progress / 100f)
                slideText.alpha = opacity
            }

            override fun onStopChanged() {

            }

            override fun onComplete() {
                logout()
            }
        })
        backButton.setOnClickListener {
            val fragmentMain = FragmentMain()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.setCustomAnimations(slideInFromLeftAnimation, slideOutToRightAnimation)
            fragmentTransaction?.replace(R.id.Main_fragment, fragmentMain)
            fragmentMain.arguments = putDataToFragment()
            bottomBar?.selectTabById(R.id.Home)
            fragmentTransaction?.commitNow()

        }
    }

    private fun putDataToFragment(): Bundle {
        val bundle = Bundle()
        // Add data to the bundle
        bundle.putString("avatar", avatar)
        bundle.putString("phone", phone)
        bundle.putString("name", name)
        bundle.putString("date", date)
        bundle.putInt("point", point)
        bundle.putBoolean("isAdmin", isAdmin)
        bundle.putIntArray("passionArray",passionArray.toIntArray())
        bundle.putIntArray("missingNumbersArray",missingNumbersArray.toIntArray())
        bundle.putIntArray("personalArray",personalArray.toIntArray())
        bundle.putIntArray("statsNumberArray",statsNumberArray.toIntArray())
        bundle.putStringArray("statsTitleArray",statsTitleArray.toTypedArray())
        bundle.putIntArray("statsPointArray",statsPointArray.toIntArray())
        bundle.putBooleanArray("statsLockArray",statsLockArray.toBooleanArray())
        bundle.putStringArray("statsIdArray",statsIdArray.toTypedArray())
        return bundle
    }
    private fun updateView(context: Context){

        profileDateText.text = date
        profilePhoneText.text = phone.replace("+84", "0")

        profileName.text = name
        profileID.text = "@user001"
        profileInfo.text = "user001"

        if (avatar.isNotEmpty()){
            profileAvatar.setImageBitmap(convertBase64ToImage(requireContext(),avatar))
        }
        if (picture.isNotEmpty()){
            imageProfile.setImageBitmap(convertBase64ToImage(requireContext(),picture))
        }
    }

    private fun blurImage(context: Context){
        imageProfile.post {
            ThreadUtil.startThread {
                val imageUri = convertBase64ToImage(requireContext(),picture)
                val futureTarget: FutureTarget<Bitmap> = Glide.with(context)
                    .asBitmap()
                    .load(imageUri)
                    .submit(imageProfile.width, imageProfile.height)
                val bitmap = futureTarget.get()

                ThreadUtil.startUIThread(0) {
                    imageProfile.setImageBitmap(bitmap)

                    myCardView.post{
                        var cropBitmap = Bitmap.createBitmap(imageProfile.width,imageProfile.height,Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(cropBitmap)
                        imageProfile.draw(canvas)

                        cropBitmap = Bitmap.createBitmap(cropBitmap, myCardView.x.toInt(), myCardView.y.toInt(), myCardView.width, myCardView.height)

                        val blurFirst = blurBitmap(requireContext(),cropBitmap)
                        animateBlurImage(blurFirst, context)
                    }
                }
            }
        }
    }

    private fun animateBlurImage(bitmap: Bitmap, context: Context) {
        // Set the initial image
        imageBlur.setImageBitmap(bitmap)
        imageBlur.alpha = 0f

        // Create the blur animation
        val fadeInAnimator = ValueAnimator.ofFloat(0f, 1f)
        fadeInAnimator.duration = 500
        fadeInAnimator.addUpdateListener { valueAnimator ->
            val alpha = valueAnimator.animatedValue as Float
            imageBlur.alpha = alpha
        }

        // Start the blur animation
        fadeInAnimator.start()
    }

    private fun changeCardViewWidthWithAnimation(cardView: CardView, newWidth: Int, duration: Long) {
        val currentWidth = cardView.width

        // Create a ValueAnimator
        val animator = ValueAnimator.ofInt(currentWidth, newWidth)
        animator.duration = duration

        // Set an update listener to animate the width
        animator.addUpdateListener { valueAnimator ->
            val layoutParams = cardView.layoutParams
            layoutParams.width = valueAnimator.animatedValue as Int
            cardView.layoutParams = layoutParams
        }

        // Start the animation
        animator.start()
    }

    private fun playAnimationOnTextView(textView: TextView, animationId: Int) {
        val animation: Animation = AnimationUtils.loadAnimation(textView.context, animationId)
        textView.startAnimation(animation)
        textView.visibility = View.VISIBLE
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

    private fun delayWithHandler(milliseconds: Long, callback: () -> Unit) {
        Handler().postDelayed(callback, milliseconds)
    }

    private fun getData(){
        arguments.let { bundle ->
            // Retrieve data from the bundle
            avatar = bundle?.getString("avatar").toString()
            picture = bundle?.getString("picture").toString()
            phone = bundle?.getString("phone").toString()
            name = bundle?.getString("name").toString()
            date = bundle?.getString("date").toString()
            point = bundle?.getInt("point").toString().toInt()
            isAdmin = bundle?.getBoolean("isAdmin", false) ?: false
            passionArray = bundle?.getIntArray("passionArray")?.toList() ?: emptyList()
            missingNumbersArray = bundle?.getIntArray("missingNumbersArray")?.toList() ?: emptyList()
            personalArray = bundle?.getIntArray("personalArray")?.toList() ?: emptyList()
            statsNumberArray = bundle?.getIntArray("statsNumberArray")?.toList() ?: emptyList()
            statsTitleArray = bundle?.getStringArray("statsTitleArray")?.toList() ?: emptyList()
            statsPointArray = bundle?.getIntArray("statsPointArray")?.toList() ?: emptyList()
            statsLockArray = bundle?.getBooleanArray("statsLockArray")?.toList() ?: emptyList()
            statsIdArray = bundle?.getStringArray("statsIdArray")?.toList() ?: emptyList()

            Log.d("Debug",passionArray.toString())
            Log.d("Debug",missingNumbersArray.toString())
        }
    }

    private fun convertBase64ToImage(context: Context, base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        return bitmap
    }

    private fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user", null)
        editor.putBoolean("isLoggedIn", false)
        editor.apply()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun reloadMainActivity() {
        val intent = Intent(activity, MainActivity::class.java).apply {
            putExtra("phone",phone)
            putExtra("name",name)
            putExtra("date",date)
            putExtra("isOnTab",2)
        }
        startActivity(intent)
        activity?.finish()
    }



    companion object {
        @JvmStatic
        fun newInstance(bottomBar: AnimatedBottomBar): FragmentProfile {
            val fragment = FragmentProfile()
            fragment.setBottomBar(bottomBar)
            return fragment
        }
    }
}