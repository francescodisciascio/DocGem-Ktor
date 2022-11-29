package it.docgem.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import it.docgem.dao.DAOFacade
import it.docgem.dao.DAOFacadeImpl
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

fun Application.registerEventRoutes() {
    routing {
        eventRouting()
    }
}

fun Route.eventRouting(){
    route("/event"){
        val dao: DAOFacade = DAOFacadeImpl()
        get{
            //val shortNameParam = call.request.queryParameters["shortName"]
            //val listEvents = dao.allCountriesFiltered(shortNameParam)
            val listEvents = dao.allEvents()
            if(listEvents.isEmpty())
                call.respondText("Nessun elemento trovato", status = HttpStatusCode.NotFound)
            else
                call.respond(listEvents)
        }
        get("{id}"){
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val event = dao.event((id))
            if(event != null)
                call.respond(event)
            else {
                call.respondText("Nessun elemento trovato", status = HttpStatusCode.NotFound)
            }
        }
        post{
            val formParameters = call.receiveParameters()
            val description = formParameters.getOrFail("description")
            val doctorId= formParameters.getOrFail("doctorId").toInt()
            val endTime= LocalDateTime.parse(formParameters.getOrFail("endTime"))
            val healthServiceId= formParameters.getOrFail("healthServiceId").toInt()
            val isEventDone= formParameters.getOrFail("isEventDone").toBoolean()
            val isRecurring= formParameters.getOrFail("isRecurring").toBoolean()
            val patientId= formParameters.getOrFail("patientId").toInt()
            val patientName= formParameters.getOrFail("patientName")
            val patientSurname= formParameters.getOrFail("patientSurname")
            val phoneNumber= formParameters.getOrFail("phoneNumber")
            val startTime= formParameters.getOrFail("startTime").toLocalDateTime()
            val event = dao.addNewEvent(description, doctorId, endTime, healthServiceId, isEventDone, isRecurring, patientId, patientName, patientSurname, phoneNumber, startTime)
            call.respondRedirect("/event/${event?.id}")
        }
        put("{id}") {
            val formParameters = call.receiveParameters()
            val eventId = call.parameters.getOrFail<Int>("id").toInt()
            val description = formParameters.getOrFail("description")
            val doctorId= formParameters.getOrFail("doctorId").toInt()
            val endTime= formParameters.getOrFail("endTime").toLocalDateTime()
            val healthServiceId= formParameters.getOrFail("healthServiceId").toInt()
            val isEventDone= formParameters.getOrFail("isEventDone").toBoolean()
            val isRecurring= formParameters.getOrFail("isRecurring").toBoolean()
            val patientId= formParameters.getOrFail("patientId").toInt()
            val patientName= formParameters.getOrFail("patientName")
            val patientSurname= formParameters.getOrFail("patientSurname")
            val phoneNumber= formParameters.getOrFail("phoneNumber")
            val startTime= formParameters.getOrFail("startTime").toLocalDateTime()
            val event = dao.editEvent(eventId, description, doctorId, endTime, healthServiceId, isEventDone, isRecurring, patientId, patientName, patientSurname, phoneNumber, startTime)
            if(event)
                call.respondRedirect("/event/${eventId}")
            else
                call.respondText("Nessun aggiornamento effettuato", status = HttpStatusCode.NotFound)
        }
        delete("{id}"){
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val result = dao.deleteEvent(id)
            if (result)
                call.respondRedirect("/event")
            else
                call.respondText("Nessuna cancellazione effettuata", status = HttpStatusCode.NotFound)
        }
    }

}