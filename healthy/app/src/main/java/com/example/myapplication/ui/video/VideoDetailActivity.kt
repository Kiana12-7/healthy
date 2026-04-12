package com.example.myapplication.ui.video

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.databinding.ActivityVideoDetailBinding

class VideoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoDetailBinding
    private var exoPlayer: ExoPlayer? = null // 改名避免冲突
    private lateinit var videoItem: CourseItem.TrainingVideo
    private var isFullScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // 获取视频数据
        videoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("VIDEO_ITEM", CourseItem.TrainingVideo::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("VIDEO_ITEM") as CourseItem.TrainingVideo
        }

        binding.btnBack.setOnClickListener { finish() }

        if (videoItem.videoUrl.isNotEmpty()) {
            initPlayer(videoItem.videoUrl)
        }

        bindActionDesc()
        initFullScreenButton()

        // 返回键处理
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isFullScreen) {
                    exitFullScreen()
                } else {
                    finish()
                }
            }
        })
    }

    private fun bindActionDesc() {
        binding.tvPreparePose.text = videoItem.preparePose
        binding.tvActionProcess.text = videoItem.actionProcess
        binding.tvBreathRhythm.text = videoItem.breathRhythm
        binding.tvAttention.text = videoItem.attention
    }

    private fun initFullScreenButton() {
        val fullScreenBtn = binding.playerView.findViewById<ImageButton>(androidx.media3.ui.R.id.exo_fullscreen)
        fullScreenBtn?.setOnClickListener {
            if (isFullScreen) exitFullScreen() else enterFullScreen()
        }
    }

    private fun enterFullScreen() {
        isFullScreen = true
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }

        binding.btnBack.visibility = View.GONE
        binding.scrollDesc.visibility = View.GONE

        val params = binding.playerView.layoutParams
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.playerView.layoutParams = params
    }

    private fun exitFullScreen() {
        isFullScreen = false
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }

        binding.btnBack.visibility = View.VISIBLE
        binding.scrollDesc.visibility = View.VISIBLE

        val params = binding.playerView.layoutParams
        params.height = (220 * resources.displayMetrics.density).toInt()
        binding.playerView.layoutParams = params
    }

    // ✅ 核心修复：Player 初始化 + 绑定
    private fun initPlayer(url: String) {
        exoPlayer = ExoPlayer.Builder(this).build()

        // 关键：强制指定 PlayerView 并设置播放器
        val playerView: PlayerView = binding.playerView
        playerView.player = exoPlayer

        val mediaItem = MediaItem.fromUri(url)
        exoPlayer?.setMediaItem(mediaItem)

        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                binding.progressBar.visibility = if (state == Player.STATE_BUFFERING) View.VISIBLE else View.GONE
            }
        })

        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }

    override fun onStop() {
        super.onStop()
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null
    }
}