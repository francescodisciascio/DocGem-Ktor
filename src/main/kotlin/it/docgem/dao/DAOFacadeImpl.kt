package it.docgem.dao

import io.ktor.http.*
import it.docgem.models.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.joda.time.DateTime
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import kotlin.reflect.full.declaredMemberProperties

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToCountry(row: ResultRow) = Country(
        countryId = row[Country_T.countryId],
        iso2 = row[Country_T.iso2],
        shortName = row[Country_T.shortName],
        longName = row[Country_T.longName],
        iso3 = row[Country_T.iso3],
        numcode = row[Country_T.numcode],
        //callingCode = row[Country_T.callingCode],
        unMember = row[Country_T.unMember]
        //cctId = row[Country_T.cctId]
    )

    override suspend fun allCountries(): List<Country> = DatabaseFactory.dbQuery{
        Country_T.selectAll().map(::resultRowToCountry)
    }



    override suspend fun allCountriesFiltered(shortName: String?): List<Country> {
        return if(shortName != null){
            DatabaseFactory.dbQuery {
                Country_T
                    .select { Country_T.shortName like "%"+shortName+"%" }
                    .map(::resultRowToCountry)
            }
        } else {
            Country_T.selectAll().map(::resultRowToCountry)
        }
    }

    override suspend fun country(countryId: Int): Country? = DatabaseFactory.dbQuery {
        Country_T
            .select { Country_T.countryId eq countryId }
            .map(::resultRowToCountry)
            .singleOrNull()
    }

    override suspend fun addNewCountry(
        iso2: String,
        shortName: String,
        longName: String,
        iso3: String,
        numcode: String,
        unMember: String
    ): Country? = DatabaseFactory.dbQuery {
        val insertStatement = Country_T.insert {
            it[Country_T.iso2] = iso2
            it[Country_T.shortName] = shortName
            it[Country_T.longName] = longName
            it[Country_T.iso3] = iso3
            it[Country_T.numcode] = numcode
            it[Country_T.unMember] = unMember
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCountry)
    }

    override suspend fun editCountry(
        countryId: Int,
        iso2: String,
        shortName: String,
        longName: String,
        iso3: String,
        numcode: String,
        unMember: String
    ): Boolean  = DatabaseFactory.dbQuery{
        Country_T.update ({Country_T.countryId eq countryId}){
            it[Country_T.iso2] = iso2
            it[Country_T.shortName] = shortName
            it[Country_T.longName] = longName
            it[Country_T.iso3] = iso3
            it[Country_T.numcode] = numcode
            it[Country_T.unMember] = unMember
        } > 0
    }

    override suspend fun deleteCountry(countryId: Int): Boolean = DatabaseFactory.dbQuery{
        Country_T.deleteWhere { Country_T.countryId eq countryId } > 0
    }



    private fun resultRowToSCap(row: ResultRow) = SCap(
        idcap = row[S_Cap.idcap],
        regione = row[S_Cap.regione],
        prov = row[S_Cap.prov],
        comune = row[S_Cap.comune],
        cap = row[S_Cap.cap],
        codice = row[S_Cap.codice]
    )

    private fun parametersToSCap(parameters: Parameters) = SCap(
        idcap = parameters["idcap"]?.toInt(),
        regione = parameters["regione"],
        prov = parameters["prov"],
        comune = parameters["comune"],
        cap = parameters["cap"]?.toInt(),
        codice = parameters["codice"]
    )

    private fun resultRowToEvent(row: ResultRow) = Event(
        id = row[Events.id],
        description = row[Events.description],
        doctorId = row[Events.doctorId],
        endTime = row[Events.endTime].toString().toInstant().toLocalDateTime(TimeZone.currentSystemDefault()),
        healthServiceId = row[Events.healthServiceId],
        isEventDone = (row[Events.isEventDone].toString()).toBoolean(),
        isRecurring = (row[Events.isRecurring].toString()).toBoolean(),
        patientId = row[Events.patientId],
        patientName = row[Events.patientName],
        patientSurname = row[Events.patientSurname],
        phoneNumber = row[Events.phoneNumber],
        startTime = row[Events.startTime].toString().toInstant().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    override suspend fun allSCap(): List<SCap> = DatabaseFactory.dbQuery{
        S_Cap.selectAll().map(::resultRowToSCap)
    }



    override suspend fun allSCapFiltered(codice: String?): List<SCap> {
        return if(codice.isNullOrEmpty()){
            DatabaseFactory.dbQuery {
                S_Cap.selectAll().map(::resultRowToSCap)
            }
        } else {
            DatabaseFactory.dbQuery {
                S_Cap
                    .select { S_Cap.codice like "%$codice%" }
                    .map(::resultRowToSCap)
            }
        }
    }

    override suspend fun allSCapFiltered2(parameters: Parameters): List<SCap> {
        return if (parameters.isEmpty()){
            DatabaseFactory.dbQuery {
                S_Cap.selectAll().map(::resultRowToSCap)
            }
        }
        else {
            DatabaseFactory.dbQuery {
                val query = S_Cap.selectAll()

                parameters.forEach { it, list ->
                    println("this $it $list")
                    for(property in S_Cap::class.declaredMemberProperties){
                        if(property.name == it) {
                            println("found $it")
                            //SqlExpressionBuilder s =

                            //parameters["$it"]?.let{query.andWhere { S_Cap.getPropertyByName("$it") like "$list" }}
                        }
                    }

                }

                parameters["regione"]?.let { query.andWhere { S_Cap.regione like it } }
                parameters["prov"]?.let { query.andWhere { S_Cap.prov like it } }
                parameters["comune"]?.let { query.andWhere { S_Cap.comune like it } }
                parameters["cap"]?.let { query.andWhere { S_Cap.cap eq it.toInt() } }
                parameters["codice"]?.let { query.andWhere { S_Cap.codice like "%$it%" } }

              query.map { resultRowToSCap(it) }

            }


        }


    }

    override suspend fun sCap(idCap: Int): SCap? = DatabaseFactory.dbQuery {
        S_Cap
            .select { S_Cap.idcap eq idCap }
            .map(::resultRowToSCap)
            .singleOrNull()
    }

    override suspend fun addNewSCap(
        idCap: Int,
        regione: String,
        prov: String,
        comune: String,
        cap: Int,
        codice: String
    ): SCap? = DatabaseFactory.dbQuery {
        val insertStatement = S_Cap.insert {
            it[idcap] = idCap
            it[S_Cap.regione] = regione
            it[S_Cap.prov] = prov
            it[S_Cap.comune] = comune
            it[S_Cap.cap] = cap
            it[S_Cap.codice] = codice
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToSCap)
    }

    override suspend fun editSCap(
        idCap: Int,
        regione: String,
        prov: String,
        comune: String,
        cap: Int,
        codice: String
    ): Boolean  = DatabaseFactory.dbQuery{
        S_Cap.update ({S_Cap.idcap eq idCap}){
            it[S_Cap.regione] = regione
            it[S_Cap.prov] = prov
            it[S_Cap.comune] = comune
            it[S_Cap.cap] = cap
            it[S_Cap.codice] = codice
        } > 0
    }

    override suspend fun deleteSCap(idCap: Int): Boolean = DatabaseFactory.dbQuery{
        S_Cap.deleteWhere { S_Cap.idcap eq idCap } > 0
    }


    override suspend fun allEvents(): List<Event> = DatabaseFactory.dbQuery{
        Events.selectAll().map(::resultRowToEvent)
    }

    override suspend fun event(eventId: Int): Event? = DatabaseFactory.dbQuery {
        Events
            .select { Events.id eq eventId }
            .map(::resultRowToEvent)
            .singleOrNull()
    }



    override suspend fun addNewEvent(
        description: String,
        doctorId: Int,
        endTime: LocalDateTime,
        healthServiceId: Int,
        isEventDone: Boolean,
        isRecurring: Boolean,
        patientId: Int,
        patientName: String,
        patientSurname: String,
        phoneNumber: String,
        startTime: LocalDateTime
    ): Event? = DatabaseFactory.dbQuery {
        val insertStatement = Events.insert {
            it[Events.description] = description
            it[Events.doctorId] = doctorId
            it[Events.endTime] = DateTime.parse(endTime.toString())
            it[Events.healthServiceId] = healthServiceId
            it[Events.isEventDone] = isEventDone
            it[Events.isRecurring] = isRecurring
            it[Events.patientId] = patientId
            it[Events.patientName] = patientName
            it[Events.patientSurname] = patientSurname
            it[Events.phoneNumber] = phoneNumber
            it[Events.startTime] = DateTime.parse(startTime.toString())
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToEvent)
    }

    override suspend fun editEvent(
        id: Int,
        description: String,
        doctorId: Int,
        endTime: LocalDateTime,
        healthServiceId: Int,
        isEventDone: Boolean,
        isRecurring: Boolean,
        patientId: Int,
        patientName: String,
        patientSurname: String,
        phoneNumber: String,
        startTime: LocalDateTime
    ): Boolean  = DatabaseFactory.dbQuery{
        Events.update ({Events.id eq id}){
            it[Events.description] = description
            it[Events.doctorId] = doctorId
            it[Events.endTime] = DateTime.parse(endTime.toString())
            it[Events.healthServiceId] = healthServiceId
            it[Events.isEventDone] = isEventDone
            it[Events.isRecurring] = isRecurring
            it[Events.patientId] = patientId
            it[Events.patientName] = patientName
            it[Events.patientSurname] = patientSurname
            it[Events.phoneNumber] = phoneNumber
            it[Events.startTime] = DateTime.parse(startTime.toString())
        } > 0
    }

    override suspend fun deleteEvent(eventId: Int): Boolean = DatabaseFactory.dbQuery{
        Events.deleteWhere { Events.id eq eventId } > 0
    }


}