package com.cthacademy.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.adapter.StatsAdapter
import com.cthacademy.android.custom.GridSpacingItemDecoration
import com.cthacademy.android.modal.StatsModal

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var todayGrid : RecyclerView
private lateinit var mainGrid : RecyclerView
private lateinit var passionGrid : RecyclerView
private lateinit var missingNumbersGrid : RecyclerView
private lateinit var phraseGrid : RecyclerView
private lateinit var challengeGrid : RecyclerView
private lateinit var aggingGrid : RecyclerView

@SuppressLint("StaticFieldLeak")
private lateinit var BackBtn : RelativeLayout

@SuppressLint("StaticFieldLeak")
private lateinit var todayGridText : TextView
@SuppressLint("StaticFieldLeak")
private lateinit var mainGridText : TextView
@SuppressLint("StaticFieldLeak")
private lateinit var passionGridText : TextView
@SuppressLint("StaticFieldLeak")
private lateinit var missingNumbersGridText : TextView
@SuppressLint("StaticFieldLeak")
private lateinit var phraseGridText : TextView
@SuppressLint("StaticFieldLeak")
private lateinit var challengeGridText : TextView
@SuppressLint("StaticFieldLeak")
private lateinit var aggingGridText : TextView

private lateinit var todayAdapter : StatsAdapter
private lateinit var mainAdapter : StatsAdapter
private lateinit var passionAdapter : StatsAdapter
private lateinit var missingNumbersAdapter : StatsAdapter
private lateinit var phraseAdapter : StatsAdapter
private lateinit var challengeAdapter : StatsAdapter
private lateinit var aggingAdapter : StatsAdapter

private var todayList = mutableListOf<StatsModal>()
private var mainList = mutableListOf<StatsModal>()
private var passionList = mutableListOf<StatsModal>()
private var missingNumbersList = mutableListOf<StatsModal>()
private var phraseList = mutableListOf<StatsModal>()
private var challengeList = mutableListOf<StatsModal>()
private var aggingList = mutableListOf<StatsModal>()

private lateinit var name: String
private lateinit var date: String

@SuppressLint("StaticFieldLeak")
private lateinit var nameText: TextView
@SuppressLint("StaticFieldLeak")
private lateinit var dateText: TextView

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMiniProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMiniProfile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_mini_profile, container, false)
        getData()
        updateView(view)
        clearData()
        caculateStats(name, date)
        BackBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }


        return view
    }

    private fun getData(){
        arguments.let { bundle ->
            // Retrieve data from the bundle
            name = bundle?.getString("name").toString()
            date = bundle?.getString("date").toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateView(view: View){
        nameText = view.findViewById(R.id.Miniprofile_name)
        dateText = view.findViewById(R.id.Miniprofile_date)

        todayGrid = view.findViewById(R.id.MiniProfile_today_grid)
        mainGrid = view.findViewById(R.id.MiniProfile_main_grid)
        passionGrid = view.findViewById(R.id.MiniProfile_Passion_grid)
        missingNumbersGrid = view.findViewById(R.id.MiniProfile_MissingNumbers_grid)
        phraseGrid = view.findViewById(R.id.MiniProfile_Phrase_grid)
        challengeGrid = view.findViewById(R.id.MiniProfile_Challange_grid)
        aggingGrid = view.findViewById(R.id.MiniProfile_Agging_grid)

        todayGridText = view.findViewById(R.id.MiniProfile_today_grid_text)
        mainGridText = view.findViewById(R.id.MiniProfile_main_grid_text)
        passionGridText = view.findViewById(R.id.MiniProfile_Passion_grid_text)
        missingNumbersGridText = view.findViewById(R.id.MiniProfile_MissingNumbers_grid_text)
        phraseGridText = view.findViewById(R.id.MiniProfile_Phrase_grid_text)
        challengeGridText = view.findViewById(R.id.MiniProfile_Challange_grid_text)
        aggingGridText = view.findViewById(R.id.MiniProfile_Agging_grid_text)

        BackBtn = view.findViewById(R.id.BackBtn)

        nameText.text = name
        dateText.text = date

        todayGridText.text = "Chỉ số hôm nay"
        mainGridText.text = "Chỉ số chính"
        passionGridText.text = "Chỉ số đam mê"
        missingNumbersGridText.text = "Chỉ số số thiếu"
        phraseGridText.text = "Chỉ số chặng"
        challengeGridText.text = "Chỉ số thách thức"
        aggingGridText.text = "Chỉ số tuổi chặng"
    }

    private fun caculateStats(name: String,date: String){

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

        val phraseArray = listOf(phrase1,phrase2,phrase3,phrase4)
        val challangeArray = listOf(challange1,challange2,challange3,challange4)
        val aggingArray = listOf(agging1,agging2,agging3,agging4)
        val personalArray = listOf(personalDaySum,personalMonthSum,personalYearSum)

        val mainStatsNumberArray = listOf(
            life, destiny, connection, growth, soul,
            personality, connecting, balance, rationalThinking, mentalPower,
            dayOfBirth
        )
        val mainStatsTitleArray = listOf(
            "Đường đời","Sứ Mệnh","Liên kết\nĐ.Đời + S.Mệnh","Trưởng Thành","Linh Hồn",
            "Nhân Cách","Liên kết\nL.Hồn + N.Cách","Cân Bằng","Tư Duy Lý Trí","Sức Mạnh\nTiềm Thức",
            "Ngày Sinh"
        )
        val personalArrayStatsTitleArray = listOf(
            "Ngày", "Tháng", "Năm"
        )
        for (i in mainStatsNumberArray.indices) {
            mainList.add(
                StatsModal(
                    mainStatsNumberArray[i],
                    mainStatsTitleArray[i],
                    0,
                    true,
                    ""
                )
            )
        }
        for (i in personalArray.indices) {
            todayList.add(
                StatsModal(
                    personalArray[i],
                    personalArrayStatsTitleArray[i],
                    0,
                    true,
                    ""
                )
            )
        }
        for (i in passion.indices) {
            passionList.add(
                StatsModal(
                    passion[i],
                    "",
                    0,
                    true,
                    ""
                )
            )
        }
        if (missingNumbers.isEmpty()) {
            missingNumbersList.add(
                StatsModal(
                    0,
                    "",
                    0,
                    true,
                    ""
                )
            )
        }else{
            for (i in missingNumbers.indices) {
                missingNumbersList.add(
                    StatsModal(
                        missingNumbers[i],
                        "",
                        0,
                        true,
                        ""
                    )
                )
            }
        }


        for (i in phraseArray.indices) {
            phraseList.add(
                StatsModal(
                    phraseArray[i],
                    "",
                    0,
                    true,
                    ""
                )
            )
        }
        for (i in challangeArray.indices) {
            challengeList.add(
                StatsModal(
                    challangeArray[i],
                    "",
                    0,
                    true,
                    ""
                )
            )
        }
        for (i in aggingArray.indices) {
            aggingList.add(
                StatsModal(
                    aggingArray[i],
                    "",
                    0,
                    true,
                    ""
                )
            )
        }
        todayAdapter = StatsAdapter(todayList)
        mainAdapter = StatsAdapter(mainList)
        passionAdapter = StatsAdapter(passionList)
        missingNumbersAdapter = StatsAdapter(missingNumbersList)
        phraseAdapter = StatsAdapter(phraseList)
        challengeAdapter = StatsAdapter(challengeList)
        aggingAdapter = StatsAdapter(aggingList)

        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.vertical_spacing)
        todayGrid.addItemDecoration(GridSpacingItemDecoration(1, verticalSpacing))
        mainGrid.addItemDecoration(GridSpacingItemDecoration(3, verticalSpacing))
        passionGrid.addItemDecoration(GridSpacingItemDecoration(1, verticalSpacing))
        missingNumbersGrid.addItemDecoration(GridSpacingItemDecoration(1, verticalSpacing))
        phraseGrid.addItemDecoration(GridSpacingItemDecoration(1, verticalSpacing))
        challengeGrid.addItemDecoration(GridSpacingItemDecoration(1, verticalSpacing))
        aggingGrid.addItemDecoration(GridSpacingItemDecoration(1, verticalSpacing))

        todayGrid.adapter = todayAdapter
        mainGrid.adapter = mainAdapter
        passionGrid.adapter = passionAdapter
        missingNumbersGrid.adapter = missingNumbersAdapter
        phraseGrid.adapter = phraseAdapter
        challengeGrid.adapter = challengeAdapter
        aggingGrid.adapter = aggingAdapter

        todayGrid.layoutManager = GridLayoutManager(requireContext(),1, LinearLayoutManager.HORIZONTAL, false)
        mainGrid.layoutManager = GridLayoutManager(requireContext(),3, LinearLayoutManager.HORIZONTAL,false)
        passionGrid.layoutManager = GridLayoutManager(requireContext(),1, LinearLayoutManager.HORIZONTAL, false)
        missingNumbersGrid.layoutManager = GridLayoutManager(requireContext(),1,LinearLayoutManager.HORIZONTAL, false)
        phraseGrid.layoutManager = GridLayoutManager(requireContext(),1, LinearLayoutManager.HORIZONTAL, false)
        challengeGrid.layoutManager = GridLayoutManager(requireContext(),1, LinearLayoutManager.HORIZONTAL, false)
        aggingGrid.layoutManager = GridLayoutManager(requireContext(),1, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun clearData(){
        todayList.clear()
        mainList.clear()
        passionList.clear()
        missingNumbersList.clear()
        phraseList.clear()
        challengeList.clear()
        aggingList.clear()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMiniProfile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMiniProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}