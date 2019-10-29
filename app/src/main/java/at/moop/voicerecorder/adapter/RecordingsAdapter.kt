package at.moop.voicerecorder.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.moop.voicerecorder.R
import at.moop.voicerecorder.adapter.vh.RecordingViewHolder
import at.moop.voicerecorder.model.Recording
import java.text.DateFormat

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class RecordingsAdapter(private val clickHandler: (recording: Recording) -> Unit) :
    RecyclerView.Adapter<RecordingViewHolder>() {

    private val items = mutableListOf<Recording>()
    private val dateFormat = DateFormat.getDateTimeInstance()

    fun setData(data: List<Recording>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingViewHolder {
        return RecordingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recording,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecordingViewHolder, position: Int) {
        val item = items[position]

        holder.ivIcon.backgroundTintList = ColorStateList.valueOf(
            Color.HSVToColor(
                floatArrayOf(
                    getRecordingHue(item), 0.8f, 0.9f
                )
            )
        )

        if (item.isRunning()) {
            holder.tvDate.setText(R.string.main_status_recording)
            holder.tvDuration.text = null
            holder.itemView.setOnClickListener {}
        } else {
            holder.tvDate.text = dateFormat.format(item.startTime)
            holder.tvDuration.text = item.getDuration(false)?.toString()

            holder.itemView.setOnClickListener {
                clickHandler.invoke(item)
            }
        }


    }

    private fun getRecordingHue(recording: Recording): Float {
        val duration = recording.getDuration(true) ?: 0
        return duration.rem(365).toFloat()
    }
}
