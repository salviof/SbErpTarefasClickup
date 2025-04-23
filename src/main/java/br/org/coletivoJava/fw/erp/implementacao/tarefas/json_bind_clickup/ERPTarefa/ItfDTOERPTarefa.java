package br.org.coletivoJava.fw.erp.implementacao.tarefas.json_bind_clickup.ERPTarefa;

import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.erp.dto.ItfDTOSBJSON;
import br.org.coletivoJava.fw.api.erp.tarefas.model.ItfERPTarefa;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import br.org.coletivoJava.fw.erp.implementacao.tarefas.json_bind_clickup.ERPTarefa.JsonBindDTOERPTarefa;
import java.lang.String;

@JsonDeserialize(using = JsonBindDTOERPTarefa.class)
public interface ItfDTOERPTarefa extends ItfDTOSBJSON, ItfERPTarefa {

    @Override
    public default String getNome() {
        return (String) getValorPorReflexao();
    }

    @Override
    public default String getNomeCurto() {
        return (String) getValorPorReflexao();
    }

    @Override
    public default String getIconeDaClasse() {
        return (String) getValorPorReflexao();
    }

    @Override
    public default Long getId() {
        return (long) getValorPorReflexao();
    }
}
