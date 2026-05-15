package ivan.karpiuk.mindful.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@JvmInline
value class DayKey(
    val value: String,
) {
    companion object {
        fun today(
            clock: Clock,
            timeZone: TimeZone = TimeZone.currentSystemDefault(),
        ): DayKey = of(clock.now().toLocalDateTime(timeZone).date)

        fun of(date: LocalDate): DayKey = DayKey(date.toString())
    }
}
