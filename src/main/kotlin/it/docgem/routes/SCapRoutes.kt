import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import it.docgem.dao.DAOFacade
import it.docgem.dao.DAOFacadeImpl

fun Application.registerSCapRoutes() {
    routing {
        sCapRouting()
    }
}

fun Route.sCapRouting(){
    route("/sCap"){
        val dao: DAOFacade = DAOFacadeImpl()
        get{

            //val codiceParam = call.request.queryParameters["codice"]
            //val listSCap = dao.allSCapFiltered(codiceParam)
            val listSCap = dao.allSCapFiltered2(call.request.queryParameters)
            if(listSCap.isEmpty())
                call.respondText("Nessun elemento trovato", status = HttpStatusCode.NotFound)
            else
                call.respond(listSCap)
        }
        get("{id}"){
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val sCap = dao.sCap((id))
            if(sCap != null)
                call.respond(sCap)
            else {
                call.respondText("Nessun elemento trovato", status = HttpStatusCode.NotFound)
            }
        }
        post{
            val formParameters = call.receiveParameters()
            val idCap = formParameters.getOrFail<Int>("idCap").toInt()
            val regione = formParameters.getOrFail("regione")
            val prov= formParameters.getOrFail("prov")
            val comune= formParameters.getOrFail("comune")
            val cap= formParameters.getOrFail<Int>("cap").toInt()
            val codice= formParameters.getOrFail("codice")

            val sCap = dao.addNewSCap(idCap, regione, prov, comune, cap, codice)
            call.respondRedirect("/sCap/${sCap?.idcap}")
        }
        put("{id}") {
            val formParameters = call.receiveParameters()
            val idCap = call.parameters.getOrFail<Int>("id").toInt()
            val regione = formParameters.getOrFail("regione")
            val prov= formParameters.getOrFail("prov")
            val comune= formParameters.getOrFail("comune")
            val cap= formParameters.getOrFail<Int>("cap").toInt()
            val codice= formParameters.getOrFail("codice")
            val sCap = dao.editSCap(idCap, regione, prov, comune, cap, codice)
            if(sCap)
                call.respondRedirect("/sCap/${idCap}")
            else
                call.respondText("Nessun aggiornamento effettuato", status = HttpStatusCode.NotFound)
        }
        delete("{id}"){
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val result = dao.deleteSCap(id)
            if (result)
                call.respondText("elemento con id $id eliminato correttamente", status = HttpStatusCode.Accepted)
            else
                call.respondText("Nessuna cancellazione effettuata", status = HttpStatusCode.NotFound)
        }
    }

}