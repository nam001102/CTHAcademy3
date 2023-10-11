package com.cthacademy.android.navicationBar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.cthacademy.android.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.firestore.FirebaseFirestore
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.io.ByteArrayInputStream
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWallet.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWallet : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var point: Int? = 0
    private lateinit var avatar: String
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

    private lateinit var nameTitle : TextView
    private lateinit var dayTitle : TextView
    private lateinit var userAvatar : ImageView
    private lateinit var userName : TextView
    private lateinit var userPoint : TextView

    private var rewardedAd: RewardedAd? = null
    private final var TAG = "AdsActivity"

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
        }

    private lateinit var billingClient : BillingClient

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
        val view = inflater.inflate(R.layout.fragment_wallet, container, false)

        getData()
        updateView(view)

        MobileAds.initialize(requireContext()) {}
        loadAds()

        billingClient = BillingClient.newBuilder(requireContext())
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()


        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("users").document(phone)

        rewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                rewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                rewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }

        nameTitle.text ="Hi ${ name.split(" ").last() }"
        var partOfDay =""
        when(getCurrentPartOfDay()){
            0->{
                partOfDay = "Bạn ăn sáng chưa?"
            }
            1->{
                partOfDay = "Bạn nhớ ngủ trưa để đảm bảo tỉnh táo."
            }
            2->{
                partOfDay = "Chúc bạn làm việc đầy năng lượng."
            }
            3->{
                partOfDay = "Hôm nay bạn đã làm việc vất vả rồi."
            }
            4->{
                partOfDay = "Chúc bạn ngủ ngon."
            }
        }
        dayTitle.text = "$partOfDay"
        userAvatar.setImageBitmap(convertBase64ToImage(requireContext(),avatar))
        userName.text = name
        userPoint.text = point.toString()

        return view
    }
    private fun loadAds(){
        if (rewardedAd == null){
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(requireContext(),"ca-app-pub-9023634106327303/4051907253", adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d(TAG, it) }
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }
            })
        }
    }
    private fun showRewardedVideo() : Int {
        var callback = -1
        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad was dismissed.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null
                        loadAds()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d(TAG, "Ad failed to show.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.")
                        // Called when ad is dismissed.
                    }
                }

            rewardedAd?.show(
                requireActivity(),
                OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    point = point?.plus(rewardAmount)
                    callback = 1
                    Log.d("TAG", "User earned the reward.")
                }
            )
        }
        return callback
    }

    private fun getCurrentPartOfDay(): Int {
        val calendar = Calendar.getInstance()

        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 6..9 -> 0
            in 10..12 -> 1
            in 13..17 -> 2
            in 18..21 -> 3
            else -> 4
        }
    }
    private fun convertBase64ToImage(context: Context, base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(decodedBytes)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        return bitmap
    }

    private fun updateView(view: View){
        nameTitle = view.findViewById(R.id.wallet_profile_name_text)
        dayTitle = view.findViewById(R.id.wallet_day_text)
        userAvatar = view.findViewById(R.id.wallet_avatar)
        userName = view.findViewById(R.id.MainScreen_Fullname)
        userPoint = view.findViewById(R.id.MainScreen_Point)

    }
    private fun getData() {
        arguments.let { bundle ->
            // Retrieve data from the bundle
            avatar = bundle?.getString("avatar").toString()
            phone = bundle?.getString("phone").toString()
            point = bundle?.getInt("point")
            name = bundle?.getString("name").toString()
            date = bundle?.getString("date").toString()
            isAdmin = bundle?.getBoolean("isAdmin", false) ?: false
            passionArray = bundle?.getIntArray("passionArray")?.toList() ?: emptyList()
            missingNumbersArray =
                bundle?.getIntArray("missingNumbersArray")?.toList() ?: emptyList()
            personalArray = bundle?.getIntArray("personalArray")?.toList() ?: emptyList()
            statsNumberArray = bundle?.getIntArray("statsNumberArray")?.toList() ?: emptyList()
            statsTitleArray = bundle?.getStringArray("statsTitleArray")?.toList() ?: emptyList()
            statsPointArray = bundle?.getIntArray("statsPointArray")?.toList() ?: emptyList()
            statsLockArray = bundle?.getBooleanArray("statsLockArray")?.toList() ?: emptyList()
            statsIdArray = bundle?.getStringArray("statsIdArray")?.toList() ?: emptyList()

            Log.d("Debug", passionArray.toString())
            Log.d("Debug", missingNumbersArray.toString())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bottomBar: AnimatedBottomBar): FragmentMain {
            val fragment = FragmentMain()
            fragment.setBottomBar(bottomBar)
            return fragment
        }
    }
}