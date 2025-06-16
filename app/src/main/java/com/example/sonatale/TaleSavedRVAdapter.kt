package com.example.sonatale

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sonatale.databinding.ItemTaleBookBinding

class TaleSavedRVAdapter(
    private val items: List<Pair<String, String>>,
    private val onPlayClick: (List<String>) -> Unit,
    private val onPauseClick: () -> Unit,
    private val onItemLongClick: (String) -> Unit
) : RecyclerView.Adapter<TaleSavedRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaleBookBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemTaleBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String, String>) {
            val (emotion, audioPath) = item
            val uriList = listOf(audioPath)

            binding.tvTitle.text = "감정: $emotion"

            binding.ivPlay.setOnClickListener {
                onPlayClick(uriList)
                binding.ivPlay.visibility = View.INVISIBLE
                binding.ivPause.visibility = View.VISIBLE
            }

            binding.ivPause.setOnClickListener {
                onPauseClick()
                binding.ivPlay.visibility = View.VISIBLE
                binding.ivPause.visibility = View.INVISIBLE
            }

            binding.layoutItem.setOnLongClickListener {
                onItemLongClick(emotion)
                true
            }
        }
    }
}
