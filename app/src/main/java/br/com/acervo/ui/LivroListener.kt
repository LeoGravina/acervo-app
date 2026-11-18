package br.com.acervo.ui

import br.com.acervo.model.Livro

interface LivroListener {
    fun aoClicar(livro: Livro)
    fun aoMarcarLido(livro: Livro, lido: Boolean)
    fun aoExcluir(livro: Livro)
}
