package com.cthacademy.android

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.makeramen.roundedimageview.RoundedImageView
import java.io.ByteArrayInputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentShopBook.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentShopBook : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var avatar: String
    private lateinit var phone: String
    private lateinit var name: String
    private lateinit var date: String
    private var isAdmin : Boolean = false
    private var point : Int? = 0

    private lateinit var image1: String
    private lateinit var image2: String
    private lateinit var image3: String
    private var imageText: Int = 0
    private var bookPoint: Int = 0
    private lateinit var bookDescription: String

    private lateinit var mainImage: ImageView
    private lateinit var mImage1: RoundedImageView
    private lateinit var mImage2: RoundedImageView
    private lateinit var mImage3: RoundedImageView
    private lateinit var mTitle: ImageView
    private lateinit var mPoint: TextView
    private lateinit var mDescription: TextView
    private lateinit var backBtn: RelativeLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shop_book, container, false)

        getData()
        getId(view)
        mImage1.setImageBitmap(convertBase64ToImage(requireContext(),image1))
        mImage2.setImageBitmap(convertBase64ToImage(requireContext(),image2))
        mImage3.setImageBitmap(convertBase64ToImage(requireContext(),image3))

        updateMainImage(requireContext(),image1,mImage1)

        mImage1.setOnClickListener {
            updateMainImage(requireContext(),image1,mImage1)
            mImage2.borderColor = Color.WHITE
            mImage3.borderColor = Color.WHITE
        }
        mImage2.setOnClickListener {
            updateMainImage(requireContext(),image2,mImage2)
            mImage1.borderColor = Color.WHITE
            mImage3.borderColor = Color.WHITE
        }
        mImage3.setOnClickListener {
            updateMainImage(requireContext(),image3,mImage3)
            mImage1.borderColor = Color.WHITE
            mImage2.borderColor = Color.WHITE
        }

        mTitle.setImageResource(imageText)
        mPoint.text = "$bookPoint P"
        mDescription.text = "$bookDescription"
        backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        return view
    }

    private fun updateMainImage(context: Context, str: String, img: RoundedImageView){
        mainImage.setImageBitmap(convertBase64ToImage(context,str))
        mainImage.scaleType = ImageView.ScaleType.CENTER_CROP
        img.borderColor = Color.parseColor("#16E324")

    }
    private fun convertBase64ToImage(context: Context, base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        return bitmap
    }


    private fun getId(view: View) {
        mainImage = view.findViewById(R.id.Shop_Book_Image)
        mImage1 = view.findViewById(R.id.Shop_Book_Image_1)
        mImage2 = view.findViewById(R.id.Shop_Book_Image_2)
        mImage3 = view.findViewById(R.id.Shop_Book_Image_3)
        mTitle = view.findViewById(R.id.Shop_Book_Image_Text)
        mPoint = view.findViewById(R.id.Shop_Book_Point)
        mDescription = view.findViewById(R.id.Shop_Book_Description)

        backBtn = view.findViewById(R.id.BackBtn)
    }

    private fun getData() {
        arguments.let { bundle ->
            // Retrieve data from the bundle
            avatar = bundle?.getString("avatar").toString()
            phone = bundle?.getString("phone").toString()
            name = bundle?.getString("name").toString()
            date = bundle?.getString("date").toString()
            point = bundle?.getInt("point")
            image1 = bundle?.getString("image1").toString()
            image2 = bundle?.getString("image2").toString()
            image3 = bundle?.getString("image3").toString()
            imageText = bundle?.getInt("imageText")!!
            bookPoint = bundle.getLong("bookPoint").toInt()
            bookDescription = bundle?.getString("bookDescription").toString()
            point = bundle?.getInt("point")
            isAdmin = bundle?.getBoolean("isAdmin", false) ?: false

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentShopBook.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentShopBook().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}