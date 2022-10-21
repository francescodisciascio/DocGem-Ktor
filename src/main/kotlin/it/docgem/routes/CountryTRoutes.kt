import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import it.docgem.dao.DAOFacade
import it.docgem.dao.DAOFacadeImpl

fun Application.registerCountryTRoutes() {
    routing {
        countryTRouting()
    }
}

fun Route.countryTRouting(){
    route("/countryT"){
        val dao: DAOFacade = DAOFacadeImpl()
        /*runBlocking {
            if(dao.allCountries().isEmpty()){

            }
        }*/
        get{
            val shortNameParam = call.request.queryParameters["shortName"]
            val listCountries = dao.allCountriesFiltered(shortNameParam)
            if(listCountries.isEmpty())
                call.respondText("Nessun elemento trovato", status = HttpStatusCode.NotFound)
            else
                call.respond(listCountries)
        }
        get("{id}"){
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val country = dao.country((id))
            if(country != null)
                call.respond(country)
            else {
                call.respondText("Nessun elemento trovato", status = HttpStatusCode.NotFound)
            }
        }
        post{
            val formParameters = call.receiveParameters()
            val iso2 = formParameters.getOrFail("iso2")
            val shortName= formParameters.getOrFail("shortName")
            val longName= formParameters.getOrFail("longName")
            val iso3= formParameters.getOrFail("iso3")
            val numcode= formParameters.getOrFail("numcode")
            val unMember= formParameters.getOrFail("unMember")
            val country = dao.addNewCountry(iso2, shortName, longName, iso3, numcode, unMember)
            call.respondRedirect("/countryT/${country?.countryId}")
        }
        put("{id}") {
            val formParameters = call.receiveParameters()
            val countryId = call.parameters.getOrFail<Int>("id").toInt()
            val iso2 = formParameters.getOrFail("iso2")
            val shortName= formParameters.getOrFail("shortName")
            val longName= formParameters.getOrFail("longName")
            val iso3= formParameters.getOrFail("iso3")
            val numcode= formParameters.getOrFail("numcode")
            val unMember= formParameters.getOrFail("unMember")
            val country = dao.editCountry(countryId, iso2, shortName, longName, iso3, numcode, unMember)
            if(country)
                call.respondRedirect("/countryT/${countryId}")
            else
                call.respondText("Nessun aggiornamento effettuato", status = HttpStatusCode.NotFound)
        }
        delete("{id}"){
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val result = dao.deleteCountry(id)
            if (result)
                call.respondRedirect("/countryT")
            else
                call.respondText("Nessuna cancellazione effettuata", status = HttpStatusCode.NotFound)
        }
    }

}