package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity

object ReminderNotificationHelper {
    private const val CHANNEL_ID = "chemarena_study_channel"
    private const val CHANNEL_NAME = "ChemArena Study Reminders"
    private const val CHANNEL_DESC = "Daily reminders and study streaks for Chemistry exam prep"
    private const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showStudyReminder(context: Context, streak: Int = 1) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val titles = listOf(
            "🧪 Chemistry Lab is Open!",
            "⚡ Keep Your ${streak}-Day Study Streak Alive!",
            "🔬 Ready for a Speed Chemistry Battle?",
            "🧠 Quick QCAA Chemistry Practice Awaits!"
        )
        val bodies = listOf(
            "Test your knowledge on Equilibrium & Acids with a quick 5-question round.",
            "Complete today's chemistry quiz to earn mastery points and retain your streak.",
            "Challenge AI Bots or jump into a Buzzer Blitz arena right now!",
            "Explore past QCAA exam questions and solidify your Units 3 & 4 concepts."
        )

        val title = titles.random()
        val body = bodies.random()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
