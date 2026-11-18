package br.com.acervo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.acervo.R
import br.com.acervo.model.Livro

class LivroAdapter(
    private val listener: LivroListener
) : RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    private var livros: List<Livro> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun atualizar(novaLista: List<Livro>) {
        livros = novaLista
        notifyDataSetChanged()
    }

    class LivroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtInicial: TextView = itemView.findViewById(R.id.txtInicial)
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val txtAutorAno: TextView = itemView.findViewById(R.id.txtAutorAno)
        val txtGenero: TextView = itemView.findViewById(R.id.txtGenero)
        val chkLido: CheckBox = itemView.findViewById(R.id.chkLido)
        val btnExcluir: ImageButton = itemView.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro, parent, false)
        return LivroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = livros[position]
        val contexto = holder.itemView.context

        holder.txtInicial.text = livro.titulo.take(1).uppercase()
        holder.txtTitulo.text = livro.titulo
        holder.txtAutorAno.text = contexto.getString(R.string.autor_ano, livro.autor, livro.ano)
        holder.txtGenero.text = livro.genero

        // Tira o listener antigo antes de mudar o isChecked (a casca é reciclada)
        holder.chkLido.setOnCheckedChangeListener(null)
        holder.chkLido.isChecked = livro.lido
        holder.chkLido.setOnCheckedChangeListener { _, marcado ->
            listener.aoMarcarLido(livro, marcado)
        }

        holder.itemView.setOnClickListener { listener.aoClicar(livro) }
        holder.btnExcluir.setOnClickListener { listener.aoExcluir(livro) }
    }

    override fun getItemCount(): Int = livros.size
}
