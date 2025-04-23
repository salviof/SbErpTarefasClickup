/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.coletivoJava.testes.erp;

import br.org.coletivoJava.fw.api.erp.tarefas.ERPTarefas;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import org.apache.logging.log4j.LogManager;
import org.coletivojava.fw.api.objetoNativo.log.LogPadraoSB;
import org.junit.Test;
import testes.geradorCodigo.erp.GeradorERPImplementacaoContexto;
import testes.geradorCodigo.erp.dto.GeradorDTOInterface;
import testes.geradorCodigo.erp.dto.GeradorDTOPojo;
import testes.geradorCodigo.erp.dto.GeradorDTOProcessador;
import testesFW.ConfigCoreJunitPadraoDevAcaoPermissao;

/**
 *
 * @author desenvolvedor
 */
public class CriarImplementacao {

    @Test
    public void criarImplementaca() {

        try {
            SBCore.configurar(new ConfigCoreJunitPadraoDevAcaoPermissao(), SBCore.ESTADO_APP.DESENVOLVIMENTO);
            new GeradorERPImplementacaoContexto(ERPTarefas.CLICKUP).salvarEmDiretorioPadraCASO_NAO_EXISTA();

            for (Class entidade : ERPTarefas.CLICKUP.getInterfacesDeEntidade()) {
                GeradorDTOInterface geradorInterface = new GeradorDTOInterface(ERPTarefas.CLICKUP, entidade);
                geradorInterface.salvarEmDiretorioPadraoSubstituindoAnterior();
                GeradorDTOPojo geradorPojo = new GeradorDTOPojo(ERPTarefas.CLICKUP, entidade);
                geradorPojo.salvarEmDiretorioPadraCASO_NAO_EXISTA();
                GeradorDTOProcessador geradorProcessador = new GeradorDTOProcessador(ERPTarefas.CLICKUP, entidade);
                geradorProcessador.salvarEmDiretorioPadraCASO_NAO_EXISTA();

            }
        } catch (Throwable t) {
            LogManager.getLogger(LogPadraoSB.class).error("Erro Criando anotações", t);
        }
    }

}
