package infix.imrankst1221.codecanyon.setting

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.onesignal.OSNotification
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import infix.imrankst1221.codecanyon.MainActivity

class OnesignalMessagingService: Application() {
    val TAG = "---Onesignal"
    lateinit var mContext: Context

    override fun onCreate() {
        super.onCreate()
        mContext = this
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(NotificationHandler())
                .autoPromptLocation(true)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(NotificationReceivedHandler())
                .init()

        OneSignal.idsAvailable { userId, registrationId -> Log.d("", ""+userId) }
    }


    inner class NotificationHandler : OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        override fun notificationOpened(result: OSNotificationOpenResult) {
            val actionType = result.action.type
            val data = result.notification.payload.additionalData
            val customKey: String?

            if (data != null) {
                customKey = data.optString("url", null)
                if (customKey != null) {
                    AppDataInstance.getINSTANCE(mContext)
                    AppDataInstance.notificationUrl = customKey
                    AppDataInstance.isOpenOutside = data.optBoolean("is_open_outside", false)
                    Log.i(TAG, "Link set with value: $customKey")
                }
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i(TAG, "Button pressed with id: " + result.action.actionID)

            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private inner class NotificationReceivedHandler : OneSignal.NotificationReceivedHandler {
        override fun notificationReceived(notification: OSNotification) {
            // receive a notification
        }
    }
}