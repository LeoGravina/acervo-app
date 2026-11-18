package br.com.acervo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.acervo.data.LivroDao
import br.com.acervo.data.LivroDaoSQLite
import br.com.acervo.model.Livro
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FormLivroActivity : AppCompatActivity() {

    private lateinit var dao: LivroDao

    private lateinit var tilTitulo: TextInputLayout
    private lateinit var edtTitulo: TextInputEditText
    private lateinit var tilAutor: TextInputLayout
    private lateinit var edtAutor: TextInputEditText
    private lateinit var tilAno: TextInputLayout
    private lateinit var edtAno: TextInputEditText
    private lateinit var edtGenero: TextInputEditText
    private lateinit var swtLido: MaterialSwitch

    // 0L = livro novo; qualquer outro valor = edição
    private var idLivro = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_livro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dao = LivroDaoSQLite(this)

        tilTitulo = findViewById(R.id.tilTitulo)
        edtTitulo = findViewById(R.id.edtTitulo)
        tilAutor = findViewById(R.id.tilAutor)
        edtAutor = findViewById(R.id.edtAutor)
        tilAno = findViewById(R.id.tilAno)
        edtAno = findViewById(R.id.edtAno)
        edtGenero = findViewById(R.id.edtGenero)
        swtLido = findViewById(R.id.swtLido)

        idLivro = intent.getLongExtra(EXTRA_ID, 0L)
        if (idLivro != 0L) {
            findViewById<TextView>(R.id.txtTituloTela).setText(R.string.titulo_editar)
            preencherCampos()
        }

        findViewById<Button>(R.id.btnSalvar).setOnClickListener { salvar() }
        findViewById<Button>(R.id.btnCancelar).setOnClickListener { finish() }
    }

    private fun preencherCampos() {
        val livro = dao.buscarPorId(idLivro) ?: return
        edtTitulo.setText(livro.titulo)
        edtAutor.setText(livro.autor)
        edtAno.setText(livro.ano.toString())
        edtGenero.setText(livro.genero)
        swtLido.isChecked = livro.lido
    }

    private fun salvar() {
        val titulo = edtTitulo.text.toString().trim()
        val autor = edtAutor.text.toString().trim()
        val ano = edtAno.text.toString().toIntOrNull()
        val genero = edtGenero.text.toString().trim()

        tilTitulo.error = if (titulo.isEmpty()) getString(R.string.erro_obrigatorio) else null
        tilAutor.error = if (autor.isEmpty()) getString(R.string.erro_obrigatorio) else null
        tilAno.error = if (ano == null || ano !in 1..2100) getString(R.string.erro_ano) else null

        if (titulo.isEmpty() || autor.isEmpty() || ano == null || ano !in 1..2100) return

        val livro = Livro(
            id = idLivro,
            titulo = titulo,
            autor = autor,
            ano = ano,
            genero = genero.ifEmpty { getString(R.string.sem_genero) },
            lido = swtLido.isChecked
        )

        if (idLivro == 0L) {
            dao.inserir(livro)
        } else {
            dao.atualizar(livro)
        }
        Toast.makeText(this, R.string.livro_salvo, Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
