package it.docgem.dao

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {
    fun init() {
        val driverClassName = "com.mysql.cj.jdbc.Driver"
        /** connessione da usare da IDE **/
        val jdbcURL = "jdbc:mysql://127.0.0.1:3306/docgem_studiozoccali"
        /** connessione da usare dal docker container **/
        //val jdbcURL = "jdbc:mysql://host.docker.internal:3306/docgem_studiozoccali"
        //val connection = Database.connect(jdbcURL, driverClassName, user="root", password = "root")
        Database.connect(jdbcURL, driverClassName, user="root", password = "root")
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction (Dispatchers.IO) { block() }
}