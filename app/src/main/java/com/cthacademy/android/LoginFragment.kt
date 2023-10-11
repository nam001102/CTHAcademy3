package com.cthacademy.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cthacademy.android.modal.UserData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import getDecryptedPassword


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var DalInput : TextInputEditText
    private lateinit var DalLayout : TextInputLayout
    private lateinit var DalpassInput : TextInputEditText
    private lateinit var DalpassLayout : TextInputLayout
    private lateinit var LoginFailed : TextView
    private lateinit var ForgotPassword : TextView
    private lateinit var Login_Password_Visibility : ImageView
    private lateinit var loginbtn : Button

    private var isForgot = false

    private lateinit var loading : RelativeLayout

    private var auth = FirebaseAuth.getInstance()
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
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        loading = view.findViewById(R.id.Loading)

        DalInput = view.findViewById(R.id.DalInput)
        DalLayout = view.findViewById(R.id.DalLayout)
        DalpassInput = view.findViewById(R.id.DalpassInput)
        DalpassLayout = view.findViewById(R.id.DalpassLayout)
        LoginFailed = view.findViewById(R.id.LoginFailed)
        ForgotPassword = view.findViewById(R.id.ForgotPassword)
        Login_Password_Visibility = view.findViewById(R.id.Login_Password_Visibility)
        loginbtn = view.findViewById(R.id.loginbtn)
        var isVisible = false
        view.findViewById<ImageView>(R.id.Login_Password_Visibility).setOnClickListener{
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

        view.findViewById<TextView>(R.id.ForgotPassword).setOnClickListener{
            isForgot = !isForgot
            val DalInput = DalInput.text.toString().trim()
            if (DalInput.isEmpty()||DalInput.length<10){
                LoginFailed.text = "Vui lòng nhập đúng số điện thoại"
                LoginFailed.visibility = View.VISIBLE
                DalLayout.setBackgroundResource(R.drawable.round_corner_white_error)
                DalLayout.error = null
                DalLayout.requestFocus()
                isForgot = false
                return@setOnClickListener
            }
            if (isForgot){
                gotoLoginRecieveFragment(DalInput,isForgot)
                loading.visibility = View.VISIBLE
            }

        }

        DalpassInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginbtn.performClick() // Trigger button click
                return@setOnEditorActionListener true
            }
            false
        }


        var isLoggingin = false
        view.findViewById<Button>(R.id.loginbtn).setOnClickListener{

            isLoggingin = !isLoggingin
            val DalInput = DalInput.text.toString().trim()
            val DalpassInput = DalpassInput.text.toString().trim()

            if (DalInput.isEmpty()||DalInput.length<10){
                DalLayout.error = null
                DalLayout.errorContentDescription = "Số điện thoại không hợp lệ"
                DalLayout.requestFocus()
                isLoggingin = false
                return@setOnClickListener
            }
            if (DalpassInput.isEmpty()){
                DalpassLayout.errorContentDescription = "Mật khẩu không được để trống"
                DalpassLayout.error = null
                DalpassLayout.requestFocus()
                isLoggingin = false
                return@setOnClickListener
            }
            if (isLoggingin){
                loading.visibility = View.VISIBLE
                startLoggingIn(DalInput,DalpassInput)
            }else{
                loading.visibility = View.GONE
            }

        }

        return view
    }

    private fun startLoggingIn(phoneNumber: String, Password : String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document("+84"+phoneNumber.substring(1))
//        val encrypt = encryptAndSavePassword(requireContext(),Password)
//        userRef.update("Password",encrypt)
        userRef.get().addOnSuccessListener { i->
            if (i != null && i.exists()){
                val password = i.getString("password")

                if(password != null){
                    val encryptDataArray = password.replace("{","").replace("}","").split(",").toTypedArray()
                    val encrypt = encryptDataArray.last()
                    val vector = encryptDataArray[0]
                    val key = encryptDataArray[1]

                    val decrypt = getDecryptedPassword(encrypt,key,vector)
                    if(decrypt == Password){
                        auth.signInWithEmailAndPassword("+84"+ phoneNumber.substring(1)+"@cth.coach","+84"+ phoneNumber.substring(1)).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Login successful
                                val user = FirebaseAuth.getInstance().currentUser
                                val uid = user?.uid
                                val data = hashMapOf(
                                    "uid" to uid
                                )
                                userRef.set(data, SetOptions.merge())
                                gotoMainActivity("+84"+ phoneNumber.substring(1))
                                val sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

                                // Get the SharedPreferences.Editor
                                val editor = sharedPreferences.edit()

                                // Store the login state
                                editor.putString("user","+84"+phoneNumber.substring(1))
                                editor.putBoolean("isLoggedIn", true)
                                editor.apply()

                                Log.d("user","$decrypt|$password")
                            } else {
                                // Login failed
                                Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                                Log.d(TAG,"Authentication failed.")
                            }

                        }
                    } else {
                        LoginFailed.text = "Đăng nhập thất bại, tài khoản hoặc mật khẩu sai."
                        loading.visibility = View.GONE
                        DalLayout.setBackgroundResource(R.drawable.round_corner_white_error)
                        DalpassLayout.setBackgroundResource(R.drawable.round_corner_white_error)
                    }
                }
            }
        }
    }
    private fun gotoLoginRecieveFragment(phoneNumber: String,forgot: Boolean) {
        val loginRevievedFragment = LoginRevievedFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.login_frame, loginRevievedFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        val bundle = Bundle()
        bundle.putString("phone", phoneNumber)
        bundle.putBoolean("isForgot", forgot)
        loginRevievedFragment.arguments = bundle

    }
//    private fun startPhoneNumberVerification(phoneNumber: String) {
//        // [START start_phone_auth]
//        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
//            .setPhoneNumber("+84${phoneNumber.substring(1)}")       // Phone number to verify
//            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//            .setActivity(requireActivity())                 // Activity (for callback binding)
//            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
//                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//                    signInWithPhoneAuthCredential(p0)
//                }
//
//                override fun onVerificationFailed(p0: FirebaseException) {
//                    Toast.makeText(requireContext(),"Lỗi Xác Nhận", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
//                    super.onCodeSent(p0, p1)
//                    gotoLoginRecieveFragment(phoneNumber,p0)
//                }
//
//            })          // OnVerificationStateChangedCallbacks
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//        // [END start_phone_auth]
//    }
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(ContentValues.TAG, "signInWithCredential:success")
//
//                    val user = task.result?.user
//                    if (!isForgot){
//                        gotoMainActivity(user?.phoneNumber)
//                    }else{
//                        gotoReNewPassword(user?.phoneNumber)
//                    }
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
    private fun gotoReNewPassword(phoneNumber: String?) {
        val reNewPasswordFragment = ReNewPasswordFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.login_frame, reNewPasswordFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        val bundle = Bundle()
        bundle.putString("phone", phoneNumber)
        reNewPasswordFragment.arguments = bundle
    }
    private fun gotoMainActivity(phoneNumber: String?) {

        val db = FirebaseFirestore.getInstance()

        // create a reference to the document for the current user
        val userDocRef = db.collection("users").document(phoneNumber.toString())

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
                val isAdmin = document.get("isAdmin") != null
                if (name != null) {
                    if (date != null) {
                        val coreInfo = CaculateNumberology(name,date)
                        val intent = Intent(activity, MainActivity::class.java).apply {
                            putExtra("coreInfo", coreInfo)
                            putExtra("userData", userData)
                            putExtra("name", name)
                            putExtra("date", date)
                            putExtra("phone", phoneNumber)
                            putExtra("isAdmin", isAdmin)
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