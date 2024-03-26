/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package br.org.coletivoJava.fw.erp.implementacao.tarefas;

import br.org.coletivoJava.fw.api.erp.tarefas.model.ItfERPTarefa;
import br.org.coletivoJava.testes.erp.ConfigCoreApiErpTarefas;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author salvio
 */
public class TarefasClickupimplTest {

    /**
     * Test of getTarefaByID method, of class TarefasClickupimpl.
     */
    @Test
    public void testGetTarefaByID() {
        SBCore.configurar(new ConfigCoreApiErpTarefas(), SBCore.ESTADO_APP.DESENVOLVIMENTO);
        TarefasClickupimpl gestaoDeTarefas = new TarefasClickupimpl();
        ItfERPTarefa tarefasErvice = gestaoDeTarefas.getTarefaByID("86a2rx5y9");

        System.out.println(tarefasErvice.getId());
    }

}
