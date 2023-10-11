package com.cthacademy.android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.cthacademy.android.adapter.GridShopAdapter
import com.cthacademy.android.custom.HorizontalSpacingItemDecoration
import com.cthacademy.android.modal.BookModal
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayInputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentShop.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentShop : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var backBtn : RelativeLayout

    private lateinit var shopGrid : RecyclerView
    private lateinit var shopGridAdapter : GridShopAdapter
    private var books = mutableListOf<BookModal>()

    private lateinit var avatar: String
    private lateinit var phone: String
    private lateinit var name: String
    private lateinit var date: String

    private lateinit var userAvatar: ImageView
    private lateinit var userPoint: TextView
    private lateinit var userName: TextView
    private var isAdmin : Boolean = false
    private var point : Int? = 0

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
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        books.clear()
        getData()
        val bookImageTextList = listOf(R.drawable.book_1,R.drawable.book_2,R.drawable.book_3,R.drawable.book_4)
        val bookImageList = listOf(R.drawable.book_1_img,R.drawable.book_2_img,R.drawable.book_3_img,R.drawable.book_4_img)

        shopGrid = view.findViewById(R.id.Shop_grid)
        backBtn = view.findViewById(R.id.BackBtn)
        userAvatar = view.findViewById(R.id.Shop_profile_avatar)
        userPoint = view.findViewById(R.id.Shop_profile_point)
        userName = view.findViewById(R.id.Shop_profile_name)

        userAvatar.setImageBitmap(convertBase64ToImage(requireContext(),avatar))
        userPoint.text = point.toString()
        userName.text = name

        val db = FirebaseFirestore.getInstance()
        val bookShop = db.collection("Shop").document("books")
        bookShop.collection("ID").limit(4).get().addOnSuccessListener { snapshot ->
            for (document in snapshot.documents) {
                val documentId = document.id
                val image1 = document.getString("image1") ?: ""
                val image2 = document.getString("image2") ?: ""
                val image3 = document.getString("image3") ?: ""
                val title = document.getString("title") ?: ""
                val description = document.getString("description") ?: ""
                val point = document.getLong("point") ?: 0
                val bookImage =
                    when(documentId.toInt()){
                        1->{
                            R.drawable.book_2_img
                        }
                        2->{
                            R.drawable.book_3_img
                        }
                        3->{
                            R.drawable.book_1_img
                        }
                        4->{
                            R.drawable.book_4_img
                        }
                        else -> {0}
                    }
                val bookTitle =
                    when(title.toInt()){
                        1->{
                            R.drawable.book_2
                        }
                        2->{
                            R.drawable.book_3
                        }
                        3->{
                            R.drawable.book_1
                        }
                        4->{
                            R.drawable.book_4
                        }
                        else -> {0}
                    }
                val bookDescription =
                    when(description.toInt()){
                        1->{
                            R.drawable.book_2
                        }
                        2->{
                            R.drawable.book_3
                        }
                        3->{
                            R.drawable.book_1
                        }
                        4->{
                            R.drawable.book_4
                        }
                        else -> {0}
                    }


                val bookModal = BookModal(bookImage,image1,image2,image3, bookTitle,description, point)
                books.add(bookModal)
            }


            shopGridAdapter = GridShopAdapter(books)
            val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.horizontal_spacing)
            shopGrid.addItemDecoration(HorizontalSpacingItemDecoration(horizontalSpacing))
            shopGrid.adapter = shopGridAdapter
            shopGrid.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(shopGrid)

            shopGridAdapter.setBookClickListener(object : GridShopAdapter.BookClickListener{
                override fun onBookClick(position: Int) {
                    val data = books[position]
                    val bookImage = data.Image
                    val image1 = data.Image1
                    val image2 = data.Image2
                    val image3 = data.Image3
                    val title = data.ImageText
                    val bookPoint = data.Point
                    val bookDescription = data.Description
                    val fragmentShopBook = FragmentShopBook()
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentShopBook.arguments = putDataToFragment(bookImage,image1,image2,image3,title,bookPoint,bookDescription)
                    fragmentTransaction?.replace(R.id.Main_fragment, fragmentShopBook)
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                }

            })
        }





        backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }


        return view
    }

    private fun convertBase64ToImage(context: Context, base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        return bitmap
    }

    private fun  putDataToFragment(image: Int,image1: String,image2: String,image3: String, imageText: Int, bookPoint: Long, bookDescription: String): Bundle{
        val bundle = Bundle()
        // Add data to the bundle
        bundle.putString("avatar", avatar)
        bundle.putInt("image", image)
        bundle.putString("image1", image1)
        bundle.putString("image2", image2)
        bundle.putString("image3", image3)
        bundle.putInt("imageText", imageText)
        bundle.putLong("bookPoint", bookPoint)
        bundle.putString("bookDescription", bookDescription)
        bundle.putString("phone", phone)
        bundle.putString("name", name)
        bundle.putString("date", date)
        point?.let { bundle.putInt("point", it) }
        bundle.putBoolean("isAdmin", isAdmin)

        return bundle
    }

    private fun getData() {
        arguments.let { bundle ->
            // Retrieve data from the bundle
            avatar = bundle?.getString("avatar").toString()
            phone = bundle?.getString("phone").toString()
            name = bundle?.getString("name").toString()
            date = bundle?.getString("date").toString()
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
         * @return A new instance of fragment FragmentShop.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentShop().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}