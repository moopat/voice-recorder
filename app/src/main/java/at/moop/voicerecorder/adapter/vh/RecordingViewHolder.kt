package at.moop.voicerecorder.adapter.vh

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.moop.voicerecorder.R

/**
 * @author Markus Deutsch <markus@moop.at>
 */
class RecordingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvDuration = itemView.findViewById<TextView>(R.id.tvDuration)
    val tvDate = itemView.findViewById<TextView>(R.id.tvDate)

}
