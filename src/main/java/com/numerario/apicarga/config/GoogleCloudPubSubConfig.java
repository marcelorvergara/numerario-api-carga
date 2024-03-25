package com.numerario.apicarga.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.numerario.apicarga.entities.FileStatus;
import com.numerario.apicarga.services.LancamentosPAService;
import com.numerario.apicarga.services.TiposOperacaoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


@Configuration
public class GoogleCloudPubSubConfig {

    private static final Log LOGGER = LogFactory.getLog(GoogleCloudPubSubConfig.class);

    @Autowired
    TiposOperacaoService tiposOperacaoService;

    @Autowired
    LancamentosPAService lancamentosPAService;

    @Autowired
    private ProcessedFileInterface processedFileInterface;

    @Bean
    public MessageChannel pubsubInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(@Qualifier("pubsubInputChannel") MessageChannel inputChannel, PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "read");
        adapter.setAckMode(AckMode.AUTO_ACK);
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void messageReceiver(Message<String> message) {
        try {
            String payload = message.getPayload();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(payload);
            String id = root.path("id").asText();
            LOGGER.info("Novo arquivo carregado: " + id);

            if (id.contains("carga-arquivos/automatizados/Sensibilização de Saldos.xlsx")) {
                sensibilizaSaldos();
            } else if (id.contains("carga-arquivos/automatizados/lancamentos-pa/")) {
                String lancamentosFileName = root.path("name").asText();
                checkNewFile(lancamentosFileName);
            } else {
                LOGGER.error("Arquivo carregado não identificado: " + root.path("name").asText());
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao processar a mensagem pubsub: " + e.getMessage());
        }
    }

    private void checkNewFile(String lancamentosFileName) {
        boolean isNew = processedFileInterface.processFileFreshness(lancamentosFileName);
        if (isNew) {
            LOGGER.info("Lançamento: " + lancamentosFileName.split("/")[2] + " encontrado. Iniciando processamento...");
            try {
                insertLancamentosPAs(lancamentosFileName);
                processedFileInterface.updateFileProcessStatus(lancamentosFileName, FileStatus.FINALIZADO);
            } catch (Exception e) {
                processedFileInterface.updateFileProcessStatus(lancamentosFileName, FileStatus.FALHA);
                throw e;
            }
        } else {
            LOGGER.info("Arquivo: " + lancamentosFileName.split("/")[2] + " em processamento ou já finalizado");
        }
    }

    private void insertLancamentosPAs(String fileNameLancamentoPa) {
        try {
            var resultLancamentosPA = this.lancamentosPAService.executeLancamentosPA(fileNameLancamentoPa);
            LOGGER.info("Foram inseridos: " + resultLancamentosPA.size() + " movimentações");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void sensibilizaSaldos() {
        try {
            var resultTiposOperacao = this.tiposOperacaoService.executeTiposOperacaoService();
            LOGGER.info("Foram inseridos: " + resultTiposOperacao.size() + " tipos de operacao");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
