package com.copel.picmicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.copel.picmicroservice.externo.scs.PerfilDTO;
import com.copel.picmicroservice.externo.scs.SCSRest;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class SCSRestTests {

	private static final Logger logger = LoggerFactory.getLogger(SCSRestTests.class);



	@Autowired
	SCSRest scsRest;
	

	@BeforeAll
	public static void beforeAll() throws Exception {

	}

	@BeforeEach
	public void init() throws Exception {

	}

	@Test
	void testaPesquisaVersaoSCS() throws Exception {
		String versao = scsRest.getVersaoServico();
		assertEquals("2.0.20", versao);
	}

	@Test
	void testaGetPerfis() throws Exception {
		List<PerfilDTO> perfis = scsRest.getPerfis("c032141", "PIC");
		assertNotNull(perfis);
		perfis.stream().forEach(p -> {
			logger.info("perfil --> " + p.getNome());
			printFunc(p);

		});

	}

	private void printFunc(PerfilDTO p) {
		p.getFuncionalidades().stream().forEach(f -> logger.info("  funcionalidade --> " + f.getApelido()));

	}

	@Test
	void testaC025427PossuiFuncionalidadeCadFunc() throws Exception {
		boolean possui = scsRest.possuiFuncionalidade("c025427", "SCS", "CadFunc");
		assertTrue(possui);

	}
	
	@Test
	void testaC025427PossuiPerfilDESENVOLVEDOR() throws Exception {
		boolean possui = scsRest.possuiPerfil("c025427", "SCS", "DESENVOLVEDOR");
		assertTrue(possui);

	}



}
