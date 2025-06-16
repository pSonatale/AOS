package com.example.sonatale

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sonatale.databinding.FragmentTaleBinding
import com.example.sonatale.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaleFragment : Fragment() {
    private lateinit var binding: FragmentTaleBinding
    private var mediaPlayer: MediaPlayer? = null
    private var currentUriIndex = 0
    private var playingUris: List<String> = emptyList()
    private var isPaused = false
    private var emotionMusicMap: Map<String, String> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaleBinding.inflate(layoutInflater)

        binding.fabGenerateMusic.setOnClickListener {
            val intent = Intent(requireContext(), MusicActivity::class.java)
            startActivity(intent)
        }

        fetchTaleListFromServer()
        return binding.root
    }

    private fun fetchTaleListFromServer() {
        val api = RetrofitClient.getClient().create(MainInterface::class.java)
        api.getTTSList().enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    emotionMusicMap = response.body() ?: emptyMap()
                    setupRecyclerView()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                // 실패 로그 처리
            }
        })
    }

    private fun setupRecyclerView() {
        val adapter = TaleSavedRVAdapter(
            items = emotionMusicMap.toList(),
            onPlayClick = { uriList -> handlePlay(uriList) },
            onPauseClick = { handlePause() },
            onItemLongClick = { key -> confirmAndRemoveKey(key) }
        )
        binding.rvBook.adapter = adapter
        binding.rvBook.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handlePlay(uriList: List<String>) {
        if (isPaused) {
            mediaPlayer?.start()
            isPaused = false
            return
        }

        if (uriList.isEmpty()) return

        playingUris = uriList
        currentUriIndex = 0
        playNext()
    }

    private fun handlePause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPaused = true
        }
    }

    private fun playNext() {
        if (currentUriIndex >= playingUris.size) {
            mediaPlayer?.release()
            mediaPlayer = null
            return
        }

        val uri = playingUris[currentUriIndex]
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource("https://your.server.domain/$uri") // 실제 서버 URL로 교체
            prepareAsync()
            setOnPreparedListener {
                start()
            }
            setOnCompletionListener {
                currentUriIndex++
                playNext()
            }
        }
    }

    private fun confirmAndRemoveKey(key: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("감정 삭제")
            .setMessage("\"$key\" 항목을 목록에서 제거하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                emotionMusicMap = emotionMusicMap.filterNot { it.key == key }
                setupRecyclerView()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
