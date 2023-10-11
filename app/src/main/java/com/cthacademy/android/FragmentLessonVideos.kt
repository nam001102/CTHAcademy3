package com.cthacademy.android

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.adapter.GridNewLessonVideoAdapter
import com.cthacademy.android.adapter.LessonVideosAdapter
import com.cthacademy.android.custom.VerticalSpacingItemDecoration
import com.cthacademy.android.modal.LessonVideoModal
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLessonVideos.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLessonVideos : Fragment(), GridNewLessonVideoAdapter.OnClickListener  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val REQUEST_VIDEO_PLAYER = 1

    private lateinit var backBtn : RelativeLayout

    private lateinit var adminUpload : CardView
    private lateinit var adminToolBar : RelativeLayout
    private lateinit var adminVideoToolBar : LinearLayout
    private lateinit var adminNumber : EditText
    private lateinit var adminEditVideo : CardView
    private lateinit var adminDeleteVideo : CardView
    private lateinit var adminVideoContainer : CardView
    private lateinit var searchVideoContainer : CardView
    private lateinit var searchVideoBtn : SearchView

    private lateinit var mvideoTitle: TextView
    private lateinit var mvideoUploaderAvatar: ImageView
    private lateinit var mvideoUploaderName: TextView
    private lateinit var mvideoDescription: TextView
    private lateinit var mTitle: TextView
    private lateinit var mPlay: ImageView
    private lateinit var mClose: ImageView

    private lateinit var leftBar : LinearLayout
    private lateinit var leftBarRecyleViewPagesNavication : RecyclerView
    private lateinit var leftBarAdapterPagesNavication : GridNewLessonVideoAdapter
    private lateinit var videoRecyleView : RecyclerView
    private lateinit var videoAdapter : LessonVideosAdapter

    private lateinit var playerContainer : FrameLayout
    private lateinit var motionLayout : MotionLayout
    private lateinit var playerView: PlayerView
    private lateinit var videoPlayer: SimpleExoPlayer
    private var isFullscreen: Boolean = false

    private var videoTitle: String? = null
    private var videoUrl: String? = null
    private var videoThumbnailUrl: String? = null
    private var videoUploader: String? = null
    private var pageNum: Int = 1
    private val documentsPerPage = 12
    private var currentPage: Int = 1
    private var pageCount: Int = 1
    private var initialNumber: Int = 0

    private var documentList = mutableListOf<LessonVideoModal>()

    private lateinit var phone: String
    private var number: Int = 0
    private lateinit var avatar: String
    private lateinit var name: String
    private lateinit var date: String
    private var isAdmin: Boolean = false
    private var onClickAdmin: Boolean = false
    private var point: Int = 0

    private lateinit var passionArray : List<Int>
    private lateinit var missingNumbersArray : List<Int>
    private lateinit var personalArray : List<Int>
    private lateinit var statsNumberArray : List<Int>
    private lateinit var statsTitleArray : List<String>
    private lateinit var statsPointArray : List<Int>
    private lateinit var statsLockArray : List<Boolean>
    private lateinit var statsIdArray : List<String>

    private lateinit var fullScreenBnt: ImageView

    private lateinit var fragmentContext: Context
    private var isControllerVisible = true

    private val handler = Handler()
    private val hideControllerRunnable = Runnable {
        if (isControllerVisible) {
            hideController()
        }
    }

    private val CHILD_FRAGMENT_REQUEST_CODE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_PLAYER && resultCode == Activity.RESULT_OK) {
            val seekbarPosition = data?.getLongExtra("seekbarPosition", 0)
            if (seekbarPosition != null) {
                videoPlayer.seekTo(seekbarPosition)
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lesson_videos, container, false)
        getUserData()
        getViewID(view)
        updateAdapter(phone, number)
        videoAdapter = LessonVideosAdapter(documentList,requireContext(),onClickAdmin)
        onClickHandle(phone)
        leftBarRecyleViewPagesNavication.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        initialNumber = number
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.vertical_spacing)
        leftBarRecyleViewPagesNavication.addItemDecoration(VerticalSpacingItemDecoration(verticalSpacing))
        videoRecyleView.addItemDecoration(VerticalSpacingItemDecoration(verticalSpacing))


        // Initialize ExoPlayer
        videoPlayer = SimpleExoPlayer
            .Builder(requireContext())
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        playerView.player = videoPlayer

        fullScreenBnt.setOnClickListener {
            val currentPosition = videoPlayer.currentPosition

            videoUrl?.let { openFullscreenActivity(currentPosition, it) }
        }


        return view
    }
    private fun onClickHandle(phone: String){
        val slideOutToRightAnimation = R.anim.slide_out_to_right
        val slideOutToLeftAnimation = R.anim.slide_out_to_left
        val slideInFromRightAnimation = R.anim.slide_in_from_right
        val slideInFromLeftAnimation = R.anim.slide_in_from_left
        adminUpload.setOnClickListener {
            val fragmentUploadVideo = FragmentUploadVideo()
            parentFragmentManager.beginTransaction()
                .replace(R.id.Main_fragment, fragmentUploadVideo)
                .addToBackStack(null)
                .commit()
            fragmentUploadVideo.arguments = putDataToFragment(
                "",
                name,
                "",
                "",
                "", "", 0,false)
        }
        adminVideoContainer.setOnClickListener {
            adminNumber.text.clear()
        }
        adminNumber.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN)) {
                var query = adminNumber.text.toString()
                if (query.isEmpty()) {
                    query = initialNumber.toString() // Restore the initial number if the query is empty
                    adminNumber.setText(query)
                }
                number = query.toInt() // Update the 'number' variable
                documentList.clear()
                videoAdapter.clearData()
                videoAdapter.notifyDataSetChanged()
                updateAdapter(phone, query.toInt())

                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(adminNumber.windowToken, 0)

                adminNumber.hint = query

                adminNumber.clearFocus()

                return@setOnEditorActionListener true
            }
            false
        }
        searchVideoBtn.setOnCloseListener {
            animateSearchVideoContainerWidth(80)
            false
        }
        searchVideoContainer.setOnClickListener {
            animateSearchVideoContainerWidth(700)
        }
        searchVideoBtn.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                animateSearchVideoContainerWidth(700)
            }
        }
        searchVideoBtn.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    videoAdapter.filterByTitle(query)
                    val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(searchVideoBtn.windowToken, 0)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    videoAdapter.filterByTitle(newText)
                }
                return true
            }
        })

        searchVideoBtn.setOnClickListener {
            val query = searchVideoBtn.query.toString()
            videoAdapter.filterByTitle(query)
        }

        backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

    }
    private fun getViewID(view:View){
        backBtn = view.findViewById(R.id.BackBtn)

        adminUpload = view.findViewById(R.id.Video_upload)

        adminToolBar = view.findViewById(R.id.Video_Admin_Tool)
        adminVideoToolBar = view.findViewById(R.id.Admin_Video_Tool)
        adminNumber = view.findViewById(R.id.Video_Admin_Number)
        adminEditVideo = view.findViewById(R.id.Video_edit)
        adminDeleteVideo = view.findViewById(R.id.Video_delete)
        adminVideoContainer = view.findViewById(R.id.Video_Admin_Number_Container)

        searchVideoContainer = view.findViewById(R.id.Video_Search_Container)
        searchVideoBtn = view.findViewById(R.id.Video_Search_Btn)
        leftBar = view.findViewById(R.id.lesson_video_drawer)
        videoRecyleView = view.findViewById(R.id.lesson_video_drawerRecyclerView_video)
        leftBarRecyleViewPagesNavication = view.findViewById(R.id.lesson_video_drawerRecyclerView)
        playerContainer = view.findViewById(R.id.main_imageView)
        motionLayout = view.findViewById(R.id.motionLayout)
        playerView = view.findViewById(R.id.main_player)
        fullScreenBnt = view.findViewById(R.id.exo_fullscreen)

        mvideoTitle = view.findViewById(R.id.video_title)
        mvideoDescription = view.findViewById(R.id.video_description)
        mvideoUploaderAvatar = view.findViewById(R.id.video_uploader_avatar)
        mvideoUploaderName = view.findViewById(R.id.video_uploader_name)

        mTitle = view.findViewById(R.id.title_textView)
        mClose = view.findViewById(R.id.close_imageView)
        mPlay = view.findViewById(R.id.play_imageView)

        adminVideoToolBar.visibility = View.GONE
        if (!onClickAdmin){
            adminToolBar.visibility = View.GONE
        }
        motionLayout.visibility = View.GONE
    }

    private fun updateAdapter(phone: String,number: Int) {

        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(phone)
        val vidRef = userRef.collection("LessonVideos").document(number.toString())
        val collectionRef = db.collection("MissingNumbersLesson").document("Videos")
        val docRef = collectionRef.collection("Number_$number")

        currentPage = 1
        onConfigurationChanged(Configuration())
        docRef.get().addOnSuccessListener { snapshot ->
            pageNum = snapshot.size()
            pageCount = pageNum / documentsPerPage
            if (pageNum % documentsPerPage != 0) {
                pageCount++
            }

            val pages = generatePagesList(pageCount)
            leftBarAdapterPagesNavication = GridNewLessonVideoAdapter(pages)
            leftBarRecyleViewPagesNavication.adapter = leftBarAdapterPagesNavication

            leftBarAdapterPagesNavication.setOnClickListener(object :
                GridNewLessonVideoAdapter.OnClickListener {
                override fun onButtonClick(position: Int) {
                    val clickedItem = pages[position]
                    if (pageCount != 1) {
                        currentPage = clickedItem
                        val data = getData(currentPage - 1)
                        Log.d("data", data.toString())
                        Log.d("clickedItem", clickedItem.toString())
                        videoAdapter.updateData(data)
                        videoAdapter.notifyDataSetChanged()
                    }
                }
            })

            for (document in snapshot.documents) {
                val documentId = document.id
                val uriRef = document.getString("uri")
                val image = document.getString("image")
                val title = document.getString("title")
                val uploaderPhone = document.getString("uploaderPhone")
                val description = document.getString("description")
                val isLocked = document.getBoolean("isLocked")
                val point = document.getLong("point")
                val data = hashMapOf(
                    documentId to isLocked
                )
                vidRef.get().addOnSuccessListener { i ->
                    if (!i.contains(documentId)) {
                        // Field does not exist in the document, update it with the data
                        vidRef.set(data, SetOptions.merge())
                    }
                }

                if (title != null && description != null && isLocked != null && point != null && uriRef != null && image != null && uploaderPhone != null) {
                    checkIfFragmentAttached {
                        vidRef.get().addOnSuccessListener { rdoc ->
                            val fields = rdoc.data
                            if (fields != null) {
                                for (field in fields) {
                                    val key = field.key
                                    val value = field.value as? Boolean
                                    if (key == documentId && value != null) {
                                        if (!onClickAdmin) {
                                            val documentItem = LessonVideoModal(
                                                key,
                                                uploaderPhone,
                                                uriRef,
                                                image,
                                                title,
                                                description,
                                                value,
                                                point.toInt().toLong()
                                            )
                                            val existingItem = documentList.find { it.id == key }
                                            if (existingItem == null) {
                                                documentList.add(documentItem)
                                            }
                                        } else {
                                            val documentItem = LessonVideoModal(
                                                key,
                                                uploaderPhone,
                                                uriRef,
                                                image,
                                                title,
                                                description,
                                                isLocked,
                                                point.toInt().toLong()
                                            )
                                            val existingItem = documentList.find { it.id == key }
                                            if (existingItem == null) {
                                                documentList.add(documentItem)
                                            }
                                        }
                                        Log.d("Debug", "$key|$uploaderPhone|$uriRef|$image|$title|$description|$isLocked|$point|")
                                        // Check if the item already exists in the list before adding
                                        videoAdapter = LessonVideosAdapter(
                                            documentList,
                                            requireContext(),
                                            onClickAdmin
                                        )
                                        videoRecyleView.adapter = videoAdapter
                                        videoRecyleView.layoutManager = LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )

                                    }
                                    Log.d("Debug", documentList.toString())



                                    if (!onClickAdmin) {
                                        videoAdapter.setOnItemClickListener(object :
                                            LessonVideosAdapter.OnItemClickListener {
                                            override fun onItemClick(position: Int) {
                                                // Handle the item click event here
                                                val clickedItem = documentList[position]
                                                // Do something with the clicked item
                                                searchVideoBtn.clearFocus()

                                                val muploaderPhone = clickedItem.userPhone
                                                db.collection("users").document(muploaderPhone)
                                                    .get()
                                                    .addOnSuccessListener { doc ->
                                                        if (doc.exists()) {
                                                            val muploaderAvatar =
                                                                doc.getString("avatar")
                                                            videoUploader = doc.getString("name")
                                                            mvideoUploaderAvatar.setImageBitmap(
                                                                muploaderAvatar?.let {
                                                                    convertBase64ToImage(
                                                                        requireContext(),
                                                                        it
                                                                    )
                                                                })

                                                            videoTitle = clickedItem.title
                                                            videoUrl = clickedItem.uri
                                                            videoThumbnailUrl = clickedItem.image

                                                            mvideoTitle.text = clickedItem.title
                                                            mvideoDescription.text =
                                                                clickedItem.description
                                                            mvideoUploaderName.text = videoUploader

                                                            mTitle.text = clickedItem.title

                                                            searchVideoBtn.isIconified = true
                                                            searchVideoBtn.clearFocus()

                                                            initializePlayer()
                                                            motionLayout.visibility = View.VISIBLE
                                                            motionLayout.transitionToStart()
                                                        }

                                                    }



                                            }

                                            override fun onUnlockVideoClick(position: Int) {
                                                val clickedItem = documentList[position]
                                                val mpoint = clickedItem.point
                                                val id = clickedItem.id

                                                showUnlockDialog(id, mpoint, phone, number)
                                            }
                                        })
                                    } else {
                                        videoAdapter.setOnItemClickListener(object :
                                            LessonVideosAdapter.OnItemClickListener {
                                            override fun onItemClick(position: Int) {
                                                // Handle the item click event here
                                                val clickedItem = documentList[position]
                                                // Do something with the clicked item
                                                adminVideoToolBar.visibility = View.VISIBLE

                                                val muploaderPhone = clickedItem.userPhone
                                                db.collection("users").document(muploaderPhone)
                                                    .get()
                                                    .addOnSuccessListener { doc ->
                                                        if (doc.exists()) {
                                                            val muploaderAvatar =
                                                                doc.getString("avatar")
                                                            videoUploader =
                                                                doc.getString("name")
                                                            mvideoUploaderAvatar.setImageBitmap(
                                                                muploaderAvatar?.let {
                                                                    convertBase64ToImage(
                                                                        requireContext(),
                                                                        it
                                                                    )
                                                                })

                                                            videoTitle = clickedItem.title
                                                            videoUrl = clickedItem.uri
                                                            videoThumbnailUrl =
                                                                clickedItem.image

                                                            mvideoTitle.text = clickedItem.title
                                                            mvideoDescription.text =
                                                                clickedItem.description
                                                            mvideoUploaderName.text =
                                                                videoUploader

                                                            val clpoint = clickedItem.point
                                                            val lock = clickedItem.isLocked
                                                            val cldescription =
                                                                clickedItem.description
                                                            val id = clickedItem.id

                                                            searchVideoBtn.isIconified = true
                                                            searchVideoBtn.clearFocus()

                                                            searchVideoBtn.clearFocus()
                                                            adminEditVideo.setOnClickListener {
                                                                val fragmentUploadVideo =
                                                                    FragmentUploadVideo()
                                                                parentFragmentManager.beginTransaction()
                                                                    .replace(
                                                                        R.id.Main_fragment,
                                                                        fragmentUploadVideo
                                                                    )
                                                                    .addToBackStack(null)
                                                                    .commit()
                                                                fragmentUploadVideo.arguments =
                                                                    putDataToFragment(
                                                                        id,
                                                                        videoUploader!!,
                                                                        videoUrl!!,
                                                                        videoThumbnailUrl!!,
                                                                        videoTitle!!,
                                                                        cldescription,
                                                                        clpoint,
                                                                        lock
                                                                    )
                                                            }
                                                            adminDeleteVideo.setOnClickListener {
                                                                showDeleteVideoDialog(
                                                                    videoTitle!!, id,
                                                                    videoUrl!!
                                                                )
                                                                updateAdapter(phone, number)
                                                            }
                                                        }
                                                    }

                                            }

                                            override fun onUnlockVideoClick(position: Int) {
                                                val clickedItem = documentList[position]

                                            }
                                        })
                                    }
                                    mClose.setOnClickListener {
                                        videoPlayer.release()
                                        motionLayout.visibility = View.GONE
                                    }
                                    var isPlaying = videoPlayer.isPlaying
                                    mPlay.setOnClickListener {
                                        isPlaying =!isPlaying
                                        if (!videoPlayer.isPlaying){
                                            mPlay.setImageResource(R.drawable.baseline_pause_24)
                                            videoPlayer.play()
                                            handler.removeCallbacks(hideControllerRunnable)
                                            handler.postDelayed(hideControllerRunnable, 1000)

                                        }else{
                                            videoPlayer.pause()
                                            mPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                                        }
                                        if (videoPlayer.playbackState == Player.STATE_ENDED) {
                                            // Seek the video to the beginning
                                            videoPlayer.seekTo(0)
                                            videoPlayer.play()
                                        }

                                    }
                                    videoPlayer.addListener(object : Player.Listener {
                                        override fun onPlayerStateChanged(
                                            playWhenReady: Boolean,
                                            playbackState: Int
                                        ) {
                                            when (playbackState) {
                                                Player.STATE_READY -> {
                                                    // Video is ready to play or paused in the ready state
                                                    mPlay.setImageResource(if (playWhenReady) R.drawable.baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24)
                                                    if (videoPlayer.isPlaying){
                                                        handler.removeCallbacks(hideControllerRunnable)
                                                        handler.postDelayed(hideControllerRunnable, 1000)
                                                    }
                                                }
                                                Player.STATE_ENDED -> {
                                                    // Video playback is completed
                                                    mPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                                                }
                                                // Other states can be handled if needed
                                                // Player.STATE_IDLE, Player.STATE_BUFFERING
                                            }
                                            isPlaying = playbackState == Player.STATE_READY && playWhenReady

                                            // Show controller if playback has just started or the player is ended
                                            if (playbackState == Player.STATE_ENDED || playbackState == Player.STATE_READY) {
                                                showController()
                                            }
                                        }
                                    })

                                    playerView.setOnTouchListener { _, _ ->
                                        if (isControllerVisible) {
                                            handler.removeCallbacks(hideControllerRunnable)
                                            handler.postDelayed(hideControllerRunnable, 1000)
                                        }
                                        true
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun convertBase64ToImage(context: Context, base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        return bitmap
    }

    private fun putDataToFragment(id: String,videoUploader: String,videoUri: String, imageUri: String, title: String, description: String, point: Long, isLocked: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString("number", number.toString())
        bundle.putString("phone", phone)
        bundle.putString("id", id)
        bundle.putString("videoUploader", videoUploader)
        bundle.putString("videoUri", videoUri)
        bundle.putString("imageUri", imageUri)
        bundle.putString("title", title)
        bundle.putString("description", description)
        bundle.putLong("point", point)
        bundle.putBoolean("isLocked", isLocked)

        return bundle
    }

    private fun getUserData(){
        arguments.let { bundle ->
            avatar = bundle?.getString("avatar").toString()
            phone = bundle?.getString("phone").toString()
            number = bundle?.getInt("number").toString().toInt()
            name = bundle?.getString("name").toString()
            date = bundle?.getString("date").toString()
            point = bundle?.getInt("point").toString().toInt()
            isAdmin = bundle?.getBoolean("isAdmin", false) ?: false
            onClickAdmin = bundle?.getBoolean("onClickAdmin", false) ?: false
            passionArray = bundle?.getIntArray("passionArray")?.toList() ?: emptyList()
            missingNumbersArray = bundle?.getIntArray("missingNumbersArray")?.toList() ?: emptyList()
            personalArray = bundle?.getIntArray("personalArray")?.toList() ?: emptyList()
            statsNumberArray = bundle?.getIntArray("statsNumberArray")?.toList() ?: emptyList()
            statsTitleArray = bundle?.getStringArray("statsTitleArray")?.toList() ?: emptyList()
            statsPointArray = bundle?.getIntArray("statsPointArray")?.toList() ?: emptyList()
            statsLockArray = bundle?.getBooleanArray("statsLockArray")?.toList() ?: emptyList()
            statsIdArray = bundle?.getStringArray("statsIdArray")?.toList() ?: emptyList()
        }
    }

    private fun getData(startPosition: Int): List<LessonVideoModal> {
        // Assuming you have a list of YourItem objects to display in the RecyclerView
        val yourItemList = documentList

        val endIndex = startPosition + 12
        if (startPosition < yourItemList.size) {
            // Return a sublist containing the next 10 items
            return yourItemList.subList(startPosition, endIndex)
        }

        // If the startPosition exceeds the size of yourItemList, return an empty list
        return emptyList()
    }

    private fun animateSearchVideoContainerWidth(targetWidth: Int) {
        val containerLayoutParams = searchVideoContainer.layoutParams
        val searchViewLayoutParams = searchVideoBtn.layoutParams

        val animator = ValueAnimator.ofInt(containerLayoutParams.width, targetWidth)
        animator.addUpdateListener { animation ->
            val width = animation.animatedValue as Int
            containerLayoutParams.width = width
            searchViewLayoutParams.width = width

            searchVideoContainer.layoutParams = containerLayoutParams
            searchVideoBtn.layoutParams = searchViewLayoutParams
        }
        animator.duration = 300 // Set the animation duration in milliseconds
        animator.start()

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                // Check if the animation completed and the target width is the original width
                if (targetWidth == containerLayoutParams.width) {
                    // Animation finished and the width is back to the original width
                    // Perform additional actions here if needed
                }
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    private fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }

    private fun showUnlockDialog(id: String, point: Long, phone:String, num:Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Mở khóa Video")
        alertDialogBuilder.setMessage("Mở khóa Video này cần $point điểm ")
        alertDialogBuilder.setPositiveButton("Mở khóa") { _, _ ->
            // Perform the unlock action here
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(phone)
            val vidRef = userRef.collection("LessonVideos").document(num.toString())
            userRef.get().addOnSuccessListener {doc ->
                if (doc != null){
                    val userPoint = doc.getLong("point")
                    if (userPoint != null && userPoint >= point){
                        userRef.update("point", userPoint - point)
                        vidRef.get().addOnSuccessListener { mdoc ->
                            val isLocked = mdoc.getBoolean(id)
                            if (isLocked != null){
                                vidRef.update(id,!isLocked)
                                Toast.makeText(requireContext(),"Mở Khóa Video thành công", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(requireContext(),"Bạn không đủ điểm để mở khóa Video này", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        alertDialogBuilder.setNegativeButton("Hủy", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showDeleteVideoDialog(title: String,videoId: String,videoUrl: String){

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Xác nhận xóa?")
        alertDialogBuilder.setMessage("Bạn chắc chắn muốn xóa video $title")
        alertDialogBuilder.setPositiveButton("Xóa") { _, _ ->
            // Delete the file from Firebase Storage
            val storageRef = Firebase.storage.reference
            val fileReference = storageRef.child(videoUrl)

            fileReference.delete()
                .addOnSuccessListener {
                    // File successfully deleted
                    // Now you can delete the document from Firestore
                    val db = FirebaseFirestore.getInstance()
                    val collectionRef = db.collection("MissingNumbersLesson").document("Videos")
                    val docRef = collectionRef.collection("Number_$number").document(videoId) // Replace `videoId` with the actual ID of the document you want to delete

                    docRef.delete()
                        .addOnSuccessListener {
                            // Document successfully deleted
                            Toast.makeText(requireContext(), "Xóa thành công video $title", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            // An error occurred while deleting the document
                            Toast.makeText(requireContext(), "Xóa video thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    // An error occurred while deleting the file
                    Toast.makeText(requireContext(), "Xóa file thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        alertDialogBuilder.setNegativeButton("Hủy") { _, _ ->

        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun initializePlayer() {
        // Create a new ExoPlayer instance
        val player = SimpleExoPlayer.Builder(requireContext())
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()

        // Build the video URI
        val uri = Uri.parse(videoUrl)

        // Create a Firebase storage reference
        val storageRef = Firebase.storage.reference
        // Get the video file from Firebase storage
        val videoRef = storageRef.child(uri.toString())

        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Download the video file to a local cache
                val localFile = File.createTempFile("video", "mp4")
                videoRef.getFile(localFile).await()

                withContext(Dispatchers.Main) {
                    // Create a media item using the local file URI
                    val mediaItem = MediaItem.fromUri(Uri.fromFile(localFile))

                    // Set the media item to the player
                    videoPlayer.setMediaItem(mediaItem)

                    // Prepare the player
                    videoPlayer.prepare()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    private fun hideController() {
        isControllerVisible = false
        playerView.hideController()
    }

    private fun showController() {
        isControllerVisible = true
        playerView.showController()
        handler.removeCallbacks(hideControllerRunnable)
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoPlayer.playWhenReady = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoPlayer.playWhenReady = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer.release()
    }

    private fun generatePagesList(end: Int): List<Int> {
        val pages = mutableListOf<Int>()
        for (i in 1 .. end) {
            pages.add(i)
        }
        return pages
    }

    private fun openFullscreenActivity(seekbar : Long,videoUrl : String) {
        val intent = Intent(requireContext(), VideoPlayerActivity::class.java).apply {
            putExtra("seekbar",seekbar)
            putExtra("videoUrl",videoUrl)
            putExtra("isFullScreen",isFullscreen)

        }
        startActivityForResult(intent, REQUEST_VIDEO_PLAYER)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentLessonVideos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLessonVideos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onButtonClick(position: Int) {
        TODO("Not yet implemented")
    }
}