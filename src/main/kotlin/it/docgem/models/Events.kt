package it.docgem.models




import it.docgem.models.Country_T.autoIncrement
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

import java.sql.Timestamp


@Serializable
data class Event(val id: Int?,
                 val description: String?,
                 val doctorId: Int?,
                 val endTime: LocalDateTime?,
                 val healthServiceId: Int?,
                 val isEventDone: Boolean,
                 val isRecurring: Boolean,
                 val patientId: Int?,
                 val patientName: String?,
                 val patientSurname: String?,
                 val phoneNumber: String?,
                 val startTime: LocalDateTime?
)

object Events: Table(name = "Event"){
    val id = integer("id").autoIncrement()
    val description = varchar("description", 255)
    val doctorId = integer("doctor_id")
    val endTime = datetime("end_time")
    val healthServiceId = integer("health_service_id")
    val isEventDone = bool("is_event_done")
    val isRecurring = bool("is_recurring")
    val patientId = integer("patient_id")
    val patientName = varchar("patient_name", 255)
    val patientSurname = varchar("patient_surname", 255)
    val phoneNumber = varchar("phone_number", 20)
    val startTime = datetime("start_time")

}

