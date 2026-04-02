package com.example.myapplication.ui.video

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.myapplication.databinding.ActivityVideoDetailBinding

class VideoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoDetailBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 隐藏状态栏和标题栏，实现沉浸式播放
        supportActionBar?.hide()

        // 点击返回按钮退出
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 接收数据（以后接后端，这里逻辑不变，只需确保传过来的 URL 有效）
        val videoUrl = intent.getStringExtra("VIDEO_URL") ?: ""

        if (videoUrl.isNotEmpty()) {
            initPlayer(videoUrl)
        }
    }

    private fun initPlayer(url: String) {
        // 创建播放器并配置
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)

            // 状态监听：处理转圈显示
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    binding.progressBar.visibility = if (state == Player.STATE_BUFFERING)
                        View.VISIBLE else View.GONE
                }
            })

            exoPlayer.prepare()
            exoPlayer.playWhenReady = true // 自动播放
        }
    }

    // 适配 Activity 生命周期，极其重要！
    override fun onPause() {
        super.onPause()
        player?.pause() // 切换到后台时暂停
    }

    override fun onStop() {
        super.onStop()
        releasePlayer() // 停止时释放
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}