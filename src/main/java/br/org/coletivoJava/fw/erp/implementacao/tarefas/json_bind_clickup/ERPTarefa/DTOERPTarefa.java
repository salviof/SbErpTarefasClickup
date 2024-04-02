package br.org.coletivoJava.fw.erp.implementacao.tarefas.json_bind_clickup.ERPTarefa;

import br.org.coletivoJava.fw.erp.implementacao.tarefas.json_bind_clickup.ERPTarefa.ItfDTOERPTarefa;
import br.org.coletivoJava.fw.erp.implementacao.tarefas.json_bind_clickup.ERPTarefa.JsonBindDTOERPTarefa;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.erp.dto.DTO_SBGENERICO;

public class DTOERPTarefa extends DTO_SBGENERICO<ItfDTOERPTarefa>
        implements
        ItfDTOERPTarefa {

    public DTOERPTarefa(String pJson) {
        super(JsonBindDTOERPTarefa.class, pJson);
    }

    public DTOERPTarefa() {
        super(null, null);
    }

    @Override
    public String getIdAppRemoto() {
        return (String) getValorPorReflexao();
    }
}
