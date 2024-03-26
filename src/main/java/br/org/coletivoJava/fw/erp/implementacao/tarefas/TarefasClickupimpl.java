package br.org.coletivoJava.fw.erp.implementacao.tarefas;

import br.org.coletivoJava.fw.api.erp.tarefas.ERPTarefas;
import br.org.coletivoJava.fw.api.erp.tarefas.ItfERPTarefasService;
import br.org.coletivoJava.fw.api.erp.tarefas.model.ItfERPTarefa;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.erp.repositorioLinkEntidades.RepositorioLinkEntidadesGenerico;
import br.org.coletivoJava.fw.api.erp.tarefas.TarefasClickup;
import com.super_bits.Super_Bits.intClickup.regras_de_negocio_e_controller.FabIntRestClickupTarefa;
import com.super_bits.Super_Bits.intClickup.regras_de_negocio_e_controller.FabIntRestClickupTimes;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreJson;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.ItfRespostaWebServiceSimples;
import com.super_bits.modulosSB.SBCore.modulos.erp.ErroJsonInterpredador;
import jakarta.json.JsonObject;
import java.util.Map;
import java.util.Optional;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

@TarefasClickup
public class TarefasClickupimpl extends RepositorioLinkEntidadesGenerico
        implements
        ItfERPTarefasService {

    private Map<String, String> espacos;
    private Map<String, String> times;

    private String timePadrao;

    @Override
    public ItfERPTarefa getTarefaByID(java.lang.String s) {

        ItfRespostaWebServiceSimples respTarefa = FabIntRestClickupTarefa.TAREFA_VER.getAcao(s).getResposta();
        respTarefa.getRespostaComoObjetoJson();
        System.out.println(respTarefa.getRespostaTexto());
        try {
            ItfERPTarefa tarefa = ERPTarefas.CLICKUP.getDTO(respTarefa.getRespostaTexto(), ItfERPTarefa.class);
            return tarefa;
        } catch (ErroJsonInterpredador ex) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, s, ex);
            return null;
        }

    }

    @Override
    public String getCodigoEspacoByName(String pCodigoTime, String pNomeEpaco) {

        ItfRespostaWebServiceSimples resposta = FabIntRestClickupTimes.TIME_LISTAR.getAcao().getResposta();
        System.out.println(resposta.getRespostaTexto());
        JsonObject json = resposta.getRespostaComoObjetoJson();
        System.out.println(UtilSBCoreJson.getTextoByJsonObjeect(json));

        Optional<JsonObject> jsonNome = json.getJsonArray("teams").stream().map(time -> time.asJsonObject())
                .filter(esp -> esp.getString("name").equals(pNometIME))
                .findFirst();
        JsonObject time = jsonNome.get();
        String id = time.getString("id");

    }

    @Override
    public String getCodigoTimeByName(String pSlug) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ItfERPTarefa getTarefaEmListaBySlug(String pCodigoLista, String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ItfERPTarefa getTarefaEmListaByFolder(String pCodigoLista, String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getCodigoModeloBySlugs(String pCodigoTime, String... pSlugs) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
