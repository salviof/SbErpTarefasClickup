package br.org.coletivoJava.fw.api.erp.tarefas;

import javax.inject.Qualifier;
import com.super_bits.modulosSB.SBCore.modulos.erp.InfoReferenciaApiErp;
import br.org.coletivoJava.fw.api.erp.tarefas.ERPTarefas;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import br.org.coletivoJava.fw.api.erp.tarefas.ItfERPTarefasService;

@Qualifier
@InfoReferenciaApiErp(tipoObjeto = ERPTarefas.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TarefasClickup {
}