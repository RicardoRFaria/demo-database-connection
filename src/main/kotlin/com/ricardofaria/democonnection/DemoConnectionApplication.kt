package com.ricardofaria.democonnection

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@SpringBootApplication
class DemoConnectionApplication

fun main(args: Array<String>) {
    runApplication<DemoConnectionApplication>(*args)
}
