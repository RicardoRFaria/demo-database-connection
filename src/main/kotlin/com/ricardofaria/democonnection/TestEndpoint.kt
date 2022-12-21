package com.ricardofaria.democonnection

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


@OptIn(ExperimentalTime::class)
@Controller
class TestEndpoint(private val jdbcTemplate: NamedParameterJdbcTemplate, private val transactionalRepositoryTest: TransactionalRepositoryTest) {

    @GetMapping("/test/test-existent-connection", produces = [MediaType.TEXT_PLAIN_VALUE])
    fun testExistent(): ResponseEntity<String> {
        println("--------------------------------------")
        val elapsed: Duration = measureTime {
            val params = mapOf("novo_nome" to "Empresa do Ricardo LTDA.")
            val numberOfUpdated = jdbcTemplate.update("UPDATE empresa SET nome = :novo_nome ", params)
            println("Number of documents updated: $numberOfUpdated")
        }
        val responseString = "Tempo total $elapsed na operação com conexão reutilizada"
        println(responseString)
        return ResponseEntity.ok(responseString)
    }

    private val blockingOpCounter = AtomicInteger()
    @GetMapping("/test/test-blocking-op", produces = [MediaType.TEXT_PLAIN_VALUE])
    fun testBlockingOp(): ResponseEntity<String> {
        println("--------------------------------------")
        val opIdentifier = blockingOpCounter.incrementAndGet()
        println("Eu sou a operação de número $opIdentifier e estou esperando uma connection")
        val elapsed: Duration = measureTime {
            transactionalRepositoryTest.delayedTransactionalOperation(opIdentifier)
        }
        val responseString = "Tempo total $elapsed na operação de update com bloqueio de propósito"
        println(responseString)
        return ResponseEntity.ok(responseString)
    }

    @GetMapping("/test/test-direct-connection", produces = [MediaType.TEXT_PLAIN_VALUE])
    fun testDirect(): ResponseEntity<String> {
        println("--------------------------------------")
        val conn: Connection
        val url = "jdbc:postgresql://localhost:5432/postgres"
        val props = Properties()
        props.setProperty("user", "postgres")
        props.setProperty("password", "postgres")
        val elapsedWhileOpening: Duration = measureTime {

            conn = DriverManager.getConnection(url, props)
        }
        val responseString = "Tempo total para abrir a conexão $elapsedWhileOpening \nNOTE: Importante destacar que aqui tem execução de código java também, não somente de connection"
        println(responseString)
        conn.close()
        return ResponseEntity.ok(responseString)
    }


    fun preparedStatementCode() {
        println("--------------------------------------")
        val conn: Connection = jdbcTemplate.jdbcTemplate.dataSource!!.connection

        val stmt: PreparedStatement = conn.prepareStatement("UPDATE empresa SET nome = ? ")
        stmt.setString(0, "Novo valor")
        stmt.execute()

        stmt.setString(0, "Novo valor 1 ")
        stmt.execute()

        stmt.setString(0, "Novo valor 2")
        stmt.execute()

        conn.close()
    }
}