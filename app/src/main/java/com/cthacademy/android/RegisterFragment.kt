package com.cthacademy.android

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import encryptAndSavePassword
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var DalInput : TextInputEditText
    private lateinit var DalLayout : TextInputLayout
    private lateinit var DalpassInput : TextInputEditText
    private lateinit var DalpassLayout : TextInputLayout
    private lateinit var DalpassReInput : TextInputEditText
    private lateinit var DalpassReLayout : TextInputLayout
    private lateinit var DateInput : TextInputEditText
    private lateinit var DateLayout : TextInputLayout
    private lateinit var NameInput : TextInputEditText
    private lateinit var NameLayout : TextInputLayout
    private lateinit var Login_Password_Visibility : ImageView
    private lateinit var Login_Password_Re_Visibility : ImageView

    private lateinit var loading : RelativeLayout

    private lateinit var auth: FirebaseAuth
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
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        loading = view.findViewById(R.id.Loading)

        DalInput = view.findViewById(R.id.DalInput)
        DalLayout = view.findViewById(R.id.DalLayout)
        DalpassInput = view.findViewById(R.id.DalpassInput)
        DalpassLayout = view.findViewById(R.id.DalpassLayout)
        DalpassReInput = view.findViewById(R.id.DalpassReInput)
        DalpassReLayout = view.findViewById(R.id.DalpassReLayout)
        DateInput = view.findViewById(R.id.DateInput)
        DateLayout = view.findViewById(R.id.DateLayout)
        NameInput = view.findViewById(R.id.NameInput)
        NameLayout = view.findViewById(R.id.NameLayout)
        Login_Password_Visibility = view.findViewById(R.id.Login_Password_Visibility)
        Login_Password_Re_Visibility = view.findViewById(R.id.Login_Password_Re_Visibility)
        var isVisible = false
        view.findViewById<ImageView>(R.id.Login_Password_Visibility).setOnClickListener {
            isVisible = !isVisible
            if (isVisible) {
                // If the input type is currently visible, switch to hidden
                DalpassInput.transformationMethod = null
                Login_Password_Visibility.setImageResource(R.drawable.baseline_visibility_off_24)
            } else {
                // If the input type is currently hidden, switch to visible
                DalpassInput.transformationMethod = PasswordTransformationMethod()
                Login_Password_Visibility.setImageResource(R.drawable.baseline_visibility_24)
            }

            // Move cursor to the end of the text to ensure it stays visible after input type change
            DalpassInput.setSelection(DalpassInput.text?.length ?: 0)

        }
        var isReVisible = false

        view.findViewById<ImageView>(R.id.Login_Password_Re_Visibility).setOnClickListener{
            isReVisible = !isReVisible

            if (isReVisible) {
                // If the input type is currently visible, switch to hidden
                DalpassReInput.transformationMethod = null
                Login_Password_Re_Visibility.setImageResource(R.drawable.baseline_visibility_off_24)
            } else {
                // If the input type is currently hidden, switch to visible
                DalpassReInput.transformationMethod = PasswordTransformationMethod()
                Login_Password_Re_Visibility.setImageResource(R.drawable.baseline_visibility_24)
            }

            // Move cursor to the end of the text to ensure it stays visible after input type change
            DalpassReInput.setSelection(DalpassReInput.text?.length ?: 0)

        }


        view.findViewById<Button>(R.id.registerbtn).setOnClickListener{


            val DalInput = DalInput.text.toString().trim()
            val DalpassInput = DalpassInput.text.toString().trim()
            val DalpassReInput = DalpassReInput.text.toString().trim()
            val DateInput = DateInput.text.toString().trim()
            val NameInput = NameInput.text.toString().trim()
            if (DalInput.isEmpty()||DalInput.length<10){
                DalLayout.error = "Số điện thoại không hợp lệ"
                DalLayout.requestFocus()
                return@setOnClickListener
            }
            if (DalpassInput.isEmpty()){
                DalpassLayout.error = "Mật khẩu không được để trống"
                DalpassLayout.requestFocus()
                return@setOnClickListener
            }
            if (DalpassReInput.isEmpty()){
                DalpassReLayout.error = "Nhập lại mật khẩu không được để trống"
                DalpassReLayout.requestFocus()
                return@setOnClickListener
            }
            if (DalpassReInput != DalpassInput){
                DalpassReLayout.error = "Nhập lại mật khẩu không giống nhau"
                DalpassReLayout.requestFocus()
                return@setOnClickListener
            }
            if (DateInput.isEmpty()){
                DateLayout.error = "Ngày sinh không được để trống"
                return@setOnClickListener
            }
            if (NameInput.isEmpty()){
                NameLayout.error = "Tên không được để trống"
                NameLayout.requestFocus()
                return@setOnClickListener
            }
            auth = Firebase.auth
            val db = FirebaseFirestore.getInstance()
            val userDocRef = db.collection("users").document("+84${DalInput.substring(1)}")
            userDocRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Toast.makeText(context, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show()
                    } else {
                        loading.visibility = View.VISIBLE
                        startRegistering("+84${DalInput.substring(1)}", DalpassInput, NameInput, DateInput)
                    }
                }
            }

        }
        val editText = DateLayout.editText as? EditText
        editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    // Update the text field with the selected date
                    editText.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        return view
    }

    private fun startRegistering(phoneNumber: String, Password : String, Name : String, Date : String) {
        gotoLoginRecieveFragment(phoneNumber,Password,Name,Date)
    }
    private fun gotoLoginRecieveFragment(phoneNumber: String, Password : String, Name : String, Date : String) {
        val loginRevievedFragment = LoginRevievedFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.login_frame, loginRevievedFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        val encrypt = encryptAndSavePassword(Password,phoneNumber)
        val bundle = Bundle()
        bundle.putString("phone", phoneNumber)
        bundle.putString("password", encrypt)
        bundle.putString("name", Name)
        bundle.putString("date", Date)
        loginRevievedFragment.arguments = bundle

    }

//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(ContentValues.TAG, "signInWithCredential:success")
//
//                    val user = task.result?.user
//                    gotoMainActivity(user?.phoneNumber)
//                } else {
//                    // Sign in failed, display a message and update the UI
//                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                    }
//                    // Update UI
//                }
//            }
//    }
//    private fun gotoMainActivity(phoneNumber: String?) {
//        val intent = Intent(activity, MainActivity::class.java)
//        intent.putExtra("phone",phoneNumber)
//        startActivity(intent)
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginSendFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginSendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}