package it.docgem.dao

import io.ktor.http.*
import it.docgem.models.*
import kotlinx.datetime.LocalDateTime


interface DAOFacade {
    suspend fun allCountries(): List<Country>
    suspend fun allCountriesFiltered(shortName: String?): List<Country>
    suspend fun country(countryId: Int): Country?
    suspend fun addNewCountry(iso2: String, shortName: String, longName: String, iso3: String, numcode: String, unMember: String): Country?
    suspend fun editCountry(countryId: Int, iso2: String, shortName: String, longName: String, iso3: String, numcode: String, unMember: String): Boolean
    suspend fun deleteCountry(countryId: Int): Boolean

    suspend fun allSCap(): List<SCap>
    suspend fun allSCapFiltered(codice: String?): List<SCap>
    suspend fun allSCapFiltered2(parameters: Parameters): List<SCap>
    suspend fun sCap(idCap: Int): SCap?
    suspend fun addNewSCap(idCap: Int, regione: String, prov: String, comune: String, cap: Int, codice: String): SCap?
    suspend fun editSCap(idCap: Int, regione: String, prov: String, comune: String, cap: Int, codice: String): Boolean
    suspend fun deleteSCap(idCap: Int): Boolean

    suspend fun allEvents(): List<Event>
    suspend fun event(id: Int): Event?
    suspend fun addNewEvent(description: String,
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
    ): Event?
    suspend fun editEvent(id: Int,
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
    ): Boolean
    suspend fun deleteEvent(id: Int): Boolean
}