package com.corebanking.api.messaging;

import com.corebanking.api.model.Transaksi;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQSender {
    
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${app.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;
    
    public void sendTransaksiNotification(Transaksi transaksi) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("nomorRekening", transaksi.getRekening().getNomorRekening());
            message.put("nasabahId", transaksi.getRekening().getNasabah().getId());
            message.put("namaNasabah", transaksi.getRekening().getNasabah().getNamaLengkap());
            message.put("nomorReferensi", transaksi.getNomorReferensi());
            message.put("jenisTransaksi", transaksi.getJenisTransaksi());
            message.put("jumlah", transaksi.getJumlah());
            message.put("tanggalTransaksi", transaksi.getTanggalTransaksi());
            message.put("saldoSesudah", transaksi.getSaldoSesudah());
            
            log.info("Mengirim notifikasi transaksi: {}", message);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("Notifikasi transaksi berhasil dikirim");
        } catch (Exception e) {
            log.error("Gagal mengirim notifikasi transaksi: {}", e.getMessage());
        }
    }
} 