package de.miraculixx.mlog.sql

import de.miraculixx.mlog.LOGGER
import de.miraculixx.mlog.utils.readJson
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.Language
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

object SQL {
    private val sqlCredentials = File("config/sql.json").readJson<SQLCredentials>()
    private var connection: Connection = connect()

    private fun connect(): Connection {
        val con = DriverManager.getConnection(
            sqlCredentials.url,
            sqlCredentials.user,
            sqlCredentials.password
        )
        if (con.isValid(0)) LOGGER.info(">> Connection established to ${sqlCredentials.url}")
        else LOGGER.warning("ERROR >> MariaDB refused the connection")
        return con
    }

    suspend inline fun call(@Language("SQL") statement: String, arguments: PreparedStatement.() -> Unit = {}): ResultSet = try {
            buildStatement(statement).apply(arguments).executeQuery()
        } catch (e: Exception) {
            LOGGER.warning(e.message)
            EmptyResultSet()
        }

    suspend inline fun update(@Language("SQL") statement: String, arguments: PreparedStatement.() -> Unit = {}): Int = try {
            buildStatement(statement).apply(arguments).executeUpdate()
        } catch (e: Exception) {
            LOGGER.warning(e.message)
            -1
        }

    suspend fun buildStatement(statement: String): PreparedStatement {
        var tries = 0
        while (!connection.isValid(1)) {
            tries++
            LOGGER.warning("ERROR >> SQL - No valid connection. Retry in ${tries}s")
            connection = connect()
            delay(tries * 1000L)
        }
        return connection.prepareStatement(statement)
    }
}