package com.copel.picmicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.copel.icl.PicMicroserviceApplication;
import com.copel.icl.dao.TarefaDAO;
import com.copel.icl.entidade.Tarefa;


@SpringBootTest(
		  classes = PicMicroserviceApplication.class)
//@TestPropertySource(
	//	  locations = "classpath:application-test.properties",
@TestMethodOrder(OrderAnnotation.class)
public class DemoTarefaDAOTest {
	
	@Autowired
	private TarefaDAO tarefaDAO;
	
	@Autowired
	private  DataSource datasource;
	
	@Test
	@Order(1)
	public void testaInclusaoTarefa() {
		
		Tarefa tarefa = new Tarefa();
		tarefa.setNome("tarefa teste 1");
		tarefa.setStatus("OK");
		tarefa = tarefaDAO.save(tarefa);
		assertNotEquals(0, tarefa.getId());
		assertNotNull(tarefa.getId());
		
		
	}
	
	@Test
	@Order(3)
	public void testaListaTarefas() {
		
		
		List<Tarefa> lista = tarefaDAO.findAll();
		assertEquals(2, lista.size());
		
	}
	
	@Test
	@Order(2)
	public void testaAlteraTarefa() {
		
		Tarefa tarefa = new Tarefa();
		tarefa.setNome("tarefa teste 1");
		tarefa.setStatus("OK");
		tarefa = tarefaDAO.save(tarefa);

		Long id = tarefa.getId();
		tarefa.setStatus("Nao OK");
		
		
		tarefa = tarefaDAO.save(tarefa);
		
		assertEquals(id, tarefa.getId());
		assertEquals("Nao OK", tarefa.getStatus());	
		
		
		
		
	}
	
	@Test
	@Order(4)
	public void testaSalvaBancoEmArquivo() throws Exception {
		
		IDatabaseConnection iconnection = new DatabaseConnection(datasource.getConnection());
		
	    // partial database export
        QueryDataSet partialDataSet = new QueryDataSet(iconnection);
        partialDataSet.addTable("TAREFA", "SELECT * FROM TAREFA ");
        FlatXmlDataSet.write(partialDataSet, new FileOutputStream("./src/test/resources/dados/dados-extracao.xml"));

		
	}
	
	@Test
	@Order(5)
	public void testaCarregaBancodeArquivo() throws Exception {
		
		IDatabaseConnection iconnection = new DatabaseConnection(datasource.getConnection());

		IDataSet dataset = new FlatXmlDataSet(new FileInputStream("./src/test/resources/dados/dados-tabela.xml"));

		String[] tabelas = dataset.getTableNames();
		Arrays.stream(tabelas).forEach(nome -> System.out.println(nome));

		DatabaseOperation.CLEAN_INSERT.execute(iconnection, dataset);
		
	}
	
	
	@Test
	@Order(6)
	public void testaListaDepoisDaCargaArquivo() throws Exception {
		
		List<Tarefa> lista = tarefaDAO.findAll();
		assertEquals(8, lista.size());
		
	}
	
	
	@Test
	@Order(7)
	public void testaApagarDepoisDaCargaArquivo() throws Exception {
		
		Optional<Tarefa> tarefa = tarefaDAO.findById(2l);
		tarefaDAO.delete(tarefa.get());
		
		
		assertEquals(7, tarefaDAO.count());
		
	}
		
	
	
	
	
	
	

}
