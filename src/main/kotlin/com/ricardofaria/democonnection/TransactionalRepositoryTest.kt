package com.ricardofaria.democonnection

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicInteger

@Repository
class TransactionalRepositoryTest(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    private val blockingOpCounter = AtomicInteger()

    @Transactional
    fun delayedTransactionalOperation() {
        val opIdentifier = blockingOpCounter.incrementAndGet()
        val params = mapOf("novo_nome" to "Empresa do Ricardo LTDA.")
        println("Eu sou a operação de número $opIdentifier e estou esperando uma connection")
        val numberOfUpdated = jdbcTemplate.update("UPDATE empresa SET nome = :novo_nome ", params)
        println("Eu sou a operação de número $opIdentifier e obtive uma connection")
        Thread.sleep(60000)
        println("Number of documents updated: $numberOfUpdated")
    }


}