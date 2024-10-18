package org.persistencia.service;

import org.persistencia.model.Curso;
import org.persistencia.model.Disciplina;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UniversidadeServico {
    private static Connection connection;
    private static UniversidadeServico universidadeServico;
    private static final List<Curso> cursos = new ArrayList<>();

    private UniversidadeServico(){
        try{
            System.out.println("CONECTANDO COM O BANCO DE DADOS...");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/avaliacao1", "admin", "root");
            System.out.println("CONECTADO COM SUCESSO!");
        }
        catch (Exception e){
            System.out.println("ERRO AO CONECTAR COM O BANCO DE DADOS: " + e.getMessage());
        }

        System.out.println("CARREGANDO ARQUIVO XML...");
        lerXml();
        System.out.println("ARQUIVO XML LIDO COM SUCESSO!");
    }

    public static synchronized UniversidadeServico getInstance(){
        if(universidadeServico == null){
            universidadeServico = new UniversidadeServico();
        }

        return universidadeServico;
    }

    public void buscarPorNome(String nome){
        List<Curso> filtro;

        if(nome.isEmpty() || nome.isBlank()) filtro = cursos;
        else {
            filtro = cursos.stream().filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()) || c.getDisciplina().getNome().toLowerCase().contains(nome)).toList();
        }

        if(filtro.isEmpty()){
            System.out.println("Nenhum curso encontrado.");
            return;
        }

        for(Curso curso : filtro) {
            System.out.println(curso);
        }
        System.out.println("Foram encontrados: " + filtro.size() + " cursos");
    }

    public void buscarPorNomeAno(String nome, String ano){
        try{
            Integer anoInt = Integer.parseInt(ano);

            List<Curso> filtro = cursos.stream().filter(c -> c.getAno().equals(anoInt)).toList();
            if(!nome.isEmpty() && !nome.isBlank()) filtro = filtro.stream().filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase())).toList();

            if(filtro.isEmpty()){
                System.out.println("Nenhum curso encontrado.");
                return;
            }

            for(Curso curso : filtro) {
                System.out.println(curso);
            }
            System.out.println("Foram encontrados: " + filtro.size() + " cursos");
        }
        catch (NumberFormatException e){
            System.out.println("Número inválido");
        }
    }

    public void listarTodosCursos(){
        for(Curso curso : cursos) {
            System.out.println(curso);
        }

        System.out.println("Foram encontrados: " + cursos.size() + " cursos");
    }


    public void inserirCursos(){
        PreparedStatement psCurso;
        PreparedStatement psDisciplina;

        try{
            String sqlCurso = "INSERT INTO CURSO (IDEN, ANO, NOME) VALUES (?, ?, ?)";

            String sqlDisciplina = "INSERT INTO DISCIPLINA (NOME, CH, IDEN_CURSO) VALUES (?, ?, ?)";

            connection.setAutoCommit(false);
            psCurso = connection.prepareStatement(sqlCurso);
            psDisciplina = connection.prepareStatement(sqlDisciplina);

            for(Curso curso : cursos){
                psCurso.setInt(1, curso.getIden());
                psCurso.setInt(2, curso.getAno());
                psCurso.setString(3, curso.getNome());
                psCurso.addBatch();

                psDisciplina.setString(1, curso.getDisciplina().getNome());
                psDisciplina.setInt(2, curso.getDisciplina().getCh());
                psDisciplina.setInt(3, curso.getIden());
                psDisciplina.addBatch();
            }

            psCurso.executeBatch();
            connection.commit();
            psDisciplina.executeBatch();
            connection.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                connection.rollback();
            }
            catch (SQLException sqlException){
                System.out.printf("ERRO AO REALIZAR ROLLBACK: " + sqlException.getMessage());
            }
        }
        System.out.println("----------------------------------------");
        System.out.println("CURSOS INSERIDOS NO BANCO DE DADOS!");
    }

    private static void lerXml(){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("src/main/resources/dados.xml");
            NodeList nodeList = doc.getElementsByTagName("curso");

            for(int i = 0; i < nodeList.getLength(); i++){
                Curso curso = new Curso();
                Disciplina disciplina = new Disciplina();
                Node node = nodeList.item(i);

                NodeList child = node.getChildNodes();
                for(int j = 0; j < child.getLength(); j++){
                    Node element = child.item(j);
                    switch (child.item(j).getNodeName()){
                        case "iden":
                            curso.setIden(Integer.parseInt(element.getTextContent()));
                            break;
                        case "ano":
                            curso.setAno(Integer.parseInt(element.getTextContent()));
                            break;
                        case "nome":
                            curso.setNome(element.getTextContent());
                            break;
                        case "disciplina":
                            disciplina.setNome(element.getTextContent());
                            break;
                        case "ch":
                            disciplina.setCh(Integer.parseInt(element.getTextContent()));
                            break;
                    }
                }

                curso.setDisciplina(disciplina);
                cursos.add(curso);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
