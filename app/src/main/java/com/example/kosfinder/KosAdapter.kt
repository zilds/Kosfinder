package com.example.kosfinder

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class KosAdapter(

    // List data kos yang akan ditampilkan
    private val listKos: List<KosModel>,

    // Penanda apakah adapter dipakai owner atau user biasa
    // false = HomeActivity
    // true = OwnerKosActivity
    private val isOwnerMode: Boolean = false

) : RecyclerView.Adapter<KosAdapter.KosViewHolder>() {

    class KosViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Menghubungkan komponen XML item_kos.xml
        val imgItemKos: ImageView = view.findViewById(R.id.imgItemKos)
        val txtTipeKos: TextView = view.findViewById(R.id.txtItemTipe)
        val txtNamaKos: TextView = view.findViewById(R.id.txtItemNama)
        val txtLokasiKos: TextView = view.findViewById(R.id.txtItemLokasi)
        val txtHargaKos: TextView = view.findViewById(R.id.txtItemHarga)
    }

    // Membuat tampilan item dari item_kos.xml
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KosViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kos, parent, false)

        return KosViewHolder(view)
    }

    // Mengisi data ke setiap item RecyclerView
    override fun onBindViewHolder(
        holder: KosViewHolder,
        position: Int
    ) {

        // Ambil data kos sesuai posisi
        val kos = listKos[position]

        // URL storage Laravel
        val baseUrl =
            "https://editor-mountable-dreamy.ngrok-free.dev/storage/"

        // Menampilkan thumbnail kos
        Glide.with(holder.itemView.context)
            .load(baseUrl + kos.thumbnail)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.imgItemKos)

        // Menampilkan data kos ke TextView
        holder.txtTipeKos.text = kos.tipe
        holder.txtNamaKos.text = kos.nama
        holder.txtLokasiKos.text = kos.lokasi
        holder.txtHargaKos.text = kos.harga

        // Saat item kos diklik
        holder.itemView.setOnClickListener {

            val context = holder.itemView.context

            // ================= MODE OWNER =================
            if (isOwnerMode) {

                // Pindah ke halaman edit kos
                val intent = Intent(
                    context,
                    EditKosActivity::class.java
                )

                // Kirim data kos ke halaman edit
                intent.putExtra("id", kos.id)
                intent.putExtra("nama_kost", kos.nama)
                intent.putExtra("alamat", kos.lokasi)
                intent.putExtra("harga", kos.harga)
                intent.putExtra("jenis_kost", kos.tipe)
                intent.putExtra("jumlah_kamar", kos.jumlahKamar)
                intent.putExtra("kamar_tersedia", kos.kamarTersedia)
                intent.putStringArrayListExtra(
                    "fasilitas",
                    ArrayList(kos.fasilitas ?: emptyList())
                )
                intent.putExtra("thumbnail", kos.thumbnail)
                intent.putExtra("gambar_kamar_mandi", kos.gambarKamarMandi)
                intent.putExtra("gambar_interior", kos.gambarInterior)
                intent.putExtra("gambar_depan", kos.gambarDepan)
                intent.putExtra("EXTRA_KOST_ID", kos.id)

                // Buka EditKosActivity
                context.startActivity(intent)

            }
            // ================= MODE USER =================
            else {

                // Pindah ke halaman detail kos
                val intent = Intent(
                    context,
                    DetailKosActivity::class.java
                )

                // Kirim data kos ke halaman detail
                intent.putExtra("EXTRA_NAMA_KOS", kos.nama)
                intent.putExtra("EXTRA_LOKASI_KOS", kos.lokasi)
                intent.putExtra("EXTRA_HARGA_KOS", kos.harga)
                intent.putExtra("EXTRA_TIPE_KOS", kos.tipe)
                intent.putExtra("EXTRA_THUMBNAIL_KOS", kos.thumbnail)
                intent.putExtra("EXTRA_KAMAR_MANDI", kos.gambarKamarMandi)
                intent.putExtra("EXTRA_INTERIOR", kos.gambarInterior)
                intent.putExtra("EXTRA_DEPAN", kos.gambarDepan)
                intent.putExtra("EXTRA_KOST_ID", kos.id)
                intent.putStringArrayListExtra(
                    "EXTRA_FASILITAS",
                    ArrayList(kos.fasilitas ?: emptyList())
                )


                // Buka halaman detail
                context.startActivity(intent)
            }
        }
    }

    // Mengembalikan jumlah data kos
    override fun getItemCount(): Int {
        return listKos.size
    }
}