package com.cthacademy.android.navicationBar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.FragmentMiniProfile
import com.cthacademy.android.FragmentShop
import com.cthacademy.android.FragmentStats
import com.cthacademy.android.R
import com.cthacademy.android.adapter.ImageSliderImagesAdapter
import com.cthacademy.android.adapter.MiniProfileAdapter
import com.cthacademy.android.custom.HorizontalSpacingItemDecoration
import com.cthacademy.android.custom.ImageSlider
import com.cthacademy.android.modal.MiniProfile
import com.github.mmin18.widget.RealtimeBlurView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import decimalToAlphabet
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.io.ByteArrayInputStream
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.IsoFields
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMain : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var blurView : RealtimeBlurView
    private lateinit var overlayView : View
    private lateinit var miniProfileGrid : RecyclerView
    private lateinit var miniProfileAdapter : MiniProfileAdapter
    private lateinit var miniProfileText : TextView
    private lateinit var miniProfileName : EditText
    private lateinit var miniProfileDate : EditText
    private lateinit var miniProfileSubmit : TextView
    private lateinit var miniProfileContainer : CardView

    private lateinit var imageSliderView : ImageView
    private lateinit var pointText : TextView
    private lateinit var mainScreenName : TextView
    private lateinit var mainScreenFullName : TextView
    private lateinit var mainScreenInvitationCode : TextView
    private lateinit var mainScreenImageSliderImages : RecyclerView

    private lateinit var profile : ImageView
    private lateinit var newProfile : CardView
    private lateinit var lesson : CardView
    private lateinit var classroom : CardView
    private lateinit var shop : CardView
    private lateinit var stats : CardView

    private val handler = Handler()
    private val timer = Timer()
    private lateinit var blinkAnimation: Animation

    private lateinit var checkinCD : TextView
    private lateinit var checkinNotification : ImageView
    private lateinit var checkinBtn : ImageView

    private lateinit var avatar: String
    private lateinit var phone: String
    private lateinit var name: String
    private lateinit var date: String
    private var isAdmin: Boolean = false
    private var point: Int = 0

    private lateinit var passionArray : List<Int>
    private lateinit var missingNumbersArray : List<Int>
    private lateinit var phraseArray : List<Int>
    private lateinit var challangeArray : List<Int>
    private lateinit var aggingArray : List<Int>
    private lateinit var personalArray : List<Int>
    private lateinit var statsNumberArray : List<Int>
    private lateinit var statsTitleArray : List<String>
    private lateinit var statsPointArray : List<Int>
    private lateinit var statsLockArray : List<Boolean>
    private lateinit var statsIdArray : List<String>
    private lateinit var imageSliderImages : List<Int>


    private var bottomBar: AnimatedBottomBar? = null
    private val db = FirebaseFirestore.getInstance()
    fun setBottomBar(bottomBar: AnimatedBottomBar) {
        this.bottomBar = bottomBar
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        imageSliderView = view.findViewById(R.id.MainScreen_SlideView)

        mainScreenName = view.findViewById(R.id.MainScreen_Name)
        mainScreenFullName = view.findViewById(R.id.MainScreen_Fullname)
        mainScreenInvitationCode = view.findViewById(R.id.MainScreen_InvitationCode)
        mainScreenImageSliderImages = view.findViewById(R.id.MainScreen_SlideView_Locator)
        blurView = view.findViewById(R.id.BlurView)
        overlayView = view.findViewById(R.id.overlayView)
        pointText = view.findViewById(R.id.MainScreen_Point)
        classroom = view.findViewById(R.id.MainScreen_Classroom)
        lesson = view.findViewById(R.id.MainScreen_Video)
        shop = view.findViewById(R.id.MainScreen_Shop)
        profile = view.findViewById(R.id.MainScreen_Avatar)
        newProfile = view.findViewById(R.id.MainScreen_NewProfile)
        stats = view.findViewById(R.id.MainScreen_Stats)

        checkinCD = view.findViewById(R.id.MainScreen_Checkin_CD)
        checkinNotification = view.findViewById(R.id.MainScreen_notification)

        miniProfileGrid = view.findViewById(R.id.Miniprofile_grid)
        miniProfileText = view.findViewById(R.id.Miniprofile_text)
        miniProfileSubmit = view.findViewById(R.id.Miniprofile_submit)
        miniProfileName = view.findViewById(R.id.Miniprofile_Name)
        miniProfileDate = view.findViewById(R.id.Miniprofile_Date)
        miniProfileContainer = view.findViewById(R.id.Miniprofile_container)

        getData()
        getDayOfWeek(phone)
        profile.setImageBitmap(convertBase64ToImage(requireContext(),avatar))
        pointText.text = point.toString()

        val last2InArray = name.split(" ").takeLast(2).joinToString(" ")
        mainScreenFullName.text = name
        mainScreenName.text = last2InArray
        mainScreenInvitationCode.text = decimalToAlphabet(phone.replace("+84","0"))
//        mainScreenInvitationCode.text = alphabetToDecimal("FILIIHGHGK",phone.replace("+84","0"))


        val userDocRef = db.collection("users").document(phone)

        val slideOutToRightAnimation = R.anim.slide_out_to_right
        val slideOutToLeftAnimation = R.anim.slide_out_to_left
        val slideInFromRightAnimation = R.anim.slide_in_from_right
        val slideInFromLeftAnimation = R.anim.slide_in_from_left

        val imageSlider = ImageSlider(
            requireContext(),
            imageSliderView,
            listOf(R.drawable.ic_event0, R.drawable.ic_event1)
        )

        val numberList = mutableListOf<Int>()
        for (i in 1..imageSlider.getItemCount()) {
            numberList.add(i)
        }

        imageSliderImages = numberList

        val imageSliderAdapter = ImageSliderImagesAdapter(imageSliderImages)
        mainScreenImageSliderImages.adapter = imageSliderAdapter
        mainScreenImageSliderImages.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Set the onClickListener for each image
        imageSlider.setOnPositionChangedListener(object : ImageSlider.OnPositionChangedListener {
            override fun onPositionChanged(position: Int) {
                imageSliderAdapter.setCurrentPosition(position)
            }
        })
        imageSlider.setOnPositionChangedListener(object : ImageSlider.OnPositionChangedListener {
            override fun onPositionChanged(position: Int) {
                imageSliderAdapter.setCurrentPosition(position)
            }
        })



        overlayView.setOnClickListener {
            overlayView.visibility = View.GONE
            miniProfileContainer.visibility = View.GONE
        }

        imageSlider.startSlideshow()

//        wallet.setOnClickListener {
//            val fragmentWallet = FragmentWallet()
//            val fragmentTransaction = fragmentManager?.beginTransaction()
//            fragmentWallet.arguments = putDataToFragment()
//            fragmentTransaction?.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
//            fragmentTransaction?.replace(R.id.Main_fragment, fragmentWallet)
//            fragmentTransaction?.commitNow()
//            bottomBar?.selectTabById(R.id.Wallet)
//        }
        lesson.setOnClickListener {
            val fragmentLesson = FragmentLesson()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentLesson.arguments = putDataToFragment()
            fragmentTransaction?.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
            fragmentTransaction?.replace(R.id.Main_fragment, fragmentLesson)
            fragmentTransaction?.commit()
            fragmentLesson.arguments = putDataToFragment()
            bottomBar?.selectTabById(R.id.Lesson)
        }
        profile.setOnClickListener {
            val fragmentProfile = FragmentProfile()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentProfile.arguments = putDataToFragment()
            fragmentTransaction?.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
            fragmentTransaction?.replace(R.id.Main_fragment, fragmentProfile)
            fragmentTransaction?.commitNow()
            bottomBar?.selectTabById(R.id.Profile)
        }
        classroom.setOnClickListener {
            val fragmentClassroom = FragmentClassroom()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentClassroom.arguments = putDataToFragment()
            fragmentTransaction?.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
            fragmentTransaction?.replace(R.id.Main_fragment, fragmentClassroom)
            fragmentTransaction?.commit()
            bottomBar?.selectTabById(R.id.Classroom)
        }
        shop.setOnClickListener {
            val fragmentShop = FragmentShop()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentShop.arguments = putDataToFragment()
            fragmentTransaction?.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
            fragmentTransaction?.replace(R.id.Main_fragment, fragmentShop)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        stats.setOnClickListener {
            val fragmentStats = FragmentStats()
            fragmentStats.arguments = putDataToFragment()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
            fragmentTransaction?.replace(R.id.Main_fragment, fragmentStats)
            fragmentTransaction?.commitNow()
        }
        newProfile.setOnClickListener {
            val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
            overlayView.visibility = View.VISIBLE
            miniProfileContainer.visibility = View.VISIBLE
            miniProfileContainer.startAnimation(fadeInAnimation)
        }

        getDayOfWeek(phone)
        val currentDay = Calendar.getInstance().time
        val calendar3: Calendar = Calendar.getInstance()
        calendar3.time = currentDay

        val indexOfDay = when (calendar3.get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> {-1}
        }

        val newDaysOfWeekArr =arrayOf(0,0,0,0,0,0,0)
        checkinBtn = view.findViewById(R.id.MainScreen_Checkin)
        checkinBtn.setOnClickListener {
            when(indexOfDay){
                0 ->{
                    view?.findViewById<ImageView>(R.id.checkin_mon)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                1 ->{
                    view?.findViewById<ImageView>(R.id.checkin_tue)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                2 ->{
                    view?.findViewById<ImageView>(R.id.checkin_wed)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                3 ->{
                    view?.findViewById<ImageView>(R.id.checkin_thu)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                4 ->{
                    view?.findViewById<ImageView>(R.id.checkin_fri)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                5 ->{
                    view?.findViewById<ImageView>(R.id.checkin_sat)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                6 ->{
                    view?.findViewById<ImageView>(R.id.checkin_sun)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
            }
            userDocRef.get()
                .addOnSuccessListener { doc ->
                    if (doc.contains("daysOfWeekChecked")){
                        val timestamp = doc.getTimestamp("daychecked")
                        val checked = doc.getLong("checked")?.toInt()
                        val daysOfWeekChecked = doc.getString("daysOfWeekChecked")
                        val point = doc.getLong("point")

                        if (timestamp != null && checked != null && daysOfWeekChecked != null && point != null){
                            val daysOfWeekArr = daysOfWeekChecked
                                .replace("[","")
                                .replace("]","")
                                .replace(" ","")
                                .split(",").map { it.toInt() }.toIntArray()
                            resetWeeklyArray(daysOfWeekArr)

                            val dayCheckedWeek = timestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                            val todayCheckedWeek = LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)

                            val timestampDate = timestamp.toDate()
                            val currentDate= Date()

                            val calendar: Calendar = Calendar.getInstance()
                            calendar.time = timestampDate
                            calendar.add(Calendar.HOUR_OF_DAY,24)
                            val newDate: Date = calendar.time

                            val calendar2: Calendar = Calendar.getInstance()
                            calendar2.time = timestampDate
                            calendar2.add(Calendar.HOUR_OF_DAY,48)
                            val nextDate: Date = calendar2.time

                            if (currentDate == newDate||currentDate.after(newDate)){

                                if (indexOfDay!=-1){

                                    if (dayCheckedWeek<todayCheckedWeek){
                                        userDocRef.update("daysOfWeekChecked",newDaysOfWeekArr.contentToString())
                                    }

                                    daysOfWeekArr[indexOfDay] =1
                                    userDocRef.update("checked",checked+1)
                                    userDocRef.update("daychecked", FieldValue.serverTimestamp())
                                    userDocRef.update("daysOfWeekChecked",daysOfWeekArr.contentToString())
                                    if (checked.plus(1)<=15){
                                        userDocRef.update("point", point.plus(5))
                                    }else if (checked.plus(1) in 16..30){
                                        userDocRef.update("point", point.plus(7))
                                    }else if (checked.plus(1)>=31){
                                        userDocRef.update("point", point.plus(10))
                                    }
                                    val currentTimeStamp = Timestamp.now()
                                    dayCheckUI(daysOfWeekArr.contentToString())
                                    Timer(currentTimeStamp)
                                    checkinCD.setTextColor(Color.parseColor("#CF0707"))
                                    getDayOfWeek(phone)
                                }

                            }else{

                                blinkAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink)
                                checkinCD.startAnimation(blinkAnimation)
                                checkinCD.setTextColor(Color.parseColor("#CF0707"))
                                checkinCD.postDelayed({
                                    checkinCD.clearAnimation()
                                    checkinCD.setTextColor(resources.getColor(R.color.black))
                                }, 1000)


                            }
                            if(currentDate == nextDate||currentDate.after(nextDate)){
                                if (indexOfDay!=-1){

                                    daysOfWeekArr[indexOfDay] =1
                                    userDocRef.update("checked",1)
                                    userDocRef.update("point", point.plus(5))
                                    userDocRef.update("daysOfWeekChecked",daysOfWeekChecked.toString())
                                    userDocRef.update("daychecked", FieldValue.serverTimestamp())
                                    getDayOfWeek(phone)
                                    dayCheckUI(daysOfWeekChecked)
                                }
                            }
                        }
                    }else{
                        val point = doc.getLong("point")
                        if (indexOfDay!=-1 && point!=null){
                            newDaysOfWeekArr[indexOfDay] =1
                            val data = hashMapOf(
                                "daychecked" to FieldValue.serverTimestamp(),
                                "checked" to 1,
                                "daysOfWeekChecked" to newDaysOfWeekArr.contentToString()
                            )
                            userDocRef.set(data, SetOptions.merge())
                            userDocRef.update("point", point.plus(5))
                            dayCheckUI(newDaysOfWeekArr.contentToString())
                            getDayOfWeek(phone)
                        }
                    }
                }

        }
        val documentList = mutableListOf<MiniProfile>()
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.horizontal_spacing_small)
        miniProfileGrid.addItemDecoration(HorizontalSpacingItemDecoration(horizontalSpacing))
        miniProfileGrid.layoutManager = layoutManager
        miniProfileAdapter = MiniProfileAdapter(documentList)
        userDocRef.collection("profiles")
            .get()
            .addOnSuccessListener { doc ->

            for (profile in doc.documents) {
                val miniProfileName = profile.getString("miniName")
                val miniProfileDate = profile.getString("miniDate")
                if (miniProfileName != null && miniProfileDate != null) {
                    val documentItem = MiniProfile(
                        miniProfileName,
                        miniProfileDate
                    )
                    documentList.add(documentItem)
                }
            }
            if (documentList.isEmpty()) {
                miniProfileText.visibility = View.VISIBLE
            } else {
                miniProfileAdapter = MiniProfileAdapter(documentList)
            }
                miniProfileGrid.adapter = miniProfileAdapter
                miniProfileAdapter.setMiniProfileClickListener(object : MiniProfileAdapter.OnClickListener{
                    override fun onMiniProfileClick(position: Int) {
                        val clickedItem = documentList[position]
                        val miniName = clickedItem.name
                        val miniDate = clickedItem.date
                        Log.d("debug",clickedItem.toString())
                        Toast.makeText(requireContext(),clickedItem.toString(),Toast.LENGTH_SHORT).show()
                        val fragmentMiniProfile = FragmentMiniProfile()
                        val fragmentTransaction = fragmentManager?.beginTransaction()
                        fragmentMiniProfile.arguments = putMiniProfileData(miniName,miniDate)
                        fragmentTransaction?.setCustomAnimations(R.anim.fragment_slide_in, R.anim.fragment_slide_out)
                        fragmentTransaction?.replace(R.id.Main_fragment, fragmentMiniProfile)
                        fragmentTransaction?.addToBackStack(null)

                        fragmentTransaction?.commit()
                    }
                })
        }

        miniProfileSubmit.setOnClickListener {
            if (miniProfileName.text.isEmpty()){
                miniProfileName.error = null
                miniProfileName.requestFocus()
                return@setOnClickListener
            }
            if (miniProfileDate.text.isEmpty()){
                miniProfileDate.error = null
                return@setOnClickListener
            }

            val data = hashMapOf(
                "miniName" to miniProfileName.text.toString(),
                "miniDate" to miniProfileDate.text.toString()
            )

            userDocRef.collection("profiles")
                .get()
                .addOnSuccessListener { doc ->
                val documentCount = doc.size()
                userDocRef.collection("profiles")
                    .document("${documentCount+1}")
                    .set(data, SetOptions.merge())
            }
            val documentItem = MiniProfile(
                miniProfileName.text.toString(),
                miniProfileDate.text.toString()
            )
            miniProfileAdapter.insertData(documentItem)
            miniProfileName.text.clear()
            miniProfileDate.text.clear()
        }

        miniProfileDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    // Update the text field with the selected date
                    miniProfileDate.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        return view
    }

    private fun convertBase64ToImage(context: Context, base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        return bitmap
    }

    private fun getData(){
        arguments.let { bundle ->
            avatar = bundle?.getString("avatar").toString()
            phone = bundle?.getString("phone").toString()
            name = bundle?.getString("name").toString()
            date = bundle?.getString("date").toString()
            point = bundle?.getInt("point").toString().toInt()
            isAdmin = bundle?.getBoolean("isAdmin", false) ?: false
            passionArray = bundle?.getIntArray("passionArray")?.toList() ?: emptyList()
            missingNumbersArray = bundle?.getIntArray("missingNumbersArray")?.toList() ?: emptyList()
            phraseArray = bundle?.getIntArray("phraseArray")?.toList() ?: emptyList()
            challangeArray = bundle?.getIntArray("challangeArray")?.toList() ?: emptyList()
            aggingArray = bundle?.getIntArray("aggingArray")?.toList() ?: emptyList()
            personalArray = bundle?.getIntArray("personalArray")?.toList() ?: emptyList()
            statsNumberArray = bundle?.getIntArray("statsNumberArray")?.toList() ?: emptyList()
            statsTitleArray = bundle?.getStringArray("statsTitleArray")?.toList() ?: emptyList()
            statsPointArray = bundle?.getIntArray("statsPointArray")?.toList() ?: emptyList()
            statsLockArray = bundle?.getBooleanArray("statsLockArray")?.toList() ?: emptyList()
            statsIdArray = bundle?.getStringArray("statsIdArray")?.toList() ?: emptyList()
        }
    }

    private fun putMiniProfileData(miniProfileName:String?, miniProfileDate:String?): Bundle {
        val bundle = Bundle()
        bundle.putString("name",miniProfileName)
        bundle.putString("date",miniProfileDate)
        return bundle
    }

    private fun putDataToFragment(): Bundle {
        val bundle = Bundle()
        // Add data to the bundle
        bundle.putString("avatar", avatar)
        bundle.putString("phone", phone)
        bundle.putInt("point", point)
        bundle.putString("name", name)
        bundle.putString("date", date)
        bundle.putBoolean("isAdmin", isAdmin)
        bundle.putIntArray("passionArray",passionArray.toIntArray())
        bundle.putIntArray("missingNumbersArray",missingNumbersArray.toIntArray())
        bundle.putIntArray("phraseArray",phraseArray.toIntArray())
        bundle.putIntArray("challangeArray",challangeArray.toIntArray())
        bundle.putIntArray("aggingArray",aggingArray.toIntArray())
        bundle.putIntArray("personalArray",personalArray.toIntArray())
        bundle.putIntArray("statsNumberArray",statsNumberArray.toIntArray())
        bundle.putStringArray("statsTitleArray",statsTitleArray.toTypedArray())
        bundle.putIntArray("statsPointArray",statsPointArray.toIntArray())
        bundle.putBooleanArray("statsLockArray",statsLockArray.toBooleanArray())
        bundle.putStringArray("statsIdArray",statsIdArray.toTypedArray())
        return bundle
    }

    private fun convertMillisecondsToTime(milliseconds: Long): String {
        if (milliseconds <= 0) {
            return "00:00:00" // or handle the non-positive case as needed
        }
        val duration = Duration.ofMillis(milliseconds)
        val localTime = LocalTime.MIDNIGHT.plus(duration)
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return localTime.format(formatter)
    }

    private fun resetWeeklyArray(array: IntArray) {
        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (currentDayOfWeek == Calendar.MONDAY) {
            val initialArray = intArrayOf(0, 0, 0, 0, 0, 0, 0)
            System.arraycopy(initialArray, 0, array, 0, initialArray.size)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDayOfWeek(phone:String){
        val userDocRef = db.collection("users").document(phone)
        userDocRef.get().addOnSuccessListener { i ->
            if (i.exists()){
                val daysOfWeekChecked = i.getString("daysOfWeekChecked")
                val timestamp = i.getTimestamp("daychecked")

                if (daysOfWeekChecked != null && timestamp != null){
                    Timer(timestamp)
                    dayCheckUI(daysOfWeekChecked)
                }
            }
        }
    }

    private fun Timer(timestamp: Timestamp){
        val timestampDate = timestamp.toDate()
        val thisCalendar: Calendar = Calendar.getInstance()
        thisCalendar.time = timestampDate
        thisCalendar.add(Calendar.HOUR_OF_DAY,24)
        val newDate: Date = thisCalendar.time
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val thisCurrentDate= Date()
                val differenceInMillis: Long = newDate.time - thisCurrentDate.time
                val time = convertMillisecondsToTime(differenceInMillis)
                checkinCD.text = time
//                            Log.d("bottom_bar", time)
                val thisTimeArr = time.split(":").map { it.toInt() }.toIntArray()
                val thisHour = thisTimeArr[0]
                val thisMin = thisTimeArr[1]
                val thisSec = thisTimeArr[2]
                if (thisHour == 0 && thisMin <= 5&& thisSec == 0){
                    checkinCD.setTextColor(resources.getColor(R.color.red_A400))
                }
                if (thisHour == 0 && thisMin == 0 && thisSec == 0){
                    checkinCD.setTextColor(resources.getColor(R.color.green_A400))
                    cancel()
                }
            }
        }, 0, 1000)
    }

    private fun dayCheckUI(str: String){
        val daysOfWeekArr = str
            .replace("[","")
            .replace("]","")
            .replace(" ","")
            .split(",").map { it.toInt() }.toIntArray()
        val checked = mutableListOf<Int>()
        val unchecked = mutableListOf<Int>()
        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        for ((index, day) in daysOfWeekArr.withIndex()){
            if (day==1){
                checked.add(index)
            }else{
                unchecked.add(index)
            }
        }
        for (day in checked) {
            when (day){
                0 ->{
                    view?.findViewById<ImageView>(R.id.checkin_mon)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                1 ->{
                    view?.findViewById<ImageView>(R.id.checkin_tue)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                2 ->{
                    view?.findViewById<ImageView>(R.id.checkin_wed)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                3 ->{
                    view?.findViewById<ImageView>(R.id.checkin_thu)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                4 ->{
                    view?.findViewById<ImageView>(R.id.checkin_fri)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                5 ->{
                    view?.findViewById<ImageView>(R.id.checkin_sat)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
                6 ->{
                    view?.findViewById<ImageView>(R.id.checkin_sun)?.setImageDrawable(
                        resources.getDrawable(R.drawable.ic_checkin_checked)
                    )
                }
            }
        }
        for (day in unchecked) {
            if (day < currentDayOfWeek-1){
                when (day){
                    0 ->{
                        view?.findViewById<ImageView>(R.id.checkin_mon)?.setImageDrawable(
                            resources.getDrawable(R.drawable.ic_checkin_unchecked)
                        )
                    }
                    1 ->{
                        view?.findViewById<ImageView>(R.id.checkin_tue)?.setImageDrawable(
                            resources.getDrawable(R.drawable.ic_checkin_unchecked)
                        )
                    }
                    2 ->{
                        view?.findViewById<ImageView>(R.id.checkin_wed)?.setImageDrawable(
                            resources.getDrawable(R.drawable.ic_checkin_unchecked)
                        )
                    }
                    3 ->{
                        view?.findViewById<ImageView>(R.id.checkin_thu)?.setImageDrawable(
                            resources.getDrawable(R.drawable.ic_checkin_unchecked)
                        )
                    }
                    4 ->{
                        view?.findViewById<ImageView>(R.id.checkin_fri)?.setImageDrawable(
                            resources.getDrawable(R.drawable.ic_checkin_unchecked)
                        )
                    }
                    5 ->{
                        view?.findViewById<ImageView>(R.id.checkin_sat)?.setImageDrawable(
                            resources.getDrawable(R.drawable.ic_checkin_unchecked)
                        )
                    }
                    6 ->{
                        view?.findViewById<ImageView>(R.id.checkin_sun)?.setImageDrawable(
                            resources.getDrawable(R.drawable.ic_checkin_unchecked)
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the runnable when the activity is destroyed
        handler.removeCallbacksAndMessages(null)
        timer.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(bottomBar: AnimatedBottomBar): FragmentMain {
            val fragment = FragmentMain()
            fragment.setBottomBar(bottomBar)
            return fragment
        }
    }
}