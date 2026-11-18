package br.com.acervo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.acervo.data.LivroDao
import br.com.acervo.data.LivroDaoSQLite
import br.com.acervo.model.Livro
import br.com.acervo.ui.LivroAdapter
import br.com.acervo.ui.LivroListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LivroListener {

    private lateinit var dao: LivroDao
    private lateinit var adapter: LivroAdapter
    private lateinit var txtResumo: TextView
    private lateinit var txtVazio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dao = LivroDaoSQLite(this)
        txtResumo = findViewById(R.id.txtResumo)
        txtVazio = findViewById(R.id.txtVazio)

        adapter = LivroAdapter(this)
        val rvLivros = findViewById<RecyclerView>(R.id.rvLivros)
        rvLivros.layoutManager = LinearLayoutManager(this)
        rvLivros.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAdicionar).setOnClickListener {
            startActivity(Intent(this, FormLivroActivity::class.java))
        }
    }

    // Recarrega sempre que a tela volta a aparecer (ex.: ao voltar do formulário)
    override fun onResume() {
        super.onResume()
        carregarLivros()
    }

    private fun carregarLivros() {
        val livros = dao.listarTodos()
        adapter.atualizar(livros)

        val lidos = livros.count { it.lido }
        txtResumo.text = getString(R.string.resumo_acervo, livros.size, lidos)
        txtVazio.visibility = if (livros.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun aoClicar(livro: Livro) {
        val intent = Intent(this, FormLivroActivity::class.java)
        intent.putExtra(FormLivroActivity.EXTRA_ID, livro.id)
        startActivity(intent)
    }

    override fun aoMarcarLido(livro: Livro, lido: Boolean) {
        dao.atualizar(livro.copy(lido = lido))
        carregarLivros()
    }

    override fun aoExcluir(livro: Livro) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.titulo_excluir)
            .setMessage(getString(R.string.mensagem_excluir, livro.titulo))
            .setNegativeButton(R.string.botao_cancelar, null)
            .setPositiveButton(R.string.botao_excluir) { _, _ ->
                dao.excluir(livro.id)
                carregarLivros()
                Toast.makeText(this, R.string.livro_excluido, Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}
