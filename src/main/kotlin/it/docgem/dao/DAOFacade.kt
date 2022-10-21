package it.docgem.dao

import io.ktor.http.*
import it.docgem.models.*

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
}