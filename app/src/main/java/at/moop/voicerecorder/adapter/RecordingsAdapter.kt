package at.moop.voicerecorder.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.moop.voicerecorder.R
import at.moop.voicerecorder.adapter.vh.RecordingViewHolder
import at.moop.voicerecorder.formatAsDuration
import at.moop.voicerecorder.getColor
import at.moop.voicerecorder.model.Recording
import java.text.DateFormat
import java.util.*

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class RecordingsAdapter(private val clickHandler: (recording: Recording) -> Unit) :
    RecyclerView.Adapter<RecordingViewHolder>() {

    private val items = mutableListOf<Recording>()
    private val dateFormat = DateFormat.getDateInstance()
    private val dateTimeFormat = DateFormat.getDateTimeInstance()
    private val timeFormat = DateFormat.getTimeInstance()

    private val todayAsString = dateFormat.format(Date())

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

        holder.ivIcon.backgroundTintList = ColorStateList.valueOf(item.getColor())

        if (item.isRunning()) {
            holder.tvDate.setText(R.string.main_status_recording)
            holder.tvDuration.text = null
            holder.itemView.setOnClickListener {}
        } else {
            holder.tvDate.text = item.getStartTimeString(holder.itemView.context)
            holder.tvDuration.text = item.getDuration(false)?.formatAsDuration()

            holder.itemView.setOnClickListener {
                clickHandler.invoke(item)
            }
        }


    }

    private fun Recording.getStartTimeString(context: Context): String {
        if (dateFormat.format(startTime) == todayAsString) {
            return context.getString(R.string.recordings_item_today, timeFormat.format(startTime))
        } else {
            return dateTimeFormat.format(startTime)
        }
    }
}
