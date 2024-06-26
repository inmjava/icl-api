package com.copel.icl.dao;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.copel.icl.dto.EmployeeCastLightDTO;

@Repository
public class ProfissionalDAOImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public ProfissionalDAOImpl() {
        super();
    }

    public List<EmployeeCastLightDTO> loadEmployees() {
        var sql = "WITH organogramas_dis AS ( "
        		+ "    select sigla_rdis, "
        		+ "        NUM_ORG, "
        		+ "        NOME_ORG, "
        		+ "        SIGLA_ORG, "
        		+ "        COD_NIV_ORG, "
        		+ "        tipo_prof_geren_tit_org, "
        		+ "        num_prof_geren_tit_org, "
        		+ "        nome_gerente_org,  "
        		+ "        cpf_gerente_org, "
        		+ "        path, "
        		+ "        case  "
        		+ "            when path like('*5-DIS*5-DCR%') "
        		+ "            then REPLACE(PATH, '*5-DIS*5-DCR','*5-DCR') "
        		+ "            else path "
        		+ "        end as path_ajustado, "
        		+ "        case  "
        		+ "            when cod_niv_org = 9 OR cod_niv_org = 10 "
        		+ "            then ( "
        		+ "                select  "
        		+ "                       p.nome_compt_prof "
        		+ "                    from profissional p,  "
        		+ "                    profissional_complemento pc, "
        		+ "                    funcao_empregado fun, "
        		+ "                    organograma org_lotacao "
        		+ "                    where p.num_reg_prof = pc.num_reg_prof "
        		+ "                        and p.tipo_prof in(1,77)  "
        		+ "                        and p.data_desligamento_prof is null "
        		+ "                        and p.cod_fun_prof = fun.cod_fepg "
        		+ "                        and p.num_org_lot_prof = org_lotacao.num_org "
        		+ "                        and p.num_org_lot_prof IN (TODOS_ORGS.NUM_ORG, TODOS_ORGS.NUM_ORIG_ORG) "
        		+ "                        and fun.desc_fepg like 'SUPERVISOR DE SETOR%'    "
        		+ "                        AND ROWNUM = 1 "
        		+ "            ) "
        		+ "        end as nome_supervisor, "
        		+ "        case  "
        		+ "            when cod_niv_org = 9 OR cod_niv_org = 10 "
        		+ "            then ( "
        		+ "                select  "
        		+ "                       pc.num_cpf_prof "
        		+ "                    from profissional p,  "
        		+ "                    profissional_complemento pc, "
        		+ "                    funcao_empregado fun, "
        		+ "                    organograma org_lotacao "
        		+ "                    where p.num_reg_prof = pc.num_reg_prof "
        		+ "                        and p.tipo_prof in(1,77)  "
        		+ "                        and p.data_desligamento_prof is null "
        		+ "                        and p.cod_fun_prof = fun.cod_fepg "
        		+ "                        and p.num_org_lot_prof = org_lotacao.num_org "
        		+ "                        and p.num_org_lot_prof IN (TODOS_ORGS.NUM_ORG, TODOS_ORGS.NUM_ORIG_ORG) "
        		+ "                        and fun.desc_fepg like 'SUPERVISOR DE SETOR%'    "
        		+ "                        AND ROWNUM = 1 "
        		+ "            ) "
        		+ "        end as cpf_supervisor      "
        		+ "    from( "
        		+ "        SELECT "
        		+ "          reg.sigla_rdis, "
        		+ "          ORG.NUM_ORG, "
        		+ "          ORG.NUM_ORIG_ORG, "
        		+ "          ORG.NOME_ORG, "
        		+ "          ORG.SIGLA_ORG, "
        		+ "          ORG.COD_NIV_ORG, "
        		+ "          tipo_prof_geren_tit_org, "
        		+ "          num_prof_geren_tit_org, "
        		+ "          p_gerente.nome_compt_prof as nome_gerente_org,  "
        		+ "          pc_gerente.num_cpf_prof as cpf_gerente_org, "
        		+ "          SYS_CONNECT_BY_PATH(ORG.COD_NIV_ORG||'-'||SIGLA_ORG, '*') as path "
        		+ "        FROM "
        		+ "          ORGANOGRAMA ORG, localidade l, REGIONAL_DISTRIBUICAO REG, profissional p_gerente, profissional_complemento pc_gerente "
        		+ "        WHERE "
        		+ "          ORG.COD_SIT_ORG = 'A' "
        		+ "          and org.cod_local_org = l.cod_local (+) "
        		+ "          and L.COD_REG_DISTR_LOCAL = REG.COD_RDIS (+) "
        		+ "          and num_prof_geren_tit_org = p_gerente.num_reg_prof "
        		+ "          and p_gerente.num_reg_prof = pc_gerente.num_reg_prof "
        		+ "        CONNECT BY "
        		+ "          PRIOR ORG.NUM_ORG = ORG.NUM_ORIG_ORG "
        		+ "        START WITH           "
        		+ "          ORG.NUM_ORG = 113550 "
        		+ "      )TODOS_ORGS "
        		+ "), "
        		+ "pivoted_dis_areas AS ( "
        		+ "    select *  "
        		+ "    from  "
        		+ "        ( "
        		+ "        select  "
        		+ "            sigla_rdis as regional, "
        		+ "            sigla_org, "
        		+ "            NOME_ORG, "
        		+ "            COD_NIV_ORG,  "
        		+ "            num_org, "
        		+ "            path,  "
        		+ "            substr(extracted_value ,2,1) as nivel , "
        		+ "            extracted_value, "
        		+ "            tipo_prof_geren_tit_org, "
        		+ "            num_prof_geren_tit_org, "
        		+ "            nome_gerente_org, "
        		+ "            cpf_gerente_org, "
        		+ "            nome_supervisor, "
        		+ "            cpf_supervisor "
        		+ "        from( "
        		+ "            SELECT * "
        		+ "            FROM ( "
        		+ "              SELECT "
        		+ "                sigla_rdis, "
        		+ "                sigla_org, "
        		+ "                num_org, "
        		+ "                NOME_ORG, "
        		+ "                COD_NIV_ORG, "
        		+ "                tipo_prof_geren_tit_org, "
        		+ "                num_prof_geren_tit_org, "
        		+ "                nome_gerente_org, "
        		+ "                cpf_gerente_org, "
        		+ "                nome_supervisor, "
        		+ "                cpf_supervisor, "
        		+ "                path, "
        		+ "                REGEXP_SUBSTR(path_ajustado, '\\*\\d+-[^*]+', 1, LEVEL) as extracted_value "
        		+ "              FROM organogramas_dis "
        		+ "              CONNECT BY "
        		+ "                REGEXP_SUBSTR(path_ajustado, '\\*\\d+-[^*]+', 1, LEVEL) IS NOT NULL "
        		+ "                AND PRIOR SYS_GUID() IS NOT NULL "
        		+ "                AND PRIOR path_ajustado = path_ajustado "
        		+ "            ) "
        		+ "        ) "
        		+ "    ) "
        		+ "    PIVOT "
        		+ "    ( "
        		+ "      MAX(REGEXP_REPLACE(extracted_value, '\\*\\d+-', '')) "
        		+ "      FOR nivel IN ('5' AS diretoria, '6' AS superintendencia, '7' AS departamento, '8' AS divisao, '9' AS setor, '1' AS unidade) "
        		+ "    ) "
        		+ ") "
        		+ ", all_copel_employes as ( "
        		+ "    select distinct p.num_reg_prof, "
        		+ "        p.nome_compt_prof,  "
        		+ "        p.data_admissao_prof, "
        		+ "        pc.num_cpf_prof, "
        		+ "        fun.desc_fepg,  "
        		+ "        p.num_org_lot_prof, "
        		+ "        org_lotacao.NUM_ORIG_ORG, "
        		+ "        p.end_email_prof "
        		+ "    from profissional p,  "
        		+ "    profissional_complemento pc, "
        		+ "    funcao_empregado fun, "
        		+ "    organograma org_lotacao "
        		+ "    where p.num_reg_prof = pc.num_reg_prof "
        		+ "        and p.tipo_prof in(1,77)  "
        		+ "        and p.data_desligamento_prof is null "
        		+ "        and p.cod_fun_prof = fun.cod_fepg "
        		+ "        and p.num_org_lot_prof = org_lotacao.num_org "
        		+ "), "
        		+ "all_copel_dis_employes as ( "
        		+ "    select ace.*,  "
        		+ "        dis_areas.*, "
        		+ "        org_superior.sigla_org as sigla_org_superior, "
        		+ "        org_superior.num_prof_geren_tit_org as num_prof_geren_tit_org_superior, "
        		+ "        orgs_dis.nome_gerente_org as nome_gerente_org_superior, "
        		+ "        orgs_dis.cpf_gerente_org as cpf_gerente_org_superior "
        		+ "    from pivoted_dis_areas dis_areas,  "
        		+ "        all_copel_employes ace, "
        		+ "        organograma org_superior,  "
        		+ "        organogramas_dis orgs_dis "
        		+ "    where dis_areas.num_org = ace.num_org_lot_prof "
        		+ "        and ace.NUM_ORIG_ORG(+) = org_superior.num_org "
        		+ "        and orgs_dis.num_org(+) = org_superior.num_org "
        		+ "), diretorias_dis as ( "
        		+ "    select *  "
        		+ "    from( "
        		+ "        select rownum as contador,pc.num_cpf_prof as cpf_manager "
        		+ "        from ( "
        		+ "            select ORG.num_org, org.num_prof_geren_tit_org "
        		+ "            FROM ORGANOGRAMA ORG "
        		+ "            WHERE org.COD_SIT_ORG = 'A' and org.cod_niv_org = 5 "
        		+ "            CONNECT BY PRIOR ORG.NUM_ORG = ORG.NUM_ORIG_ORG "
        		+ "            START WITH ORG.NUM_ORG = 113550) diretorias_dis, profissional_complemento pc "
        		+ "        where pc.num_reg_prof = diretorias_dis.num_prof_geren_tit_org  "
        		+ "     "
        		+ "    ) "
        		+ "    PIVOT "
        		+ "    ( "
        		+ "      MAX(cpf_manager) "
        		+ "      FOR contador IN ('1' AS diretoria1, '2' AS diretoria2) "
        		+ "    ) "
        		+ ") "
        		+ "select      "
        		+ "        acde.nome_compt_prof as name, "
        		+ "        acde.NUM_CPF_PROF as cpf, "
        		+ "        acde.end_email_prof as email, "
        		+ "        'CLT' as contract_type, "
        		+ "        acde.desc_fepg as position, "
        		+ "        '(00) 0000-0000' as phone_number, "
        		+ "        TO_CHAR(acde.data_admissao_prof, 'DD-MM-YYYY') as admission_date, "
        		+ "        '' as resignation_date,        "
        		+ "        acde.regional as regional, "
        		+ "        'COPEL DISTRIBUIÇÃO S.A.' as business_unit, "
        		+ "         "
        		+ "        acde.diretoria as title_area1, "
        		+ "        1 as instructure_level_area1, "
        		+ "        'Diretoria' as instructure_title_area1, "
        		+ "         "
        		+ "        acde.superintendencia as title_area2, "
        		+ "        2 as instructure_level_area2, "
        		+ "        'Superintendencia' as instructure_title_area2, "
        		+ "         "
        		+ "        acde.departamento as title_area3, "
        		+ "        3 as instructure_level_area3, "
        		+ "        'Departamento' as instructure_title_area3, "
        		+ "         "
        		+ "        acde.divisao as title_area4, "
        		+ "        4 as instructure_level_area4, "
        		+ "        'Divisão' as instructure_title_area4, "
        		+ "         "
        		+ "        acde.setor as title_area5, "
        		+ "        5 as instructure_level_area5, "
        		+ "        'Setor' as instructure_title_area5, "
        		+ "         "
        		+ "        acde.unidade as title_area6, "
        		+ "        6 as instructure_level_area6, "
        		+ "        'Unidade' as instructure_title_area6,         "
        		+ "         "
        		+ "        acde.num_reg_prof as registration, "
        		+ "        '|-----|' as separator, "
        		+ "        diretorias_dis.diretoria1 as cpf_diretor1, "
        		+ "        diretorias_dis.diretoria2 as cpf_diretor2, "
        		+ "        case "
        		+ "            when num_prof_geren_tit_org = num_reg_prof "
        		+ "                then cpf_gerente_org_superior "
        		+ "            else cpf_gerente_org "
        		+ "        end as cpf_gerente_imediato,     "
        		+ "        case  "
        		+ "            when num_prof_geren_tit_org = num_reg_prof "
        		+ "                then nome_gerente_org_superior "
        		+ "            else nome_gerente_org "
        		+ "        end as nome_gerente_imediato,     "
        		+ "        case "
        		+ "            when NUM_CPF_PROF != cpf_supervisor "
        		+ "                then cpf_supervisor "
        		+ "        end as cpf_supervisor,     "
        		+ "        case  "
        		+ "            when NUM_CPF_PROF != cpf_supervisor "
        		+ "                then nome_supervisor "
        		+ "        end as nome_supervisor    "
        		+ "from all_copel_dis_employes acde, diretorias_dis "
        		+ "where num_org_lot_prof not in ( "
        		+ "    select ORG.NUM_ORG "
        		+ "    FROM ORGANOGRAMA ORG "
        		+ "    WHERE org.COD_SIT_ORG = 'A' "
        		+ "    CONNECT BY PRIOR ORG.NUM_ORG = ORG.NUM_ORIG_ORG "
        		+ "    START WITH ORG.NUM_ORG = 112936) "
        		//+ "    START WITH ORG.NUM_ORG = 112936) and num_reg_prof in (43291, 47370,49855,48620,24594) "
        		+ "order by path ";

        
        return this.getAll(sql).stream()
                .map(EmployeeCastLightDTO::new)
                .collect(Collectors.toList());
    }




    @SuppressWarnings("unchecked")
    private List<Object[]> getAll(String sql) {
        var query = this.entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

}
