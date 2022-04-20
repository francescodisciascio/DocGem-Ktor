package it.docgem.dao

import it.docgem.models.*

interface DAOFacade {
    suspend fun allCountries(): List<Country>
    suspend fun allCountriesFiltered(shortName: String?): List<Country>
    suspend fun country(countryId: Int): Country?
    suspend fun addNewCountry(iso2: String, shortName: String, longName: String, iso3: String, numcode: String, unMember: String): Country?
    suspend fun editCountry(countryId: Int, iso2: String, shortName: String, longName: String, iso3: String, numcode: String, unMember: String): Boolean
    suspend fun deleteCountry(countryId: Int): Boolean
}