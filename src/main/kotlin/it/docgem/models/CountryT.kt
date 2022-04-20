package it.docgem.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Country(val countryId: Int,
                    val iso2: String,
                    val shortName: String,
                    val longName: String,
                    val iso3: String,
                    val numcode: String,
                    val unMember: String
                    //val callingCode: String
                    //val cctId: String
                    )

object Country_T: Table(){
    val countryId = integer("country_id").autoIncrement()
    val iso2 = varchar("iso2", 255)
    val shortName = varchar("short_name", 255)
    val longName = varchar("long_name", 255)
    val iso3 = varchar("iso3", 255)
    val numcode = varchar("numcode", 255)
    val unMember = varchar("un_member", 255)
    //val callingCode = varchar("calling_code", 255)
    //val cctId = varchar("cctId", 255)

    override val primaryKey = PrimaryKey(countryId)
}
