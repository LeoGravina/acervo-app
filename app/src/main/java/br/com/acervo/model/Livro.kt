package br.com.acervo.model

data class Livro(
    val id: Long = 0,
    val titulo: String,
    val autor: String,
    val ano: Int,
    val genero: String,
    val lido: Boolean = false
)
