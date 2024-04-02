package br.org.coletivoJava.fw.erp.implementacao.tarefas;

import br.org.coletivoJava.fw.api.erp.tarefas.ERPTarefas;
import br.org.coletivoJava.fw.api.erp.tarefas.ItfERPTarefasService;
import br.org.coletivoJava.fw.api.erp.tarefas.model.ItfERPTarefa;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.erp.repositorioLinkEntidades.RepositorioLinkEntidadesGenerico;
import br.org.coletivoJava.fw.api.erp.tarefas.TarefasClickup;
import com.super_bits.Super_Bits.intClickup.regras_de_negocio_e_controller.FabIntRestClickupEspacos;
import com.super_bits.Super_Bits.intClickup.regras_de_negocio_e_controller.FabIntRestClickupListas;
import com.super_bits.Super_Bits.intClickup.regras_de_negocio_e_controller.FabIntRestClickupPastas;
import com.super_bits.Super_Bits.intClickup.regras_de_negocio_e_controller.FabIntRestClickupTarefa;
import com.super_bits.Super_Bits.intClickup.regras_de_negocio_e_controller.FabIntRestClickupTimes;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreJson;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringValidador;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.WS.conexaoWebServiceClient.ItfRespostaWebServiceSimples;
import com.super_bits.modulosSB.SBCore.integracao.libRestClient.api.transmissao_recepcao_rest_client.ItfAcaoApiRest;
import com.super_bits.modulosSB.SBCore.modulos.erp.ErroJsonInterpredador;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.junit.Assert;

@TarefasClickup
public class TarefasClickupimpl extends RepositorioLinkEntidadesGenerico
        implements
        ItfERPTarefasService {

    private Map<String, String> espacos = new HashMap<>();
    private Map<String, String> listas = new HashMap<>();
    private Map<String, String> times = new HashMap<>();

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
        final String chaveEspaco = pCodigoTime + "-" + pNomeEpaco;
        if (espacos.containsKey(chaveEspaco)) {
            return espacos.get(chaveEspaco);
        }
        try {
            ItfAcaoApiRest acap = FabIntRestClickupEspacos.ESPACO_LISTAR.getAcao(pCodigoTime);
            Assert.assertTrue("Falha obtendo espacos do time" + pCodigoTime + acap.getResposta().getRespostaTexto(),
                    acap.getResposta().isSucesso());
            JsonObject jsonEspacos = acap.getResposta().getRespostaComoObjetoJson();
            Optional<JsonObject> pesquisaEspaco = jsonEspacos.getJsonArray("spaces").stream().map(spc -> spc.asJsonObject())
                    .filter(spcJson -> spcJson.getString("name").equals(pNomeEpaco)).findFirst();
            if (!pesquisaEspaco.isPresent()) {
                return null;
            }
            JsonObject espacoJson = pesquisaEspaco.get();
            String codigo = espacoJson.getString("id");
            return codigo;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Falha obtendo espaço " + pNomeEpaco + " no time" + pCodigoTime, t);
            return null;
        }

    }

    @Override
    public String getCodigoTimeByName(String pSlug) {
        try {
            if (times.containsKey(pSlug)) {
                return times.get(pSlug);
            }
            ItfRespostaWebServiceSimples resposta = FabIntRestClickupTimes.TIME_LISTAR.getAcao().getResposta();
            System.out.println(resposta.getRespostaTexto());
            JsonObject json = resposta.getRespostaComoObjetoJson();
            System.out.println(UtilSBCoreJson.getTextoByJsonObjeect(json));

            Optional<JsonObject> pesquisaTime = json.getJsonArray("teams").stream().map(time -> time.asJsonObject())
                    .filter(esp -> esp.getString("name").equals(pSlug))
                    .findFirst();
            if (!pesquisaTime.isPresent()) {
                return null;
            }
            JsonObject time = pesquisaTime.get();
            String id = time.getString("id");
            return id;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Falha obtendo time " + pSlug, t);
            return null;
        }

    }

    @Override
    public ItfERPTarefa getTarefaEmListaByNameContendo(String pCodigoLista, String pTrechoNome) {

        try {
            ItfRespostaWebServiceSimples respListaDetalhes = FabIntRestClickupTarefa.TAREFA_LISTAR.getAcao(pCodigoLista).getResposta();
            JsonObject listaDetalhes = respListaDetalhes.getRespostaComoObjetoJson();
            JsonArray tarefas = listaDetalhes.getJsonArray("tasks");
            for (JsonValue trf : tarefas) {
                String nome = trf.asJsonObject().getString("name");
                if (nome.toUpperCase().contains(pTrechoNome.toUpperCase())) {
                    ItfERPTarefa tarefa = ERPTarefas.CLICKUP.getDTO(UtilSBCoreJson.getTextoByJsonObjeect(trf.asJsonObject()), ItfERPTarefa.class);
                    return tarefa;
                }
            }
            return null;
        } catch (ErroJsonInterpredador ex) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro " + pTrechoNome, ex);
            return null;
        }
    }

    private boolean trechoContem(String pValor, List<String> pTchos) {
        boolean encontrou = true;
        if (UtilSBCoreStringValidador.isNuloOuEmbranco(pValor)) {
            return false;
        }
        for (String trecho : pTchos) {
            if (!pValor.toUpperCase().contains(trecho.toUpperCase())) {
                encontrou = false;
            }
        }
        return encontrou;
    }

    @Override
    public String getCodigoModeloByNameContendo(String pCodigoTime, String... pTrechosNome) {

        boolean fim = false;
        boolean encontrou = false;
        final List<String> idsEncontrados = new ArrayList<>();
        List<String> trechos = Arrays.asList(pTrechosNome);
        int pagina = 0;
        while (!fim && !encontrou) {

            ItfRespostaWebServiceSimples respostaModelo = FabIntRestClickupTarefa.TAREFA_MODELO_LISTAR.getAcao(pCodigoTime, Integer.valueOf(pagina)).getResposta();
            JsonObject respModelJson = respostaModelo.getRespostaComoObjetoJson();
            JsonArray modelosJson = respModelJson.getJsonArray("templates");
            if (modelosJson.isEmpty()) {
                return null;
            }
            fim = modelosJson.stream().map(mod -> mod.asJsonObject().getString("id"))
                    .filter(codigoModelo -> idsEncontrados.contains(codigoModelo))
                    .findFirst().isPresent();
            if (!fim) {
                Optional<JsonObject> pesquisaJsonModelo = modelosJson.stream().map(mod -> mod.asJsonObject())
                        .filter(itemjsonModelo -> (trechos.stream()
                        .filter(trecho -> trechoContem(itemjsonModelo.getString("name"), trechos)).findFirst()
                        .isPresent()))
                        .findFirst();
                encontrou = pesquisaJsonModelo.isPresent();
                if (encontrou) {
                    return pesquisaJsonModelo.get().getString("id");
                }
            }
            modelosJson.stream().map(json -> json.asJsonObject().getString("id")).forEach(idsEncontrados::add);
            pagina++;
        }
        return null;
    }

    @Override
    public String getListaDoEspacoByNameContendo(String pCodigoEspaco, String pTrechoNome) {
        try {
            ItfRespostaWebServiceSimples resp = FabIntRestClickupListas.LISTAS_DO_ESPACO.getAcao(pCodigoEspaco).getResposta();
            JsonObject listas = resp.getRespostaComoObjetoJson();

            Optional<JsonObject> modeloEspaco = listas.getJsonArray("lists").stream()
                    .map(spc -> spc.asJsonObject())
                    .filter(spcJson -> spcJson.getString("name")
                    .contains(pTrechoNome)).findFirst();
            if (!modeloEspaco.isPresent()) {
                return null;
            }

            JsonObject espacoJson = modeloEspaco.get();

            String codigo = espacoJson.getString("id");
            return codigo;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Falha obtendo lista com o trecho:" + pTrechoNome + " no espaço: " + pCodigoEspaco, t);
            return null;
        }
    }

    @Override
    public String getListaDaPastaByNameContendo(String pCodigoPasta, String pTrechoNome) {
        try {
            ItfRespostaWebServiceSimples resp = FabIntRestClickupPastas.PASTAS_DETALHES.getAcao(pCodigoPasta).getResposta();
            JsonObject respPastaJson = resp.getRespostaComoObjetoJson();

            Optional<JsonObject> listaServicosEmExecucao = respPastaJson.getJsonArray("lists").stream()
                    .map(spc -> spc.asJsonObject())
                    .filter(spcJson -> spcJson.getString("name").toUpperCase()
                    .contains(pTrechoNome.toUpperCase())).findFirst();

            if (listaServicosEmExecucao.isPresent()) {
                return listaServicosEmExecucao.get().getString("id");
            } else {
                return null;
            }
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Falha pesquisando lista da pasta", t);
            return null;
        }

    }

    @Override
    public ItfERPTarefa getTarefaEmPAByNameContendo(String pCodigoLista, String pTrechoNome) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean criarTarefaViaModelo(String pCodigoLista, String pModeloTarefa, String pNomeTarefa) {
        ItfRespostaWebServiceSimples respostaCricaoModelo = FabIntRestClickupTarefa.TAREFA_CRIAR_VIA_MODELO.getAcao(pCodigoLista, pModeloTarefa, pNomeTarefa).getResposta();
        if (respostaCricaoModelo.isSucesso()) {
            JsonObject jsonModeloCRiado = respostaCricaoModelo.getRespostaComoObjetoJson();
            String tarefaCriada = jsonModeloCRiado.getString("id");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getCodigoPastaByNameContendo(String pCodigoEspaco, String pTrechoNome) {
        ItfRespostaWebServiceSimples respPastas = FabIntRestClickupPastas.PASTAS_LISTAR.getAcao(pCodigoEspaco).getResposta();
        JsonObject respPastasJson = respPastas.getRespostaComoObjetoJson();
        JsonArray pastas = respPastasJson.getJsonArray("folders");
        for (JsonValue valuePasta : pastas) {
            JsonObject pasta = valuePasta.asJsonObject();
            System.out.println(UtilSBCoreJson.getTextoByJsonObjeect(pasta));
        }

        Optional<String> psta = pastas.stream().map(pst -> pst.asJsonObject())
                .filter(nome -> nome.getString("name").contains(pTrechoNome))
                .map(json -> json.getString("id"))
                .findFirst();

        if (psta.isPresent()) {
            return psta.get();
        }
        return null;
    }

    @Override
    public String criarPasta(String pCodigoEspaco, String pNome) {

        try {
            ItfRespostaWebServiceSimples respCriacaoPasta = FabIntRestClickupPastas.PASTAS_CRIAR.getAcao(pCodigoEspaco, pNome).getResposta();
            Assert.assertTrue(respCriacaoPasta.isSucesso());
            return respCriacaoPasta.getRespostaComoObjetoJson().getString("id");
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro criando pasta " + pNome + " no espaço " + pCodigoEspaco, t);
            return null;
        }
    }

    @Override
    public String criarListaNaPasta(String pCodigoPasta, String pNomeListEmExecucao) {
        try {
            ItfRespostaWebServiceSimples respDetalhesPAsta = FabIntRestClickupPastas.PASTAS_DETALHES.getAcao(pCodigoPasta).getResposta();
            String nomePasata = respDetalhesPAsta.getRespostaComoObjetoJson().getString("name");
            String nomnomeCliente = nomePasata.substring(0, nomePasata.indexOf("["));

            ItfRespostaWebServiceSimples respCriacaoLisaTarefasProjeto = FabIntRestClickupListas.LISTA_CRIAR_NA_PASTA
                    .getAcao(pCodigoPasta,
                            pNomeListEmExecucao,
                            "Projeto sazonal programada, de " + nomnomeCliente + ", para " + pNomeListEmExecucao.substring(0, pNomeListEmExecucao.indexOf("["))).getResposta();

            Assert.assertTrue("A lista não foi criada", respCriacaoLisaTarefasProjeto.isSucesso());
            String idLista = respCriacaoLisaTarefasProjeto.getRespostaComoObjetoJson().getString("id");
            return idLista;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "erro criando lista '" + pNomeListEmExecucao + "' ", t);
            return null;
        }

    }

}
