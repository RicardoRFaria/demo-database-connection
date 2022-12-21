package com.ricardofaria.democonnection

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicInteger

@Repository
class TransactionalRepositoryTest(private val jdbcTemplate: NamedParameterJdbcTemplate) {



    @Transactional
    fun delayedTransactionalOperation(opIdentifier: Int) {
        println("Eu sou a operação de número $opIdentifier e obtive uma connection")
        val params = mapOf("novo_nome" to "Empresa do Ricardo LTDA.",
                "id" to opIdentifier)
        val numberOfUpdated = jdbcTemplate.update("UPDATE empresa SET nome = :novo_nome WHERE id_empresa = :id ", params)
        Thread.sleep(60000)
        println("Number of documents updated: $numberOfUpdated")
    }


}