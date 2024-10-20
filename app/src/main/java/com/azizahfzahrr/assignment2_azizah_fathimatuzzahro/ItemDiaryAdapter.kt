package com.azizahfzahrr.assignment2_azizah_fathimatuzzahro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.assignment2_azizah_fathimatuzzahro.databinding.DiaryListItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ItemDiaryAdapter(
    private var diaryList: MutableList<DiaryEntity>,
    private val listener: ItemDiaryListener
) : RecyclerView.Adapter<ItemDiaryAdapter.ItemDiaryViewHolder>() {

    class ItemDiaryViewHolder(val binding: DiaryListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(diary: DiaryEntity, listener: ItemDiaryListener) {
            binding.tvTitle.text = diary.title
            binding.tvDesc.text = diary.description

            val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val formattedDate = simpleDateFormat.format(Date(diary.date))
            binding.tvDateDiary.text = formattedDate

            binding.root.setOnClickListener {
                listener.onEditClicked(diary)
            }

            binding.ivDeleteDiary.setOnClickListener {
                listener.onDeleteClicked(diary)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDiaryViewHolder {
        val binding = DiaryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemDiaryViewHolder(binding)
    }

    override fun getItemCount() = diaryList.size

    override fun onBindViewHolder(holder: ItemDiaryViewHolder, position: Int) {
        val itemDiary = diaryList[position]
        holder.bind(itemDiary, listener)
    }

    fun updateDiaries(newDiaries: List<DiaryEntity>) {
        this.diaryList.clear()
        this.diaryList.addAll(newDiaries)
        notifyDataSetChanged() 
    }
}
