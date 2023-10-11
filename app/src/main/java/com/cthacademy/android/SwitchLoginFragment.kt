package com.cthacademy.android

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SwitchLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SwitchLoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var switchButton : Button
    private lateinit var switchText : ImageView
    private var switchLogin : Boolean = true

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
        val view = inflater.inflate(R.layout.switch_login_register, container, false)

        switchButton = view.findViewById(R.id.Switch_login_btn)
        switchText = view.findViewById(R.id.Switch_login_Title)

        val loginFragment = LoginFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.Switch_login_frame, loginFragment)
        transaction.commit()
        switchButton.setOnClickListener {
            switchLogin = !switchLogin
            when(switchLogin){
                true->{
                    switchButton.text = "Đăng Ký"
                    switchText.setImageResource(R.drawable.ic_login_text)
                    gotoLoginFragment()
                }
                false->{
                    switchButton.text = "Đăng Nhập"
                    switchText.setImageResource(R.drawable.ic_register_text)
                    gotoRegisterFragment()
                }
            }
        }

        return view
    }

    private fun gotoLoginFragment() {
        val loginFragment = LoginFragment()
        if (isAdded) {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.Switch_login_frame, loginFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } else {
            // Fragment is not attached, handle the error or show a message
            Log.e(TAG, "SwitchLoginFragment is not attached to an activity")
        }
    }
    private fun gotoRegisterFragment() {
        val registerFragment = RegisterFragment()
        if (isAdded) {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.Switch_login_frame, registerFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } else {
            // Fragment is not attached, handle the error or show a message
            Log.e(TAG, "SwitchLoginFragment is not attached to an activity")
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