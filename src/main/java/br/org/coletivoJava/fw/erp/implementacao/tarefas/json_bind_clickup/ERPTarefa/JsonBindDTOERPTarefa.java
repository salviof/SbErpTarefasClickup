package br.org.coletivoJava.fw.erp.implementacao.tarefas.json_bind_clickup.ERPTarefa;

import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.erp.dto.DTO_SB_JSON_PROCESSADOR_GENERICO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class JsonBindDTOERPTarefa
        extends
        DTO_SB_JSON_PROCESSADOR_GENERICO<DTOERPTarefa> {

    public JsonBindDTOERPTarefa() {
        super(DTOERPTarefa.class);
    }

    @Override
    public DTOERPTarefa deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);
        DTOERPTarefa dto = new DTOERPTarefa();
        adicionarPropriedadeInteiro("id", node, "id");
        selarProcesamento(dto);
        return dto;
    }
}
