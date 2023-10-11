package com.cthacademy.android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cthacademy.android.modal.UserData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import encryptAndSavePassword

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReNewPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReNewPasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var DalpassInput : TextInputEditText
    private lateinit var DalpassLayout : TextInputLayout
    private lateinit var DalpassReInput : TextInputEditText
    private lateinit var DalpassReLayout : TextInputLayout
    private lateinit var renewbtn : Button
    private lateinit var phoneNumber: String

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
        val view = inflater.inflate(R.layout.fragment_re_new_password, container, false)
        DalpassInput = view.findViewById(R.id.DalpassInput)
        DalpassReInput = view.findViewById(R.id.DalpassReInput)
        DalpassLayout = view.findViewById(R.id.DalpassLayout)
        DalpassReLayout = view.findViewById(R.id.DalpassReLayout)
        renewbtn = view.findViewById(R.id.renewbtn)

        getDataBundle()

        renewbtn.setOnClickListener{
            val DalpassInput = DalpassInput.text.toString().trim()
            val DalpassReInput = DalpassReInput.text.toString().trim()
            if (DalpassInput!=DalpassReInput){
                DalpassLayout.setBackgroundResource(R.drawable.round_corner_white_error)
                DalpassReLayout.setBackgroundResource(R.drawable.round_corner_white_error)
                DalpassLayout.error = null
                DalpassReLayout.error = null
                DalpassLayout.requestFocus()
                return@setOnClickListener
            }
            if (DalpassInput.isEmpty()){
                DalpassLayout.setBackgroundResource(R.drawable.round_corner_white_error)
                DalpassLayout.error = null
                DalpassLayout.requestFocus()
                return@setOnClickListener
            }
            if (DalpassReInput.isEmpty()){
                DalpassReLayout.setBackgroundResource(R.drawable.round_corner_white_error)
                DalpassReLayout.error = null
                DalpassReLayout.requestFocus()
                return@setOnClickListener
            }
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(phoneNumber)
            val encrypt = encryptAndSavePassword(DalpassInput,phoneNumber)
            userRef.update("password",encrypt)
            gotoMainActivity(phoneNumber)
        }
        return view
    }
    private fun getDataBundle() {
        val bundle = this.arguments
        if (bundle != null) {
            phoneNumber = bundle.getString("phone", "")
        }
    }
    private fun gotoMainActivity(phone: String) {

        val db = FirebaseFirestore.getInstance()


        // create a reference to the document for the current user
        val userDocRef = phone.let { db.collection("users").document(it) }

        // read the user's data from the Firestore document
        userDocRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // if the document exists, get the user's data as a custom data class
                val name = document.getString("name")
                val date = document.getString("date")
                val point = document.getLong("point")
                val userData = UserData(
                    phone = phoneNumber,
                    name = name,
                    date = date,
                    point = point
                )
                if (name != null) {
                    if (date != null) {
                        val coreInfo = CaculateNumberology(name,date)
                        val intent = Intent(activity, MainActivity::class.java).apply {
                            putExtra("coreInfo", coreInfo)
                            putExtra("userData", userData)
                            putExtra("name", name)
                            putExtra("date", date)
                            putExtra("phone", phoneNumber)
                        }
                        startActivity(intent)
                    }
                }
            } else {
                // if the document does not exist, go to MainActivity to create Data
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("phone",phoneNumber)
                startActivity(intent)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReNewPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReNewPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}