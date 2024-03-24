/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package br.org.coletivoJava.fw.api.erp.tarefas;

import com.super_bits.modulosSB.SBCore.modulos.erp.ApiERPColetivoJavaFW;
import com.super_bits.modulosSB.SBCore.modulos.erp.ItfApiErpSuperBits;

/**
 *
 * @author salvio
 */
@ApiERPColetivoJavaFW(descricaoApi = "Tarefas", nomeApi = "Tarefas", slugInicial = "Tarefas")
public enum ERPTarefas implements ItfApiErpSuperBits<ItfERPTarefas> {

    CLICKUP,
    ASANA;

    @Override
    public Class<? extends ItfERPTarefas> getInterface() {
        return ItfERPTarefas.class;
    }
}
