package com.cthacademy.android

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.adapter.StatsAdapter
import com.cthacademy.android.adapter.StatsItemNumberAdapter
import com.cthacademy.android.custom.HorizontalSpacingItemDecoration
import com.cthacademy.android.custom.VerticalSpacingItemDecoration
import com.cthacademy.android.modal.StatsModal
import com.cthacademy.android.navicationBar.FragmentMain
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayInputStream
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentStats.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentStats : Fragment(), TextToSpeech.OnInitListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var statsTitle : TextView
    private lateinit var statsData : TextView
    private lateinit var statsSpeaker : ImageView
    private lateinit var backButton : ImageView
    private lateinit var avatarImage : ImageView

    private lateinit var statsLifeImage : LinearLayout
    private lateinit var statsImage1 : RelativeLayout

    private lateinit var leftBarStats : RecyclerView
    private lateinit var leftBarStatsAdapter : StatsAdapter
    private lateinit var statsItemNumber : RecyclerView
    private var statsList = mutableListOf<StatsModal>()

    private lateinit var avatar: String
    private lateinit var phone: String
    private lateinit var name: String
    private lateinit var date: String
    private var userPoint: Int = 0
    private var unlockStat: Boolean = false
    private var isAdmin: Boolean = false
    private var isSpeak = false
    private var isSpeakInitialized = false

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

    private lateinit var stats : List<Int>

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
        val view = inflater.inflate(R.layout.fragment_stats, container, false)
        getData()
        val db = FirebaseFirestore.getInstance()
        val statsDataDocRef = db.collection("Stats")

        textToSpeech = TextToSpeech(requireContext(), this)

        val slideOutToRightAnimation = R.anim.slide_out_to_right
        val slideOutToLeftAnimation = R.anim.slide_out_to_left
        val slideInFromRightAnimation = R.anim.slide_in_from_right
        val slideInFromLeftAnimation = R.anim.slide_in_from_left

        statsLifeImage = view.findViewById(R.id.Stats_Life_item)
        statsImage1 = view.findViewById(R.id.Stats_image_1)

        statsTitle = view.findViewById(R.id.Stats_title)
        statsData = view.findViewById(R.id.Stats_data)
        statsSpeaker = view.findViewById(R.id.Stats_speaker)

        leftBarStats = view.findViewById(R.id.Stats_stats)
        statsItemNumber = view.findViewById(R.id.Stats_grid)

        statsTitle.text = "Đường đời"
        var dataString =""
        val statsName = statsDataDocRef.document("Life")
        statsName.collection("Life-1")
            .document("${statsNumberArray[0]}")
            .get()
            .addOnSuccessListener {stats->
                if (stats.exists().and(stats != null)){
                    val chalange = stats.getString("Challange")
                    val energy = stats.getString("Energy")
                    val environment = stats.getString("Environment")
                    val lesson = stats.getString("Lesson")
                    val imageBase64String = stats.getString("Image")
                    var data = ""
                    if(statsNumberArray[0]==11||statsNumberArray[0]==22||statsNumberArray[0]==33){
                        data ="$energy"
                        statsLifeImage.visibility = View.GONE
                    }else{
                        val image = convertBase64ToImage(requireContext(),imageBase64String)
                        statsImage1.background = BitmapDrawable(resources, image)
                        data = "Năng lượng:\n$energy" +
                                "\n\nMôi trường:\n$environment" +
                                "\n\nThách thức:\n$chalange" +
                                "\n\nBài học:\n$lesson"
                    }
                    val str = data
                        .replace("\n","\n")
                        .replace("\\n","\n")
                        .replace("\n","\n\n")
                        .replace("\n\n","\n\n")
                    statsData.text = str
                    dataString = data.replace("\n","").replace("\n\n","")
                    if (isSpeakInitialized){
                        speak(dataString)
                    }
                }
            }
        for (i in statsLockArray.indices) {
            statsList.add(
                StatsModal(
                    statsNumberArray[i],
                    statsTitleArray[i],
                    statsPointArray[i],
                    statsLockArray[i],
                    statsIdArray[i]
                )
            )
        }
        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.horizontal_spacing_ExtraSmall)
        statsItemNumber.addItemDecoration(HorizontalSpacingItemDecoration(horizontalSpacing))
        statsItemNumber.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        leftBarStatsAdapter = StatsAdapter(statsList)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.vertical_spacing)
        leftBarStats.addItemDecoration(VerticalSpacingItemDecoration(verticalSpacing))
        leftBarStats.adapter = leftBarStatsAdapter
        leftBarStats.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        leftBarStatsAdapter.setButtonClickListener(object : StatsAdapter.OnClickListener {
            override fun onButtonClick(position: Int) {
                pauseSpeaking()
                when(isSpeak){
                    true->{
                        isSpeak = false
                        statsSpeaker.setImageResource(R.drawable.ic_speaker)
                    }false->{
                        statsSpeaker.setImageResource(R.drawable.ic_speaker)
                    }
                }
                statsLifeImage.visibility = View.VISIBLE
                val clickedItem = statsList[position]
                val statsId = clickedItem.id
                val statsNumber = clickedItem.number
                statsTitle.text = clickedItem.title
                when(statsId){
                    "Life"->{
                        dataString=""
                        statsData.text = ""
                        statsDataDocRef.document("$statsId")
                            .collection("Life-1")
                            .document("$statsNumber")
                            .get()
                            .addOnSuccessListener {stats->
                                if (stats.exists().and(stats != null)){
                                    val chalange = stats.getString("Challange")
                                    val energy = stats.getString("Energy")
                                    val environment = stats.getString("Environment")
                                    val lesson = stats.getString("Lesson")
                                    val imageBase64String = stats.getString("Image")
                                    var data = ""
                                    if (statsNumber != null) {
                                        if(statsNumber.toInt()==11||statsNumber.toInt()==22||statsNumber.toInt()==33){
                                            data ="$energy"
                                            statsLifeImage.visibility = View.GONE
                                        }else{
                                            val image = convertBase64ToImage(requireContext(),imageBase64String)
                                            statsImage1.background = BitmapDrawable(resources, image)
                                            data = "Năng lượng:\n$energy" +
                                                    "\n\nMôi trường:\n$environment" +
                                                    "\n\nThách thức:\n$chalange" +
                                                    "\n\nBài học:\n$lesson"
                                        }
                                    }
                                    val str = data
                                        .replace("\n","\n")
                                        .replace("\\n","\n")
                                        .replace("\n","\n\n")
                                        .replace("\n\n","\n\n")
                                    statsData.text = str
                                    dataString = data.replace("\n","").replace("\n\n","")
                                }
                            }
                    }"Passion"->{
                        dataString=""
                        statsData.text = ""
                        statsLifeImage.visibility = View.VISIBLE
                        statsItemNumber.visibility = View.VISIBLE
                        statsImage1.visibility = View.GONE
                        val statsItemNumberAdapter = StatsItemNumberAdapter(passionArray.toList())
                        stats = passionArray.toList()
                        statsItemNumber.adapter= statsItemNumberAdapter
                        statsItemNumberAdapter.setButtonClickListener(object : StatsItemNumberAdapter.OnClickListener{
                            override fun onButtonClick(position: Int) {
                                val data = stats[position]
                                Log.d("Passion","$data")
                                pauseSpeaking()
                                when(isSpeak){
                                    true->{
                                        isSpeak = false
                                        statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                    }false->{
                                    statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                }
                                }

                                statsDataDocRef.document("$statsId")
                                    .collection("ID")
                                    .document("$data")
                                    .get()
                                    .addOnSuccessListener {stats->
                                        if (stats.exists().and(stats != null)){
                                            val data = stats.getString("Data")
                                            if (data != null) {
                                                val str = data.replace("\n","\n").replace("\\n","\n").replace("\n","\n\n")
                                                dataString = data.replace("\n","").replace("\n\n","")
                                                statsData.text = str
                                            }
                                        }
                                    }

                            }
                        })
                    }"MissingNumbers"->{
                        dataString=""
                        statsData.text = ""
                        statsLifeImage.visibility = View.VISIBLE
                        statsItemNumber.visibility = View.VISIBLE
                        statsImage1.visibility = View.GONE
                        if(missingNumbersArray.isEmpty()){
                            statsDataDocRef.document("$statsId")
                                .collection("ID")
                                .document("0")
                                .get()
                                .addOnSuccessListener {stats->
                                    if (stats.exists().and(stats != null)){
                                        val data = stats.getString("Data")
                                        if (data != null) {
                                            val str = data.replace("\n","\n").replace("\\n","\n").replace("\n","\n\n")
                                            dataString = data.replace("\n","").replace("\n\n","")
                                            statsData.text = str
                                        }
                                    }
                                }
                        }else{
                            val statsItemNumberAdapter = StatsItemNumberAdapter(missingNumbersArray.toList())
                            stats = missingNumbersArray.toList()
                            statsItemNumber.adapter= statsItemNumberAdapter
                            statsItemNumberAdapter.setButtonClickListener(object : StatsItemNumberAdapter.OnClickListener{
                                override fun onButtonClick(position: Int) {
                                    val data = stats[position]
                                    Log.d("MissingNumbers","$data")
                                    pauseSpeaking()
                                    when(isSpeak){
                                        true->{
                                            isSpeak = false
                                            statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                        }false->{
                                        statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                    }
                                    }

                                    statsDataDocRef.document("$statsId")
                                        .collection("ID")
                                        .document("$data")
                                        .get()
                                        .addOnSuccessListener {stats->
                                            if (stats.exists().and(stats != null)){
                                                val data = stats.getString("Data")
                                                if (data != null) {
                                                    val str = data.replace("\n","\n").replace("\\n","\n").replace("\n","\n\n")
                                                    dataString = data.replace("\n","").replace("\n\n","")
                                                    statsData.text = str
                                                }
                                            }
                                        }

                                }
                            })
                        }
                    }"Phrase"->{
                        dataString=""
                        statsData.text = ""
                        statsLifeImage.visibility = View.VISIBLE
                        statsItemNumber.visibility = View.VISIBLE
                        statsImage1.visibility = View.GONE
                        stats = phraseArray.toList()
                        val statsItemNumberAdapter = StatsItemNumberAdapter(phraseArray.toList())
                        statsItemNumber.adapter= statsItemNumberAdapter
                        statsItemNumberAdapter.setButtonClickListener(object : StatsItemNumberAdapter.OnClickListener{
                            override fun onButtonClick(position: Int) {
                                val data = stats[position]
                                Log.d("MissingNumbers","$data")
                                pauseSpeaking()
                                when(isSpeak){
                                    true->{
                                        isSpeak = false
                                        statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                    }false->{
                                    statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                }
                                }

                                statsDataDocRef.document("$statsId")
                                    .collection("ID")
                                    .document("$data")
                                    .get()
                                    .addOnSuccessListener {stats->
                                        if (stats.exists().and(stats != null)){
                                            val data = stats.getString("Data")
                                            if (data != null) {
                                                val str = data.replace("\n","\n").replace("\\n","\n").replace("\n","\n\n")
                                                dataString = data.replace("\n","").replace("\n\n","")
                                                statsData.text = str
                                            }
                                        }
                                    }

                            }
                        })
                    }"Challange"->{
                        dataString=""
                        statsData.text = ""
                        statsLifeImage.visibility = View.VISIBLE
                        statsItemNumber.visibility = View.VISIBLE
                        statsImage1.visibility = View.GONE
                        stats = challangeArray.toList()
                        val statsItemNumberAdapter = StatsItemNumberAdapter(challangeArray.toList())
                        statsItemNumber.adapter= statsItemNumberAdapter
                        statsItemNumberAdapter.setButtonClickListener(object : StatsItemNumberAdapter.OnClickListener{
                            override fun onButtonClick(position: Int) {
                                val data = stats[position]
                                Log.d("MissingNumbers","$data")
                                pauseSpeaking()
                                when(isSpeak){
                                    true->{
                                        isSpeak = false
                                        statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                    }false->{
                                    statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                    }
                                }

                                statsDataDocRef.document("$statsId")
                                    .collection("ID")
                                    .document("$data")
                                    .get()
                                    .addOnSuccessListener {stats->
                                        if (stats.exists().and(stats != null)){
                                            val data = stats.getString("Data")
                                            if (data != null) {
                                                val str = data.replace("\n","\n").replace("\\n","\n").replace("\n","\n\n")
                                                dataString = data.replace("\n","").replace("\n\n","")
                                                statsData.text = str
                                            }
                                        }
                                    }

                            }
                        })
                    }"Agging"->{
                        dataString=""
                        statsData.text = ""
                        statsLifeImage.visibility = View.VISIBLE
                        statsItemNumber.visibility = View.VISIBLE
                        statsImage1.visibility = View.GONE
                        val statsItemNumberAdapter = StatsItemNumberAdapter(aggingArray.toList())
                        statsItemNumber.adapter= statsItemNumberAdapter
                        stats = aggingArray.toList()
                        statsItemNumberAdapter.setButtonClickListener(object : StatsItemNumberAdapter.OnClickListener{
                            override fun onButtonClick(position: Int) {
                                val data = stats[position]
                                Log.d("MissingNumbers","$data")
                                pauseSpeaking()
                                when(isSpeak){
                                    true->{
                                        isSpeak = false
                                        statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                    }false->{
                                    statsSpeaker.setImageResource(R.drawable.ic_speaker)
                                }
                                }

                                statsDataDocRef.document("$statsId")
                                    .collection("ID")
                                    .document("$data")
                                    .get()
                                    .addOnSuccessListener {stats->
                                        if (stats.exists().and(stats != null)){
                                            val data = stats.getString("Data")
                                            if (data != null) {
                                                val str = data.replace("\n","\n").replace("\\n","\n").replace("\n","\n\n")
                                                dataString = data.replace("\n","").replace("\n\n","")
                                                statsData.text = str
                                            }
                                        }
                                    }

                            }
                        })
                    }else->{
                        dataString=""
                        statsData.text = ""
                        statsLifeImage.visibility = View.GONE
                        statsItemNumber.visibility = View.GONE
                        statsDataDocRef.document("$statsId")
                            .collection("ID")
                            .document("$statsNumber")
                            .get()
                            .addOnSuccessListener {stats->
                                if (stats.exists().and(stats != null)){
                                    val data = stats.getString("Data")
                                    if (data != null) {
                                        val str = data.replace("\n","\n").replace("\\n","\n").replace("\n","\n\n")
                                        dataString = data.replace("\n","").replace("\n\n","")
                                        statsData.text = str
                                    }
                                }
                            }
                    }
                }
            }

            override fun onLockVideoClick(position: Int) {
                val clickedItem = statsList[position]
                val statsId = clickedItem.id
                val statsPoint = clickedItem.point
                if (statsPoint != null && statsId != null) {

                    val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Mở khóa chỉ số")
                    alertDialogBuilder.setMessage("Mở khóa chỉ số này cần $statsPoint điểm ")
                    alertDialogBuilder.setPositiveButton("Mở khóa") { _, _ ->
                        // Perform the unlock action here
                        val userDocRef = db.collection("users").document(phone)
                        val statsDocRef = userDocRef.collection("Stats").document("State")
                        userDocRef.get().addOnSuccessListener { p->
                            val str = p.getLong("point")
                            if (str != null) {
                                userPoint = str.toInt()
                                if (userPoint>=180){
                                    userDocRef.get().addOnSuccessListener { document->
                                        val pointSql = document.getLong("point")
                                        if (pointSql != null) {
                                            userPoint = pointSql.toInt().minus(statsPoint)
                                            userDocRef.update("point", userPoint)
                                            statsDocRef.update(statsId, true)
                                            clickedItem.isLocked = true
                                            leftBarStatsAdapter.notifyItemChanged(position)
                                        }
                                    }
                                }else{
                                    val builder = AlertDialog.Builder(requireContext())
                                    builder.setMessage("Bạn không đủ điểm, vui lòng mua thêm điểm.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                                    val alert = builder.create()
                                    alert.show()
                                }
                            }

                        }


                    }
                    alertDialogBuilder.setNegativeButton("Hủy", null)
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }

            }


        }
        )
        backButton = view.findViewById(R.id.BackBtn)
        backButton.setOnClickListener {
            val fragmentMain = FragmentMain()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.setCustomAnimations(slideInFromLeftAnimation, slideOutToRightAnimation)
            fragmentTransaction?.replace(R.id.Main_fragment, fragmentMain)
            fragmentMain.arguments = putDataToFragment()
            fragmentTransaction?.commitNow()

        }

        avatarImage = view.findViewById(R.id.Stats_user)
        avatarImage.setImageBitmap(convertBase64ToImage(requireContext(),avatar))

        statsSpeaker.setOnClickListener {
            isSpeak = !isSpeak
            when(isSpeak){
                true->{
                    statsSpeaker.setImageResource(R.drawable.ic_speaker_mute)
                    speak(dataString)
                    Log.d("data",dataString)
                }false->{
                    statsSpeaker.setImageResource(R.drawable.ic_speaker)
                    pauseSpeaking()
                }
            }
        }

        return view
    }

    private fun convertBase64ToImage(context: Context, base64String: String?): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)

        return BitmapFactory.decodeStream(inputStream)
    }

    private fun getData(){
        arguments.let { bundle ->
            // Retrieve data from the bundle
            avatar = bundle?.getString("avatar").toString()
            phone = bundle?.getString("phone").toString()
            userPoint = bundle?.getInt("point").toString().toInt()
            name = bundle?.getString("name").toString()
            date = bundle?.getString("date").toString()
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

    private fun putDataToFragment(): Bundle {
        val bundle = Bundle()
        // Add data to the bundle
        bundle.putString("avatar", avatar)
        bundle.putString("phone", phone)
        bundle.putInt("point", userPoint.toInt())
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentStats.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentStats().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set language for TextToSpeech
            isSpeakInitialized = true
            val result = textToSpeech.setLanguage(Locale("vi", "VN"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported")
            } else {
                // Set the female voice
                textToSpeech.voice = Voice("vi-vn-x-oda#female_1-local", Locale("vi", "VN"), Voice.QUALITY_HIGH, Voice.LATENCY_HIGH, false, null)
            }
        } else {
            Log.e("TTS", "Initialization failed: $status")
        }
    }
    private fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)


    }
    private fun pauseSpeaking() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
    }
    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }

}