package br.com.acervo.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import br.com.acervo.model.Livro

class LivroDaoSQLite(context: Context) : LivroDao {

    private val banco = BancoHelper(context)

    override fun listarTodos(): List<Livro> {
        val livros = mutableListOf<Livro>()
        val db = banco.readableDatabase
        val cursor = db.query(
            BancoHelper.TABELA_LIVROS,          // tabela
            null,                               // colunas (null = todas)
            null, null,                         // WHERE e seus argumentos
            null, null,                         // GROUP BY e HAVING
            "${BancoHelper.COL_TITULO} COLLATE NOCASE ASC"   // ORDER BY
        )
        cursor.use {
            while (it.moveToNext()) {
                livros.add(cursorParaLivro(it))
            }
        }
        return livros
    }

    override fun buscarPorId(id: Long): Livro? {
        val db = banco.readableDatabase
        val cursor = db.query(
            BancoHelper.TABELA_LIVROS,
            null,
            "${BancoHelper.COL_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) cursorParaLivro(it) else null
        }
    }

    override fun inserir(livro: Livro): Long {
        val db = banco.writableDatabase
        return db.insert(BancoHelper.TABELA_LIVROS, null, paraContentValues(livro))
    }

    override fun atualizar(livro: Livro): Int {
        val db = banco.writableDatabase
        return db.update(
            BancoHelper.TABELA_LIVROS,
            paraContentValues(livro),
            "${BancoHelper.COL_ID} = ?",
            arrayOf(livro.id.toString())
        )
    }

    override fun excluir(id: Long): Int {
        val db = banco.writableDatabase
        return db.delete(
            BancoHelper.TABELA_LIVROS,
            "${BancoHelper.COL_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun paraContentValues(livro: Livro) = ContentValues().apply {
        put(BancoHelper.COL_TITULO, livro.titulo)
        put(BancoHelper.COL_AUTOR, livro.autor)
        put(BancoHelper.COL_ANO, livro.ano)
        put(BancoHelper.COL_GENERO, livro.genero)
        put(BancoHelper.COL_LIDO, if (livro.lido) 1 else 0)
    }

    private fun cursorParaLivro(cursor: Cursor) = Livro(
        id = cursor.getLong(cursor.getColumnIndexOrThrow(BancoHelper.COL_ID)),
        titulo = cursor.getString(cursor.getColumnIndexOrThrow(BancoHelper.COL_TITULO)),
        autor = cursor.getString(cursor.getColumnIndexOrThrow(BancoHelper.COL_AUTOR)),
        ano = cursor.getInt(cursor.getColumnIndexOrThrow(BancoHelper.COL_ANO)),
        genero = cursor.getString(cursor.getColumnIndexOrThrow(BancoHelper.COL_GENERO)),
        lido = cursor.getInt(cursor.getColumnIndexOrThrow(BancoHelper.COL_LIDO)) == 1
    )
}
