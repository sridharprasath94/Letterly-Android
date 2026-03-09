package com.flash.letterly.presentation.game

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.flash.letterly.R
import com.flash.letterly.domain.model.LetterState
import com.flash.letterly.domain.model.LetterTile

class BoardAdapter :
    androidx.recyclerview.widget.ListAdapter<LetterTile, BoardAdapter.TileViewHolder>(DiffCallback) {

    fun submitBoard(board: List<List<LetterTile>>) {
        submitList(board.flatten())
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_letter_tile, parent, false)
        return TileViewHolder(view)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TileViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val letterText: TextView = view.findViewById(R.id.letterText)

        fun bind(tile: LetterTile) {
            letterText.text = tile.letter?.toString() ?: ""

            val backgroundColor = when (tile.state) {
                LetterState.CORRECT -> "#6AAA64".toColorInt()
                LetterState.PRESENT -> "#C9B458".toColorInt()
                LetterState.ABSENT -> "#787C7E".toColorInt()
                LetterState.EMPTY -> Color.WHITE
            }

            val textColor = when (tile.state) {
                LetterState.CORRECT,
                LetterState.PRESENT,
                LetterState.ABSENT -> Color.WHITE

                LetterState.EMPTY -> Color.BLACK
            }

            letterText.setBackgroundColor(backgroundColor)
            letterText.setTextColor(textColor)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<LetterTile>() {

        override fun areItemsTheSame(oldItem: LetterTile, newItem: LetterTile): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: LetterTile, newItem: LetterTile): Boolean {
            return oldItem == newItem
        }
    }
}