package br.com.acervo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.acervo.model.Livro

// Adapter comum (Parte 3): usa o layout pronto do sistema. Foi substituído pelo LivroAdapter.
class LivroSimplesAdapter(
    private val livros: List<Livro>
) : RecyclerView.Adapter<LivroSimplesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val texto1: TextView = itemView.findViewById(android.R.id.text1)
        val texto2: TextView = itemView.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val livro = livros[position]
        holder.texto1.text = livro.titulo
        holder.texto2.text = livro.autor
    }

    override fun getItemCount(): Int = livros.size
}
