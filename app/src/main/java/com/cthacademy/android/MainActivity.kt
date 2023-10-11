package com.cthacademy.android

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.cthacademy.android.navicationBar.FragmentClassroom
import com.cthacademy.android.navicationBar.FragmentLesson
import com.cthacademy.android.navicationBar.FragmentMain
import com.cthacademy.android.navicationBar.FragmentProfile
import com.cthacademy.android.navicationBar.FragmentWallet
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.google.firebase.firestore.FirebaseFirestore
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var parent: LinearLayout
    private var parentBgColor : Int? = 0
    private var isReload : Boolean = false

    private lateinit var bottomBar: AnimatedBottomBar
    private var tab: Int = 0
    private lateinit var avatar: String
    private lateinit var picture: String
    private lateinit var phone: String
    private lateinit var name: String
    private lateinit var date: String
    private var point: Int = 0
    private var isAdmin: Boolean = false
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
    private fun checkForUpdate(){
        val appUpdater = AppUpdater(this)
            .setUpdateFrom(UpdateFrom.GITHUB)
            .setGitHubUserAndRepo("javiersantos", "AppUpdater")
            .setTitleOnUpdateAvailable("Có bản cập nhật mới.")
            .setContentOnUpdateAvailable("Xin vui lòng cập nhật lên bản mới nhất để có được những trải nghiệm tốt nhất!")
            .setButtonUpdate("Cập nhật")
            .setButtonDismiss("Đóng")
        appUpdater.start()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        checkForUpdate()
        phone = intent.getStringExtra("phone").toString()
        name = intent.getStringExtra("name").toString()
        date = intent.getStringExtra("date").toString()
        tab = intent.getIntExtra("isOnTab",0)
        getDataFromDB(phone,tab)

        parent = findViewById(R.id.parent)
        parentBgColor = (parent.background as? ColorDrawable)?.color

    }
    private fun getDataFromDB(phone: String, isOnTab : Int){
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("users").document(phone)
        userDocRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // if the document exists, get the user's data as a custom data class
                avatar = document.getString("avatar").toString()
                picture = document.getString("picture").toString()
                point = document.getLong("point").toString().toInt()
                isAdmin = document.get("isAdmin") != null

                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                val statsDocRef = userDocRef.collection("Stats").document("State")

                val coreInfo =  CaculateNumberology(name, date)

                val life = coreInfo.Life
                val destiny = coreInfo.Destiny
                val connection = coreInfo.Connection
                val growth = coreInfo.Growth
                val soul = coreInfo.Soul
                val personality = coreInfo.Personality
                val connecting = coreInfo.Connecting
                val balance = coreInfo.Balance
                val rationalThinking = coreInfo.RationalThinking
                val mentalPower = coreInfo.MentalPower
                val dayOfBirth = coreInfo.DayOfBirth
                val passion = coreInfo.Passion
                val missingNumbers = coreInfo.MissingNumbers
                val personalYearSum = coreInfo.PersonalYearSum
                val personalMonthSum = coreInfo.PersonalMonthSum
                val personalDaySum = coreInfo.PersonalDaySum
                val phrase1 = coreInfo.Phrase1
                val phrase2 = coreInfo.Phrase2
                val phrase3 = coreInfo.Phrase3
                val phrase4 = coreInfo.Phrase4
                val challange1 = coreInfo.Challange1
                val challange2 = coreInfo.Challange2
                val challange3 = coreInfo.Challange3
                val challange4 = coreInfo.Challange4
                val agging1 = coreInfo.Agging1
                val agging2 = coreInfo.Agging2
                val agging3 = coreInfo.Agging3
                val agging4 = coreInfo.Agging4

                phraseArray = listOf(phrase1,phrase2,phrase3,phrase4)
                challangeArray = listOf(challange1,challange2,challange3,challange4)
                aggingArray = listOf(agging1,agging2,agging3,agging4)

                passionArray = passion
                missingNumbersArray = missingNumbers
                personalArray = listOf(personalDaySum,personalMonthSum,personalYearSum)

                statsNumberArray = listOf(
                    life, destiny, connection, growth, soul,
                    personality, connecting, balance, rationalThinking, mentalPower,
                    dayOfBirth, passion.size, missingNumbers.size, phraseArray.size,
                    challangeArray.size, aggingArray.size
                )

                statsTitleArray = listOf(
                    "Đường đời","Sứ Mệnh","Liên kết\nĐ.Đời + S.Mệnh","Trưởng Thành","Linh Hồn",
                    "Nhân Cách","Liên kết\nL.Hồn + N.Cách","Cân Bằng","Tư Duy Lý Trí","Sức Mạnh\nTiềm Thức",
                    "Ngày Sinh","Đam Mê","Số Thiếu","Chặng",
                    "Thách Thức","Tuổi Chặng"
                )

                statsPointArray = listOf(
                    180,180,180,180,
                    180,180,180,180,
                    180,180,180,180,
                    180,180,180,180
                )

                statsDocRef.get().addOnSuccessListener {statsLock->
                    if (statsLock.exists() && statsLock!=null){
                        val lifeLock = statsLock.getBoolean("Life")?: false
                        val destinyLock = statsLock.getBoolean("Destiny")?: false
                        val connectionLock = statsLock.getBoolean("Connection")?: false
                        val growthLock = statsLock.getBoolean("Growth")?: false
                        val soulLock = statsLock.getBoolean("Soul")?: false
                        val personalityLock = statsLock.getBoolean("Personality")?: false
                        val connectingLock = statsLock.getBoolean("Connecting")?: false
                        val balanceLock = statsLock.getBoolean("Balance")?: false
                        val rationalThinkingLock = statsLock.getBoolean("RationalThinking")?: false
                        val mentalPowerLock = statsLock.getBoolean("MentalPower")?: false
                        val dayOfBirthLock = statsLock.getBoolean("DayOfBirth")?: false
                        val passionLock = statsLock.getBoolean("Passion")?: false
                        val missingNumbersLock = statsLock.getBoolean("MissingNumbers")?: false
                        val phraseArrayLock = statsLock.getBoolean("Phrase")?: false
                        val challangeArrayLock = statsLock.getBoolean("Challange")?: false
                        val aggingArrayLock = statsLock.getBoolean("Agging")?: false

                        statsLockArray = listOf(
                            lifeLock,destinyLock,connectionLock,growthLock,
                            soulLock,personalityLock,connectingLock,balanceLock,
                            rationalThinkingLock,mentalPowerLock,dayOfBirthLock,passionLock,
                            missingNumbersLock,phraseArrayLock,challangeArrayLock,aggingArrayLock
                        )
                        statsIdArray = listOf(
                            "Life","Destiny","Connection","Growth",
                            "Soul","Personality","Connecting","Balance",
                            "RationalThinking","MentalPower","DayOfBirth","Passion",
                            "MissingNumbers","Phrase","Challange","Agging"
                        )
                        bottomBar= findViewById(R.id.bottom_navigation)
//        val movingLine: View = findViewById(R.id.movingLine)
                        val fragmentMain = FragmentMain.newInstance(bottomBar)
                        fragmentTransaction.replace(R.id.Main_fragment, fragmentMain)
                        fragmentTransaction.commit()
                        fragmentMain.arguments = putDataToFragment()

                        when(isOnTab){
                            0->{
                                bottomBar.selectTabById(R.id.Home)
                            }
                            1->{
                                bottomBar.selectTabById(R.id.Lesson)
                            }
                            2->{
                                bottomBar.selectTabById(R.id.Profile)
                            }
                            3->{
                                bottomBar.selectTabById(R.id.Wallet)
                            }
                            4->{
                                bottomBar.selectTabById(R.id.Classroom)
                            }
                        }

                        bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
                            override fun onTabSelected(
                                lastIndex: Int,
                                lastTab: AnimatedBottomBar.Tab?,
                                newIndex: Int,
                                newTab: AnimatedBottomBar.Tab
                            ) {
//                val tabWidth = bottom_bar.width / bottom_bar.tabCount
//                val translationX = newIndex * tabWidth.toFloat() // Calculate the desired translationX based on the selected tab's index
//                movingLine.animate().translationX(translationX).setDuration(300).start()


                                Log.d("bottom_bar", "Selected index: $newIndex, title: ${newTab.title}")
                                swipeToUpdateImageWithSlideInAnimation(newIndex,lastIndex)
                                val tabColorArray = changeColor(newIndex,lastIndex)
                                parent.animateBackgroundColor(tabColorArray.last(), tabColorArray.first(),500)
                            }

                            // An optional method that will be fired whenever an already selected tab has been selected again.
                            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
                                updateFragment(index)
                            }

                        })

                    }
                }
            }
        }

        val channel = NotificationChannel(
            "main",
            "My Notification Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

    }
    private fun putDataToFragment(): Bundle {
        val bundle = Bundle()
        // Add data to the bundle
        bundle.putString("avatar", avatar)
        bundle.putString("picture", picture)
        bundle.putString("phone", phone)
        bundle.putString("name", name)
        bundle.putString("date", date)
        bundle.putInt("point", point)
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

    private fun swipeToUpdateImageWithSlideInAnimation(newTab : Int,lastTab : Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragmentMain = FragmentMain.newInstance(bottomBar)
        val fragmentLesson = FragmentLesson()
        val fragmentProfile = FragmentProfile.newInstance(bottomBar)
        val fragmentWallet = FragmentWallet()
        val fragmentClassroom = FragmentClassroom()

        val slideOutToRightAnimation = R.anim.slide_out_to_right
        val slideOutToLeftAnimation = R.anim.slide_out_to_left
        val slideInFromRightAnimation = R.anim.slide_in_from_right
        val slideInFromLeftAnimation = R.anim.slide_in_from_left
        when(newTab){
            0->{
                if (0<lastTab){
                    fragmentTransaction.setCustomAnimations(slideInFromLeftAnimation, slideOutToRightAnimation)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentMain)
                    fragmentMain.arguments = putDataToFragment()
                }

            }
            1->{
                if (1<lastTab){
                    fragmentTransaction.setCustomAnimations(slideInFromLeftAnimation, slideOutToRightAnimation)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentLesson)
                    fragmentLesson.arguments = putDataToFragment()
                }else if (1>lastTab){
                    fragmentTransaction.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentLesson)
                    fragmentLesson.arguments = putDataToFragment()
                }
            }
            2->{
                if (2<lastTab){
                    fragmentTransaction.setCustomAnimations(slideInFromLeftAnimation, slideOutToRightAnimation)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentProfile)
                    fragmentProfile.arguments = putDataToFragment()
                }else if (2>lastTab){
                    fragmentTransaction.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentProfile)
                    fragmentProfile.arguments = putDataToFragment()
                }
            }
            3->{
                if (3<lastTab){
                    fragmentTransaction.setCustomAnimations(slideInFromLeftAnimation, slideOutToRightAnimation)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentWallet)
                    fragmentWallet.arguments = putDataToFragment()
                }else if (3>lastTab){
                    fragmentTransaction.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentWallet)
                    fragmentWallet.arguments = putDataToFragment()
                }
            }
            4->{
                if (4>lastTab){
                    fragmentTransaction.setCustomAnimations(slideInFromRightAnimation, slideOutToLeftAnimation)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentClassroom)
                    fragmentClassroom.arguments = putDataToFragment()
                }
            }
        }
        fragmentTransaction.commitNow()
    }

    private fun updateFragment(Tab : Int) {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.Main_fragment)
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragmentMain = FragmentMain.newInstance(bottomBar)
        val fragmentLesson = FragmentLesson()
        val fragmentProfile = FragmentProfile()
        val fragmentWallet = FragmentWallet()
        val fragmentClassroom = FragmentClassroom()


        val fadeIn = R.anim.fade_in
        val fadeOut = R.anim.fade_out
        when(Tab){
            0->{
                if (currentFragment is FragmentShop || currentFragment is FragmentStats){
                    fragmentTransaction.setCustomAnimations(fadeIn, fadeOut)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentMain)
                    fragmentMain.arguments = putDataToFragment()
                }

            }
            1->{
                if (currentFragment is FragmentLessonVideos || currentFragment is FragmentUploadVideo){
                    fragmentTransaction.setCustomAnimations(fadeIn, fadeOut)
                    fragmentTransaction.replace(R.id.Main_fragment, fragmentLesson)
                    fragmentLesson.arguments = putDataToFragment()
                }

            }
//            2->{
//                fragmentTransaction.setCustomAnimations(fadeIn, fadeOut)
//                fragmentTransaction.replace(R.id.Main_fragment, fragmentProfile)
//                fragmentProfile.arguments = putDataToFragment()
//            }
//            3->{
//                fragmentTransaction.setCustomAnimations(fadeIn, fadeOut)
//                fragmentTransaction.replace(R.id.Main_fragment, fragmentWallet)
//                fragmentWallet.arguments = putDataToFragment()
//            }
//            4->{
//                fragmentTransaction.setCustomAnimations(fadeIn, fadeOut)
//                fragmentTransaction.replace(R.id.Main_fragment, fragmentClassroom)
//                fragmentClassroom.arguments = putDataToFragment()
//            }
        }
        fragmentTransaction.commitNow()
    }
    private fun LinearLayout.animateBackgroundColor(startColor: Int, endColor: Int, duration: Long) {
        val animator = ObjectAnimator.ofObject(
            this,
            "backgroundColor",
            ArgbEvaluator(),
            startColor,
            endColor
        )
        animator.duration = duration
        animator.start()
    }

    private fun changeColor(mewtab : Int, lastTab: Int) : List<Int>{
        var newTabColor: Int = 0
        var lastTabColor: Int = 0
        when(mewtab){
            0->{
                when(lastTab){
                    1->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    2->{
                        lastTabColor = Color.parseColor("#FFFFFF")
                    }
                    3->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    4->{
                        lastTabColor = Color.parseColor("#FFFFFF")
                    }
                }
                newTabColor = Color.parseColor("#01081E")
            }
            1->{
                when(lastTab){
                    0->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    2->{
                        lastTabColor = Color.parseColor("#FFFFFF")
                    }
                    3->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    4->{
                        lastTabColor = Color.parseColor("#FFFFFF")
                    }
                }
                newTabColor = Color.parseColor("#01081E")
            }
            2->{
                when(lastTab){
                    0->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    1->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    3->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    4->{
                        lastTabColor = Color.parseColor("#FFFFFF")
                    }
                }
                newTabColor = Color.parseColor("#FFFFFF")
            }
            3->{
                when(lastTab){
                    0->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    1->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    2->{
                        lastTabColor = Color.parseColor("#FFFFFF")
                    }
                    4->{
                        lastTabColor = Color.parseColor("#FFFFFF")
                    }
                }
                newTabColor = Color.parseColor("#01081E")
            }
            4->{
                when(lastTab){
                    0->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    1->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                    2->{
                        lastTabColor = Color.parseColor("#FFFFFF")
                    }
                    3->{
                        lastTabColor = Color.parseColor("#01081E")
                    }
                }
                newTabColor = Color.parseColor("#FFFFFF")
            }
        }
        return listOf(newTabColor,lastTabColor)
    }
}



