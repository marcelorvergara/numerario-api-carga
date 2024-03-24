package com.numerario.apicarga.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.numerario.apicarga.NumerarioApiCargaApplication;
import com.numerario.apicarga.services.TiposOperacaoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class GoogleCloudPubSubConfig {

    private static final Log LOGGER = LogFactory.getLog(NumerarioApiCargaApplication.class);

    @Autowired
    TiposOperacaoService tiposOperacaoService;

    @Bean
    public MessageChannel pubsubInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(
            @Qualifier("pubsubInputChannel") MessageChannel inputChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "read");
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void messageReceiver(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(payload);
            String id = root.path("id").asText();
            LOGGER.info("Novo arquivo carregado: " + id);
            if(id.contains("carga-arquivos/automatizados/Sensibilização de Saldos.xlsx")){
                try {
                    var resultTiposOperacao = this.tiposOperacaoService.executeTiposOperacaoService();
                    LOGGER.info("Foram inseridos: " + resultTiposOperacao.size() + " tipos de operacao");
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao processar a mensagem pubsub: " + e.getMessage());
        }
    }
}
