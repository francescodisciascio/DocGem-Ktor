package it.docgem.dao

import it.docgem.models.Country_T
import it.docgem.models.Country
import org.jetbrains.exposed.sql.*

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
}