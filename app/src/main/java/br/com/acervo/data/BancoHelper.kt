package br.com.acervo.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BancoHelper(context: Context) :
    SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_BANCO) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABELA_LIVROS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITULO TEXT NOT NULL,
                $COL_AUTOR TEXT NOT NULL,
                $COL_ANO INTEGER NOT NULL,
                $COL_GENERO TEXT NOT NULL,
                $COL_LIDO INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
        inserirExemplo(db, "Dom Casmurro", "Machado de Assis", 1899, "Romance")
        inserirExemplo(db, "Vidas Secas", "Graciliano Ramos", 1938, "Romance")
        inserirExemplo(db, "A Hora da Estrela", "Clarice Lispector", 1977, "Novela")
        inserirExemplo(db, "Auto da Compadecida", "Ariano Suassuna", 1955, "Teatro")
    }

    override fun onUpgrade(db: SQLiteDatabase, versaoAntiga: Int, versaoNova: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABELA_LIVROS")
        onCreate(db)
    }

    private fun inserirExemplo(
        db: SQLiteDatabase, titulo: String, autor: String, ano: Int, genero: String
    ) {
        val valores = ContentValues().apply {
            put(COL_TITULO, titulo)
            put(COL_AUTOR, autor)
            put(COL_ANO, ano)
            put(COL_GENERO, genero)
            put(COL_LIDO, 0)
        }
        db.insert(TABELA_LIVROS, null, valores)
    }

    companion object {
        const val NOME_BANCO = "acervo.db"
        const val VERSAO_BANCO = 1

        const val TABELA_LIVROS = "livros"
        const val COL_ID = "id"
        const val COL_TITULO = "titulo"
        const val COL_AUTOR = "autor"
        const val COL_ANO = "ano"
        const val COL_GENERO = "genero"
        const val COL_LIDO = "lido"
    }
}
