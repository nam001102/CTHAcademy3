package com.cthacademy.android

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginRevievedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginRevievedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var et1: EditText
    private lateinit var et2: EditText
    private lateinit var et3: EditText
    private lateinit var et4: EditText
    private lateinit var et5: EditText
    private lateinit var et6: EditText

    private lateinit var loading: CircularProgressIndicator
    private lateinit var errorTxt: TextView
    private lateinit var DalTxt: TextView
    private lateinit var resendBtn: Button

    private lateinit var phoneNumber: String
    private lateinit var password: String
    private lateinit var name: String
    private lateinit var date: String
    private lateinit var mforceResendingToken: PhoneAuthProvider.ForceResendingToken
    private var verificationId: String =""
    private var isForgot = false

    private var countDownTimer: CountDownTimer? = null


    private lateinit var auth: FirebaseAuth
//    private var storedVerificationId: String? = ""
//    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
//    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

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
        val view = inflater.inflate(R.layout.fragment_login_revieved, container, false)
        getDataBundle()

        startPhoneNumberVerification(phoneNumber,password,name,date)

        loading = view.findViewById(R.id.LoadingIndicator)
        errorTxt = view.findViewById(R.id.ErrorTxt)
        DalTxt = view.findViewById(R.id.DalTxt)
        DalTxt.text = "Chúng tôi đã gửi mã xác nhận đến số điện thoại ${phoneNumber.replace("+84","0")}"

        et1 = view.findViewById(R.id.digit1)
        et2 = view.findViewById(R.id.digit2)
        et3 = view.findViewById(R.id.digit3)
        et4 = view.findViewById(R.id.digit4)
        et5 = view.findViewById(R.id.digit5)
        et6 = view.findViewById(R.id.digit6)

        et1.addTextChangedListener(PinCodeTextWatcher(et2))
        et2.addTextChangedListener(PinCodeTextWatcher(et3))
        et3.addTextChangedListener(PinCodeTextWatcher(et4))
        et4.addTextChangedListener(PinCodeTextWatcher(et5))
        et5.addTextChangedListener(PinCodeTextWatcher(et6))

        et1.setOnKeyListener(pinCodeKeyListener)
        et2.setOnKeyListener(pinCodeKeyListener)
        et3.setOnKeyListener(pinCodeKeyListener)
        et4.setOnKeyListener(pinCodeKeyListener)
        et5.setOnKeyListener(pinCodeKeyListener)
        et6.setOnKeyListener(pinCodeKeyListener)

        resendBtn = view.findViewById(R.id.resendbtn)
        resendBtn.isEnabled = false
        auth = Firebase.auth
        view.findViewById<Button>(R.id.loginbtn).setOnClickListener {
            val strOtp = et1.text.toString() +
                    et2.text.toString() +
                    et3.text.toString() +
                    et4.text.toString() +
                    et5.text.toString() +
                    et6.text.toString()
            if(strOtp.isEmpty()){
                errorTxt.text = "Mã không được để trống."
                et1.requestFocus()
                return@setOnClickListener
            }
            loading.visibility = View.VISIBLE
            onClickSendOtp(strOtp)
        }

        resendBtn.setOnClickListener {
            resendBtn.isEnabled = false
            startCountdownTimer(60)
            onClickReSendOtp()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopCountdownTimer() // Stop the countdown timer when the view is destroyed
    }

    private fun startCountdownTimer(seconds: Int) {
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                // Calculate remaining seconds and display in text view
                val remainingSeconds = millisUntilFinished / 1000
                resendBtn.text =
                    getString(R.string.countdown_timer_text, remainingSeconds) + getString(R.string.countdown_timer_finished_text)
            }

            override fun onFinish() {
                // Countdown timer finished
                resendBtn.text = getString(R.string.countdown_timer_finished_text)
                resendBtn.isEnabled = true
            }
        }.start()
    }

    private fun stopCountdownTimer() {
        countDownTimer?.cancel()
    }

    private val pinCodeKeyListener = object : View.OnKeyListener {
        override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                // Handle backspace key press
                when (view) {
                    et6 -> {
                        // Clear the last EditText view and move the focus to the previous one
                        et6.text.clear()
                        et5.requestFocus()
                        return true
                    }
                    et5 -> {
                        // Clear the last EditText view and move the focus to the previous one
                        et5.text.clear()
                        et4.requestFocus()
                        return true
                    }
                    et4 -> {
                        // Clear the last EditText view and move the focus to the previous one
                        et4.text.clear()
                        et3.requestFocus()
                        return true
                    }
                    et3 -> {
                        // Clear the last EditText view and move the focus to the previous one
                        et3.text.clear()
                        et2.requestFocus()
                        return true
                    }
                    et2 -> {
                        // Clear the last EditText view and move the focus to the previous one
                        et2.text.clear()
                        et1.requestFocus()
                        return true
                    }
                    else -> {
                        // If the first EditText view is in focus, do nothing
                        return false
                    }
                }
            }
            return false
        }
    }

    private fun onClickSendOtp(strOtp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, strOtp)
        signInWithPhoneAuthCredential(credential)
    }


    private fun onClickReSendOtp() {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setForceResendingToken(mforceResendingToken)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(requireContext(), "Lỗi Xác Nhận", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(
                    VerificationId: String,
                    ForceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(VerificationId, ForceResendingToken)
                    verificationId = VerificationId
                    mforceResendingToken = ForceResendingToken
                    startCountdownTimer(60)
                }

            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]

    }

    private fun getDataBundle() {
        val bundle = this.arguments
        if (bundle != null) {
            phoneNumber = bundle.getString("phone", "")
            password = bundle.getString("password", "")
            name = bundle.getString("name", "")
            date = bundle.getString("date", "")
            isForgot = bundle.getBoolean("isForgot", false)

        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String, Password : String, Name : String, Date : String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    loading.visibility = View.GONE
                    Toast.makeText(requireContext(),p0.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificationId = p0
                    mforceResendingToken = p1
                    startCountdownTimer(60)
                }

            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    if (!isForgot){
                        gotoMainActivity(user?.phoneNumber)
                    }else{
                        gotoReNewPassword(user?.phoneNumber)
                    }

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        loading.visibility = View.GONE
                        errorTxt.text = "Mã Otp sai"
                    }
                    // Update UI
                }
            }
    }

    private fun gotoMainActivity(phoneNumber: String?) {

        if (phoneNumber != null) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword("$phoneNumber@cth.coach", phoneNumber)
        }
        val coreInfo = CaculateNumberology(name, date)
        val db = FirebaseFirestore.getInstance()
        // create a reference to the document for the current user
        val userDocRef = phoneNumber?.let { db.collection("users").document(it)}
        val statsDocRef = userDocRef?.collection("Stats")?.document("State")
        val cardNum = Random.nextInt(1, 5)
        val data = hashMapOf(
            "name" to name,
            "password" to password,
            "date" to date,
            "point" to 0,
            "avatar" to "",
            "picture" to "",
            "cardNum" to cardNum
        )
        Log.d(TAG,name)
        Log.d(TAG,date)
        val stats = hashMapOf(
            "Life" to true,
            "Destiny" to false,
            "Connection" to false,
            "Growth" to false,
            "Soul" to false,
            "Personality" to false,
            "Connecting" to false,
            "Balance" to false,
            "RationalThinking" to false,
            "MentalPower" to false,
            "DayOfBirth" to false,
            "Passion" to false,
            "MissingNumbers" to false,
            "PersonalYear" to false,
            "PersonalMonth" to false,
            "PersonalDay" to false,
            "Phrase" to false,
            "Challange" to false,
            "Agging" to false
        )

        userDocRef?.set(data)
        statsDocRef?.set(stats)
        val avatar =""
        val picture =""
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            putExtra("coreInfo", coreInfo)
            putExtra("name", name)
            putExtra("date", date)
            putExtra("avatar", avatar)
            putExtra("picture", picture)
            putExtra("phone", phoneNumber.toString())
        }
        startActivity(intent)
    }
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginRevievedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginRevievedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private inner class PinCodeTextWatcher(private val nextEditText: EditText?) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not needed
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Not needed
        }

        override fun afterTextChanged(s: Editable?) {
            if (s?.length == 1 && nextEditText != null) {
                nextEditText.requestFocus()
            }
        }
    }

}
