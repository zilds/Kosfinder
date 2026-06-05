package com.example.kosfinder.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kosfinder.R
import com.example.kosfinder.response.BookingOwnerData

class OwnerBookingAdapter(
    private val bookings: List<BookingOwnerData>
) : RecyclerView.Adapter<OwnerBookingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNamaPenyewa: TextView = itemView.findViewById(R.id.txtNamaPenyewa)
        val txtNoHpPenyewa: TextView = itemView.findViewById(R.id.txtNoHpPenyewa)
        val txtNamaKosBooking: TextView = itemView.findViewById(R.id.txtNamaKosBooking)
        val txtTanggalMasuk: TextView = itemView.findViewById(R.id.txtTanggalMasuk)
        val txtDurasiSewa: TextView = itemView.findViewById(R.id.txtDurasiSewa)
        val txtStatusBooking: TextView = itemView.findViewById(R.id.txtStatusBooking)
        val txtCatatan: TextView = itemView.findViewById(R.id.txtCatatan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_owner_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = bookings[position]

        holder.txtNamaPenyewa.text = "👤 ${booking.nama_penyewa}"
        holder.txtNoHpPenyewa.text = "📞 ${booking.no_hp}"
        holder.txtNamaKosBooking.text = "🏠 ${booking.kost.nama_kost}"
        holder.txtTanggalMasuk.text = "📅 Masuk: ${booking.tanggal_masuk}"
        holder.txtDurasiSewa.text = "⏳ Durasi: ${booking.durasi_sewa} Bulan"
        holder.txtStatusBooking.text =
            "${booking.status} | ${booking.status_pembayaran}"

        when (booking.status_pembayaran.lowercase()) {

            "sudah_bayar" -> {
                holder.txtStatusBooking.setTextColor(
                    Color.parseColor("#4CAF50")
                )
            }

            "belum_bayar" -> {
                holder.txtStatusBooking.setTextColor(
                    Color.parseColor("#FFC107")
                )
            }

            "gagal", "failed" -> {
                holder.txtStatusBooking.setTextColor(
                    Color.parseColor("#F44336")
                )
            }

            else -> {
                holder.txtStatusBooking.setTextColor(
                    Color.parseColor("#333333")
                )
            }
        }
        holder.txtCatatan.text =
            "📝 Catatan: ${booking.catatan ?: "-"}"
    }

    override fun getItemCount(): Int = bookings.size
}