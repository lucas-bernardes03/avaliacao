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
        else filtro = cursos.stream().filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase())).toList();

        if(filtro.isEmpty()){
            System.out.println("Nenhum curso encontrado.");
            return;
        }

        for(Curso curso : filtro) {
            System.out.println(curso);
        }
        System.out.println("Foram encontrados: " + cursos.size() + " cursos");
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
