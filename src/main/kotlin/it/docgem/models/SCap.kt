package it.docgem.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@Serializable
data class SCap(val idcap: Int?,
                    val regione: String?,
                    val prov: String?,
                    val comune: String?,
                    val cap: Int?,
                    val codice: String?
                    )

object S_Cap: Table(){
    val idcap = integer("idcap")
    val regione = varchar("regione", 21)
    val prov = varchar("prov", 2)
    val comune = varchar("comune", 33)
    val cap = integer("cap")
    val codice = varchar("codice", 4)

    //override val primaryKey = PrimaryKey(idcap)
    fun getPropertyByName(propName: String): Column<*> {
        if (propName == this.regione.name){
            return this.regione
        }
        if (propName == this.prov.name){
            return this.prov
        }
        if (propName == this.comune.name){
            return this.comune
        }
        if (propName == this.cap.name){
            return this.cap
        }
        if (propName == this.codice.name){
            return this.codice
        }

        return this.idcap
    }
}
