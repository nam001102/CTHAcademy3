package com.cthacademy.android.navicationBar

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.cthacademy.android.FragmentLessonVideos
import com.cthacademy.android.R
import com.cthacademy.android.adapter.MissingNumberAdapter
import com.cthacademy.android.custom.VerticalSpacingItemDecoration
import jp.wasabeef.glide.transformations.BlurTransformation

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLesson.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLesson : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
    private var onClickAdmin : Boolean = false
    private var number : Int = 0

    private lateinit var videoBtn : CardView
    private lateinit var leftBarMissingNumber : RecyclerView
    private lateinit var leftBarMissingNumberAdapter: MissingNumberAdapter
    private var documentList = mutableListOf<Int>()

    private lateinit var missingNumberImage: ImageView
    private lateinit var missingNumberImageBg: ImageView

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
        val view = inflater.inflate(R.layout.fragment_lesson, container, false)
        getData()
        missingNumberImage = view.findViewById(R.id.missingnumber_number_image)
        missingNumberImageBg = view.findViewById(R.id.missingnumber_number_image_bg)

        leftBarMissingNumber = view.findViewById(R.id.missingnumber_grid)
        videoBtn = view.findViewById(R.id.missingnumber_number_video)

        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.vertical_spacing)
        leftBarMissingNumber.addItemDecoration(VerticalSpacingItemDecoration(verticalSpacing))
        leftBarMissingNumber.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val missingNumberTemp: List<Int> = if (isAdmin){
            listOf(0,1,2,3,4,5,6,7,8,9)
        }else{
            missingNumbersArray.ifEmpty {
                listOf(0)
            }

        }

        documentList.addAll(missingNumberTemp)
        missingNumberImage.setImageResource(firstNumberInList(missingNumberTemp.first()))
        missingNumberImageBg.setImageResource(firstNumberInListBackground(missingNumberTemp.first()))
        leftBarMissingNumberAdapter = MissingNumberAdapter(documentList,isAdmin)
        leftBarMissingNumber.adapter = leftBarMissingNumberAdapter

        leftBarMissingNumberAdapter.setButtonClickListener(object : MissingNumberAdapter.ButtonClickListener {
            override fun onButtonClick(position: Int) {
                val clickedItem = documentList[position]
                missingNumberImage.setImageResource(firstNumberInList(clickedItem))
                missingNumberImageBg.setImageResource(firstNumberInListBackground(clickedItem))

                number = clickedItem
                val bitmap = BitmapFactory.decodeResource(resources, firstNumberInListBackground(clickedItem))

                val blurredBitmap = blurBitmap(bitmap, 20f)

                Glide.with(requireContext())
                    .asBitmap()
                    .load(blurredBitmap)
                    .apply(RequestOptions().transform(BlurTransformation(30, 3)))
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(missingNumberImageBg)
            }

            override fun onAdminButtonClick() {
                val fragmentLessonVideos = FragmentLessonVideos()
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.Main_fragment, fragmentLessonVideos)
                fragmentTransaction?.addToBackStack(null)
                fragmentTransaction?.commit()
                onClickAdmin = true
                fragmentLessonVideos.arguments = putDataToFragment()
            }
        })

        videoBtn.setOnClickListener {
            val fragmentLessonVideos = FragmentLessonVideos()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.Main_fragment, fragmentLessonVideos)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
            fragmentLessonVideos.arguments = putDataToFragment()
        }

        return view
    }

    private fun getData(){
        arguments.let { bundle ->
            // Retrieve data from the bundle
            phone = bundle?.getString("phone").toString()
            name = bundle?.getString("name").toString()
            date = bundle?.getString("date").toString()
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

    private fun putDataToFragment(): Bundle {
        val bundle = Bundle()
        // Add data to the bundle
        bundle.putString("phone", phone) // Replace "key" and "value" with your actual data
        bundle.putInt("number", number) // Replace "key" and "value" with your actual data
        bundle.putBoolean("isAdmin", isAdmin)
        bundle.putBoolean("onClickAdmin", onClickAdmin)
        bundle.putString("name", name)
        bundle.putString("date", date)
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

    private fun firstNumberInList(number : Int) : Int {
        return when (number){
            0 -> R.drawable.ic_number_0_new
            1 -> R.drawable.ic_number_1_new
            2 -> R.drawable.ic_number_2_new
            3 -> R.drawable.ic_number_3_new
            4 -> R.drawable.ic_number_4_new
            5 -> R.drawable.ic_number_5_new
            6 -> R.drawable.ic_number_6_new
            7 -> R.drawable.ic_number_7_new
            8 -> R.drawable.ic_number_8_new
            9 -> R.drawable.ic_number_9_new
            else -> R.drawable.ic_number_0_new
        }
    }
    private fun firstNumberInListBackground(number : Int) : Int {
        return when (number){
            0 -> R.drawable.ic_number_0_new_bg
            1 -> R.drawable.ic_number_1_new_bg
            2 -> R.drawable.ic_number_2_new_bg
            3 -> R.drawable.ic_number_3_new_bg
            4 -> R.drawable.ic_number_4_new_bg
            5 -> R.drawable.ic_number_5_new_bg
            6 -> R.drawable.ic_number_6_new_bg
            7 -> R.drawable.ic_number_7_new_bg
            8 -> R.drawable.ic_number_8_new_bg
            9 -> R.drawable.ic_number_9_new_bg
            else -> R.drawable.ic_number_0_new_bg
        }
    }
    private fun blurBitmap(originalBitmap: Bitmap, blurRadius: Float): Bitmap {
        val paint = Paint().apply {
            color = Color.parseColor("#0800FF")
            isAntiAlias = true
            maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
        }

        val blurredBitmap = Bitmap.createBitmap(originalBitmap.width, originalBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(blurredBitmap)
        canvas.drawBitmap(originalBitmap, 0f, 0f, paint)

        return blurredBitmap
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
}