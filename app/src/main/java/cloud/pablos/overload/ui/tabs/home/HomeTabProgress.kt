package cloud.pablos.overload.ui.tabs.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import cloud.pablos.overload.ui.views.TextView

@Composable
fun HomeTabProgress(
    progressData: ProgressData,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant

    Surface(
        tonalElevation = NavigationBarDefaults.Elevation,
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(15.dp),
        ) {
            Canvas(
                modifier = modifier.size(30.dp),
            ) {
                val strokeWidth = 6.dp.toPx()

                drawArc(
                    startAngle = 270f, // 270 is 0 degree
                    sweepAngle = 360f,
                    useCenter = false,
                    color = backgroundColor,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )

                drawArc(
                    startAngle = 270f, // 270 is 0 degree
                    sweepAngle = progressData.progress,
                    useCenter = false,
                    color = progressData.color,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                TextView(title, maxLines = 2)
                TextView(subtitle)
            }
        }
    }
}
class ProgressData(
    progress: State<Float>,
    color: State<Color>,
) {
    val progress by progress
    val color by color
}
