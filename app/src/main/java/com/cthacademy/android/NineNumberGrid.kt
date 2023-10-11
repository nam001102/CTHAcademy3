package com.cthacademy.android

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cthacademy.android.adapter.GridNumberAdapter
import com.google.firebase.firestore.FirebaseFirestore

class NineNumberGrid : AppCompatActivity() {
    private lateinit var gridView : GridView
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.layout_9_numbers)


//        gridView = findViewById(R.id.Grid_number)
//        textView = findViewById(R.id.Grid_text)

        val Data = intent.getIntArrayExtra("data")
        val text = intent.getStringExtra("txt")

        val isLesson = intent.getBooleanExtra("isLesson", false)
        if (Data != null) {
            gridView.adapter = GridNumberAdapter(Data.toList())
            val numbers = Data.toList()
            gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                // Handle item click here
                val number = numbers[position]
                if (isLesson){
                    isLesson(number,numbers)
                }
                Toast.makeText(this, "Clicked number: $number", Toast.LENGTH_SHORT).show()
            }
            textView.text = text
        }
    }
    private fun isLesson(number:Int,data:List<Int>){
        val db = FirebaseFirestore.getInstance()

        val userDocRef = db.collection("Stats").document("MissingNumbers")
        val statsDocRef = userDocRef.collection("ID").document("1")

        statsDocRef.get().addOnSuccessListener { i ->
            if (i != null && i.exists()){
//                var text = ""
//                if (data.isEmpty()){
//                    text = "Chúc mừng bạn, bạn không thiếu 1 con số nào"
//                }else{
//                    text = "Hãy chọn ra con số mà bạn muốn bồi bổ thêm cho bản thân"
//                }
                val phone = intent.getStringExtra("phone")
                val weakpoints = i.getString("WeakPoints")
                val solutions = i.getString("Solutions")
//                val intent = Intent(this, ItemViewer::class.java).apply {
//                    putExtra("phone", phone)
//                    putExtra("number", number)
//                    putExtra("title", "Số thiếu")
//                    putExtra("item1", "Điểm Yếu")
//                    putExtra("item2", "Giải Pháp")
//                    putExtra("item1_txt", weakpoints)
//                    putExtra("item2_txt", solutions)
//                    putExtra("item_lesson", true)
//                }
//                startActivity(intent)
            }
        }
    }
}