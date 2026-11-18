package br.com.acervo.data

import br.com.acervo.model.Livro

interface LivroDao {
    fun listarTodos(): List<Livro>
    fun buscarPorId(id: Long): Livro?
    fun inserir(livro: Livro): Long
    fun atualizar(livro: Livro): Int
    fun excluir(id: Long): Int
}
