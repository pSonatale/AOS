package com.example.sonatale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sonatale.databinding.FragmentTaleBinding
import android.media.MediaPlayer
import android.widget.Toast

class TaleFragment : Fragment() {
    private lateinit var binding: FragmentTaleBinding
    private var mediaPlayer: MediaPlayer? = null
    
    // 동화 - 음원 매핑한 부분 (수정)
    private val titleToResId = mapOf(
        "잠자는 숲속의 공주" to R.raw.joy01,
        "헨젤과 그레텔" to R.raw.anxiety01,
        "빨간 망토" to R.raw.nautral02
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaleBinding.inflate(layoutInflater)

        setTaleSavedRecyclerView()
        setMediaControls()
        return binding.root
    }
    
    // 동화 리스트 설정
    private fun setTaleSavedRecyclerView() {
        val taleTitle = listOf(
            "잠자는 숲속의 공주", "헨젤과 그레텔", "빨간 망토"
        )
        
        // 제목 클릭 - 매핑한 음원 재생
        val adapter = TaleSavedRVAdapter(taleTitle) { selectedTitle ->
            playSelectedTale(selectedTitle)
        }

        binding.rvBook.adapter = adapter
        binding.rvBook.layoutManager = LinearLayoutManager(requireContext())
    }

    // 음원 재생 함수 
    private fun playSelectedTale(title: String) {
        val resId = titleToResId[title] ?: return
        releaseMediaPlayer()
        mediaPlayer = MediaPlayer.create(requireContext(), resId)
        mediaPlayer?.setOnCompletionListener {
            Toast.makeText(requireContext(), "음원 재생 완료", Toast.LENGTH_SHORT).show()
            releaseMediaPlayer()
            updatePlayPauseButton(false)
        }
        mediaPlayer?.start()
        updatePlayPauseButton(true)
    }

    private fun setMediaControls() {
        binding.btnPlayPause.setOnClickListener {
            if (mediaPlayer == null) {
                return@setOnClickListener
            }

            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                updatePlayPauseButton(false)
            } else {
                mediaPlayer?.start()
                updatePlayPauseButton(true)
            }
        }

        // 처음으로 되감기
        binding.btnPrev.setOnClickListener {
            if (mediaPlayer != null) {
                mediaPlayer?.seekTo(0)
            }
        }

        // 맨 끝으로
        binding.btnNext.setOnClickListener {
            if (mediaPlayer != null) {
                mediaPlayer?.seekTo(mediaPlayer?.duration ?: 0)
            }
        }
    }


    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releaseMediaPlayer()
    }

    // 재생 상태에 따른 아이콘 변경 함수
    private fun updatePlayPauseButton(isPlaying: Boolean) {
        val icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        binding.btnPlayPause.setImageResource(icon)
    }
}