package infix.imrankst1221.codecanyon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.onesignal.OneSignal
import infix.imrankst1221.codecanyon.setting.AppDataInstance
import infix.imrankst1221.codecanyon.ui.home.HomeActivity
import infix.imrankst1221.codecanyon.ui.splash.SplashActivity
import infix.imrankst1221.rocket.library.setting.ConfigureRocketWeb
import infix.imrankst1221.rocket.library.utility.PreferenceUtils
import infix.imrankst1221.rocket.library.utility.Constants
import infix.imrankst1221.rocket.library.utility.UtilMethods

class MainActivity : AppCompatActivity() {
    private val TAG: String = "---MainActivity"

    private lateinit var mContext: Context
    private lateinit var configureRocketWeb: ConfigureRocketWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        mContext = this

        // inti and configure
        initSetup()

        // go to next screen to start app
        goNextActivity()
    }

    // init and configure
    private fun initSetup(){
        // custom crash list
        FirebaseApp.initializeApp(this)
        AppDataInstance.getINSTANCE(mContext)
        PreferenceUtils.getInstance().initPreferences(mContext)

        configureRocketWeb  = ConfigureRocketWeb(mContext)
        configureRocketWeb.readConfigureData("rocket_web.io")

        AppDataInstance.navigationMenus =  configureRocketWeb.getMenuData("rocket_web.io")
        if(configureRocketWeb.configureData.themeColor == Constants.THEME_PRIMARY) {
            PreferenceUtils.getInstance().editIntegerValue(Constants.KEY_COLOR_PRIMARY, R.color.colorPrimary)
            PreferenceUtils.getInstance().editIntegerValue(Constants.KEY_COLOR_PRIMARY_DARK, R.color.colorPrimaryDark)
        }

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init()
    }

    // goto next screen
    private fun goNextActivity(){
        if (PreferenceUtils.getInstance().getBooleanValue(Constants.KEY_SPLASH_SCREEN_ACTIVE, true)) {
            startActivity(Intent(this@MainActivity, SplashActivity::class.java))
        }else{
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }
        finish()
    }
}
