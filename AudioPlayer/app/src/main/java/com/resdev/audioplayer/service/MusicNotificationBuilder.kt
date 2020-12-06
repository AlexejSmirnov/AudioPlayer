package com.resdev.audioplayer.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.resdev.audioplayer.R
import com.resdev.audioplayer.model.items.SongItem
import com.resdev.audioplayer.view.activity.MainActivity
import com.resdev.audioplayer.MyApplication

class MusicNotificationBuilder{
    lateinit var notificationBuilder: NotificationCompat.Builder
    var context = MyApplication.getApplicationContext()

    fun createNotification(songItem: SongItem, packageName: String): Notification {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0, notificationIntent, 0
        )
        notificationBuilder =  NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
            .setCustomBigContentView(createExpandedNotificationRemoteView(songItem.title, songItem.author, songItem.getCover(), packageName))
            .setCustomContentView(createSmallNotificationRemoteView(songItem.title, songItem.author, songItem.getCover(), packageName))
            .setContentIntent(pendingIntent)
            .setStyle(androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
        return notificationBuilder.build()

    }


    private fun createSmallNotificationRemoteView(title:String, artist:String, bitmap: Bitmap, packageName: String): RemoteViews{
        var smallView = RemoteViews(packageName, R.layout.song_small_notification_layout)
        smallView.setImageViewBitmap(R.id.small_notification_cover_view, bitmap)
        smallView.setTextViewText(
            R.id.small_notification_title_view, title)
        smallView.setTextViewText(
            R.id.small_notification_artist_view, artist)

        return smallView
    }

    private fun createExpandedNotificationRemoteView(title:String, artist:String, bitmap: Bitmap, packageName: String): RemoteViews{
        var expandedView = RemoteViews(packageName, R.layout.song_big_notification_layout)
        expandedView.setImageViewBitmap(R.id.notification_cover_view, bitmap)
        expandedView.setTextViewText(
            R.id.notification_title_view, title)
        expandedView.setTextViewText(
            R.id.notification_artist_view, artist)
        setNotificationButtonLogic(expandedView)
        return expandedView
    }

    private fun setNotificationButtonLogic(expandedView: RemoteViews){
        val previous = Intent(context, BackgroundSongPlayerService::class.java)
        previous.action = "previous"
        expandedView.setOnClickPendingIntent(
            R.id.previous_button,
            PendingIntent.getService(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT)
        )
        val next = Intent(context, BackgroundSongPlayerService::class.java)
        next.action = "next"
        expandedView.setOnClickPendingIntent(
            R.id.next_button,
            PendingIntent.getService(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT)
        )
        val pause = Intent(context, BackgroundSongPlayerService::class.java)
        pause.action = "pause"
        expandedView.setOnClickPendingIntent(
            R.id.play_button,
            PendingIntent.getService(context, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT)
        )

        val close = Intent(context, BackgroundSongPlayerService::class.java)
        close.action = "close"
        expandedView.setOnClickPendingIntent(
            R.id.exit_button,
            PendingIntent.getService(context, 0, close, PendingIntent.FLAG_UPDATE_CURRENT)
        )
    }

    fun setPaused(): Notification?{
        notificationBuilder.bigContentView.setImageViewResource(R.id.play_button,  R.drawable.ic_play_arrow_black_24dp)
        return notificationBuilder.build()

    }

    fun setResumed(): Notification?{
        notificationBuilder.bigContentView.setImageViewResource(R.id.play_button,  R.drawable.ic_pause_black_24dp)
        return notificationBuilder.build()

    }




}