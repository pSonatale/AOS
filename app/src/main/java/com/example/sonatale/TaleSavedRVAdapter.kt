package com.example.sonatale

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sonatale.databinding.ItemTaleBookBinding

class TaleSavedRVAdapter(private val tale: List<String>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<TaleSavedRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): TaleSavedRVAdapter.ViewHolder {
        val binding: ItemTaleBookBinding =
            ItemTaleBookBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaleSavedRVAdapter.ViewHolder, position: Int) {
        holder.bind(tale[position])
    }

    override fun getItemCount(): Int = tale.size

    inner class ViewHolder(val binding: ItemTaleBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.tvTitle.text = title
            binding.root.setOnClickListener {
                onItemClick(title)
            }
        }
    }
}